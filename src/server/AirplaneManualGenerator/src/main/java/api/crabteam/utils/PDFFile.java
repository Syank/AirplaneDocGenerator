package api.crabteam.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFFile {

	private PdfWriter writer;
	private Document pdfDocument;
	
	
	public PDFFile(PdfWriter writer, Document pdfDocument) {
		this.writer = writer;
		this.pdfDocument = pdfDocument;
		
	}
	
	public PdfWriter getWriter() {
		return writer;
	}
	public void setWriter(PdfWriter writer) {
		this.writer = writer;
	}
	public Document getPdfDocument() {
		return pdfDocument;
	}
	public void setPdfDocument(Document pdfDocument) {
		this.pdfDocument = pdfDocument;
	}
	
}
