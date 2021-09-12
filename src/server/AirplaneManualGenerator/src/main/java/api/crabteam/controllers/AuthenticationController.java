package api.crabteam.controllers;


import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.crabteam.controllers.requestsBody.LoginCredentials;
import api.crabteam.model.Administrador;
import api.crabteam.model.Usuario;
import api.crabteam.model.repositories.UsuarioRepository;

/**
 * Controller de serviços relacionados a autenticação do usuário
 * 
 * @author Rafael Furtado
 */
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
	
	@Autowired
    UsuarioRepository usuarioRepository;
	
	
	/**
	 * Realiza o login do usuário na aplicação
	 * 
	 * @param loginCredentials - Credencias de autenticação do usuário.
	 *                           O objeto da requisição deve ser um JSON com as chaves <i>email</i> e <i>password</i>.
	 *                           O Spring se encarregará de fazer a associação com o objeto LoginCredentials
	 * 
	 * @param request - Contexto da requisição, objeto gerido internamente pelo servidor
	 * @return Retorna <b>true</b> caso a autenticação tenha sido bem sucedida, caso contrário,
	 *         retorna <b>false</b>
	 * @author Rafael Furtado
	 */
	@PostMapping("/login")
	public boolean authenticateUser(@RequestBody LoginCredentials loginCredentials, ServletRequest request) {
		String email = loginCredentials.getEmail();
		String password = loginCredentials.getPassword();
		
		Usuario user = usuarioRepository.findByEmail(email);
		
		if(user != null) {
			boolean authenticate = user.getSenha().checkPassword(password);

			if(authenticate) {
				HttpSession session = ((HttpServletRequest) request).getSession(true);
				
				session.setAttribute("userName", user.getNome());
				session.setAttribute("userLogin", email);
				
				boolean admin = false;
				
				if(user instanceof Administrador) {
					admin = true;
					
				}
				
				session.setAttribute("admin", admin);
				
				return true;
			}
			
		}
		
		return false;
	}
	
	/**
	 * Verifica se para a sessão do usuário para seu contexto de requisição, o usuário é
	 * do tipo <b>Adminsitrador</b>
	 * 
	 * @param request - Contexto da requisição, objeto gerido internamente pelo servidor
	 * @return Retorna <b>true</b> se o usuário for administrador, caso contrário,
	 *         retorna <b>false</b>
	 * @author Rafael Furtado
	 */
	@GetMapping("/isUserAdmin")
	public boolean isUserAdmin(ServletRequest request) {
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		
		if(session != null) {
			boolean isAdmin = (boolean) session.getAttribute("admin");
			
			return isAdmin;
		}
		
		return false;
	}
	
}
