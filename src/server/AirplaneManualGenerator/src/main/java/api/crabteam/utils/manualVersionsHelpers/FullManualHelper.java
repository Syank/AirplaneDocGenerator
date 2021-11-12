package api.crabteam.utils.manualVersionsHelpers;

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
import api.crabteam.model.enumarations.EnvironmentVariables;
import api.crabteam.utils.FileVerifications;

public class FullManualHelper {
	
	/**
	 * Método que organiza e separa as partes do manual
	 * @param project o projeto ao qual o manual pertence
	 * @param remark o remark escolhido para gerar o Delta
	 * @return o objeto com os caminhos dos arquivos para a geração do manual
	 * @author Bárbara Port
	 */
	private static ManualsHelper arrangePages (Projeto project, Remark remark) {
		List<Linha> projectLines = project.getCodelist().getLinhas();
		String selectedRemarkID = remark.getTraco();
		String projectName = project.getNome();
		String deltaLetter = "";
		String deltaCover = "";
		String deltaLEP = "";
		List<String> remainingPages = new ArrayList<>();
		String sFilePath = "";
		for (Linha line : projectLines) {
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
		return new ManualsHelper(deltaLetter, deltaCover, deltaLEP, remainingPages);
	}
	
	/**
	 * Método que concatena um arquivo ao arquivo PDF da versão Delta do manual
	 * @param copy arquivo final do manual Delta
	 * @param file caminho do arquivo a ser concatenado
	 * @throws DocumentException
	 * @throws IOException
	 * @author Bárbara Port
	 */
	private static void addFileToPDF (PdfCopy copy, String file) throws DocumentException, IOException {
		URL urlFile = new URL("file:\\".concat(file));
        PdfReader reader = new PdfReader(urlFile);
        copy.addDocument(reader);
        copy.freeReader(reader);
        reader.close();
	}
	
	/**
	 * Método que seleciona os arquivos a serem colocados no PDF do manual Delta
	 * @param manual objeto com as informações organizadas sobre cada parte essencial do manual
	 * @param projectName nome do projeto ao qual o manual pertence
	 * @param manualFileName nome do arquivo do manual, sem a extensão
	 * @throws DocumentException
	 * @throws IOException
	 * @author Bárbara Port
	 */
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
	
	/**
	 * Método que executa todos os outros métodos para gerar o manual
	 * @param project o projeto a ter o manual gerado
	 * @param remark o remark escolhido
	 * @throws DocumentException
	 * @throws IOException
	 * @author Bárbara Port
	 */
	public static void generateFullManual (Projeto project, Remark remark) throws DocumentException, IOException {
		ManualsHelper manual = arrangePages(project, remark);
		String manualFileName = project.getNome()
								.concat("-")
								.concat(remark.getTraco())
								.concat("-")
								.concat("REV".concat(String.valueOf(DeltaManualHelper.getLastRevision(project).getVersion())))
								.concat("-")
								.concat("FULL");
		mergePdfFiles(manual, project.getNome(), manualFileName);
	}
}
