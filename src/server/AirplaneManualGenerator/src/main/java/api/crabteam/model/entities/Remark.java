package api.crabteam.model.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity(name = "remark")
public class Remark {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false)
	private String traco;
	
	@Column(nullable = false)
	private String apelido;
	
	@ManyToMany(mappedBy = "remarks")
	List<Linha> linhas;
	
}
