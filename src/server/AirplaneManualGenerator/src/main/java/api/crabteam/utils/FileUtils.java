package api.crabteam.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileUtils {
	
	
	
	public static void saveFile(byte[] fileBytes, String fileName, String pathToSave) throws IOException {
		try(OutputStream output = new FileOutputStream(pathToSave + "\\" + fileName)){
			output.write(fileBytes);
			
		}
		
	}
	
	public static void saveCodelistFile(byte[] fileBytes, String fileName, String pathToSave, String codelistName) throws IOException {
		saveFile(fileBytes, fileName, pathToSave);
		
		Workbook workbook = readAsExcel(pathToSave + "\\" + fileName);
		
		int sheetsAmount = workbook.getNumberOfSheets();
		
		for (int i = sheetsAmount - 1; i >= 0; i--) {
			String sheetName = workbook.getSheetAt(i).getSheetName();
			
			if(!sheetName.equals(codelistName)) {
				workbook.removeSheetAt(i);
				
			}
			
		}
		
		try (FileOutputStream outputStream = new FileOutputStream(pathToSave + "\\" + fileName)) {
            workbook.write(outputStream);
            
        }
		
	}
	
	public static Workbook readAsExcel(String filePath) throws IOException {
		File file = new File(filePath);
		
		try(FileInputStream fileInput = new FileInputStream(file)){
			Workbook workbook = new XSSFWorkbook(fileInput);
			
			return workbook;
		}
		
	}
	
	public static boolean renameFile(String filePath, String oldName, String newName) {
		File oldCodelistFile = new File(filePath + "\\" + oldName);
		File newCodelistFile = new File(filePath + "\\" + newName);
		
		boolean fileNameChanged = oldCodelistFile.renameTo(newCodelistFile);
		
		return fileNameChanged;
	}
	
	public static void renameCodelistSheet(String filePath, String codelistFileName, String sheetOldName, String sheetNewName) throws IOException {
		Workbook workbook = readAsExcel(filePath + "\\" + codelistFileName);
		
		int sheetIndex = workbook.getSheetIndex(sheetOldName);
		
		workbook.setSheetName(sheetIndex, sheetNewName);
		
		try (FileOutputStream outputStream = new FileOutputStream(filePath + "\\" + codelistFileName)) {
            workbook.write(outputStream);
            
        }
		
	}
	
}
