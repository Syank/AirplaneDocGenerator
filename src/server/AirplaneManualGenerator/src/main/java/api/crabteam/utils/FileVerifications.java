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
		String sectionName = linha.getSectionName();
		String blockNumber = linha.getBlockNumber();
		String blockName = linha.getBlockName();
		String code = linha.getCode();
		String subsectionNumber = linha.getSubsectionNumber();
		String subsectionName = linha.getSubsectionName();
		
		String fileName = codelistName.concat("-").concat(sectionNumber).concat("-");
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
		if (linha.getSubsectionNumber() != null) {
			subsectionNumber = linha.getSubsectionNumber();
			subsectionName = linha.getSubsectionName();
			
			fileName = fileName.concat(subsectionNumber).concat("-");
			strFilePath = strFilePath
							.concat(subsectionNumber)
							.concat(" ")
							.concat(subsectionName)
							.concat("/");
		}
		
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
	
	/**
	 * Excluí as pastas vazias de um dado diretório
	 * 
	 * @param directory - Diretório para verificar as pastas vazias
	 * @author Rafael Furtado
	 */
	public static void deleteEmptyFolders(File directory) {
	    File[] contents = directory.listFiles();
	    
	    if(contents.length == 0) {
	    	directory.delete();
	    	
	    }else {
		    for (int i = 0; i < contents.length; i++) {
				File content = contents[i];
				
				if(content.isDirectory()) {
					deleteEmptyFolders(content);
					
					if(content.listFiles() != null && content.listFiles().length == 0) {
						content.delete();
						
					}
					
				}
				
			}
		    
	    }

	}

}
