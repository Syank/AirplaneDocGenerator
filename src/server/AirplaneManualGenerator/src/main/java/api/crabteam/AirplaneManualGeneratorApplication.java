package api.crabteam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import api.crabteam.model.Administrador;
import api.crabteam.model.Usuario;
import api.crabteam.model.repositories.UsuarioRepository;

@SpringBootApplication
public class AirplaneManualGeneratorApplication implements CommandLineRunner {
	
	@Autowired
	UsuarioRepository userRep;

	public static void main(String[] args) {
		SpringApplication.run(AirplaneManualGeneratorApplication.class, args);
		
	}
	
	@Override
	public void run(String... args) throws Exception {
		Usuario user = userRep.findByEmail("admin@root.crabteam");
		
		if(user == null) {
			Administrador admin = new Administrador("System Admin", "a", "a");
					
			userRep.save(admin);
			
		}
		
	}

}
