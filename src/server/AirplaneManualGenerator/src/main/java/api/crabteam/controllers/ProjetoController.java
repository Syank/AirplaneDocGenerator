package api.crabteam.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import api.crabteam.controllers.requestsBody.ChangeProjectDescription;
import api.crabteam.controllers.requestsBody.ChangeProjectName;
import api.crabteam.controllers.requestsBody.NewProject;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.builders.ProjetoBuilder;
import api.crabteam.model.enumarations.EnvironmentVariables;
import api.crabteam.model.repositories.ProjetoRepository;
import api.crabteam.utils.FileUtils;
import api.crabteam.utils.FileVerifications;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
	 * Retorna todos os projetos
	 * @param request
	 * @return ResponseEntity
	 */
	@GetMapping("/all")
    @ApiOperation("Finds all projects.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "All projects found."),
        @ApiResponse(code = 401, message = "Unauthorized.")
    })
	public ResponseEntity<?> getAllProjects (ServletRequest request) {
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		
		if (session == null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
		}
		
		List<Projeto> projects = projetoRepository.findAll();
		
		return new ResponseEntity<List<Projeto>>(projects, HttpStatus.OK);
	}
	
	/**
	 * Método que encontra um projeto pelo seu nome
	 * @param projectName
	 * @return ResponseEntity
	 */
	@GetMapping("/findByName")
    @ApiOperation("Finds a project by its name.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Project found."),
        @ApiResponse(code = 500, message = "Project wasn't found.")
    })
	public ResponseEntity<?> findProjectByName(@RequestParam String projectName){
		Projeto project = projetoRepository.findByName(projectName);
		
		if(project != null) {
			return new ResponseEntity<Projeto>(project, HttpStatus.OK);
		}
		
		return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Método que altera a descrição de um projeto
	 * @param descricao
	 * @param nomeProjeto
	 * @return ResponseEntity
	 * @author Francisco Cardoso
	 * @author Bárbara Port
	 * @author Rafael Furtado
	 */
	@PostMapping("/changeDescription")
    @ApiOperation("Finds a project by its name.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Description changed."),
        @ApiResponse(code = 500, message = "Description wasn't changed."),
        @ApiResponse(code = 400, message = "Project wasn't found.")
    })
	public ResponseEntity<?> changeDescription(@RequestBody ChangeProjectDescription newData){
		
		try {
			String nomeProjeto = newData.getProjectName().toUpperCase();
			String descricao = newData.getProjectDescription();
			
			Projeto project = projetoRepository.findByName(nomeProjeto);
			
			try {
				project.setDescricao(descricao);
				projetoRepository.save(project);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}
			catch (Exception e) {
				return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		catch (Exception e) {
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		}
		
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
		
		return new ResponseEntity<String>(failMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/changeName")
	public ResponseEntity<?> changeProjectName(@RequestBody ChangeProjectName newData) throws IOException{
		String newProjectName = newData.getNewName().toUpperCase();
		String oldProjectName = newData.getOldName().toUpperCase();

		Projeto project = projetoRepository.findByName(oldProjectName);
		Projeto supposedNewProject = projetoRepository.findByName(newProjectName);
		
		if (project == null) {
			return new ResponseEntity<String>("Não foi possível encontrar o projeto para alterar seu nome",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (supposedNewProject == null) {
			String filesDirectory = EnvironmentVariables.PROJECTS_FOLDER.getValue().concat("\\").concat(oldProjectName);
			String newFilesDirectory = EnvironmentVariables.PROJECTS_FOLDER.getValue().concat("\\").concat(newProjectName);
			
			// foi necessário fazer assim pois já temos uma classe com o mesmo nome
			// se já existir alguma pasta de antes e que não foi apagada
			org.apache.commons.io.FileUtils.deleteDirectory(new File(newFilesDirectory));
			
			String fileExtension = ".xlsx";
			
			File supposedCodelistSheet = new File(filesDirectory.concat("\\").concat(oldProjectName).concat(fileExtension));
			
			if (supposedCodelistSheet.exists()) {
				// primeiro renomeia o nome da planilha, depois renomeia o arquivo
				FileUtils.renameCodelistSheet(filesDirectory, oldProjectName + fileExtension, oldProjectName, newProjectName);
				FileUtils.renameFile(filesDirectory, oldProjectName + fileExtension, newProjectName + fileExtension);
			}

			project.getCodelist().setNome(newProjectName);
			project.setNome(newProjectName);
			
			FileVerifications.renameProjectFiles(new File(filesDirectory + "\\Master"), newProjectName);
			
			File newProjectFolder = new File(newFilesDirectory);
			File projectFolder = new File(filesDirectory);
			projectFolder.renameTo(newProjectFolder);

			projetoRepository.save(project);

			return new ResponseEntity<Boolean>(true, HttpStatus.OK);	
		}
		else {
			return new ResponseEntity<String>("Já existe um projeto com esse nome!",
					HttpStatus.NOT_ACCEPTABLE);
		}
		
	}
	
	@PostMapping("/import")
	public ResponseEntity<?> importProject(
			@RequestParam MultipartFile codelist,
			@RequestParam ArrayList<MultipartFile> projectFile) throws Exception{
		String projectName = projectFile.get(0).getOriginalFilename().split("[/]")[0];
		
		boolean validProjectName = FileVerifications.isValidProjectName(projectName);
		
		if(!validProjectName) {
			return new ResponseEntity<String>("O nome da pasta do projeto é inválido", HttpStatus.BAD_REQUEST);
		}
		
		String workDirectory = EnvironmentVariables.PROJECTS_FOLDER.getValue();
		
		try {
			org.apache.commons.io.FileUtils.deleteDirectory(new File(workDirectory + "\\" + projectName));
			
			for (int i = 0; i < projectFile.size(); i++) {
				MultipartFile file = projectFile.get(i);
				
				String destinationPath = workDirectory + "\\" + file.getOriginalFilename();
				
				File destinationFile = new File(destinationPath);
				destinationFile.mkdirs();
				
				file.transferTo(destinationFile);
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			
			org.apache.commons.io.FileUtils.deleteDirectory(new File(workDirectory + "\\" + projectName));
			
			return new ResponseEntity<String>("Ocorreu um erro interno ao realizar a importação do projeto", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
}