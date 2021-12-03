package api.crabteam.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import api.crabteam.controllers.requestsBody.ChangeProjectDescription;
import api.crabteam.controllers.requestsBody.ChangeProjectName;
import api.crabteam.controllers.requestsBody.NewProject;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.repositories.ProjetoRepository;

/**
 * Classe responsável pelo testes que faremos para a disciplina Testes de
 * Software
 * 
 * @author Bárbara Port
 * @author Rafael Furtado
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AppTests {
	
	//private static final String CODELIST_PATH = "C:\\Users\\rafs9\\Documents\\Faculdade\\Codelist.xlsx";
	private static final String CODELIST_PATH = "C:\\Users\\port3\\Desktop\\Codelist.xlsx";

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ProjetoRepository repository;

	/**
	 * Cria uma nova sessão para o contexto dos testes
	 * 
	 * @return Retorna uma sessão pertencente ao contexto dos teste
	 * @author Rafael Furtado
	 */
	private MockHttpSession getSessionForTest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		MockHttpSession session = (MockHttpSession) request.getSession(true);

		return session;
	}

	/**
	 * Converte o objeto recebido para um equivalente em String no formato JSON
	 * 
	 * @param object - Objeto a ser convertido
	 * @return Retorna uma String JSON que representa o objeto recebido
	 * @throws JsonProcessingException Caso não seja possível fazer o parse do
	 *                                 objeto para uma String JSON
	 * @author Rafael Furtado
	 */
	@SuppressWarnings("unused")
	private static String asJsonString(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}

	/**
	 * Método para realizar os sets dos atributos de um novo projeto
	 * @param name nome do projeto
	 * @param description descrição do projeto
	 * @return <b>NewProject</b> objeto do novo projeto
	 * @throws IOException
	 * @author Bárbara Port
	 */
	private NewProject newProjectHelper(String name, String description) throws IOException {
		NewProject newProject = new NewProject();
		newProject.setNome(name);
		newProject.setDescricao(description);
		newProject.setArquivoCodelist("".getBytes());
		return newProject;
	}

	/**
	 * Método que realiza a requisição para criar o projeto
	 * @param codelistFile arquivo excel da codelist
	 * @param newProject os dados do novo projeto
	 * @return <b>MockHttpServletResponse</b> resposta da requisição
	 * @throws JsonProcessingException
	 * @throws Exception
	 * @author Bárbara Port
	 */
	private MockHttpServletResponse performCreateProjectRequest(MockMultipartFile codelistFile, NewProject newProject) throws JsonProcessingException, Exception {
		MockHttpSession session = getSessionForTest();
		
		Gson gson = new Gson();
		String newProjectAsJsonString = gson.toJson(newProject, NewProject.class);
		
		MockHttpServletResponse result = (MockHttpServletResponse) mockMvc.perform(MockMvcRequestBuilders.multipart("/project/create")
																			 	.file(codelistFile)
																			 	.contentType(MediaType.APPLICATION_JSON)
																				.content(newProjectAsJsonString)
																				.session(session))
																		   .andReturn()
																		   .getResponse();

		return result;
	}

	/**
	 * Método que efetua a criação de um projeto
	 * @param name nome do projeto
	 * @param description descrição do projeto
	 * @param codelistPath caminho em que se encontra a codelist
	 * @throws Exception
	 * @author Bárbara Port
	 * @return <b>int</b> status da requisição
	 */
	private int createProject(String name, String description, String codelistPath) throws Exception {
		byte[] codelistFileBytes = Files.readAllBytes(new File(codelistPath).toPath());
		MockMultipartFile codelistFile = new MockMultipartFile("codelistFile", codelistFileBytes);
		NewProject newProject = newProjectHelper(name, description);
		MockHttpServletResponse result = performCreateProjectRequest(codelistFile, newProject);
		return result.getStatus();
	}
	
	/**
	 * Método que verifica todos os projetos registrados na plataforma
	 * @return <b>int</b> o status da requisição
	 * @throws Exception
	 * @author Bárbara Port
	 */
	private int getAllProjects () throws Exception {
		MockHttpSession session = getSessionForTest();
		MockHttpServletResponse result = (MockHttpServletResponse) mockMvc.perform(MockMvcRequestBuilders.get("/project/all")
																				.session(session))
																			.andDo(print())
																			.andReturn()
																			.getResponse();
		return result.getStatus();
	}
	
	/**
	 * Método que encontra um projeto a partir do seu nome
	 * @param projectName o nome do projeto a ser procurado
	 * @return <b>int</b> o status da requisição
	 * @throws Exception
	 * @author Bárbara Port
	 */
	private int findProjectByName (String projectName) throws Exception {
		MockHttpSession session = getSessionForTest();
		MockHttpServletResponse result = (MockHttpServletResponse) mockMvc.perform(MockMvcRequestBuilders.get("/project/findByName")
																				.session(session)
																				.param("projectName", projectName))
																			.andDo(print())
																			.andReturn()
																			.getResponse();
		return result.getStatus();
	}
	
	/**
	 * Cria um objeto que inclui os dados necessários para a mudança de descrição de um projeto
	 * @param projectName nome do projeto a ter sua descrição alterada
	 * @param newDescription nova descrição do projeto
	 * @return <b>ChangeProjectDescription</b> objeto que possui os valores necessários para a alteração
	 * @author Bárbara Port
	 */
	private ChangeProjectDescription createNewProjectDescription (String projectName, String newDescription) {
		ChangeProjectDescription newProjectDescription = new ChangeProjectDescription();
		newProjectDescription.setProjectName(projectName);
		newProjectDescription.setProjectDescription(newDescription);
		return newProjectDescription;
	}
	
	/**
	 * Testa a altera a descrição de um projeto
	 * 
	 * @param projectName - Nome do projeto para alterar a descrição
	 * @param newDescription - A nova descrição a ser alterada
	 * @return Retorna <b>true</b> caso a descrição seja alterada, se não, retorna <b>false</b>
	 * @throws Exception Caso ocorra um problema ao alterar a descrição
	 * @author Rafael Furtado
	 */
	private boolean changeDescription(String projectName, String newDescription) throws Exception {
		MockHttpSession session = getSessionForTest();
		
		ChangeProjectDescription newProjectDescription = createNewProjectDescription(projectName, newDescription);
		
		Gson gson = new Gson();
		String newDescriptionAsJsonString = gson.toJson(newProjectDescription, ChangeProjectDescription.class);
		
		mockMvc
			.perform(MockMvcRequestBuilders.post("/project/changeDescription")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(newDescriptionAsJsonString))
			.andDo(print())
			.andReturn()
			.getResponse();
		
		Projeto project = repository.findByName(projectName);
		
		String description = project.getDescricao();
		
		if(description.equals(newDescription)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Método para realizar a requisição para trocar o nome de um projeto
	 * @param newProjectName objeto que possui as informações do projeto (novo nome e nome antigo)
	 * @return <b>int</b> que representa o status da requisição
	 * @throws JsonProcessingException
	 * @throws Exception
	 * @author Bárbara Port
	 */
	private int performNameChangeRequest(ChangeProjectName newProjectName) throws JsonProcessingException, Exception {
		MockHttpSession session = getSessionForTest();
		
		Gson gson = new Gson();
		String newProjectNameAsJsonString = gson.toJson(newProjectName, ChangeProjectName.class);
		
		MockHttpServletResponse result = (MockHttpServletResponse) mockMvc.perform(MockMvcRequestBuilders.post("/project/changeName")
																			 	.contentType(MediaType.APPLICATION_JSON)
																				.content(newProjectNameAsJsonString)
																				.session(session))
																		   .andReturn()
																		   .getResponse();

		return result.getStatus();
	}
	
	/**
	 * Método que "monta" o objeto que é enviado na requisição de trocar o nome de um projeto
	 * @param chosenName o novo nome do projeto
	 * @param oldName o antigo nome do projeto
	 * @return <b>ChangeProjectName</b> objeto com as informações necessárias para a troca de nome
	 * @author Bárbara Port
	 */
	private ChangeProjectName createProjectNameChanges (String chosenName, String oldName) {
		ChangeProjectName newName = new ChangeProjectName();
		newName.setNewName(chosenName);
		newName.setOldName(oldName);
		return newName;
	}
	
	/**
	 * Método que faz todos os passos para trocar o nome de um projeto
	 * @param chosenName o novo nome do projeto
	 * @param oldName o antigo nome do projeto
	 * @return <b>int</b> status da requisição
	 * @throws JsonProcessingException
	 * @throws Exception
	 * @author Bárbara Port
	 */
	private int changeName (String chosenName, String oldName) throws JsonProcessingException, Exception {
		ChangeProjectName newProjectName = createProjectNameChanges(chosenName, oldName);
		return performNameChangeRequest(newProjectName);
	}
	
	@Test
	void test() throws Exception {
		System.err.println("Criando um projeto que não possui uma aba no arquivo da codelist");
		assertNotEquals(200, createProject("CBA-1111", "Lorem ipsum, lorem ipsum, lorem ipsum", CODELIST_PATH));
		
		System.err.println("Criando um projeto ok");
		assertEquals(200, createProject("ABC-1234", "Lorem ipsum", CODELIST_PATH));
		
		System.err.println("Criando um projeto ok");
		assertEquals(200, createProject("ABC-4321", "Lorem ipsum, lorem ipsum", CODELIST_PATH));
		
		System.err.println("Listagem de todos os projetos cadastrados");
		assertNotNull(getAllProjects());
		
		System.err.println("Procurando um projeto ok");
		assertEquals(200, findProjectByName("ABC-1234"));
		
		System.err.println("Procurando um projeto que não existe");
		assertNotEquals(200, findProjectByName("AAA-1111"));
		
		System.err.println("Trocando a descrição de um projeto ok");
		assertTrue(changeDescription("ABC-1234", "Nova descrição do projeto"));
		
		System.err.println("Verificando a troca a descrição do projeto ok");
		assertEquals(200, findProjectByName("ABC-1234"));
		
		System.err.println("Trocando o nome de um projeto");
		assertEquals(200, changeName("DEF-1234", "ABC-1234"));
	}

}
