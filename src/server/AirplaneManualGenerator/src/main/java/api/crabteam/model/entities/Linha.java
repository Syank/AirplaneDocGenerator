package api.crabteam.model.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


@Entity(name = "linha")
public class Linha {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false)
	private String secao;
	
	@Column
	private String subSecao;
	
	@Column(nullable = false)
	private String block;
	
	@Column(nullable = false)
	private String blockName;
	
	@Column(nullable = false)
	private String code;
	
	@Column(nullable = false)
	private String filePath;
	
	@ManyToMany
	@JoinTable(
		name = "linha_remarks",
		joinColumns = @JoinColumn(name = "linha"),
		inverseJoinColumns = @JoinColumn(name = "remark")
	)
	private List<Remark> remarks;
	
	@ManyToOne
	@JoinColumn(name = "codelist")
	private Codelist codelist;
	
	
	
}
