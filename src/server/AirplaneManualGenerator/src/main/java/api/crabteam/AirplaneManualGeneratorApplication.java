package api.crabteam;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import api.crabteam.model.entities.Administrador;
import api.crabteam.model.entities.Codelist;
import api.crabteam.model.entities.Linha;
import api.crabteam.model.entities.Projeto;
import api.crabteam.model.entities.Remark;
import api.crabteam.model.entities.Usuario;
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
			Administrador admin = new Administrador("System Admin", "admin@crabteam.com", "crabteam");
					
			userRep.save(admin);
			
		}
		
		Projeto projeto1 = new Projeto("ABO-1234", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ex massa, posuere quis massa sed");
		Projeto projeto2 = new Projeto("AOC-2851", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ex massa, posuere quis massa sed, bibendum fringilla purus. Vivamus est turpis, semper sit amet augue non");
		Projeto projeto3 = new Projeto("ATC-7412", "Vivamus est turpis, semper sit amet augue non");
		Projeto projeto4 = new Projeto("ACC-5233", "consectetur adipiscing elit. Nulla ex massa, posuere quis massa sed");
		Projeto projeto5 = new Projeto("BCC-1235", "posuere quis massa sed, bibendum fringilla purus. Vivamus est");
		Projeto projeto6 = new Projeto("ADC-2686", "Vivamus est turpis, semper sit amet augue non");
		Projeto projeto7 = new Projeto("ZDC-1987", "consectetur adipiscing elit. Nulla ex massa, posuere quis massa sed");
		
		projRep.save(projeto1);
		projRep.save(projeto2);
		projRep.save(projeto3);
		projRep.save(projeto4);
		projRep.save(projeto5);
		projRep.save(projeto6);
		projRep.save(projeto7);
		
		
		Projeto projeto = new Projeto("ABY-1234", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ex massa, posuere quis massa sed");
		
		Codelist codelist = new Codelist();
		codelist.setNome("ABY-1234");
		
		Linha linha = new Linha();
		linha.setBlock("00");
		linha.setBlockName("Letter");
		linha.setCode("00");
		linha.setFilePath("Path//to:c//file.txt");
		linha.setSecao("01");
		linha.setSubSecao("002");
		
		Remark remark = new Remark();
		remark.setApelido("Mars");
		remark.setTraco("-50");
		
		ArrayList<Remark> remarkList = new ArrayList<Remark>();
		remarkList.add(remark);
		
		ArrayList<Linha> linhaList = new ArrayList<Linha>();
		linhaList.add(linha);
		
		linha.setRemarks(remarkList);
		
		codelist.setLinhas(linhaList);
		
		projeto.setCodelist(codelist);
		
		projRep.save(projeto);
		
		Projeto proj = projRep.findByName("ABY-1234");
		
		System.out.println(proj.getNome());
		System.out.println(proj.getCodelist().getNome());
		System.out.println(proj.getCodelist().getLinhas().get(0).getFilePath());
		System.out.println(proj.getCodelist().getLinhas().get(0).getRemarks().get(0).getApelido());
		
	}

}
