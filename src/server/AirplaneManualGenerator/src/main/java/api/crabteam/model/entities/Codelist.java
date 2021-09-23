package api.crabteam.model.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity(name = "codelist")
public class Codelist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false)
	private String nome;
	
	@OneToOne(mappedBy = "codelist")
	private Projeto projeto;
	
	@OneToMany(mappedBy = "codelist", cascade = CascadeType.ALL)
	private List<Linha> linhas;

}
