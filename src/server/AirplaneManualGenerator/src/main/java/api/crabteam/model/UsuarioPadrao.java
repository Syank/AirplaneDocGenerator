package api.crabteam.model;

import javax.persistence.Entity;

@Entity(name="usuario_comum")
public class UsuarioPadrao extends Usuario{

	public UsuarioPadrao() {
		
	}
	
	public UsuarioPadrao(String nome, String email, String senha) {
		super(nome, email, senha);
		
	}
	
}
