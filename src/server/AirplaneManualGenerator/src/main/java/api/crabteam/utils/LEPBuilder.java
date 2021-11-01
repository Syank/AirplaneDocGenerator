package api.crabteam.utils;

import java.io.File;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.Revisao;
import api.crabteam.model.enumarations.EnvironmentVariables;

public class LEPBuilder {
	
	private Projeto project;
	private String outputFolder;
	
	private static final CMYKColor BLACK = new CMYKColor(0, 0, 0, 255);

	public LEPBuilder(Projeto project) {
		this.project = project;
		
		String projectName = this.project.getNome();
		
		this.outputFolder = EnvironmentVariables.PROJECTS_FOLDER.getValue() + "\\" + projectName + "\\LEP";
		
		createLepDirectory();
		
	}
	
	public void generateLep() throws Exception {
		String fileName = getFileName();
		
		PDFFile pdfFile = PDFBuilder.createNewPdfDocument(this.outputFolder, fileName);
		
		Document pdfDoc = pdfFile.getPdfDocument();
		PdfWriter writer = pdfFile.getWriter();

		pdfDoc.open();
		
		drawHeader(writer);
		drawFooter(writer);
		
		pdfDoc.close();
		
	}
	
	private void drawFooter(PdfWriter writer) throws Exception {
        String projectName = this.project.getNome();
        String revisionText = getRevisionFooterText();
        
        PdfContentByte canvas = writer.getDirectContent();
        
        canvas.setColorStroke(BLACK);
        
        PDFCoordinates projectNameText = new PDFCoordinates(35, 55);
        PDFEditor.writeText(canvas, projectNameText, projectName, 11, 90);
        
        PDFCoordinates bottomLine1 = new PDFCoordinates(40, 55);
        PDFCoordinates bottomLine2 = new PDFCoordinates(PDFBuilder.PDF_WIDTH - 40, 55);
        PDFEditor.drawLine(canvas, bottomLine1, bottomLine2);
        
        PDFCoordinates revisionTextPosition = new PDFCoordinates(45, 40);
        PDFEditor.writeText(canvas, revisionTextPosition, revisionText, 11);
        
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

	private String getFileName() {
		String fileName = this.project.getNome() + ".pdf";
		
		return fileName;
	}

	private void createLepDirectory() {
		File directory = new File(this.outputFolder);
		
		directory.mkdirs();
		
	}
	
}
