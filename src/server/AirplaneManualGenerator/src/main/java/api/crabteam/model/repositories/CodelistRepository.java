package api.crabteam.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.crabteam.model.entities.Codelist;

public interface CodelistRepository extends JpaRepository<Codelist, Integer> {

	@Query("select c from codelist c where c.nome = ?1")
	Codelist findByName(String name);
}
