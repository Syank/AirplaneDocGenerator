package api.crabteam.utils;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;

public class PDFEditor {

	public static void drawLine(PdfContentByte pdfCanvas, PDFCoordinates initialPoint, PDFCoordinates finalPoint) {
		pdfCanvas.moveTo(initialPoint.getX(), initialPoint.getY());
        
		pdfCanvas.lineTo(finalPoint.getX(), finalPoint.getY());
        
		pdfCanvas.closePathStroke();
        
	}
	
	public static void writeText(PdfContentByte pdfCanvas, PDFCoordinates textCoord, String textToWrite, int fontSize) throws Exception {
		writeText(pdfCanvas, textCoord, textToWrite, fontSize, 0);
		
	}
	
	public static void writeText(PdfContentByte pdfCanvas, PDFCoordinates textCoord, String textToWrite) throws Exception {
		writeText(pdfCanvas, textCoord, textToWrite, 12, 0);
		
	}
	
	public static void writeText(PdfContentByte pdfCanvas, PDFCoordinates textCoord, String textToWrite, int fontSize, int textRotation) throws Exception {
		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		
		pdfCanvas.saveState();
		
		pdfCanvas.beginText();
		pdfCanvas.setFontAndSize(bf, fontSize);
		pdfCanvas.showTextAligned(0, textToWrite, textCoord.getX(), textCoord.getY(), textRotation);
		pdfCanvas.endText();
		
		pdfCanvas.restoreState();
		
	}
	
}
