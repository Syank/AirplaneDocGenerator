package api.crabteam.utils;

import java.io.File;
import java.io.IOException;

import api.crabteam.model.entities.Linha;
import api.crabteam.model.enumarations.EnvironmentVariables;

/**
 * Classe destinada a fazer verificações 
 * @author Bárbara Port
 *
 */
public class FileVerifications {
	
	/**
	 * Método para verificar onde um arquivo ficará armazenado e qual será o seu nome
	 * @param linha a ser verificada
	 * @param codelistName a qual a linha pertence
	 * @return String[] contendo as duas informações
	 * @author Bárbara Port
	 */
	public static String[] fileDestination (Linha linha, String codelistName) {
		
		String sectionNumber = linha.getSectionNumber();
		String sectionName = "Section Name";
		String blockNumber = linha.getBlockNumber();
		String blockName = linha.getBlockName();
		String code = linha.getCode();
		String subsectionNumber = "";
		String subsectionName = "";
		
		String fileName = codelistName.concat("-").concat(blockNumber).concat("-");
		String fileExtension = ".pdf";
		String strFilePath = EnvironmentVariables.PROJECTS_FOLDER.getValue()
							.concat("/")
							.concat(codelistName)
							.concat("/Master/")
							.concat(sectionNumber)
							.concat(" ")
							.concat(sectionName)
							.concat("/");
		
		// Verifying subsection
		if (!(linha.getSubsectionNumber() == null)) {
			subsectionNumber = linha.getSubsectionNumber();
			subsectionName = "Subsection Name";
			
			fileName = fileName.concat(subsectionNumber).concat("-");
			strFilePath = strFilePath
							.concat(subsectionNumber)
							.concat(" ")
							.concat(subsectionName)
							.concat("/");
		}
		// -------------------
		fileName = fileName.concat(blockNumber).concat("c").concat(code).concat(fileExtension);
		strFilePath = strFilePath.concat(blockNumber)
								 .concat(" ")
								 .concat(blockName)
								 .concat("/");
		
		
		String[] fileInfos = {strFilePath, fileName};
		return fileInfos;
	}
	
	/**
	 * Método recursivo que verifica as subpastas e arquivos do diretório do projeto de manual
	 * @param projectPath (pasta do projeto de manual que desejo efetuar as mudanças)
	 * @param newProjectName (novo nome desse projeto de manual)
	 * @throws IOException
	 * @author Bárbara Port
	 */
	public static void getSubfolders (File projectPath, String newProjectName) throws IOException {
	    for (File content : projectPath.listFiles()) {
	       if (content.isDirectory()) {
	           getSubfolders(content, newProjectName);
	       }
	       else {
	    	   String splittedOldName[] = new String[5];
	    	   splittedOldName = content.getName().split("-");
	    	   
	    	   String[] splittedNewName = newProjectName.split("-");
	    	   
	    	   //setting letters
	    	   splittedOldName[0] = splittedNewName[0];
	    	   splittedOldName[1] = splittedNewName[1];
	    	   
	    	   String newFileName = String.join("-", splittedOldName);
	    	   content.renameTo(new File(content.getParent().concat("\\").concat(newFileName)));
	       }
	    }
	}
	
	/**
	 * Método que é mais "legível" no momento de renomear os arquivos de um projeto
	 * @param projectPath (pasta do projeto de manual que desejo efetuar as mudanças)
	 * @param newProjectName (novo nome desse projeto de manual)
	 * @throws IOException
	 * @author Bárbara Port
	 */
	public static void renameProjectFiles (File projectPath, String newProjectName) throws IOException {
		getSubfolders(projectPath, newProjectName);
	}

}
