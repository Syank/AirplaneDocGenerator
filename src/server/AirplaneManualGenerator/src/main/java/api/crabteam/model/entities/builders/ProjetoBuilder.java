package api.crabteam.model.entities.builders;

import java.io.IOException;

import org.springframework.stereotype.Service;

import api.crabteam.controllers.requestsBody.NewProject;
import api.crabteam.model.entities.Codelist;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.repositories.ProjetoRepository;

@Service
public class ProjetoBuilder {
	private boolean isPersisted = false;
	private String failMessage;
	
	private ProjetoRepository projetoRepository;
	
	
	public void build(NewProject newProject) throws IOException {
		String name = newProject.getNome().toUpperCase();
		String description = newProject.getDescricao();
		byte[] codelistFile = newProject.getArquivoCodelist();
		
		if(codelistFile == null) {
			CodelistBuilder codelistBuilder = new CodelistBuilder(name);
			
			Codelist codelist = codelistBuilder.getBuildedCodelist();
			
			Projeto projeto = new Projeto(name, description, codelist);
			
			this.isPersisted = persistProjeto(projeto);
			
		}
		else {
			try {
				CodelistBuilder codelistBuilder = new CodelistBuilder(codelistFile, name);
				
				Codelist codelist = codelistBuilder.getBuildedCodelist();
				
				Projeto projeto = new Projeto(name, description, codelist);
				
				this.isPersisted = persistProjeto(projeto);
				
			}
			catch (Exception e) {
				this.failMessage = e.getMessage();
				System.err.println(e);
			}
			
		}
		
	}
	
	public void setRepository(ProjetoRepository projetoRepository) {
		this.projetoRepository = projetoRepository;
	}
	
	public String getFailMessage() {
		return this.failMessage;
	}
	
	public boolean isPersisted() {
		return this.isPersisted;
	}
	
	private boolean persistProjeto(Projeto projeto) {
		try {
			this.projetoRepository.save(projeto);
			
			return true;
		}catch (Exception e) {
			return false;
		}
		
	}
	
}
