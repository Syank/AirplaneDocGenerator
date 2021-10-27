package api.crabteam.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Remark;
import api.crabteam.model.enumarations.CodelistColumn;

/**
 * Classe que possui métodos para gerar o arquivo do Excel de uma codelist
 * @author Bárbara Port
 *
 */
public class CodelistExporter {
	
	private static Map<String, String> codelistRemarks = new HashMap<String, String>();
	
	private static CodelistColumn[] getCodelistColumns () {
		return CodelistColumn.getValues();
	}
	
	private static void createCell (Row row, int index, String value) {
		Cell cell = row.createCell(index);
		cell.setCellValue(value);
	}
	
	private static void createRemarksColumnsAndLines (Sheet sheet, int start, List<Linha> codelistLines) {
		Row header = sheet.getRow(0);
		int control = start;
		for (Map.Entry<String, String> entry : codelistRemarks.entrySet()) {
			String title = entry.getKey().concat(" - ").concat(entry.getValue());
			createCell(header, control, title);
			for (int l = 1; l <= codelistLines.size(); l++) {
				int index = l - 1;
				List<Remark> lineRemarks = codelistLines.get(index).getRemarks();
				for (Remark remark : lineRemarks) {
					if (remark.getTraco().equals(entry.getKey())) {
						createCell(sheet.getRow(l), control, "1");
					}
				}
			}
			control += 1;
		}
	}
	
	private static void createCodelistHeader (Sheet sheet) {
		Row header = sheet.createRow(0);
		CodelistColumn[] codelistColumns = getCodelistColumns();
		for (int i = 0; i < codelistColumns.length; i++) {
			createCell(header, i, codelistColumns[i].columnType);
		}
	}
	
	private static String generateWritableRemarks (List<Remark> lineRemarks) {
		List<String> tracos = new ArrayList<>();
		for (int r = 0; r < lineRemarks.size(); r++) {
			String traco = lineRemarks.get(r).getTraco();
			String apelido = lineRemarks.get(r).getApelido();
			tracos.add("-".concat(traco));
			if (!codelistRemarks.containsKey(traco)) {
				codelistRemarks.put(traco, apelido);
			}
		}
		return String.join(", ", tracos);
	}
	
	private static void createCodelistLines (Sheet sheet, List<Linha> codelistLines) {
		for (int l = 1; l <= codelistLines.size(); l++) {
			int i = l - 1;
			Row line = sheet.createRow(l);
			CodelistColumn[] codelistColumns = getCodelistColumns();
			String cellContent = "";
			for (int c = 0; c < codelistColumns.length; c++) {
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
					cellContent = generateWritableRemarks(codelistLines.get(i).getRemarks());
					break;
				}
				createCell(line, c, cellContent);
			}
		}
	}
	
	private static void createCodelistSheet (String codelistName, List<Linha> codelistLines) throws IOException {
		
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(codelistName);
		
		createCodelistHeader(sheet);
		createCodelistLines(sheet, codelistLines);
		createRemarksColumnsAndLines(sheet, getCodelistColumns().length, codelistLines);
		
		String fileLocation = "C:\\Users\\port3\\Desktop\\exportedCodelistTest.xlsx";
		FileOutputStream outputStream = new FileOutputStream(fileLocation);
		workbook.write(outputStream);
		workbook.close();
	}

	public static void generateCodelistFile (String codelistName, List<Linha> codelistLines) throws IOException {
		createCodelistSheet(codelistName, codelistLines);
	}
}
