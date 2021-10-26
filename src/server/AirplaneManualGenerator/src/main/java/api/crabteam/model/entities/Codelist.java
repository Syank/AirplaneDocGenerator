package api.crabteam.model.entities;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

@Entity(name = "codelist")
public class Codelist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String nome;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("section_number, subsection_number, block_number, block_name, code")
	private Set<Linha> linhas = new HashSet<Linha>();
	
	@OneToOne(mappedBy = "codelist")
	private Projeto projeto;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Linha> getLinhas() {
		ArrayList<Linha> arrayList = new ArrayList<Linha>();
		
		arrayList.addAll(linhas);
		
		return arrayList;
	}

	public void setLinhas(List<Linha> linhas) {
		Set<Linha> set = new HashSet<Linha>(linhas);
		
		this.linhas = set;
	}

	public void addLinha(Linha newLine) {
		this.linhas.add(newLine);
	}

}