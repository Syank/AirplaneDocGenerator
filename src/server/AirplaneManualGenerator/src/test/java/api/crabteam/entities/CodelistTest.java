package api.crabteam.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import api.crabteam.model.entities.Codelist;
import api.crabteam.model.entities.builders.CodelistBuilder;

public class CodelistTest {
	
	@Test
	public void testeCodelistBuilder() throws Exception {
		File file = new File("C:\\Users\\rafs9\\Desktop\\Programação\\Projeto Integrador\\4º Semestre\\Mockup FATEC\\Mockup FATEC\\Codelist.xlsx");
		
		FileInputStream fileinput = new FileInputStream(file);
		
		byte[] byteFile = fileinput.readAllBytes();
		
		CodelistBuilder builder = new CodelistBuilder(byteFile, "ABC-1234");
		
		Codelist codelist = builder.getBuildedCodelist();
		
		System.out.println(codelist.getNome());
		
		assertEquals(codelist.getNome(), "ABC-1234");
		
	}
	
}
