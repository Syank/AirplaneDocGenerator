package api.crabteam.controllers.requestsBody;

/**
 * Classe que mapeia a criação de uma codelist
 * @author Bárbara Port
 *
 */
public class NewCodelist {
	
	private String nome;
	private byte[] arquivoCodelist;
	
	public NewCodelist () {
		
	}
	
	public NewCodelist (String nome, byte[] arquivoCodelist) {
		this.nome = nome;
		this.arquivoCodelist = arquivoCodelist;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public byte[] getArquivoCodelist() {
		return arquivoCodelist;
	}
	public void setArquivoCodelist(byte[] fileBytes) {
		this.arquivoCodelist = fileBytes;
	}

}
