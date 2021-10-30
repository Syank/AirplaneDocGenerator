package api.crabteam.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

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
	
	@Column(columnDefinition = "date default current_date", nullable = false, insertable = false)
	private LocalDate creationDate;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "codelist_id", referencedColumnName = "id")
	private Codelist codelist;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "project_situation_id", referencedColumnName = "id")
	private ProjectSituation situation;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("creation_date, version")
	@JoinColumn(name = "revision_id", referencedColumnName = "id")
	private Set<Revisao> revisions = new HashSet<Revisao>();
	
	public Projeto() {
		
	}
	
	public Projeto(String nome) {
		this.nome = nome;
		
		ProjectHealthCheck healthCheck = new ProjectHealthCheck(this);
		
		this.situation = healthCheck.getSituation();
		
		Revisao originalRevision = new Revisao();
		
		originalRevision.setDescription("Versão original do projeto");
		originalRevision.setVersion(0);
		
		this.addRevision(originalRevision);
		
	}
	
	public Projeto(String nome, String descricao, Codelist codelist) {
		this.nome = nome;
		this.descricao = descricao;
		this.codelist = codelist;
		
		ProjectHealthCheck healthCheck = new ProjectHealthCheck(this);
		
		this.situation = healthCheck.getSituation();
		
		Revisao originalRevision = new Revisao();
		
		originalRevision.setDescription("Versão original do projeto");
		originalRevision.setVersion(0);
		
		this.addRevision(originalRevision);
		
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

	public LocalDate getCreationDate() {
		return creationDate;
	}
	
	public Set<Revisao> getRevisions() {
		return revisions;
	}

	public void setRevisions(Set<Revisao> revisions) {
		this.revisions = revisions;
	}
	
	public void addRevision(Revisao revision) {
		this.revisions.add(revision);
		
	}
	
	public Revisao getLastRevision() {
		List<Revisao> list = new ArrayList<Revisao>();
		
		list.addAll(this.revisions);
		
		return list.get(list.size() - 1);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
