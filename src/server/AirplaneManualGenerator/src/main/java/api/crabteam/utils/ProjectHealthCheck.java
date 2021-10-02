package api.crabteam.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import api.crabteam.model.entities.Codelist;
import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.Remark;

public class ProjectHealthCheck {
	
	private ProjectSituation situation;
	
	public ProjectHealthCheck(Projeto project) {
		Codelist codelist = project.getCodelist();
		ProjectSituation situation = new ProjectSituation();
		
		if(!containsCodelist(codelist)) {
			situation.setOk(false);
			situation.setSituationTitle("Projeto sem codelist");
			situation.setSituationMessage("O projeto não possuí uma codelist, adicione linhas ou importe uma codelist já pronta para corrigir a situação");
			
		}else if(!isRemarksOk(codelist)) {
			situation.setOk(false);
			situation.setSituationTitle("Incompatibilidade de traços");
			situation.setSituationMessage("Existem uma ou mais linhas da codelist que possuem traços com declarações repetidas para o mesmo bloco");
			
		}else if (!isCodesOk(codelist)) {
			situation.setOk(false);
			situation.setSituationTitle("Códigos (CODE) inconsistentes");
			situation.setSituationMessage("Há uma ou mais linhas de um mesmo bloco que possuem o código (CODE) repetido");
			
		}else if(!isLinesOk(codelist)) {
				situation.setOk(false);
				situation.setSituationTitle("Linha da codelist sem arquivo associado");
				situation.setSituationMessage("Uma ou mais linhas da codelist não possuem arquivos associados a elas");
			
		}else {
			situation.setOk(true);
			situation.setSituationTitle("Projeto sem problemas");
			situation.setSituationMessage("Nenhuma inconsistência foi encontrada no projeto");
			
		}
		
		this.situation = situation;
		
	}
	
	private boolean isCodesOk(Codelist codelist) {
		HashMap<String, ArrayList<Linha>> sections = getSections(codelist);
		
		for (String sectionKey : sections.keySet()) {
			ArrayList<Linha> sectionLines = sections.get(sectionKey);
			
			ArrayList<String> linesCodes = new ArrayList<String>();
			
			for (int i = 0; i < sectionLines.size(); i++) {
				Linha line = sectionLines.get(i);
				
				String lineCode = line.getCode();
				
				if(!linesCodes.contains(lineCode)) {
					linesCodes.add(lineCode);
					
				}else {
					return false;
				}
				
			}
			
		}
		
		return true;
	}

	private boolean isRemarksOk(Codelist codelist) {
		HashMap<String, ArrayList<Linha>> sections = getSections(codelist);
		
		for (String sectionKey : sections.keySet()) {
			ArrayList<Linha> sectionLines = sections.get(sectionKey);
			
			for (int i = 0; i < sectionLines.size(); i++) {
				Linha lineToCheck = sectionLines.get(i);
				
				String lineCodeToCheck = lineToCheck.getCode();
				
				List<Remark> lineRemarksToCheck = lineToCheck.getRemarks();
				
				for (int j = 0; j < sectionLines.size(); j++) {
					Linha line = sectionLines.get(j);
					
					String lineCode = line.getCode();
					
					if(lineCodeToCheck.equals(lineCode)) {
						continue;
					}
					
					List<Remark> lineRemarks = line.getRemarks();
					
					for (int k = 0; k < lineRemarksToCheck.size(); k++) {
						Remark lineRemarkToCheck = lineRemarksToCheck.get(k);
						
						String lineRemarkTracoToCheck = lineRemarkToCheck.getTraco();
						
						for (int l = 0; l < lineRemarks.size(); l++) {
							Remark lineRemark = lineRemarks.get(l);
						
							String lineRemarkTraco = lineRemark.getTraco();
							
							if(lineRemarkTracoToCheck.equals(lineRemarkTraco)) {
								return false;
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
		return true;
	}
	
	private HashMap<String, ArrayList<Linha>> getSections(Codelist codelist){
		HashMap<String, ArrayList<Linha>> sections = new HashMap<String, ArrayList<Linha>>();
		
		List<Linha> linhas = codelist.getLinhas();
		
		for (int i = 0; i < linhas.size(); i++) {
			Linha linha = linhas.get(i);

			String section = linha.getSecao();
			String block = linha.getBlock();
			
			String key = section + "-" + block;
			
			if(!sections.containsKey(key)) {
				ArrayList<Linha> sectionLines = new ArrayList<Linha>();
				
				sectionLines.add(linha);
				
				sections.put(key, sectionLines);
				
			}else {
				sections.get(key).add(linha);
				
			}
			
		}
		
		return sections;
	}

	private boolean isLinesOk(Codelist codelist) {
		List<Linha> linhas = codelist.getLinhas();
		
		for (int i = 0; i < linhas.size(); i++) {
			Linha linha = linhas.get(i);
			
			String filePath = linha.getFilePath();
			
			if(filePath == null) {
				return false;
			}
			
		}
		
		return true;
	}

	private boolean containsCodelist(Codelist codelist) {
		if(codelist == null) {
			return false;
		}
		
		return true;
	}

	public ProjectSituation getSituation() {
		return situation;
	}

	public void setSituation(ProjectSituation situation) {
		this.situation = situation;
	}
	
}
