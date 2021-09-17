package api.crabteam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import api.crabteam.model.Administrador;
import api.crabteam.model.Projeto;
import api.crabteam.model.Usuario;
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
		Usuario user = userRep.findByEmail("admin@root.crabteam");
		
		if(user == null) {
			Administrador admin = new Administrador("System Admin", "a", "a");
					
			userRep.save(admin);
			
		}
		
		Projeto projeto1 = new Projeto("ABO-1234", "Um projeto qualquer apenas para teste");
		Projeto projeto2 = new Projeto("AOC-2851", "Um projeto qualquer apenas para teste");
		Projeto projeto3 = new Projeto("ATC-7412", "Um projeto qualquer apenas para teste");
		Projeto projeto4 = new Projeto("ACC-5233", "Um projeto qualquer apenas para teste");
		Projeto projeto5 = new Projeto("BCC-1235", "Um projeto qualquer apenas para teste");
		Projeto projeto6 = new Projeto("ADC-2686", "Um projeto qualquer apenas para teste");
		Projeto projeto7 = new Projeto("ZDC-1987", "Um projeto qualquer apenas para teste");
		
		projRep.save(projeto1);
		projRep.save(projeto2);
		projRep.save(projeto3);
		projRep.save(projeto4);
		projRep.save(projeto5);
		projRep.save(projeto6);
		projRep.save(projeto7);
		
	}

}
