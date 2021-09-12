package api.crabteam.controllers.requestsBody;



/**
 * Classe para mapear a requisição de registro de usuário, tendo como única e necessária
 * funcionalidade métodos getters e setters que permitam extrair os valores dos atributos recebidos na requisição
 * <p>
 * O Spring se encarregará de atribuir corretamente os atributos do JSON recebido para o objeto da classe, 
 * desde que o nome das chaves correspondam aos nomes dos atributos desta classe
 * 
 * @author Rafael Furtado
 */
public class RegisterUser {
	private String nome;
	private String email;
	private String senha;
	private boolean admin;
	
	public RegisterUser() {
		
	}
	
	public RegisterUser(String nome, String email, String senha, boolean admin) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.admin = admin;
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
}
