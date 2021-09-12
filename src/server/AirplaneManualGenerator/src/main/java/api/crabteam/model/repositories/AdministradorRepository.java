package api.crabteam.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import api.crabteam.model.Usuario;

public interface AdministradorRepository extends JpaRepository<Usuario, String> {

}
