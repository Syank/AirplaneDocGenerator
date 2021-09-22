package api.crabteam.model.factories;

import api.crabteam.controllers.requestsBody.RegisterUser;
import api.crabteam.model.entities.Administrador;
import api.crabteam.model.entities.Usuario;
import api.crabteam.model.entities.UsuarioPadrao;


/**
 * Classe do Design Pattern Factory, que facilita a instânciação de um novo Usuario
 * em razão dos parâmetros recebidos na requisição de cadastro de usuários
 * 
 * @author Rafael Furtado
 */
public class UsuarioFactory {

	/**
	 * Cria um novo Usuario, diferenciando entre comum ou administrar em razão do parâmetro
	 * recebido na requisição de registro de usuários
	 * 
	 * @param newUser - Objeto recebido na requisição de cadastro de usuários
	 * @return Retorna um Usuario. Poderá ser do tipo Administrador ou UsuarioPadrao em razão
	 *         do parâmetro recebido
	 * @author Rafael Furtado
	 */
	public Usuario create(RegisterUser newUser) {
		String userName = newUser.getNome();
		String userPassword = newUser.getSenha();
		String userEmail = newUser.getEmail();
		
		boolean isAdmin = newUser.isAdmin();
		
		if(isAdmin) {
			return new Administrador(userName, userEmail, userPassword);
		}
		
		return new UsuarioPadrao(userName, userEmail, userPassword);
	}
	
}
