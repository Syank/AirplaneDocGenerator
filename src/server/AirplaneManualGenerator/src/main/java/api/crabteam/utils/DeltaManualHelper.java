package api.crabteam.utils;

import java.util.List;

import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.Remark;
import api.crabteam.model.entities.Revisao;

public class DeltaManualHelper {
	
	Projeto project;
	
	static String deltaLetter;
	static String deltaCover;
	static String deltaLEP;
	
	static List<String> remainingPages;
	
	public DeltaManualHelper (Projeto project) {
		this.project = project;
	}
	
	private static List<Linha> getLastRevisionLines (Projeto project) {
		Revisao revision = project.getLastRevision();
		return revision.getLinhas();
	}
	
	private static void arrangePages (Projeto project, List<Linha> lines, Remark remark) {
		String selectedRemarkID = remark.getTraco();
		String projectName = project.getNome();
		String sFilePath = "";
		for (Linha line : lines) {
			List<Remark> lineRemarks = line.getRemarks();
			for (Remark r : lineRemarks) {
				String actualRemarkID = r.getTraco();
				if (selectedRemarkID.equals(actualRemarkID)) {
					String[] result = FileVerifications.fileDestination(line, projectName);
					sFilePath = result[0] + result[1];
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
	
	public static void generateDeltaManual (Projeto project, Remark remark) {
		List<Linha> lastRevisionLines = getLastRevisionLines(project);
		arrangePages(project, lastRevisionLines, remark);
		System.err.println(deltaLetter);
		System.err.println(deltaCover);
		System.err.println(deltaLEP);
		for (String page : remainingPages) {
			System.err.println(page);
		}
	}
}
