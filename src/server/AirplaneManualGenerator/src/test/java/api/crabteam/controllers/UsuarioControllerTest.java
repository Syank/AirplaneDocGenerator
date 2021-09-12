package api.crabteam.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

import api.crabteam.controllers.requestsBody.RegisterUser;
import api.crabteam.model.Administrador;
import api.crabteam.model.Usuario;
import api.crabteam.model.UsuarioPadrao;
import api.crabteam.model.repositories.UsuarioRepository;



/**
 * Classe de testes que garantem a integridade do controller <b><i>UsuarioController</i></b>
 * 
 * @author Rafael Furtado
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UsuarioControllerTest {
	
	private static String testUserEmail = "teste@cadastro.com";
	private static String testUserPassword = "testandocadastro123#";
	private static String testUserName = "Teste cadastro";

	@LocalServerPort
	private int port;
	
	@Autowired
	UsuarioRepository userRep;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	/**
	 * Realiza testes para verificar a integridade do serviço de cadastro de novos usuários
	 * <p>
	 * São feitos vários casos de testes que, caso todos passem, garante que não existirá usuários
	 * com o mesmo e-mail, apenas administradores poderão registrar usuários novos e que requisições não
	 * autentificadas não poderão cadastrar novos usuários
	 * 
	 * @throws Exception Caso algum método <i>assert</i> receba um valor diferente do esperado
	 * @author Rafael Furtado
	 */
	@Test
	public void registerNewUserTest() throws Exception {
		MockHttpSession testSession = getSessionForTest();
		
		testSession.setAttribute("admin", true);
		
		
		// ------ Caso 1 -------
		RegisterUser newUserData1 = new RegisterUser(testUserName, testUserEmail, testUserPassword, false);
		MockHttpServletResponse result1 = performRegisterUserPost(newUserData1, testSession);
		Usuario registeredUser1 = userRep.findByEmail(testUserEmail);
		
		assertNotNull(registeredUser1);
		assertTrue(registeredUser1 instanceof UsuarioPadrao);
		assertTrue(registeredUser1.getNome().equals(testUserName));
		assertTrue(registeredUser1.getEmail().equals(testUserEmail));
		assertTrue(registeredUser1.getSenha().checkPassword(testUserPassword));
		assertTrue(result1.getStatus() == HttpStatus.OK.value());
		
		
		// ------ Caso 2 -------
		RegisterUser newUserData2 = new RegisterUser(testUserName, testUserEmail + "case2", testUserPassword, true);
		MockHttpServletResponse result2 = performRegisterUserPost(newUserData2, testSession);
		Usuario registeredUser2 = userRep.findByEmail(testUserEmail + "case2");
		
		assertNotNull(registeredUser2);
		assertTrue(registeredUser2 instanceof Administrador);
		assertTrue(registeredUser2.getNome().equals(testUserName));
		assertTrue(registeredUser2.getEmail().equals(testUserEmail + "case2"));
		assertTrue(registeredUser2.getSenha().checkPassword(testUserPassword));
		assertTrue(result2.getStatus() == HttpStatus.OK.value());
		
		
		// ------ Caso 3 -------
		RegisterUser newUserData3 = new RegisterUser(testUserName, testUserEmail, testUserPassword, true);
		MockHttpServletResponse result3 = performRegisterUserPost(newUserData3, testSession);
		
		assertTrue(result3.getStatus() == HttpStatus.BAD_REQUEST.value());
		
		
		// ------ Caso 4 -------
		testSession.setAttribute("admin", false);
		
		RegisterUser newUserData4 = new RegisterUser(testUserName, testUserEmail, testUserPassword, true);
		MockHttpServletResponse result4 = performRegisterUserPost(newUserData4, testSession);
		
		assertTrue(result4.getStatus() == HttpStatus.UNAUTHORIZED.value());
		
		
		// ------ Caso 5 -------
		testSession.invalidate();
		
		RegisterUser newUserData5 = new RegisterUser(testUserName, testUserEmail, testUserPassword, true);
		MockHttpServletResponse result5 = performRegisterUserPost(newUserData5, testSession);

		assertTrue(result5.getStatus() == HttpStatus.UNAUTHORIZED.value());
		
	}
	
	/**
	 * Realiza a requisição POST para o registro de um novo usuário
	 * 
	 * @param newUserData - Objeto contendo os dados do usuário a ser cadastrado
	 * @param testSession - Sessão no contexto de testes
	 * @return Retorna a resposta recebida da requisição
	 * @throws Exception Caso ocorra algum problema durante a requisição HTTP feita internamente
	 *                   para o próprio servidor
	 * @author Rafael Furtado
	 */
	private MockHttpServletResponse performRegisterUserPost(RegisterUser newUserData, MockHttpSession testSession) throws Exception {
		MockHttpServletResponse result = mockMvc
											.perform(
												post("/user/register")
												.contentType(MediaType.APPLICATION_JSON)
												.content(asJsonString(newUserData))
												.session(testSession)
											)
											.andReturn()
											.getResponse();
		
		return result;
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
