package api.crabteam.controllers.requestsBody;

/**
 * Classe que mapeia a criação de uma linha.
 * @author Bárbara Port
 *
 */
public class NewLine {
	
	private String sectionNumber;
	private String subsectionNumber;
	private String blockNumber;
	private String blockName;
	private String code;
	private String remarksText;
	private String codelistName;

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

	public String getCodelistName() {
		return codelistName;
	}

	public void setCodelistName(String codelistName) {
		this.codelistName = codelistName;
	}

}
