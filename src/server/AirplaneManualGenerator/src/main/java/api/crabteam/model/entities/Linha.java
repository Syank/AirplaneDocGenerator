package api.crabteam.model.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Table(name = "linha")
public class Linha {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column
	private String secao;
	
	@Column
	private String subSecao;
	
	@Column
	private String block;
	
	@Column
	private String blockName;
	
	@Column
	private String code;
	
	@Column
	@OneToMany(cascade = CascadeType.ALL)
	private List<Remark> remarks;
	
	@Column
	private String filePath;
	
}
