package api.crabteam.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import api.crabteam.controllers.requestsBody.NewLine;
import api.crabteam.controllers.requestsBody.UpdatedLine;
import api.crabteam.model.entities.Codelist;
import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.Remark;
import api.crabteam.model.entities.builders.LinhaBuilder;
import api.crabteam.model.entities.builders.RemarkBuilder;
import api.crabteam.model.repositories.CodelistRepository;
import api.crabteam.model.repositories.LinhaRepository;
import api.crabteam.model.repositories.ProjetoRepository;
import api.crabteam.utils.FileVerifications;
import api.crabteam.utils.ProjectHealthCheck;
import api.crabteam.utils.ProjectSituation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Linha controller
 * 
 * @author Bárbara Port
 *
 */
@RestController
@Api("Line API")
@RequestMapping("/codelistLine")
public class LinhaController {

	@Autowired
	CodelistRepository codelistRepository;

	@Autowired
	LinhaRepository linhaRepository;
	
	@Autowired
	ProjetoRepository projectRepository;

	@Autowired
	LinhaBuilder linhaBuilder;

	/**
	 * Cria uma nova linha em uma codelist.
	 * 
	 * @param newLine
	 * @param codelistName
	 * @return ResponseEntity
	 * @author Bárbara Port
	 */
	@PostMapping("/new")
	@ApiOperation("Creates a new line into a codelist.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Line successfully created"),
		@ApiResponse(code = 400, message = "The line wasn't created")
		}
	)
	public ResponseEntity<?> createLine(@RequestParam MultipartFile lineFile, NewLine newLine) {
		Codelist codelist = codelistRepository.findByName(newLine.getCodelistName());

		Linha linha = new Linha();
		
		linha.setSectionNumber(newLine.getSectionNumber());
		linha.setSectionName(newLine.getSectionName());
		linha.setSubsectionNumber(newLine.getSubsectionNumber());
		linha.setSubsectionName(newLine.getSubsectionName());
		linha.setBlockNumber(newLine.getBlockNumber());
		linha.setBlockName(newLine.getBlockName());
		linha.setCode(newLine.getCode());
		
		ArrayList<Remark> newRemarks = new ArrayList<Remark>();
		
		String[] remarksText = newLine.getRemarksText().split(",");
		
		for (int i = 0; i < remarksText.length; i++) {
			String text = remarksText[i];
			
			HashMap<String, String> remarkMap = new HashMap<String, String>();
			
			String[] textParts = text.split("[\\(||//)]");
			
			String traco = textParts[0].replace("-", "").trim();
			String apelido =  textParts[1].trim();
			
			remarkMap.put(traco, apelido);
			
			RemarkBuilder builder = new RemarkBuilder(remarkMap);
			
			Remark remark = builder.getBuildedRemark();
			
			newRemarks.add(remark);
			
		}

		linha.setRemarks(newRemarks);

		try {			
			String[] fileInfos = FileVerifications.fileDestination(linha, codelist.getNome());
			String strFilePath = fileInfos[0];
			String fileName = fileInfos[1];

			File destinationAbsolutePath = new File(strFilePath);
			destinationAbsolutePath.mkdirs();
			lineFile.transferTo(new File(strFilePath.concat(fileName)));
			
			linha.setFilePath(destinationAbsolutePath.getAbsolutePath());
			
			codelist.addLinha(linha);
			
			codelistRepository.save(codelist);
			
			Projeto project = projectRepository.findByName(codelist.getNome());
			
			ProjectHealthCheck healthCheck = new ProjectHealthCheck(project);
			
			ProjectSituation situation = healthCheck.getSituation();
			
			project.getSituation().setOk(situation.isOk());
			project.getSituation().setSituationMessage(situation.getSituationMessage());
			project.getSituation().setSituationTitle(situation.getSituationTitle());
			
			projectRepository.save(project);
			
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			
			return new ResponseEntity<String>("A linha não foi adicionada à codelist.", HttpStatus.BAD_REQUEST);
		}

	}


	/**
	 * Atualiza as informações de uma linha.
	 * 
	 * @param updatedLine
	 * @param line
	 * @return ResponseEntity
	 * @author Bárbara Port
	 */
	@PostMapping("/update")
	@ApiOperation("Updates a line.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Line successfully updated."),
        @ApiResponse(code = 400, message = "The line wasn't updated.")
    })
	public ResponseEntity<?> updateLine (@RequestBody UpdatedLine updatedLine) {
		int lineId = updatedLine.getId();
		
		try {
			Linha line = linhaRepository.findById(lineId).get();
			
			String[] oldLineFileInfo = FileVerifications.fileDestination(line, updatedLine.getCodelistName());
			
			line.setBlockName(updatedLine.getBlockName());
			line.setBlockNumber(updatedLine.getBlockNumber());
			line.setCode(updatedLine.getCode());
			line.setSectionNumber(updatedLine.getSectionNumber());
			line.setSectionName(updatedLine.getSectionName());
			line.setSubsectionNumber(updatedLine.getSubsectionNumber());
			line.setSubsectionName(updatedLine.getSubsectionName());
			
			ArrayList<Remark> newRemarks = new ArrayList<Remark>();
			
			String[] remarksText = updatedLine.getRemarksText().split(",");
			
			for (int i = 0; i < remarksText.length; i++) {
				String text = remarksText[i];
				
				HashMap<String, String> remarkMap = new HashMap<String, String>();
				
				String[] textParts = text.split("[\\(||//)]");
				
				String traco = textParts[0].replace("-", "").trim();
				String apelido =  textParts[1].trim();
				
				remarkMap.put(traco, apelido);
				
				RemarkBuilder builder = new RemarkBuilder(remarkMap);
				
				Remark remark = builder.getBuildedRemark();
				
				newRemarks.add(remark);
				
			}
			
			line.setRemarks(newRemarks);
			
			String[] lineFileNewInfo = FileVerifications.fileDestination(line, updatedLine.getCodelistName());
			
			String oldFileAbsolutePathName = oldLineFileInfo[0] + oldLineFileInfo[1];
			String newFileAbsolutePathName = lineFileNewInfo[0];
			
			File oldFile = new File(oldFileAbsolutePathName);
			File newFile = new File(newFileAbsolutePathName);
			
			FileUtils.copyFileToDirectory(oldFile, newFile);
			oldFile.delete();
			
			File newNewFile = new File(newFileAbsolutePathName + oldLineFileInfo[1]);
			newNewFile.renameTo(new File(newFileAbsolutePathName + lineFileNewInfo[1]));
			
			linhaRepository.save(line);
			
			Projeto project = projectRepository.findByName(updatedLine.getCodelistName());
			
			ProjectHealthCheck healthCheck = new ProjectHealthCheck(project);
			
			ProjectSituation situation = healthCheck.getSituation();
			
			project.getSituation().setOk(situation.isOk());
			project.getSituation().setSituationMessage(situation.getSituationMessage());
			project.getSituation().setSituationTitle(situation.getSituationTitle());
			
			projectRepository.save(project);
			
		}catch (Exception e) {
			e.printStackTrace();
			
			return new ResponseEntity<String>("A linha não foi atualizada", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	/**
	 * Deleta uma linha de uma codelist.
	 * 
	 * @param line
	 * @return ResponseEntity
	 * @author Bárbara Port
	 */
	@DeleteMapping("/delete/{line}")
	@ApiOperation("Deletes a line.")
	@ApiResponses({ @ApiResponse(code = 200, message = "Line successfully deleted."),
			@ApiResponse(code = 400, message = "The line wasn't deleted.") })
	public ResponseEntity<?> deleteLine(@PathVariable int line) {
		try {
			linhaRepository.deleteById(line);
			
		} catch (Exception e) {
			return new ResponseEntity<String>("A linha não foi deletada", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	/**
	 * Associa um arquivo a uma linha de uma codelist.
	 * 
	 * @param filePath
	 * @param line
	 * @return ResponseEntity
	 * @author Bárbara Port
	 * @throws IOException
	 */
	@PostMapping("/attachFile")
	@ApiOperation("Attaches a file to a line.")
	@ApiResponses({ @ApiResponse(code = 200, message = "File successfully attached."),
			@ApiResponse(code = 400, message = "The file wasn't attached to the line."),
			@ApiResponse(code = 404, message = "The file wasn't found.") })
	public ResponseEntity<?> attachFile(@RequestParam(name = "file") MultipartFile file, @RequestParam(name = "line") Integer line, @RequestParam String codelistName) throws IOException {
		
		Optional<Linha> optionalLinha = linhaRepository.findById(line);
		Linha linha = optionalLinha.get();
		
		String[] fileInfos = FileVerifications.fileDestination(linha, codelistName);
		String strFilePath = fileInfos[0];
		String fileName = fileInfos[1];

		File destinationAbsolutePath = new File(strFilePath);
		destinationAbsolutePath.mkdirs();
		file.transferTo(new File(strFilePath.concat(fileName)));
		
		linha.setId(line);
		linha.setFilePath(destinationAbsolutePath.getAbsolutePath());
		linhaRepository.save(linha);
		
		Projeto project = projectRepository.findByName(codelistName);
		
		ProjectHealthCheck healthCheck = new ProjectHealthCheck(project);
		
		ProjectSituation situation = healthCheck.getSituation();
		
		project.getSituation().setOk(situation.isOk());
		project.getSituation().setSituationMessage(situation.getSituationMessage());
		project.getSituation().setSituationTitle(situation.getSituationTitle());
		
		projectRepository.save(project);

		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
}
