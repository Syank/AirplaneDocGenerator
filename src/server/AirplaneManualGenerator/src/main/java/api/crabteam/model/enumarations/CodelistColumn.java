package api.crabteam.model.enumarations;

public enum CodelistColumn {
	SECAO("Nº SEÇÃO"), 
	SUB_SECAO("Nº SUB SEÇÃO"),
	BLOCK("Nº BLOCK"), 
	BLOCK_NAME("BLOCK NAME"),
	CODE("CODE"), 
	REMARK("Remarks");
	
	public String columnType;

	CodelistColumn(String columnType) {
		this.columnType = columnType;
		
	}
	
}
