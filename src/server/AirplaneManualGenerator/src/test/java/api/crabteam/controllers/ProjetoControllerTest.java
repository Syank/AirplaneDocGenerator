package api.crabteam.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.crabteam.controllers.requestsBody.NewProject;
import api.crabteam.model.Projeto;
import api.crabteam.model.repositories.ProjetoRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProjetoControllerTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	ProjetoRepository projetoRep;
	
	@Autowired
	private MockMvc mockMvc;

	private static String testProjectName = "ABC-1234";
	private static String testProjectDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque et felis a tortor molestie mattis. Suspendisse bibendum mauris quis felis finibus semper. Duis sagittis nisi quis tempus mollis. Mauris aliquet velit enim, nec tincidunt elit dictum eget. Donec bibendum sit amet diam quis elementum. Etiam eget mi non eros cursus suscipit. Aenean risus felis, viverra vel velit vitae, tempor varius tellus. Sed volutpat ante eu tempus ullamcorper. Cras vel pulvinar eros. Nunc vitae blandit metus. Sed pulvinar nisi a orci vestibulum bibendum. Pellentesque tempor egestas nisl. Vivamus iaculis elit interdum aliquet rhoncus. In tempus luctus quam a posuere. Vestibulum dictum lectus id est facilisis, vel bibendum tellus pretium.";
	
	
	
	/**
	 * Realiza testes para o serviço de criação de projetos que, caso todos passem, assegura
	 * que um usuário poderá criar um novo projeto, um projeto não possa ser criado caso seu nome já
	 * exista no banco de dados e que uma requisição não autenticada não possa criar um projeto
	 * 
	 * @throws Exception Caso ocorra algum problema durante a requisição HTTP feita internamente
	 *                   para o próprio servidor
	 *                   
	 * @author Rafael Furtado
	 */
	@Test
	public void createNewProjectTest() throws Exception {
		MockHttpSession testSession = getSessionForTest();
		
		
		NewProject newProject = new NewProject(testProjectName, testProjectDescription);
		
		// ----- Caso 1: Assegurar um usuário cadastrar um projeto ----------
		MockHttpServletResponse result = mockMvc
				.perform(
					post("/project/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(newProject))
					.session(testSession)
				)
				.andReturn()
				.getResponse();
		
		Projeto projectFind = projetoRep.findByName(testProjectName);
		
		boolean expectedResult = Boolean.valueOf(result.getContentAsString());
		
		assertTrue(expectedResult);
		assertTrue(result.getStatus() == HttpStatus.OK.value());
		assertNotNull(projectFind);
		
		
		// ----- Caso 2: Assegurar que um projeto não seja cadastrado caso já exista outro com o mesmo nome --------
		MockHttpServletResponse result2 = mockMvc
				.perform(
					post("/project/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(newProject))
					.session(testSession)
				)
				.andReturn()
				.getResponse();
		
		boolean expectedResult2 = Boolean.valueOf(result2.getContentAsString());
		
		assertTrue(result2.getStatus() == HttpStatus.BAD_REQUEST.value());
		assertFalse(expectedResult2);
		
		
		
		// ----- Caso 3: Assegurar que uma requisição não autenticada não cadastre um projeto --------
		testSession.invalidate();
		
		MockHttpServletResponse result3 = mockMvc
				.perform(
					post("/project/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(newProject))
					.session(testSession)
				)
				.andReturn()
				.getResponse();
		
		assertTrue(result3.getStatus() == HttpStatus.UNAUTHORIZED.value());
		
	}
	
	/**
	 * Realização de testes para a listagem de todos os projetos
	 * 
	 * @author Bárbara Port
	 * @throws Exception
	 */
	@Test
	public void getAllProjectsTest () throws Exception {
		MockHttpSession testSession = getSessionForTest();
		
		// ----- Cadastrando mais um
		NewProject newProject = new NewProject("CBA-1234", "This is a short description...");
		mockMvc
		.perform(
			post("/project/create")
			.contentType(MediaType.APPLICATION_JSON)
			.content(asJsonString(newProject))
			.session(testSession)
		)
		.andReturn()
		.getResponse();
		
		// ----- Caso 1: Assegurar que um usuário possa visualizar todos os projetos ----------
		mockMvc.perform(get("/project/all")
					.session(testSession)
				)
				.andExpect(status().isOk())
				.andDo(print());
		
		// ----- Caso 2: Assegurar que uma sessão não autenticada não visualize os projetos ----------
		testSession.invalidate();
		
		mockMvc.perform(get("/project/all")
					.session(testSession)
				)
				.andExpect(status().isUnauthorized())
				.andDo(print());
	}
	
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
	 * @throws JsonProcessingException Caso não seja possível fazer o parse do objeto para uma String JSON
	 * @author Rafael Furtado
	 */
	private static String asJsonString(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}
	
}
