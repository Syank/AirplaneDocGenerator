package api.crabteam.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.Remark;
import api.crabteam.model.entities.Revisao;
import api.crabteam.model.enumarations.EnvironmentVariables;

public class DeltaManualHelper {
	
	private static Revisao getLastRevision (Projeto project) {
		Revisao revision = project.getLastRevision();
		return revision;
	}
	
	private static ManualsHelper arrangePages (Projeto project, List<Linha> projectLines, Remark remark) {
		String selectedRemarkID = remark.getTraco();
		String projectName = project.getNome();
		String deltaLetter = "";
		String deltaCover = "";
		String deltaLEP = "";
		List<String> remainingPages = new ArrayList<>();
		String sFilePath = "";
		int lastRevision = getLastRevision(project).getVersion();
		for (Linha line : projectLines) {
			if (line.getActualRevision() == lastRevision) {
				List<Remark> lineRemarks = line.getRemarks();
				for (Remark r : lineRemarks) {
					String actualRemarkID = r.getTraco();
					if (selectedRemarkID.equals(actualRemarkID)) {
						String[] result = FileVerifications.fileDestination(line, projectName);
						sFilePath = String.join("\\", result);
						switch (line.getBlockNumber()) {
							case "00":
								deltaLetter = sFilePath;
								break;
							case "01":
								deltaCover = sFilePath;
								break;
							case "02":
								deltaLEP = sFilePath;
								break;
							default:
								remainingPages.add(sFilePath);
								break;
						}
					}
				}
			}
			
		}
		return new ManualsHelper(deltaLetter, deltaCover, deltaLEP, remainingPages);
	}
	
	private static void addFileToPDF (PdfCopy copy, String file) throws DocumentException, IOException {
		URL urlFile = new URL("file:\\".concat(file));
        PdfReader reader = new PdfReader(urlFile);
        copy.addDocument(reader);
        copy.freeReader(reader);
        reader.close();
	}
	
	private static void mergePdfFiles (ManualsHelper manual, String projectName, String manualFileName) throws DocumentException, IOException {
        String pdfFileName = EnvironmentVariables.PROJECTS_FOLDER.getValue()
        					 .concat("\\")
        					 .concat(projectName)
        					 .concat("\\")
        					 .concat("Master")
        					 .concat("\\")
        					 .concat(manualFileName)
        					 .concat(".pdf");
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(pdfFileName));
        document.open();
        addFileToPDF(copy, manual.getDeltaLetter());
        addFileToPDF(copy, manual.getDeltaCover());
        addFileToPDF(copy, manual.getDeltaLEP());
        for (String file : manual.getRemainingPages()){
        	addFileToPDF(copy, file);
        }
        document.close();
	}
	
	public static void generateDeltaManual (Projeto project, Remark remark) throws DocumentException, IOException {
		
		List<Linha> projectLines = project.getCodelist().getLinhas();
		ManualsHelper manual = arrangePages(project, projectLines, remark);
		String manualFileName = project.getNome()
								.concat("-")
								.concat(remark.getTraco())
								.concat("-")
								.concat("REV".concat(String.valueOf(getLastRevision(project).getVersion())))
								.concat("-")
								.concat("DELTA");
		mergePdfFiles(manual, project.getNome(), manualFileName);
	}
}
