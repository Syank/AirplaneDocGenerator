package api.crabteam.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api.crabteam.model.entities.Linha;
import api.crabteam.model.enumarations.CodelistColumn;
import api.crabteam.model.repositories.CodelistRepository;

/**
 * Classe que possui métodos para gerar o arquivo do Excel de uma codelist
 * @author Bárbara Port
 *
 */
public class CodelistExporter {
	
	private static CodelistColumn[] getCodelistColumns () {
		return CodelistColumn.getValues();
	}
	
	private static void createCodelistHeader (Sheet sheet) {
		Row header = sheet.createRow(0);
		CodelistColumn[] codelistColumns = getCodelistColumns();
		for (int i = 0; i < codelistColumns.length; i++) {
			String cellValue = codelistColumns[i].columnType;
			Cell headerCell = header.createCell(i);
			headerCell.setCellValue(cellValue);
		}
	}
	
	public static void createCodelistLines (Sheet sheet, List<Linha> codelistLines) {
		
		for (int l = 1; l <= codelistLines.size(); l++) {
			int i = l - 1;
			Row line = sheet.createRow(l);
			CodelistColumn[] codelistColumns = getCodelistColumns();
			for (int c = 0; c < codelistColumns.length; c++) {
				Cell columnCell = line.createCell(c);
				String cellContent = "Empty";
				String cellType = codelistColumns[c].columnType;
				switch (cellType) {
				case "Nº SEÇÃO":
					cellContent = codelistLines.get(i).getSectionNumber();
					break;
				case "SEÇÃO":
					cellContent = codelistLines.get(i).getSectionName();
					break;
				case "Nº SUB SEÇÃO":
					cellContent = codelistLines.get(i).getSubsectionNumber();
					break;
				case "SUB SEÇÃO":
					cellContent = codelistLines.get(i).getSubsectionName();
					break;
				case "Nº BLOCK":
					cellContent = codelistLines.get(i).getBlockNumber();
					break;
				case "BLOCK NAME":
					cellContent = codelistLines.get(i).getBlockName();
					break;
				case "CODE":
					cellContent = codelistLines.get(i).getCode();
					break;
				case "Remarks":
					//cellContent = codelistLines.get(i).getRemarks();
					break;
				}
				columnCell.setCellValue(cellContent);
			}
		}
	}
	
	public static void createCodelistSheet (String codelistName, List<Linha> codelistLines) throws IOException {
		
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(codelistName);
		
		createCodelistHeader(sheet);
		createCodelistLines(sheet, codelistLines);
		
		String fileLocation = "C:\\Users\\port3\\Desktop\\exportedCodelistTest.xlsx";
		FileOutputStream outputStream = new FileOutputStream(fileLocation);
		workbook.write(outputStream);
		workbook.close();
	}

	public static void generateCodelistFile (String codelistName, List<Linha> codelistLines) throws IOException {
		createCodelistSheet(codelistName, codelistLines);
		
	}

}
