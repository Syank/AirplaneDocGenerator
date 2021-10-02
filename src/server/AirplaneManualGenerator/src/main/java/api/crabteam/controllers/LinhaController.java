package api.crabteam.controllers;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/line")
@Api("Line API")
public class LinhaController {
	
	@Autowired
	CodelistRepository codelistRepository;
	
	@Autowired
	LinhaRepository linhaRepository;
	
	@Autowired
	LinhaBuilder linhaBuilder;
	
	
	/**
	 * Cria uma nova linha em uma codelist.
	 * @param newLine
	 * @param codelistName
	 * @return ResponseEntity
	 * @author Bárbara Port
	 */
	@PostMapping("/new/{codelistName}")
	@ApiOperation("Creates a new line into a codelist.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Line successfully created."),
        @ApiResponse(code = 400, message = "The line wasn't created.")
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
	
	/**
	 * Atualiza as informações de uma linha.
	 * @param updatedLine
	 * @param line
	 * @return ResponseEntity
	 * @author Bárbara Port
	 */
	@PutMapping("/update/{line}")
	@ApiOperation("Updates a line.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Line successfully updated."),
        @ApiResponse(code = 400, message = "The line wasn't updated.")
    })
	public ResponseEntity<?> updateLine (@RequestBody NewLine updatedLine, @PathVariable int line) {
		
		Linha linha = linhaRepository.getById(line);
		linha.setId(line);
		linha.setSectionNumber(updatedLine.getSectionNumber());
		linha.setSubsectionNumber(updatedLine.getSubsectionNumber());
		linha.setBlockNumber(updatedLine.getBlockNumber());
		linha.setBlockName(updatedLine.getBlockName());
		linha.setCode(updatedLine.getCode());
		linha.setFilePath(updatedLine.getFilePath());
		linha.setRemarks(updatedLine.getRemarks());
		
		try {
			linhaRepository.save(linha);
		}
		catch (Exception e) {
			return new ResponseEntity<String>("A linha não foi atualizada.", HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	/**
	 * Deleta uma linha de uma codelist.
	 * @param line
	 * @return ResponseEntity
	 * @author Bárbara Port
	 */
	@DeleteMapping("/delete/{line}")
	@ApiOperation("Deletes a line.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Line successfully deleted."),
        @ApiResponse(code = 400, message = "The line wasn't deleted.")
    })
	public ResponseEntity<?> deleteLine (@PathVariable int line) {
		try {
			linhaRepository.deleteById(line);
		}
		catch (Exception e) {
			return new ResponseEntity<String>("A linha não foi deletada.", HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	/**
	 * Associa um arquivo a uma linha de uma codelist.
	 * @param filePath
	 * @param line
	 * @return ResponseEntity
	 * @author Bárbara Port
	 */
	@PostMapping("/attachFile/{line}")
	@ApiOperation("Attaches a file to a line.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "File successfully attached."),
        @ApiResponse(code = 400, message = "The file wasn't attached to the line."),
        @ApiResponse(code = 404, message = "The file wasn't found.")
    })
	public ResponseEntity<?> attachFile (@RequestParam(name = "file") MultipartFile file, @PathVariable int line) {
		
		try {
			Linha linha = linhaRepository.getById(line);
			linha.setId(line);
			linha.setFilePath(":/");
			
			try {
				linhaRepository.save(linha);
			}
			catch (Exception e) {
				return new ResponseEntity<String>("O arquivo não foi associado à linha.", HttpStatus.BAD_REQUEST);
			}
			
		}
		catch (Exception exception) {
			return new ResponseEntity<String>("A linha não foi encontrada.", HttpStatus.NOT_FOUND);
		}
		
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
}
