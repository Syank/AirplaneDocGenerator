package api.crabteam.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import api.crabteam.model.enumarations.EnvironmentVariables;

/**
 * Classe que contém os métodos necessários para exportar um projeto
 * @author Bárbara Port
 *
 */
public class ProjectExporter {

	/**
	 * Método que compacta todas as pastas do projeto
	 * @param folderToZip pasta em que os arquivos do projeto se localizam
	 * @param projectName nome do projeto
	 * @return byte[] para o envio do arquivo ao usuário
	 * @throws IOException
	 * @author Bárbara Port
	 */
	private static byte[] zipFolder (String folderToZip, String projectName) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		
		ByteArrayOutputStream fileByteArrayOutputStream = FileVerifications.getSubfolders(new File(folderToZip), bos, zos);
		
		bos.close();
	    zos.close();
	    
	    byte[] fileBytes = fileByteArrayOutputStream.toByteArray();
		return fileBytes;
	}
	
	/**
	 * Método mais legível para exportar um projeto
	 * @param projectName nome do projeto
	 * @return byte[] para o envio do arquivo ao usuário
	 * @throws IOException
	 * @author Bárbara Port
	 */
	public static byte[] exportProject (String projectName) throws IOException {
        String folderToZip = EnvironmentVariables.PROJECTS_FOLDER.getValue().concat("\\").concat(projectName);
        
        File projectFolder = new File(folderToZip);
        File outputZip = new File(EnvironmentVariables.PROJECTS_FOLDER.getValue() + "\\" + projectName + ".zip");
        
        ZipUtil.pack(projectFolder, outputZip);
        
        byte[] fileBytes = FileUtils.readFileToByteArray(outputZip);
        
        outputZip.delete();
        
        return fileBytes;
	}
}
