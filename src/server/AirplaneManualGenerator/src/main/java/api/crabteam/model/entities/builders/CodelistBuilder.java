package api.crabteam.model.entities.builders;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import api.crabteam.model.entities.Codelist;
import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Remark;
import api.crabteam.model.enumarations.CodelistColumn;
import api.crabteam.utils.FileUtils;

public class CodelistBuilder {
	private static final String PROJECTS_DIRECTORY = "..\\..\\..\\..\\..\\..\\resources\\static\\codelist";
	
	private Codelist codelist;
	
	
	public CodelistBuilder(String projectName) {
		Codelist codelist = new Codelist();
		codelist.setNome(projectName);
		
		this.codelist = codelist;
		
	}

	public CodelistBuilder(byte[] codelistBytesFile, String projectName) throws Exception {
		String fileName = projectName + "_codelist.xlsx";
		
		FileUtils.saveFile(codelistBytesFile, fileName, PROJECTS_DIRECTORY);
		
		Workbook workbook = FileUtils.readAsExcel(PROJECTS_DIRECTORY + "\\" + fileName);
		
		Sheet projectSheet = workbook.getSheet(projectName);
		
		if(projectSheet == null) {
			throw new Exception("Codelist inválida! Não existe uma planilha na Codelist para o nome do projeto: " + projectName);
		}
		
		this.codelist = new Codelist();
		
		int rowsNumber = projectSheet.getPhysicalNumberOfRows();
		
		HashMap<String, Integer> firstTableRow = getFirstTableRow(projectSheet, rowsNumber);
		
		Row headersRow = projectSheet.getRow(firstTableRow.get("linha"));
		
		HashMap<Integer, Object> columnsTypes = getColumnsNamesIndex(headersRow, firstTableRow.get("linha"));
		
		for (int i = firstTableRow.get("linha") + 1; i < rowsNumber; i++) {
			Row row = projectSheet.getRow(i);
			
			int columnsNumber = row.getPhysicalNumberOfCells();
			
			Linha linha = new Linha();
			
			for (int j = 0; j <= columnsNumber; j++) {
				Cell cell = row.getCell(j);
				
				String cellValue = null;
				
				if(cell != null) {
					CellType cellType = cell.getCellType();
					
					if(cellType == CellType.STRING) {
						cellValue = cell.getStringCellValue();
						
					}else if (cellType == CellType.NUMERIC) {
						cellValue = String.valueOf(cell.getNumericCellValue());
						
					}
					
					Object type = columnsTypes.get(j);
					
					if(type instanceof CodelistColumn) {
						CodelistColumn columnType = (CodelistColumn) type;
						switch (columnType) {
							case SECAO:
								linha.setSecao(cellValue);
	
								break;
							case SUB_SECAO:
								linha.setSubSecao(cellValue);
	
								break;
							case BLOCK:
								linha.setBlock(cellValue);
	
								break;
							case BLOCK_NAME:
								linha.setBlockName(cellValue);
								break;
							case CODE:
								linha.setCode(cellValue);
	
								break;
							case REMARK:
								if(row.getRowNum() == firstTableRow.get("linha")) {
									break;
								}
								
								ArrayList<HashMap<String, String>> remarksValues = new ArrayList<HashMap<String,String>>();
	
								for (int k = j + 1; k <= columnsNumber ; k++) {
									Cell remarkCell = row.getCell(k);
	
									double remarkCellValue = remarkCell.getNumericCellValue();
	
									if(remarkCellValue != 0) {
										String remarkNickNameColumn = (String) columnsTypes.get(k);
	
										String[] remarkNickName = remarkNickNameColumn.split("[-]");
	
										HashMap<String, String> remarkValues = new HashMap<String, String>();
	
										remarkValues.put(remarkNickName[0].trim(), remarkNickName[1].trim());
	
										remarksValues.add(remarkValues);
	
									}
	
								}
	
								for (int k = 0; k < remarksValues.size(); k++) {
									HashMap<String, String> values = remarksValues.get(k);
	
									RemarkBuilder builder = new RemarkBuilder(values);
	
									Remark remark = builder.getBuildedRemark();
	
									linha.addRemark(remark);
	
								}
	
								break;
							default:
								break;
						}

					}
					
				}
				
			}
			
			this.codelist.addLinha(linha);
			
		}
		
		this.codelist.setNome(projectName);
		
	}

	public Codelist getBuildedCodelist() {
		return this.codelist;
	}
	
	private HashMap<Integer, Object> getColumnsNamesIndex(Row headersRow, int tableOffset){
		int columnsCount = headersRow.getPhysicalNumberOfCells();
		
		HashMap<Integer, Object> mappedColumns = new HashMap<Integer, Object>();
		
		for (int i = tableOffset; i <= columnsCount; i++) {
			Cell cell = headersRow.getCell(i);
			
			String cellValue = cell.getStringCellValue();
			
			// Ajusta adequadamente a string da coluna de remarks
			if(cellValue.contains("\n")) {
				cellValue = cellValue.split("\n")[0];
				
			}
			
			CodelistColumn columnType = null;
			
			for(CodelistColumn codelistEnum : CodelistColumn.values()){
				if(codelistEnum.columnType.contains(cellValue)) {
					columnType = codelistEnum;
					
				}
				
			}
			
			if(columnType != null) {
				mappedColumns.put(i, columnType);
				
			}else {
				mappedColumns.put(i, cellValue);
				
			}
			
		}
		
		return mappedColumns;
	}
	
	private static HashMap<String, Integer> getFirstTableRow(Sheet projectSheet, int rowsNumber) throws Exception {
		String firstRowText = "Nº SEÇÃO";
		
		HashMap<String, Integer>  firstTablePositions = new HashMap<String, Integer> ();
		
		for (int i = 0; i < rowsNumber; i++) {
			Row row = projectSheet.getRow(i);
			
			int columnsNumber = row.getPhysicalNumberOfCells();
			
			for (int j = 0; j < columnsNumber; j++) {
				Cell cell = row.getCell(j);
				
				if(cell != null) {
					String cellValue = cell.getStringCellValue();
					
					if(cellValue.equals(firstRowText)) {
						firstTablePositions.put("linha", i);
						firstTablePositions.put("coluna", j);
						
						return firstTablePositions;
					}
					
				}
				
			}
			
		}
		
		throw new Exception();
	}
	
}
