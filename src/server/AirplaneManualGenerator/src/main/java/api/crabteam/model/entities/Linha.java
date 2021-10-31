package api.crabteam.model.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


@Entity(name = "linha")
public class Linha {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false)
	private String sectionNumber;
	
	@Column(nullable = false)
	private String sectionName;
	
	@Column
	private String subsectionNumber;
	
	@Column
	private String subsectionName;
	
	@Column(nullable = false)
	private String blockNumber;
	
	@Column(nullable = false)
	private String blockName;
	
	@Column(nullable = false)
	private String code;
	
	@Column
	private String filePath;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Remark> remarks = new ArrayList<Remark>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(String sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	public String getSubsectionNumber() {
		return subsectionNumber;
	}

	public void setSubsectionNumber(String subsectionNumber) {
		this.subsectionNumber = subsectionNumber;
	}

	public String getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<Remark> getRemarks() {
		return remarks;
	}

	public void setRemarks(List<Remark> remarks) {
		this.remarks = remarks;
	}
	
	public void addRemark(Remark remark) {
		this.remarks.add(remark);
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSubsectionName() {
		return subsectionName;
	}

	public void setSubsectionName(String subsectionName) {
		this.subsectionName = subsectionName;
	}

}