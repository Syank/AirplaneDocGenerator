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
	
	public static Workbook readAsExcel(String filePath) throws IOException {
		File file = new File(filePath);
		
		try(FileInputStream fileInput = new FileInputStream(file)){
			Workbook workbook = new XSSFWorkbook(fileInput);
			
			return workbook;
		}
		
	}
	
}
