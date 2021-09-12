package api.crabteam.model;

import javax.persistence.Entity;

@Entity(name="administrador")
public class Administrador extends Usuario{

	public Administrador() {
		
	}
	
	public Administrador(String nome, String email, String senha) {
		super(nome, email, senha);
		
	}
	
}
