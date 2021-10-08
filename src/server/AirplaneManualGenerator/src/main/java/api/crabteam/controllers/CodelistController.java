package api.crabteam.controllers;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import api.crabteam.model.entities.Codelist;
import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.builders.CodelistBuilder;
import api.crabteam.model.entities.builders.ProjetoBuilder;
import api.crabteam.model.repositories.CodelistRepository;
import api.crabteam.model.repositories.LinhaRepository;
import api.crabteam.model.repositories.ProjetoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Codelist controller
 * @author Bárbara Port
 *
 */
@RestController
@RequestMapping("/codelist")
@Api("Codelist API")
public class CodelistController {
	
	@Autowired
	CodelistRepository codelistRepository;
	
	@Autowired
	CodelistBuilder codelistBuilder;
	
	@Autowired
	ProjetoRepository projetoRepository;
	
	@Autowired
	ProjetoBuilder projetoBuilder;
	
	@Autowired
	LinhaRepository linhaRepository;
	
	private static final String PROJECTS_DIRECTORY = System.getenv("APIEmbraerCodelistFolder");
	
	/**
	 * Realiza o upload de um arquivo de codelist para um projeto de manual
	 * @param newCodelist
	 * @param projectName
	 * @return ResponseEntity
	 * @author Bárbara Port
	 * @throws Exception 
	 */
	@PostMapping("/upload")
    @ApiOperation("Creates a new codelist by uploading it.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Codelist successfully created."),
        @ApiResponse(code = 400, message = "Codelist wasn't created.")
    })
	public ResponseEntity<?> uploadCodelist (@RequestParam(name = "newCodelist") MultipartFile newCodelist, @RequestParam(name = "projectName") String projectName) throws Exception {
		
		CodelistBuilder codelistBuilder = new CodelistBuilder(newCodelist.getBytes(), projectName);
		Codelist codelist = codelistBuilder.getBuildedCodelist();
		
		File destinationAbsolutePath = new File(PROJECTS_DIRECTORY + "\\" + projectName + "_codelist.xlsx");
		newCodelist.transferTo(destinationAbsolutePath);
		
		List<Linha> linhas = codelist.getLinhas();
		
		Projeto projeto = projetoRepository.findByName(projectName);
		Codelist projectCodelist = projeto.getCodelist();
		
		codelistRepository.deleteAllCodelistLines(projectCodelist.getId());
		List<Linha> linhasCodelist = projectCodelist.getLinhas();
		for (Linha linha : linhasCodelist) {
			linhaRepository.deleteById(linha.getId());
		}
		
		projeto = projetoRepository.findByName(projectName);
		projectCodelist = projeto.getCodelist();
		linhasCodelist = projectCodelist.getLinhas();
		for (Linha linha : linhas) {
			projectCodelist.addLinha(linha);
		}
		projeto.setCodelist(projectCodelist);
		projetoRepository.save(projeto);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@GetMapping("/getLines")
    @ApiOperation("Get all codelists' lines.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "The codelist lines were found.")
    })
	public ResponseEntity<?> getCodelistLines (@RequestParam(name = "codelist") String name) {
		
		Codelist codelist = codelistRepository.findByName(name);
		List<?> linhas = codelistRepository.findAllLines(codelist.getId());
		
		return new ResponseEntity<List<?>>(linhas, HttpStatus.OK);
		
	}

}