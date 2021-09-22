package api.crabteam.controllers;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import api.crabteam.controllers.requestsBody.LoginCredentials;
import api.crabteam.model.entities.Administrador;
import api.crabteam.model.repositories.AdministradorRepository;



/**
 * Classe de testes centralizando os testes relacionados ao controller <b><i>AuthenticationController</i></b>
 * 
 * @author Rafael Furtado
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
	
	private static String testUserEmail = "teste@teste.com";
	private static String testUserPassword = "testeuser123@";
	
	@LocalServerPort
	private int port;
	
	@Autowired
	AdministradorRepository userRep;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private MockMvc mockMvc;

	
	
	/**
	 * Verifica os casos de possíveis respostas do servidor para uma requisição de autenticação
	 * 
	 * @author Rafael Furtado
	 */
	@Test
	public void authenticateUserTest() {
		persistTestUser();
		LoginCredentials loginCredentials = new LoginCredentials();
		
		loginCredentials.setEmail(testUserEmail);
		loginCredentials.setPassword(testUserPassword);
		boolean result1 = restTemplate.postForObject("http://localhost:" + port + "/authentication/login", loginCredentials, Boolean.class).booleanValue();
		assertTrue(result1);
		
		loginCredentials.setEmail("wrong@test.com");
		loginCredentials.setPassword(testUserPassword);
		boolean result2 = restTemplate.postForObject("http://localhost:" + port + "/authentication/login", loginCredentials, Boolean.class).booleanValue();
		assertFalse(result2);
		
		loginCredentials.setEmail("wrong@test.com");
		loginCredentials.setPassword("wrong123pass$");
		boolean result3 = restTemplate.postForObject("http://localhost:" + port + "/authentication/login", loginCredentials, Boolean.class).booleanValue();
		assertFalse(result3);
		
		loginCredentials.setEmail(testUserEmail);
		loginCredentials.setPassword("wrong123pass$");
		boolean result4 = restTemplate.postForObject("http://localhost:" + port + "/authentication/login", loginCredentials, Boolean.class).booleanValue();
		assertFalse(result4);
		
	}
	
	/**
	 * Verifica os casos de possíveis respostas do servidor para a requisição de se o
	 * usuário logado na sessão é administrador ou não
	 *  
	 * @throws Exception Caso ocorra algum problema durante a requisição HTTP feita internamente
	 *                   para o próprio servidor
	 *                   
	 * @author Rafael Furtado
	 */
	@Test
	public void isUserAdminTest() throws Exception {
		MockHttpSession testSession = getSessionForTest();

		testSession.setAttribute("admin", true);
		String result1 = mockMvc
							.perform(
									get("/authentication/isUserAdmin")
									.session(testSession)
							)
							.andReturn()
							.getResponse()
							.getContentAsString();
		boolean expectedResult1 = Boolean.valueOf(result1);
		
		testSession.setAttribute("admin", false);
		String result2 = mockMvc
				.perform(
						get("/authentication/isUserAdmin")
						.session(testSession)
				)
				.andReturn()
				.getResponse()
				.getContentAsString();
		boolean expectedResult2 = Boolean.valueOf(result2);
		
		assertTrue(expectedResult1);
		assertFalse(expectedResult2);
		
	}
	
	/**
	 * Verifica se para o serviço de logout, a sessão do usuário é invalidade
	 * 
	 * @throws Exception Caso ocorra algum problema durante a requisição HTTP feita internamente
	 *                   para o próprio servidor
	 *                   
	 * @author Rafael Furtado
	 */
	@Test
	public void userLogoutTest() throws Exception {
		MockHttpSession testSession = getSessionForTest();

		HttpSession sessionResult = mockMvc
							.perform(
									get("/authentication/logout")
									.session(testSession)
							)
							.andReturn()
							.getRequest()
							.getSession(false);
		
		assertNull(sessionResult);
		
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
	 * Cria um novo registro de usuário no banco de dados para a realização dos testes
	 * 
	 * @author Rafael Furtado
	 */
	private void persistTestUser() {
		Administrador admin = new Administrador();
		
		admin.setEmail("teste@teste.com");
		admin.setNome("Teste user");
		admin.setSenha("testeuser123@");
		
		userRep.save(admin);
		
	}
	
}
