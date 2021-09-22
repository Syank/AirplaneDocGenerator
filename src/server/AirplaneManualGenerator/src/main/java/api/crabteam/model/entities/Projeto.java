package api.crabteam.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	
	@Column(columnDefinition = "text")
	private String descricao;
	
	
	
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
	
}
