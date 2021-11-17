package api.crabteam.utils;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.Remark;
import api.crabteam.model.entities.Revisao;

public class LEPBuilder {
	
	private Projeto project;
	private JSONObject oldLinesData;
	private int tableLinePosition = 700;
	private int page = 1;
	
	private static final CMYKColor BLACK = new CMYKColor(0, 0, 0, 255);

	public LEPBuilder(Projeto project, JSONObject oldLinesData) {
		this.project = project;
		this.oldLinesData = oldLinesData;
		
	}
	
	public void generateLep() throws Exception {
		ArrayList<Linha> lines = getProjectRemarks();
		
		for (int i = 0; i < lines.size(); i++) {
			Linha line = lines.get(i);
			
			generateLep(line);
			
		}
		
	}
	
	private void generateLep(Linha line) throws Exception {
		String fileName = getFileName(line);
		String outputFolder = getOutputFolder(line);
		
		line.setFilePath(outputFolder + fileName);
		
		PDFFile pdfFile = PDFBuilder.createNewPdfDocument(outputFolder, fileName);
		
		Document pdfDoc = pdfFile.getPdfDocument();
		PdfWriter writer = pdfFile.getWriter();

		pdfDoc.open();
		
		writeRevisionsSummary(pdfDoc, writer, line);
		drawRevisionTable(pdfDoc, writer, line);
		
		pdfDoc.close();
		
	}
	
	private void drawRevisionTable(Document pdfDoc, PdfWriter writer, Linha line) throws Exception {
		drawRevisionTableHeader(pdfDoc, writer, line);
		drawRevisionTableContent(pdfDoc, writer, line);
		
	}
	
	private JSONArray getFileRevisionMap(String filePath) throws Exception {
		JSONArray data = new JSONArray();
		
		PdfReader reader = new PdfReader(filePath);
		PdfReaderContentParser textParser = new PdfReaderContentParser(reader);
		TextExtractionStrategy strategy;
		
		ArrayList<ArrayList<String>> fileLines = new ArrayList<ArrayList<String>>();

		for (int j = 1; j <= reader.getNumberOfPages(); j++) {
			strategy = textParser.processContent(j, new SimpleTextExtractionStrategy());
			
			fileLines.add(new ArrayList<String>(Arrays.asList(strategy.getResultantText().split("\n"))));

		}

		reader.close();
		
		for (int i = 0; i < fileLines.size(); i++) {
			ArrayList<String> pageLinesText = fileLines.get(i);
			
			for (int j = 0; j < pageLinesText.size(); j++) {
				String lineText = pageLinesText.get(j);
				
				// Separa as palavras da linha
				ArrayList<String> lineWords = new ArrayList<String>(Arrays.asList(lineText.split(" ")));
				
				if(lineWords.contains("REVISION")) {
					int revision = Integer.parseInt(lineWords.get(lineWords.indexOf("REVISION") + 1));
					int page = Integer.parseInt(lineWords.get(lineWords.indexOf("Page") + 1));
					
					JSONObject pageData = new JSONObject();
					
					pageData.put("revision", revision);
					pageData.put("page", page);
					
					data.put(pageData);
					
				}
				
			}
			
		}
		
		return data;
	}
	
	private JSONObject getTableLineData(Linha line) throws Exception {
		String file = getFileName(line);
		String filePath = line.getFilePath();
		String lineId = String.valueOf(line.getId());
		
		JSONArray revisionData = getFileRevisionMap(filePath + "\\" + file);

		JSONObject data = new JSONObject();
		
		data.put("new", revisionData);
		
		if(this.oldLinesData.has(lineId) && this.oldLinesData.getJSONObject(lineId).has("oldFile")) {
			String oldFilePath = this.oldLinesData.getJSONObject(lineId).getString("oldFile") + "\\" + file;
			
			JSONArray previousData = getFileRevisionMap(oldFilePath);
			
			data.put("old", previousData);
			
		}
		
		return data;
	}
	
	private void drawRevisionTableContent(Document pdfDoc, PdfWriter writer, Linha line) throws Exception {
		List<Linha> linhas = this.project.getCodelist().getLinhas();
		ArrayList<ArrayList<HashMap<String, String>>> tableData = new ArrayList<ArrayList<HashMap<String, String>>>();
		
		int lepIndex = -1;
		String lepCode = "";
		String lepBlock = "";
		
		for (int i = 0; i < linhas.size(); i++) {
			Linha linha = linhas.get(i);
			
			if(sameRemark(line, linha)) {
				if(linha.getBlockName().equals("LEP")) {
					lepIndex = tableData.size() - 1;
					
					lepBlock = line.getSectionNumber() + "-" + line.getBlockName();
						
					lepCode = line.getCode();
					
				}else {
					JSONObject lineData = getTableLineData(linha);
					
					ArrayList<HashMap<String, String>> dataToWrite = getDataToWrite(lineData, linha);
					
					tableData.add(dataToWrite);
					
				}
					
			}
			
		}
		
		int estimatedPages = getEstimatedPages(tableData);
		
		ArrayList<HashMap<String, String>> lepData = getLepData(estimatedPages, lepCode, lepBlock);
		
		tableData.add(lepIndex, lepData);
		tableData.remove(lepIndex + 1);
		
		drawBlockData(pdfDoc, writer, tableData, line);
		
	}
	
	private ArrayList<HashMap<String, String>> getLepData(int estimatedPages, String lepCode, String lepBlock) {
		ArrayList<HashMap<String, String>> lepData = new ArrayList<HashMap<String,String>>();
		String revision = this.project.getLastRevision().getVersion() > 10 ? "REVISION " + this.project.getLastRevision().getVersion() : "REVISION 0" +  this.project.getLastRevision().getVersion();
		
		for (int i = 0; i < estimatedPages; i++) {
			HashMap<String, String> pageLine = new HashMap<String, String>();
			
			pageLine.put("block", lepBlock);
			pageLine.put("code", lepCode);
			pageLine.put("page", String.valueOf(i));
			pageLine.put("modified", "*");
			pageLine.put("action", "");
			pageLine.put("change", revision);
			
			lepData.add(pageLine);
			
		}
		
		return lepData;
	}

	private int getEstimatedPages(ArrayList<ArrayList<HashMap<String, String>>> tableData) {
        int content = 0;
        int pageCount = page;
        
		for (int i = 0; i < tableData.size(); i++) {
        	ArrayList<HashMap<String, String>> dataToWrite = tableData.get(i);
        	
            for (int j = 0; j < dataToWrite.size(); j++) {
            	content++;
            	
            	if(content == 35) {
            		pageCount++;
            		content = 0;
            		
            	}
            	
            }
            
        }
        
		return pageCount;
	}

	private void drawBlockData(Document pdfDoc, PdfWriter writer, ArrayList<ArrayList<HashMap<String, String>>> tableData, Linha line) throws Exception {
        PdfContentByte canvas = writer.getDirectContent();
        int contentCount = 0;
        
        canvas.setColorStroke(BLACK);
        
        for (int i = 0; i < tableData.size(); i++) {
        	ArrayList<HashMap<String, String>> dataToWrite = tableData.get(i);
        	
            for (int j = 0; j < dataToWrite.size(); j++) {
    			HashMap<String, String> data = dataToWrite.get(j);
    			
    	        PDFCoordinates textCoordinates1 = new PDFCoordinates(60, this.tableLinePosition);
    	        PDFCoordinates textCoordinates2 = new PDFCoordinates(160, this.tableLinePosition);
    	        PDFCoordinates textCoordinates3 = new PDFCoordinates(250, this.tableLinePosition);
    	        PDFCoordinates textCoordinates4 = new PDFCoordinates(410, this.tableLinePosition);
    	        PDFCoordinates textCoordinates5 = new PDFCoordinates(300, this.tableLinePosition);
    	        PDFCoordinates textCoordinates6 = new PDFCoordinates(330, this.tableLinePosition);
    	        
    	        PDFEditor.writeText(canvas, textCoordinates1, data.get("block"));
    	        PDFEditor.writeText(canvas, textCoordinates2, data.get("code"));
    	        PDFEditor.writeText(canvas, textCoordinates3, data.get("page"));
    	        PDFEditor.writeText(canvas, textCoordinates4, data.get("change"));
    	        PDFEditor.writeText(canvas, textCoordinates5, data.get("modified"), true);
    	        PDFEditor.writeText(canvas, textCoordinates6, data.get("action"), true);
    	        
    	        this.tableLinePosition -= 15;
    	        
    	        contentCount++;
    	        
    	        if(contentCount >= 35) {
    	        	newPage(pdfDoc, writer, line);
    	        	contentCount = 0;
    	        	
    	        	this.tableLinePosition = 700;
    	        	
    	        	drawRevisionTableHeader(pdfDoc, writer, line);
    	        	
    	        }
    	        
    		}
            
            PDFCoordinates bottomLine11 = new PDFCoordinates(40, this.tableLinePosition + 10);
            PDFCoordinates bottomLine12 = new PDFCoordinates(PDFBuilder.PDF_WIDTH - 40, this.tableLinePosition + 10);
            
            PDFEditor.drawLine(canvas, bottomLine11, bottomLine12);
            
            this.tableLinePosition -= 10;
            
		}
		
	}

	private ArrayList<HashMap<String, String>> getDataToWrite(JSONObject lineData, Linha line){
		ArrayList<HashMap<String, String>> dataToWrite = new ArrayList<HashMap<String,String>>();
		
		if(!lineData.has("old")) {
			JSONArray data = lineData.getJSONArray("new");
			
			for (int i = 0; i < data.length(); i++) {
				JSONObject lineJson = data.getJSONObject(i);
				
				String page = String.valueOf(lineJson.getInt("page"));
				String change = "ORIGINAL";
				String modified = "*";
				String block;
				
				if(line.getSubsectionNumber() == null) {
					block = line.getSectionNumber() + "-" + line.getBlockNumber();
				}else {
					block = line.getSectionNumber() + "-" + line.getSubsectionNumber() + "-" + line.getBlockNumber();
				}
				
				if(line.getBlockName().equals("LEP") || line.getBlockName().equals("TOC") || line.getBlockName().equalsIgnoreCase("Cover")) {
					block = line.getSectionNumber() + "-" + line.getBlockName();
					
					if(line.getBlockName().equalsIgnoreCase("Cover")) {
						block = line.getSectionNumber() + "-TITLE";
						
					}
					
				}
				
				String code = line.getCode();
				String action = "new";
				
				HashMap<String, String> pageLine = new HashMap<String, String>();
				
				pageLine.put("block", block);
				pageLine.put("code", code);
				pageLine.put("page", page);
				pageLine.put("modified", modified);
				pageLine.put("action", action);
				pageLine.put("change", change);
				
				dataToWrite.add(pageLine);
				
			}
			
		}else {
			JSONArray comparedData = compareRevisionsData(lineData, line);
			
			for (int i = 0; i < comparedData.length(); i++) {
				JSONObject lineJson = comparedData.getJSONObject(i);
				
				String page = String.valueOf(lineJson.getInt("page"));
				String change = lineJson.getString("change").length() > 1 ? "REVISION " + lineJson.getString("change") : "REVISION 0" +  lineJson.getString("change");
				String modified = lineJson.getString("modified");
				String action = lineJson.getString("action");
				String block;
				
				if(line.getSubsectionNumber() == null) {
					block = line.getSectionNumber() + "-" + line.getBlockNumber();
				}else {
					block = line.getSectionNumber() + "-" + line.getSubsectionNumber() + "-" + line.getBlockNumber();
				}
				
				if(line.getBlockName().equals("LEP") || line.getBlockName().equals("TOC") || line.getBlockName().equalsIgnoreCase("Cover")) {
					block = line.getSectionNumber() + "-" + line.getBlockName();
					
					if(line.getBlockName().equalsIgnoreCase("Cover")) {
						block = line.getSectionNumber() + "-TITLE";
						
					}
					
				}
				
				String code = line.getCode();
				
				HashMap<String, String> pageLine = new HashMap<String, String>();
				
				pageLine.put("block", block);
				pageLine.put("code", code);
				pageLine.put("page", page);
				pageLine.put("modified", modified);
				pageLine.put("action", action);
				pageLine.put("change", change);
				
				dataToWrite.add(pageLine);
				
			}
			
		}
		
		return dataToWrite;
	}
	
	private JSONArray compareRevisionsData(JSONObject lineData, Linha line) {
		JSONArray comparedData = new JSONArray();
		
		JSONArray oldData = lineData.getJSONArray("old");
		JSONArray newData = lineData.getJSONArray("new");
		
		for (int i = 0; i < newData.length(); i++) {
			JSONObject newPageData = newData.getJSONObject(i);
			
			String page = String.valueOf(newPageData.getInt("page"));
			String modified;
			String change;
			String action;
			
			if(i > oldData.length()) {
				modified = "";
				action = "";
				change = String.valueOf(newPageData.getInt("revision"));
				
			}else {
				JSONObject oldPageData = oldData.getJSONObject(i);
				
				int oldRevisionVersion = oldPageData.getInt("revision");
				int newRevisionVersion = newPageData.getInt("revision");
				
				if(newRevisionVersion > oldRevisionVersion) {
					modified = "*";
					action = "";
					change = String.valueOf(newRevisionVersion);

				}else {
					modified = "";
					action = "";
					change = String.valueOf(newRevisionVersion);
					
				}
				
			}
			
			JSONObject newPage = new JSONObject();
			
			newPage.put("page", page);
			newPage.put("modified", modified);
			newPage.put("action", action);
			newPage.put("change", change);
		
			comparedData.put(newPage);
			
		}
		
		if(newData.length() < oldData.length()) {
			int newRevisionVersion = line.getActualRevision();
			
			for (int j = newData.length() - 1; j < oldData.length(); j++) {
				String modified = "*";
				String action = "del";
				String change = String.valueOf(newRevisionVersion);
				
				JSONObject newPage = new JSONObject();
				
				newPage.put("page", j);
				newPage.put("modified", modified);
				newPage.put("action", action);
				newPage.put("change", change);
			
				comparedData.put(newPage);
				
			}
			
		}
		
		return comparedData;
	}

	private boolean sameRemark(Linha line1, Linha line2) {
		List<Remark> remark1 = line1.getRemarks();
		List<Remark> remark2 = line2.getRemarks();
		
		for (int i = 0; i < remark1.size(); i++) {
			Remark remark = remark1.get(i);
			
			String traco = remark.getTraco();
			
			for (int j = 0; j < remark2.size(); j++) {
				if(remark2.get(i).getTraco().equals(traco)) {
					return true;
				}
				
			}
			
		}
		
		return false;
	}

	private void newPage(Document pdfDoc, PdfWriter writer, Linha line) throws Exception {
		pdfDoc.newPage();
		
		this.page++;
		
		drawHeader(writer);
		drawFooter(writer, line);
		
	}
	
	private void addIntentionallyBlankPage(Document pdfDoc, PdfWriter writer, Linha line) throws Exception {
		pdfDoc.newPage();
		
		this.page++;
		
		drawHeader(writer);
		
		PdfContentByte canvas = writer.getDirectContent();
		
        PDFCoordinates blankTextPosition = new PDFCoordinates((PDFBuilder.PDF_WIDTH / 2) - 65, PDFBuilder.PDF_HEIGHT / 2);
        PDFEditor.writeText(canvas, blankTextPosition, "INTENTIONALLY BLANK");
		
		drawFooter(writer, line);
		
	}
	
	private void drawRevisionTableHeader(Document pdfDoc, PdfWriter writer, Linha line) throws Exception {
		newPage(pdfDoc, writer, line);
		
        PdfContentByte canvas = writer.getDirectContent();
        
        canvas.setColorStroke(BLACK);
		
        PDFCoordinates bottomLine1 = new PDFCoordinates(40, 740);
        PDFCoordinates bottomLine2 = new PDFCoordinates(PDFBuilder.PDF_WIDTH - 40, 740);
        
        PDFEditor.drawLine(canvas, bottomLine1, bottomLine2);
        
        PDFCoordinates bottomLine11 = new PDFCoordinates(40, 720);
        PDFCoordinates bottomLine12 = new PDFCoordinates(PDFBuilder.PDF_WIDTH - 40, 720);
        
        PDFEditor.drawLine(canvas, bottomLine11, bottomLine12);
        
        PDFCoordinates textCoordinates1 = new PDFCoordinates(70, 725);
        PDFCoordinates textCoordinates2 = new PDFCoordinates(150, 725);
        PDFCoordinates textCoordinates3 = new PDFCoordinates(240, 725);
        PDFCoordinates textCoordinates4 = new PDFCoordinates(410, 725);
        
        PDFEditor.writeText(canvas, textCoordinates1, "Block");
        PDFEditor.writeText(canvas, textCoordinates2, "Code");
        PDFEditor.writeText(canvas, textCoordinates3, "Page");
        PDFEditor.writeText(canvas, textCoordinates4, "Change");
        
	}
	
	private void writeRevisionsSummary(Document pdfDoc, PdfWriter writer, Linha line) throws Exception {
		drawHeader(writer);
		drawFooter(writer, line);
		
		String title = "LIST OF EFFECTIVE PAGES";
		
		PdfContentByte canvas = writer.getDirectContent();
		
        PDFCoordinates revisionTextTitlePosition = new PDFCoordinates((PDFBuilder.PDF_WIDTH / 2) - 100, PDFBuilder.PDF_HEIGHT - 150);
        PDFEditor.writeText(canvas, revisionTextTitlePosition, title, 16, true);
        
        Set<Revisao> revisions = this.project.getRevisions();
        
        int textX = (PDFBuilder.PDF_WIDTH / 2) - 145;
        int textY = PDFBuilder.PDF_HEIGHT - 150;
        
        int lineHeight = 20;
        int revisionCount = 0;
        int revisionsInPage = 0;
        
        for	(Revisao revision : revisions) {
        	if(revisionsInPage == 25) {
        		newPage(pdfDoc, writer, line);
        		
        		revisionsInPage = 0;
        		lineHeight = 20;
        		
        	}
        	
        	int posX = textX;
        	
        	if(revisionCount >= 10) {
        		posX -= 5;
        		
        	}
        	
        	String formatedDate = getFormatedCreationDate(revision);
        	
        	String revisionVersion = "REVISION ................. " + revision.getVersion() + " .................. " + formatedDate;
        	
        	PDFCoordinates revisionTextPosition = new PDFCoordinates(posX, textY - lineHeight);
            PDFEditor.writeText(canvas, revisionTextPosition, revisionVersion, 14);
            
            lineHeight += 20;
            revisionCount++;
            revisionsInPage++;
            
		}
        
        addIntentionallyBlankPage(pdfDoc, writer, line);
        
	}

	private String getFormatedCreationDate(Revisao revision) {
		LocalDateTime date = revision.getCreationDate();
		
		if(date == null) {
			date = LocalDateTime.now();
			
		}
		
		int day = date.getDayOfMonth();
		String month = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.US).toUpperCase();
		int year = date.getYear();
		
		String formatedDate = month + " " + day + ", " + year;
		
		return formatedDate;
	}

	private String getOutputFolder(Linha line) {
		String path = FileVerifications.fileDestination(line, this.project.getNome())[0];
		
		int actualRevision = this.project.getLastRevision().getVersion();
		
		path = path.replace("Master", "Rev\\Rev" + actualRevision + "\\");
		
		return path;
	}

	private void drawFooter(PdfWriter writer, Linha line) throws Exception {
        String projectName = this.project.getNome();
        String revisionText = getRevisionFooterText();
        String codeText = "code " + line.getCode();
        
        PdfContentByte canvas = writer.getDirectContent();
        
        canvas.setColorStroke(BLACK);
        
        PDFCoordinates projectNameText = new PDFCoordinates(35, 55);
        PDFEditor.writeText(canvas, projectNameText, projectName, 11, 90);
        
        PDFCoordinates bottomLine1 = new PDFCoordinates(40, 55);
        PDFCoordinates bottomLine2 = new PDFCoordinates(PDFBuilder.PDF_WIDTH - 40, 55);
        PDFEditor.drawLine(canvas, bottomLine1, bottomLine2);
        
        PDFCoordinates revisionTextPosition = new PDFCoordinates(45, 40);
        PDFEditor.writeText(canvas, revisionTextPosition, revisionText, 11);
        
        PDFCoordinates codeTextPosition = new PDFCoordinates((PDFBuilder.PDF_WIDTH / 2) - 20, 40);
        PDFEditor.writeText(canvas, codeTextPosition, codeText, 12);
        
        PDFCoordinates lepTextPosition = new PDFCoordinates((PDFBuilder.PDF_WIDTH / 2) - 20, 65);
        PDFEditor.writeText(canvas, lepTextPosition, "0-LEP", 16, true);
        
        PDFCoordinates pageTextCoord = new PDFCoordinates(PDFBuilder.PDF_WIDTH  - 95, 40);
        PDFEditor.writeText(canvas, pageTextCoord, "Page " + page, 16);
        
        canvas.closePathStroke();
		
	}

	private String getRevisionFooterText() {
		Revisao lastRevision = this.project.getLastRevision();
		
		String text = "REVISION " + lastRevision.getVersion();
		
		return text;
	}

	private void drawHeader(PdfWriter writer) throws Exception {
        PdfContentByte canvas = writer.getDirectContent();
        
        canvas.setColorStroke(BLACK);

        PDFCoordinates middleLine1 = new PDFCoordinates(PDFBuilder.PDF_WIDTH / 2, 760);
        PDFCoordinates middleLine2 = new PDFCoordinates(PDFBuilder.PDF_WIDTH / 2, 830);
        
        PDFCoordinates bottomLine1 = new PDFCoordinates(40, 780);
        PDFCoordinates bottomLine2 = new PDFCoordinates(PDFBuilder.PDF_WIDTH - 40, 780);
        
        PDFEditor.drawLine(canvas, middleLine1, middleLine2);
        PDFEditor.drawLine(canvas, bottomLine1, bottomLine2);
        
        PDFCoordinates textPosition = new PDFCoordinates(PDFBuilder.PDF_WIDTH - 145, 765);
        
        PDFEditor.writeText(canvas, textPosition, "List of Effective Pages", 10);
        
        canvas.closePathStroke();
		
	}

	private String getFileName(Linha line) {
		String fileName = FileVerifications.fileDestination(line, this.project.getNome())[1];
		
		return fileName;
	}
	
	private ArrayList<Linha> getProjectRemarks(){
		ArrayList<Linha> projectRemarks = new ArrayList<Linha>();
		
		List<Linha> lines = this.project.getCodelist().getLinhas();
		
		for (int i = 0; i < lines.size(); i++) {
			Linha line = lines.get(i);
			
			String lineBlock = line.getBlockName();
			
			if(lineBlock.equals("LEP")) {
				projectRemarks.add(line);
				
			}
			
		}
		
		return projectRemarks;
	}

}
