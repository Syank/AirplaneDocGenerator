package api.crabteam.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import api.crabteam.controllers.requestsBody.RegisterUser;
import api.crabteam.model.Usuario;
import api.crabteam.model.factories.UsuarioFactory;
import api.crabteam.model.repositories.UsuarioRepository;



@RestController
@RequestMapping("/user")
public class UsuarioController {
	
	@Autowired
    UsuarioRepository usuarioRepository;
	
	
	
	/**
	 * 
	 * @param newUserData - Credencias de autenticação do usuário.
	 *                           O objeto da requisição deve ser um JSON com as chaves <i>nome</i>,<i>email</i>, <i>senha</i> e <i>admin</i>.
	 *                           O Spring se encarregará de fazer a associação com o objeto RegisterUser
	 * @param request - Contexto da requisição, objeto gerido internamente pelo servidor
	 * @return Retorna <b>true</b> caso o usuário seja cadastrado, <b>false</b> se não for possível cadastrar
	 * @author Rafael Furtado
	 */
	@PostMapping("/register")
	public ResponseEntity<Boolean> registerNewUser(@RequestBody RegisterUser newUserData, ServletRequest request) {
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		
		// Se o usuário não estiver logado ou não for administraodr, não permite o cadastro de usuários
		if(session == null || !((boolean) session.getAttribute("admin"))) {
			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
		}
		
		UsuarioFactory factory = new UsuarioFactory();
		
		Usuario newUser = factory.create(newUserData);
		
		try {
			usuarioRepository.save(newUser);
			
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	/**
	 * Verifica se o usuário da requisição está logado na plataforma
	 * 
	 * @param request - Contexto da requisição, objeto gerido internamente pelo servidor
	 * @return Retorna <b>true</b> caso exista um usuário logado, <b>false</b> se não existir
	 * @author Rafael Furtado
	 */
	@GetMapping("/isLogged")
	public ResponseEntity<Boolean> isLogged(ServletRequest request){
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		
		if(session != null) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		
		return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
	}
	
}
