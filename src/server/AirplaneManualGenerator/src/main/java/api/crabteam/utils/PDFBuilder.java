package api.crabteam.utils;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;



public class PDFBuilder {
	
	public static final int PDF_WIDTH = 600;
	public static final int PDF_HEIGHT = 850;

	public static PDFFile createNewPdfDocument(String outputFolderPath, String pdfFileName) throws Exception {
		Document pdfDoc = new Document(PageSize.A4);
		
		File pdfFile = new File(outputFolderPath + "\\" + pdfFileName);
		pdfFile.createNewFile();
		
		FileOutputStream output = new FileOutputStream(pdfFile);
		
		PdfWriter writer = PdfWriter.getInstance(pdfDoc, output);
		
		PDFFile pdfObject = new PDFFile(writer, pdfDoc);
		
		return pdfObject;
	}
	
}
