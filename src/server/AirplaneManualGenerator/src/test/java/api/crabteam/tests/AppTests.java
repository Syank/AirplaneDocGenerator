package api.crabteam.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

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

import api.crabteam.controllers.requestsBody.NewProject;

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

	@Autowired
	MockMvc mockMvc;

	/**
	 * Converte o objeto recebido para um equivalente em String no formato JSON
	 * 
	 * @param object - Objeto a ser convertido
	 * @return Retorna uma String JSON que representa o objeto recebido
	 * @throws JsonProcessingException Caso não seja possível fazer o parse do
	 *                                 objeto para uma String JSON
	 * @author Rafael Furtado
	 */
	private static String asJsonString(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
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
		return newProject;
	}

	private MockHttpServletResponse performCreateProjectRequest(MockMultipartFile codelistFile, NewProject newProject)
			throws JsonProcessingException, Exception {
		MockHttpSession session = getSessionForTest();
		String newProjectAsJsonString = asJsonString(newProject);

		MockHttpServletResponse result = (MockHttpServletResponse) mockMvc .perform(MockMvcRequestBuilders.multipart("/project/create")
																			 	.file(codelistFile)
																				.contentType(MediaType.APPLICATION_JSON)
																				.content(newProjectAsJsonString)
																				.session(session));
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
		NewProject newProject = newProjectHelper(name, description);
		MockMultipartFile codelistFile = new MockMultipartFile("codelistFile", codelistPath.getBytes());
		MockHttpServletResponse result = performCreateProjectRequest(codelistFile, newProject);
		return result.getStatus();
	}
	
	@Test
	void test() throws Exception {
		assertEquals(createProject("ABC-1234", "Lorem ipsum", "C:\\Users\\port3\\Desktop\\Codelist.xlsx"), 200);
		assertNotEquals(createProject("ABC-4321", "Lorem ipsum, lorem ipsum", "C:\\Users\\port3\\Desktop\\Codelist.xlsx"), 200);
		
	}

}
