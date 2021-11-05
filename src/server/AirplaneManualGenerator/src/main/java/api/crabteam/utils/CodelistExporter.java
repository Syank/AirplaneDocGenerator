package api.crabteam.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Remark;
import api.crabteam.model.enumarations.CodelistColumn;
import api.crabteam.model.enumarations.EnvironmentVariables;

/**
 * Classe que possui métodos para gerar o arquivo do Excel de uma codelist
 * @author Bárbara Port
 *
 */
public class CodelistExporter {
	
	/**
	 * Variável que armazena todos os remarks da codelist para que eu possa criar as colunas no arquivo do excel
	 */
	private static Map<String, String> codelistRemarks = new HashMap<String, String>();
	
	/**
	 * Método que pega todas as colunas da codelist a partir de um enum. Mesmo que o enum nude, o código ainda funcionará devidamente
	 * @return CodelistColumn[] com cada uma das colunas
	 * @author Bárbara Port
	 */
	private static CodelistColumn[] getCodelistColumns () {
		return CodelistColumn.getValues();
	}
	
	/**
	 * Método que estiliza as células
	 * @param cell -> célula a ser estilizada
	 * @author Bárbara Port
	 */
	private static void setCellStyle (Cell cell) {
		Workbook workbook = cell.getSheet().getWorkbook();
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		// border style
		BorderStyle borderStyle = BorderStyle.THIN;
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setBorderRight(borderStyle);
		cellStyle.setBorderLeft(borderStyle);
		// border color
		short borderColor = IndexedColors.BLACK.getIndex();
		cellStyle.setTopBorderColor(borderColor);
		cellStyle.setBottomBorderColor(borderColor);
		cellStyle.setRightBorderColor(borderColor);
		cellStyle.setLeftBorderColor(borderColor);
		if (cell.getRowIndex() == 1) {
			// cell background color
			short backgroundColor = IndexedColors.LAVENDER.getIndex();
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellStyle.setFillForegroundColor(backgroundColor);
		}
		// linebreaks
		cellStyle.setWrapText(true);
		cell.setCellStyle(cellStyle);
	}
	
	/**
	 * Método que cria uma célula no arquivo da codelist
	 * @param row -> linha do arquivo do excel. começa em 0
	 * @param index -> coluna do arquivo do excel. começa em 0
	 * @param value -> conteúdo a ser salvo na célula
	 * @author Bárbara Port
	 */
	private static void createCell (Row row, int index, String value) {
		Cell cell = row.createCell(index);
		cell.setCellValue(value);
		setCellStyle(cell);
	}
	
	/**
	 * Método que cria toda a parte das colunas e linhas dos remarks (aquela parte em que os remarks da linha ficam com o valor 1)
	 * @param sheet -> arquivo do excel em que a codelist está sendo salva
	 * @param start -> qual a primeira coluna do arquivo do excel que os remarks poderão ser escritos, porque as anteriores são outra informações
	 * @param codelistLines -> linhas da codelist que está sendo salva
	 * @author Bárbara Port
	 */
	private static void createRemarksColumnsAndLines (Sheet sheet, int start, List<Linha> codelistLines) {
		Row header = sheet.getRow(1);
		int control = start + 1;
		for (Map.Entry<String, String> entry : codelistRemarks.entrySet()) {
			String title = entry.getKey().concat(" - ").concat(entry.getValue());
			createCell(header, control, title);
			for (int l = 2; l < codelistLines.size() + 2; l++) {
				int index = l - 2;
				List<Remark> lineRemarks = codelistLines.get(index).getRemarks();
				for (Remark remark : lineRemarks) {
					if (remark.getTraco().equals(entry.getKey())) {
						createCell(sheet.getRow(l), control, "1.0");
					}
					else if (sheet.getRow(l).getCell(control) == null) {
						createCell(sheet.getRow(l), control, "");
					}
				}
			}
			control += 1;
		}
	}
	
	/**
	 * Método para criar uma linha
	 * @param sheet -> planilha do excel em que a linha será criada
	 * @param index -> qual o índice da linha a ser criada (começa no 0)
	 * @author Bárbara Port
	 */
	private static Row createRow (Sheet sheet, int index) {
		Row row = sheet.createRow(index);
		short height = index == 1 ? (short) 1000 : (short) 350;
		row.setHeight(height);
		return row;
	}
	
	/**
	 * Método que cria o header do arquivo no qual a codelist será salva
	 * @param sheet -> arquivo do excel em que a codelist será salva
	 * @author Bárbara Port
	 */
	private static void createCodelistHeader (Sheet sheet) {
		Row header = createRow(sheet, 1);
		CodelistColumn[] codelistColumns = getCodelistColumns();
		for (int i = 1; i <= codelistColumns.length; i++) {
			int c = i - 1;
			createCell(header, i, codelistColumns[c].columnType);
		}
	}
	
	/**
	 * Método que gera uma string com os remarks formatados
	 * @param lineRemarks -> remarks de uma linha da codelist a ser salva
	 * @return String com os remarks no formato "-50, -96, -10", por exemplo
	 * @author Bárbara Port
	 */
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
	
	/**
	 * Método responsável por criar as linhas da codelist (que não sejam das colunas dos remarks)
	 * @param sheet -> arquivo do excel no qual as linhas serão escritas
	 * @param codelistLines -> linhas da codelist a ser salva
	 * @author Bárbara Port
	 */
	private static void createCodelistLines (Sheet sheet, List<Linha> codelistLines) {
		for (int l = 2; l <= codelistLines.size() + 1; l++) {
			int i = l - 2;
			Row line = createRow(sheet, l);
			CodelistColumn[] codelistColumns = getCodelistColumns();
			String cellContent = "";
			for (int c = 1; c < codelistColumns.length + 1; c++) {
				String cellType = codelistColumns[c - 1].columnType;
				switch (cellType) {
				case "Nº SEÇÃO":
					cellContent = codelistLines.get(i).getSectionNumber();
					break;
				case "SEÇÃO":
					cellContent = codelistLines.get(i).getSectionName();
					break;
				case "Nº SUB SEÇÃO":
					String supposedSubsectionNumber = codelistLines.get(i).getSubsectionNumber();
					cellContent = supposedSubsectionNumber == null? "" : supposedSubsectionNumber;
					break;
				case "SUB SEÇÃO":
					String supposedSubsectionName = codelistLines.get(i).getSubsectionName();
					cellContent = supposedSubsectionName == null? "" : supposedSubsectionName;
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
	
	/**
	 * Método que constrói o conteúdo do arquivo da codelist
	 * @param sheet -> o arquivo do excel a ser alterado
	 * @param codelistLines -> linhas da codelist
	 * @author Bárbara Port
	 */
	private static void buildCodelistFile (Sheet sheet, List<Linha> codelistLines) {
		sheet.setDefaultColumnWidth(12);
		createCodelistHeader(sheet);
		createCodelistLines(sheet, codelistLines);
		createRemarksColumnsAndLines(sheet, getCodelistColumns().length, codelistLines);
	}
	
	/**
	 * Método que realmente gera o arquivo da codelist
	 * @param codelistName -> nome da codelist a salvar
	 * @param codelistLines -> linhas da codelist a salvar
	 * @throws IOException
	 * @author Bárbara Port
	 */
	private static byte[] createCodelistSheet (String codelistName, List<Linha> codelistLines) throws IOException {
		
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(codelistName);
		
		buildCodelistFile(sheet, codelistLines);
		
		ByteArrayOutputStream outputFile = new ByteArrayOutputStream();
		workbook.write(outputFile);
		workbook.close();
		outputFile.close();
		byte[] fileBytes = outputFile.toByteArray();
		
		return fileBytes;
	}

	/**
	 * Método mais "legível" para gerar a codelist em outras partes do projeto
	 * @param codelistName -> nome da codelist a salvar
	 * @param codelistLines -> linhas da codelist a salvar
	 * @throws IOException
	 * @author Bárbara Port
	 */
	public static byte[] generateCodelistFile (String codelistName, List<Linha> codelistLines) throws IOException {
		return createCodelistSheet(codelistName, codelistLines);
	}
	
	/**
	 * Método que atualiza o arquivo da codelist no servidor (no momento de exportar o projeto)
	 * @param codelistName -> o nome da codelist a ser alterada
	 * @param codelistLines -> as linhas da codelist a ser alterada
	 * @throws IOException
	 * @author Bárbara Port
	 */
	public static void updateCodelistFile (String codelistName, List<Linha> codelistLines) throws IOException {
		String pathToSave = EnvironmentVariables.PROJECTS_FOLDER.getValue();
		
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(codelistName);
		
		buildCodelistFile(sheet, codelistLines);
		
		String fileLocation = pathToSave.concat("\\").concat(codelistName).concat("\\").concat(codelistName).concat(".xlsx");
		FileOutputStream outputStream = new FileOutputStream(fileLocation);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
}
