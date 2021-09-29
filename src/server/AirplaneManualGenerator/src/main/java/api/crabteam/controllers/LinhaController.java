package api.crabteam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import antlr.debug.NewLineListener;
import api.crabteam.controllers.requestsBody.NewLine;
import api.crabteam.model.entities.Codelist;
import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.builders.LinhaBuilder;
import api.crabteam.model.repositories.CodelistRepository;
import api.crabteam.model.repositories.LinhaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Linha controller
 * @author Bárbara Port
 *
 */
@RestController
@RequestMapping("/linha")
@Api("Line API")
public class LinhaController {
	
	@Autowired
	CodelistRepository codelistRepository;
	
	@Autowired
	LinhaRepository linhaRepository;
	
	@Autowired
	LinhaBuilder linhaBuilder;
	
	@PostMapping("/new/{codelistName}")
	@ApiOperation("Creates a new line into a codelist.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Line successfully created."),
        @ApiResponse(code = 400, message = "Line wasn't created.")
    })
	public ResponseEntity<?> createLine (@RequestBody NewLine newLine, @PathVariable String codelistName) {
		
		Codelist codelist = codelistRepository.findByName(codelistName);
		
		Linha linha = new Linha();
		linha.setSectionNumber(newLine.getSectionNumber());
		linha.setSubsectionNumber(newLine.getSubsectionNumber());
		linha.setBlockNumber(newLine.getBlockNumber());
		linha.setBlockName(newLine.getBlockName());
		linha.setCode(newLine.getCode());
		linha.setFilePath(newLine.getFilePath());
		linha.setRemarks(newLine.getRemarks());
		
		try {
			linhaRepository.save(linha);
			try {
				codelist.addLinha(linha);
				codelistRepository.save(codelist);
			}
			catch (Exception e) {
				return new ResponseEntity<String>("A linha não foi adicionada à codelist.", HttpStatus.BAD_REQUEST);
			}
		}
		catch (Exception e) {
			return new ResponseEntity<String>("A linha não foi criada.", HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		
	}

}
