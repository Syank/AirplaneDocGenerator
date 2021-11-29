package api.crabteam.tests;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
 * Classe responsável pelo testes que faremos para a disciplina Testes de Software
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
	 * @throws JsonProcessingException Caso não seja possível fazer o parse do objeto para uma String JSON
	 * @author Rafael Furtado
	 */
	private static String asJsonString(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}
	
	private MockHttpSession getSessionForTest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		MockHttpSession session = (MockHttpSession) request.getSession(true);
		return session;
	}
	
	private NewProject newProjectHelper (String name, String description) throws IOException {
		NewProject newProject = new NewProject();
		newProject.setNome(name);
		newProject.setDescricao(description);
		return newProject;
	}
	
	private void performCreateProjectRequest (MockMultipartFile codelistFile, NewProject newProject) throws JsonProcessingException, Exception {
		MockHttpSession session = getSessionForTest();
		String newProjectAsJsonString = asJsonString(newProject);
		
		@SuppressWarnings("unused")
		MockHttpServletResponse result = (MockHttpServletResponse) mockMvc.perform(
																				MockMvcRequestBuilders.multipart("/project/create")
																				.file(codelistFile)
																	            .contentType(MediaType.APPLICATION_JSON)
																	            .content(newProjectAsJsonString)
																	            .session(session)
																            ).andExpect(status().is(200));
	}
	
//	private static void createProject (String name, String description, String codelistPath) throws IOException {
//		NewProject np1 = newProjectHelper(name, description);
//		MockMultipartFile codelistFile = new MockMultipartFile("codelistFile", codelistPath.getBytes());
//		
//		performCreateProjectRequest(codelistFile, np1);
//	}
	
	@Test
	void test() throws Exception {
		// Criando um projeto
		NewProject np1 = newProjectHelper("ABC-1234", "Lorem ipsum");
		MockMultipartFile codelistFile = new MockMultipartFile("codelistFile", "C:\\Users\\port3\\Desktop\\Codelist.xlsx".getBytes());
		performCreateProjectRequest(codelistFile, np1);
		// ------------------
	}

}
