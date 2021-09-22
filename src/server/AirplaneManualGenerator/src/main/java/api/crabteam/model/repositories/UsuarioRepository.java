package api.crabteam.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.crabteam.model.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String>{
	
	@Query("select user from usuario user where user.email = ?1")
	Usuario findByEmail(String email);
	
	@Query("delete from usuario user where user.email = ?1")
	Usuario deleteByEmail(String email);
	
}