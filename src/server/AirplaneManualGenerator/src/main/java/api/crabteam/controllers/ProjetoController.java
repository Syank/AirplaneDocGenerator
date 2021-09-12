package api.crabteam.controllers;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import api.crabteam.model.Projeto;
import api.crabteam.model.repositories.ProjetoRepository;

/**
 * Controller associado às requisições dos projetos
 * @author Bárbara Port
 *
 */
@RequestMapping("/project")
public class ProjetoController {

	@Autowired
	ProjetoRepository projetoRepository;
	
	@GetMapping("/all")
	/**
	 * Método que retorna todos os projetos
	 * @return
	 */
	public ResponseEntity<?> getAllProjects (ServletRequest request) {
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		
		if (session == null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
		}
		
		List<Projeto> projects = projetoRepository.findAll();
		
		return new ResponseEntity<List<Projeto>>(projects, HttpStatus.OK);
	}
}
