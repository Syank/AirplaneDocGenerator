package api.crabteam.controllers.requestsBody;

public class UpdatedLine {

	private int id;
	
	private String sectionNumber;
	
	private String subsectionNumber;
	
	private String blockNumber;
	
	private String blockName;
	
	private String code;
	
	private String remarksText;

	
	
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

	public String getRemarksText() {
		return remarksText;
	}

	public void setRemarksText(String remarksText) {
		this.remarksText = remarksText;
	}
	
}
