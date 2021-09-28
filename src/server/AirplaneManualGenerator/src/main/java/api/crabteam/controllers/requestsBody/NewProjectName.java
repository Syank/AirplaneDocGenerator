package api.crabteam.controllers.requestsBody;

/**
 * Classe para mapear a requisição de edição do nome de um projeto, tendo como única e necessária
 * funcionalidade métodos getters e setters que permitam extrair os valores dos atributos recebidos na requisição
 * <p>
 * O Spring se encarregará de atribuir corretamente os atributos do JSON recebido para o objeto da classe, 
 * desde que o nome das chaves correspondam aos nomes dos atributos desta classe
 * 
 * @author Carolina Margiotti
 */
public class NewProjectName {
	private String newName;
	private String oldName;
	
	public NewProjectName() {
		
	}
	
	public NewProjectName(String oldname,String newName) {
		this.oldName=oldName;
		this.newName=newName;
	}
	
	public String getOldName() {
		return oldName;
	}
	
	public String getNewName() {
		return newName;
	}
	
	public void setOldName(String oldName) {
		this.oldName=oldName;
	}
	
	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	
}
