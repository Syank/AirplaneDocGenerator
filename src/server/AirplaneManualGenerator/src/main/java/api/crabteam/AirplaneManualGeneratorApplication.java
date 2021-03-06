package api.crabteam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import api.crabteam.model.entities.Administrador;
import api.crabteam.model.entities.Usuario;
import api.crabteam.model.enumarations.EnvironmentVariables;
import api.crabteam.model.repositories.ProjetoRepository;
import api.crabteam.model.repositories.UsuarioRepository;

@SpringBootApplication
public class AirplaneManualGeneratorApplication implements CommandLineRunner {
	
	@Autowired
	UsuarioRepository userRep;
	
	@Autowired
	ProjetoRepository projRep;

	public static void main(String[] args) {
		SpringApplication.run(AirplaneManualGeneratorApplication.class, args);
		
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		// Verifica se a variável de apontamento para a pasta de uploads foi configurada
		if(EnvironmentVariables.PROJECTS_FOLDER.getValue() == null) {
			throw new Exception("A variável de ambiente para o apontamento da pasta de uploads não foi encontrada");
		}
		
		Usuario user = userRep.findByEmail("a");
		
		if(user == null) {
			Administrador admin = new Administrador("System Admin", "a", "a");
					
			userRep.save(admin);
			
		}

	}

}
