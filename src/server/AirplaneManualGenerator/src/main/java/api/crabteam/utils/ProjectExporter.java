package api.crabteam.utils;

import java.io.File;
import java.io.IOException;

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
