package api.crabteam.controllers;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.Revisao;
import api.crabteam.model.repositories.LinhaRepository;
import api.crabteam.model.repositories.ProjetoRepository;
import api.crabteam.model.repositories.RevisaoRepository;
import api.crabteam.utils.FileVerifications;

@RestController
@RequestMapping("/revision")
public class RevisionController {
	
	@Autowired
	public ProjetoRepository projetoRepository;
	
	@Autowired
	public LinhaRepository lineRepository;
	
	@Autowired
	public RevisaoRepository revisaoRepository;
	
	@PostMapping(path = "/newRevision", consumes = {"multipart/form-data"})
	public ResponseEntity<?> newRevision(
			@RequestParam String projectName, 
			@RequestParam String revisionDescription,
			@RequestParam List<MultipartFile> revisedLinesFiles,
			@RequestParam List<Integer> revisedLinesIds){
		Projeto project = projetoRepository.findByName(projectName);
		int projectId = project.getId();

		int lastRevisionVersion = project.getLastRevision().getVersion();
		String actualRevision = String.valueOf(lastRevisionVersion + 1);
		
		try {
			
			for (int i = 0; i < revisedLinesIds.size(); i++) {
				int lineId = revisedLinesIds.get(i);
				MultipartFile lineFile = revisedLinesFiles.get(i);
				
				try {
					Linha linha = lineRepository.findById(lineId).get();
					
					String[] fileInfos = FileVerifications.fileDestination(linha, projectName);
					String strFilePath = fileInfos[0];
					String fileName = fileInfos[1];
					
					// Troca o destino de Master para Rev
					strFilePath = strFilePath.replace("Master", "Rev\\Rev" + actualRevision + "\\");

					File destinationAbsolutePath = new File(strFilePath);
					destinationAbsolutePath.mkdirs();
					lineFile.transferTo(new File(strFilePath.concat(fileName)));
					
					linha.setId(lineId);
					linha.setFilePath(destinationAbsolutePath.getAbsolutePath());
					linha.setActualRevision(lastRevisionVersion + 1);
					
					lineRepository.save(linha);
					
				}catch (Exception e) {
					e.printStackTrace();
					
					return new ResponseEntity<String>("Ocorreu um problema ao revisar uma das linhas", HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
			}
			
			Revisao newRevision = new Revisao();
			newRevision.setDescription(revisionDescription);
			newRevision.setVersion(lastRevisionVersion + 1);
			
			// Saving revision lines
			List<Linha> revisionLines = lineRepository.findAllById(revisedLinesIds);
			newRevision.setLinhas(revisionLines);
			// ----------------------------------------			
			Projeto revProject = projetoRepository.findById(projectId).get();
			
			revProject.addRevision(newRevision);
			
			//LEPBuilder lepBuilder = new LEPBuilder(revProject);
			//lepBuilder.generateLep();
			
			projetoRepository.save(revProject);
			
		}catch (Exception e) {
			e.printStackTrace();
			
			return new ResponseEntity<String>("Ocorreu um problema ao registrar a revisão", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

}
