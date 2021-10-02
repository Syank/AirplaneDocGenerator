package api.crabteam.model.entities.builders;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import api.crabteam.model.entities.Remark;

@Service
public class LinhaBuilder {
	
	LinhaBuilder () {
		
	}
	
	LinhaBuilder (String sectionNumber, String subSectionNumber, String blockNumber, String blockName, String code, String filePath, List<Remark> remarks) {
		
	}
	
	private String sectionNumber;
	private String subsectionNumber;
	private String blockNumber;
	private String blockName;
	private String code;
	private String filePath;
	private List<Remark> remarks = new ArrayList<Remark>();

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

}
