package api.crabteam.controllers.requestsBody;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

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
	private byte[] arquivoCodelist;
	
	public NewProject() {
		
	}
	
	public NewProject(String nome, String descricao, MultipartFile arquivoCodelist) throws IOException {
		this(nome, descricao, arquivoCodelist.getBytes());
		
	}
	
	public NewProject(String nome, String descricao, byte[] arquivoCodelist) throws IOException {
		this.nome = nome;
		this.descricao = descricao;
		this.arquivoCodelist = arquivoCodelist;
		
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

	public byte[] getArquivoCodelist() {
		return arquivoCodelist;
	}

	public void setArquivoCodelist(byte[] arquivoCodelist) {
		this.arquivoCodelist = arquivoCodelist;
	}
	
	public void setArquivoCodelistFromMultipart(MultipartFile arquivoCodelist) throws IOException {
		this.arquivoCodelist = arquivoCodelist.getBytes();
	}
	
}
