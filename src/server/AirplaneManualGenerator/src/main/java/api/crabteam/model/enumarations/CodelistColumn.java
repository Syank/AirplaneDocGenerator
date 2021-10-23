package api.crabteam.model.enumarations;

public enum CodelistColumn {
	N_SECAO("Nº SEÇÃO"),
	NOME_SECAO("SEÇÃO"),
	N_SUB_SECAO("Nº SUB SEÇÃO"),
	NOME_SUB_SECAO("SUB SEÇÃO"),
	BLOCK("Nº BLOCK"), 
	BLOCK_NAME("BLOCK NAME"),
	CODE("CODE"), 
	REMARK("Remarks");
	
	public String columnType;

	CodelistColumn(String columnType) {
		this.columnType = columnType;
		
	}
	
}
