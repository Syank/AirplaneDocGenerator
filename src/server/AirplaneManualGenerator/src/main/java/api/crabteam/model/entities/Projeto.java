package api.crabteam.model.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import api.crabteam.utils.ProjectHealthCheck;
import api.crabteam.utils.ProjectSituation;

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
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "codelist_id", referencedColumnName = "id")
	private Codelist codelist;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "project_situation_id", referencedColumnName = "id")
	private ProjectSituation situation;
	
	public Projeto() {
		
	}
	
	public Projeto(String nome) {
		this.nome = nome;
		
	}
	
	public Projeto(String nome, String descricao, Codelist codelist) {
		this.nome = nome;
		this.descricao = descricao;
		this.codelist = codelist;
		
		ProjectHealthCheck healthCheck = new ProjectHealthCheck(this);
		
		this.situation = healthCheck.getSituation();
		
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

	public ProjectSituation getSituation() {
		return situation;
	}

	public void setSituation(ProjectSituation situation) {
		this.situation = situation;
	}
	
}
