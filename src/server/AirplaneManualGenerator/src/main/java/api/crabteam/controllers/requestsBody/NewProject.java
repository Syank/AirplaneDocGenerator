package api.crabteam.controllers.requestsBody;



/**
 * Classe para mapear a requisição de criação de um projeto, tendo como única e necessária
 * funcionalidade métodos getters e setters que permitam extrair os valores dos atributos recebidos na requisição
 * <p>
 * O Spring se encarregará de atribuir corretamente os atributos do JSON recebido para o objeto da classe, 
 * desde que o nome das chaves correspondam aos nomes dos atributos desta classe
 * 
 * @author Rafael Furtado
 */
public class NewProject {
	private String nome;
	private String descricao;
	
	public NewProject() {
		
	}
	
	public NewProject(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
		
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
