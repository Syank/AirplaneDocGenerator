package api.crabteam.utils;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
import api.crabteam.model.enumarations.EnvironmentVariables;

public class LEPBuilder {
	
	private Projeto project;
	private String outputFolder;
	private int page = 1;
	
	private static final CMYKColor BLACK = new CMYKColor(0, 0, 0, 255);

	public LEPBuilder(Projeto project) {
		this.project = project;
		
		String projectName = this.project.getNome();
		
		this.outputFolder = EnvironmentVariables.PROJECTS_FOLDER.getValue() + "\\" + projectName + "\\LEP";
		
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
		drawRevisionTableContent(line);

		
	}
	
	private void drawRevisionTableContent(Linha line) throws Exception {
		List<Linha> linhas = this.project.getCodelist().getLinhas();

		
		
		for (int i = 0; i < linhas.size(); i++) {
			Linha linha = linhas.get(i);

			String filePath = linha.getFilePath();

			if (filePath != null && sameRemark(line, linha) && !linha.getBlockName().equals("LEP")) {
				String file = getFileName(linha);

				PdfReader reader = new PdfReader(filePath + "\\" + file);
				PdfReaderContentParser textParser = new PdfReaderContentParser(reader);
				TextExtractionStrategy strategy;

				ArrayList<String> fileLines = new ArrayList<String>();

				for (int j = 1; j <= reader.getNumberOfPages(); j++) {
					strategy = textParser.processContent(j, new SimpleTextExtractionStrategy());
					
					fileLines.add(strategy.getResultantText());

				}

				reader.close();
				
				

			}

		}
		
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
