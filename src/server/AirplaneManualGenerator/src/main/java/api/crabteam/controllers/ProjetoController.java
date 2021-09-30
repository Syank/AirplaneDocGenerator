package api.crabteam.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import api.crabteam.controllers.requestsBody.NewProject;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.builders.ProjetoBuilder;
import api.crabteam.model.repositories.ProjetoRepository;

/**
 * Controller associado às requisições dos projetos
 * @author Bárbara Port
 *
 */
@RestController
@RequestMapping("/project")
public class ProjetoController {

	@Autowired
	public ProjetoRepository projetoRepository;
	@Autowired
	public ProjetoBuilder builder;
	
	
	/**
	 * Método que retorna todos os projetos
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllProjects (ServletRequest request) {
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		
		if (session == null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
		}
		
		List<Projeto> projects = projetoRepository.findAll();
		
		return new ResponseEntity<List<Projeto>>(projects, HttpStatus.OK);
	}
	
	@GetMapping("/findByName")
	public ResponseEntity<?> findProjectByName(@RequestParam String projectName){
		Projeto project = projetoRepository.findByName(projectName);
		
		if(project != null) {
			return new ResponseEntity<Projeto>(project, HttpStatus.OK);
		}
		
		return new ResponseEntity<Projeto>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping("/put")
	public Projeto atualizaProjeto(@RequestBody String descricao, @RequestBody String nomeProjeto) {
		Projeto project = projetoRepository.findByName(nomeProjeto);
		project.setDescricao(descricao);

		return projetoRepository.save(project);
	}
	
	
	
	/**
	 * Cria um nome projeto e o registra no banco de dados
	 * 
	 * @param newProject - Informações necessárias para a criação do objeto do projeto.
	 *                     O objeto da requisição deve ser um JSON com as chaves <i>nome</i> e <i>descricao</i>.
	 *                     O Spring se encarregará de fazer a associação com o objeto NewProject
	 * @return Retorna <b>true</b> caso o projeto seja criado com sucesso, caso contrário, retorna <b>false</b>
	 * @author Rafael Furtado
	 * @throws IOException 
	 */
	@PostMapping("/create")
	public ResponseEntity<?> createNewProject(@RequestParam(required = false) MultipartFile codelistFile, NewProject newProject) throws IOException {
		builder.setRepository(projetoRepository);
		
		if(codelistFile != null) {
			newProject.setArquivoCodelist(codelistFile);
			
		}
		
		builder.build(newProject);
		
		if(builder.isPersisted()) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		
		String failMessage = builder.getFailMessage();
		
		return new ResponseEntity<String>(failMessage, HttpStatus.BAD_REQUEST);
	}
	
}
