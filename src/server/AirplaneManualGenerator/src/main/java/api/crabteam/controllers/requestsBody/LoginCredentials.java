package api.crabteam.controllers.requestsBody;



/**
 * Classe para mapear a requisição de autenticação no momento da solicitação de um login, tendo como única e necessária
 * funcionalidade métodos getters e setters que permitam extrair os valores dos atributos recebidos na requisição
 * <p>
 * O Spring se encarregará de atribuir corretamente os atributos do JSON recebido para o objeto da classe, 
 * desde que o nome das chaves correspondam aos nomes dos atributos desta classe
 * 
 * @author Rafael Furtado
 */
public class LoginCredentials {
	private String email;
	private String password;
	
	
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
