package api.crabteam.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.crabteam.controllers.requestsBody.NewCodelist;
import api.crabteam.controllers.requestsBody.NewProject;
import api.crabteam.model.entities.Codelist;
import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Remark;
import api.crabteam.model.repositories.CodelistRepository;
import api.crabteam.model.repositories.ProjetoRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CodelistControllerTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ProjetoRepository projetoRepository;
	
	@Autowired
	CodelistRepository codelistRepository;
	
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
	
	private byte[] getCodelistFileBytes() throws IOException {
		File file = new File("C:\\Users\\port3\\Desktop\\Codelist.xlsx");
		
		FileInputStream fileinput = new FileInputStream(file);
		
		byte[] byteFile = fileinput.readAllBytes();
		
		return byteFile;
	}
	
	/**
	 * Verifica se a codelist será upada com sucesso
	 * @throws Exception
	 * @author Bárbara Port
	 */
	@Test
	public void uploadCodelist () throws Exception {
		
		MockHttpSession testSession = getSessionForTest();
		
		byte[] fileBytes = null;
		
		// ----- Cadastrando um projeto sem nenhum arquivo
		NewProject newProject = new NewProject("ABC-1234", "This is a short description...", fileBytes);
		mockMvc
		.perform(
			post("/project/create")
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.param("newProject", asJsonString(newProject))
			.session(testSession)
		)
		.andReturn()
		.getResponse();
		
		// ----- Upando uma codelist para o manual
		File file = new File("C:\\Users\\port3\\Desktop\\Codelist.xlsx");
		FileInputStream fileInput = new FileInputStream(file);
		fileBytes = fileInput.readAllBytes();
		
		NewCodelist newCodelist = new NewCodelist();
		newCodelist.setNome(newProject.getNome());
		newCodelist.setArquivoCodelist(fileBytes);
		
		MockHttpServletResponse result = mockMvc.perform(
				MockMvcRequestBuilders
				.post("/codelist/upload")
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.param("newCodelist", asJsonString(newCodelist))
				.session(testSession)
		)
		.andExpect(status().isOk())
		.andReturn()
		.getResponse();
		
		boolean realResult = Boolean.valueOf(result.getContentAsString());
		
		assertTrue(realResult);
		assertTrue(result.getStatus() == HttpStatus.OK.value());
		
		Codelist foundCodelist = codelistRepository.findByName(newCodelist.getNome());
		assertNotNull(foundCodelist);
		
		List<Linha> linhas = foundCodelist.getLinhas();
		for (Linha linha : linhas) {
			assertNotNull(linha);
			
			List<Remark> remarks = linha.getRemarks();
			for (Remark remark : remarks) {
				assertNotNull(remark);
			}
		}
		
	}
}
