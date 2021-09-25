package api.crabteam.model.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Classe que representa a entidade "Projeto" no banco de dados
 * 
 * @author Rafael Furtado
 */
@Entity(name = "projeto")
public class Projeto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String nome;
	
	@Column(columnDefinition = "text", nullable = false)
	private String descricao;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "codelist_id", referencedColumnName = "id")
	private Codelist codelist;
	
	public Projeto() {
		
	}
	
	public Projeto(String nome) {
		this.nome = nome;
		
	}
	
	public Projeto(String nome, String descricao) {
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

	public Codelist getCodelist() {
		return codelist;
	}

	public void setCodelist(Codelist codelist) {
		this.codelist = codelist;
	}
	
}
