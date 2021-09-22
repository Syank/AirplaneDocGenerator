package api.crabteam.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.crabteam.model.entities.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {

	@Query("select proj from projeto proj where proj.nome = ?1")
	Projeto findByName(String name);
}
