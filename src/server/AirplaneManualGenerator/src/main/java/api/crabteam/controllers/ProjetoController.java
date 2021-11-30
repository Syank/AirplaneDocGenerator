package api.crabteam.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
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

import com.itextpdf.text.DocumentException;

import api.crabteam.controllers.requestsBody.ChangeProjectDescription;
import api.crabteam.controllers.requestsBody.ChangeProjectName;
import api.crabteam.controllers.requestsBody.NewProject;
import api.crabteam.model.entities.Codelist;
import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.Remark;
import api.crabteam.model.entities.Revisao;
import api.crabteam.model.entities.builders.CodelistBuilder;
import api.crabteam.model.entities.builders.ProjetoBuilder;
import api.crabteam.model.enumarations.EnvironmentVariables;
import api.crabteam.model.repositories.CodelistRepository;
import api.crabteam.model.repositories.LinhaRepository;
import api.crabteam.model.repositories.ProjetoRepository;
import api.crabteam.model.repositories.RemarkRepository;
import api.crabteam.utils.CodelistExporter;
import api.crabteam.utils.FileUtils;
import api.crabteam.utils.FileVerifications;
import api.crabteam.utils.ProjectExporter;
import api.crabteam.utils.manualVersionsHelpers.DeltaManualHelper;
import api.crabteam.utils.manualVersionsHelpers.FullManualHelper;
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
	public CodelistRepository codelistRepository;
	
	@Autowired
	public LinhaRepository linhaRepository;
	
	@Autowired
	public RemarkRepository remarkRepository;
	
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
	public ResponseEntity<?> createNewProject(@RequestParam(required = false) MultipartFile codelistFile, @RequestBody NewProject newProject) throws IOException {
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
			@RequestParam(name = "codelist") MultipartFile codelistFile,
			@RequestParam ArrayList<MultipartFile> projectFile) throws Exception{
		String projectName = projectFile.get(0).getOriginalFilename().split("[/]")[0];
		
		if(projetoRepository.findByName(projectName) != null) {
			return new ResponseEntity<String>("Já existe um projeto cadastrado com este mesmo nome", HttpStatus.BAD_REQUEST);
		}
		
		boolean validProjectName = FileVerifications.isValidProjectName(projectName);
		
		if(!validProjectName) {
			return new ResponseEntity<String>("O nome da pasta do projeto é inválido", HttpStatus.BAD_REQUEST);
		}
		
		String workDirectory = EnvironmentVariables.PROJECTS_FOLDER.getValue();
		
		try {
			CodelistBuilder codelistBuilder = new CodelistBuilder(codelistFile.getBytes(), projectName);
			
			Codelist codelist = codelistBuilder.getBuildedCodelist();
			
			Projeto projeto = new Projeto(projectName, "Projeto importado", codelist);
			projeto.setCodelist(codelist);
			
			org.apache.commons.io.FileUtils.deleteDirectory(new File(workDirectory + "\\" + projectName));
			
			ArrayList<String> revisionFiles = new ArrayList<String>();
			
			for (int i = 0; i < projectFile.size(); i++) {
				MultipartFile file = projectFile.get(i);
				
				String destinationPath = workDirectory + "/" + file.getOriginalFilename();
				
				File destinationFile = new File(destinationPath);
				destinationFile.mkdirs();
				
				file.transferTo(destinationFile);
				
				if(destinationPath.contains("Rev")) {
					revisionFiles.add(destinationPath);
					
				}
				
			}
			
			List<Linha> codelistLines = codelist.getLinhas();
			
			for (int i = 0; i < codelistLines.size(); i++) {
				Linha line = codelistLines.get(i);
				
				String[] destination = FileVerifications.fileDestination(line, projectName);
				String expectedFileName = destination[1];
				
				File destinationFile = new File(destination[0] + expectedFileName);
				
				if(destinationFile.exists()) {
					String absolutePath = destinationFile.getAbsolutePath();
					
					line.setFilePath(absolutePath);
					
				}
				
			}
			
			HashMap<String, ArrayList<String>> revisionsMap = new HashMap<String, ArrayList<String>>();
			int actualRevision = 0;
			
			for (int i = 0; i < revisionFiles.size(); i++) {
				String revisionFilePath = revisionFiles.get(i);
				
				ArrayList<String> pathDirectories = new ArrayList<String>(Arrays.asList(revisionFilePath.split("[/]")));
				
				int revIndex = pathDirectories.indexOf("Rev");
				
				String revFolder = pathDirectories.get(revIndex + 1);
				
				if(revisionsMap.containsKey(revFolder)) {
					revisionsMap.get(revFolder).add(revisionFilePath);
					
				}else {
					revisionsMap.put(revFolder, new ArrayList<String>());
					
				}
				
			}
			
			ArrayList<Revisao> revisions = new ArrayList<Revisao>();
			
			actualRevision++;
			
			for (String revisionToCheck : revisionsMap.keySet()) {
				ArrayList<String> revisionFilesPaths = revisionsMap.get(revisionToCheck);
				
				for (int i = 0; i < revisionFilesPaths.size(); i++) {
					String path = revisionFilesPaths.get(i);
					
					Linha line = codelist.getLinhaByFileName(path.replace("Rev/" + revisionToCheck, "Master"));
					
					if(line != null) {
						line.setFilePath(path);
						
						int revision = Integer.parseInt(revisionToCheck.substring(revisionToCheck.length() - 1));
						
						line.setActualRevision(revision);
						
					}
					
				}
				
				Revisao rev = new Revisao();
				rev.setDescription("Revisão proveniente da importação do projeto");
				rev.setVersion(actualRevision);
				
				revisions.add(rev);
				
				actualRevision++;
				
			}
			
			File destinationAbsolutePath = new File(EnvironmentVariables.PROJECTS_FOLDER.getValue() + "\\" + projectName + "\\" + projectName + ".xlsx");
			codelistFile.transferTo(destinationAbsolutePath);

			for (int i = 0; i < revisions.size(); i++) {
				projeto.addRevision(revisions.get(i));
				
			}
			
			projetoRepository.save(projeto);
			
		}catch (Exception e) {
			e.printStackTrace();
			
			org.apache.commons.io.FileUtils.deleteDirectory(new File(workDirectory + "\\" + projectName));
			
			return new ResponseEntity<String>("Ocorreu um erro interno ao realizar a importação do projeto", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@GetMapping("/export")
    @ApiOperation("Exports all files from a project.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Project succesfully exported."),
        @ApiResponse(code = 500, message = "Somenthing went wrong.")
    })
	public ResponseEntity<?> exportProject (@RequestParam (name = "projectName") String projectName) throws IOException {
		List<Linha> codelistLines = projetoRepository.findByName(projectName).getCodelist().getLinhas();
		CodelistExporter.updateCodelistFile(projectName, codelistLines);
		
		byte[] file = ProjectExporter.exportProject(projectName);
		
		String base64File = new String(Base64.getEncoder().encode(file));
		JSONObject fileObject = new JSONObject();
		fileObject.put("file", base64File);
	
		return new ResponseEntity<String>(fileObject.toString(), HttpStatus.OK);
	}
	
	@PostMapping("/delete")
    @ApiOperation("Deletes a entire project.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Project succesfully deleted."),
        @ApiResponse(code = 400, message = "Somenthing went wrong.")
    })
	public ResponseEntity<?> deleteProject (@RequestParam(name = "projectName") String projectName) {
		try {
			projetoRepository.deleteByName(projectName);
			
			Codelist codelist = codelistRepository.findByName(projectName); 
			codelistRepository.deleteAllCodelistLines(codelist.getId());
			List<Linha> linhasCodelist = codelist.getLinhas();
			for (Linha linha : linhasCodelist) {
				linhaRepository.deleteById(linha.getId());
			}
			
			codelistRepository.deleteByName(projectName);
			
			String projectFolder = EnvironmentVariables.PROJECTS_FOLDER.getValue().concat("\\").concat(projectName);
			FileVerifications.deleteEntireFolder(projectFolder);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@PostMapping("/generateDelta")
    @ApiOperation("Generates the Delta version of a manual.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "The Delta manual was successfully generated."),
        @ApiResponse(code = 500, message = "Somenthing went wrong.")
    })
	public ResponseEntity<?> generateDeltaManual (@RequestParam(name = "projectId") int projectId, @RequestParam(name = "variation") String variation) throws DocumentException, IOException {
		try {
			Projeto project = projetoRepository.findById(projectId).get();
			Remark remark = remarkRepository.findAllRemarks(variation).get(0);
			byte[] file = DeltaManualHelper.generateDeltaManual(project, remark);
			String fileName = DeltaManualHelper.getDeltaManualFileName(project, remark);
			
			String base64File = new String(Base64.getEncoder().encode(file));
			JSONObject fileObject = new JSONObject();
			fileObject.put("file", base64File);
			fileObject.put("fileName", fileName);
			
			return new ResponseEntity<String>(fileObject.toString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/generateFull")
    @ApiOperation("Generates the Full version of a manual.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "The Full manual was successfully generated."),
        @ApiResponse(code = 500, message = "Somenthing went wrong.")
    })
	public ResponseEntity<?> generateFullManual (@RequestParam(name = "projectId") int projectId, @RequestParam(name = "variation") String variation) {
		try {
			Projeto project = projetoRepository.findById(projectId).get();
			Remark remark = remarkRepository.findAllRemarks(variation).get(0);
			byte[] file = FullManualHelper.generateFullManual(project, remark);
			String fileName = FullManualHelper.getFullManualFileName(project, remark);
			
			String base64File = new String(Base64.getEncoder().encode(file));
			JSONObject fileObject = new JSONObject();
			fileObject.put("file", base64File);
			fileObject.put("fileName", fileName);
			
			return new ResponseEntity<String>(fileObject.toString(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}