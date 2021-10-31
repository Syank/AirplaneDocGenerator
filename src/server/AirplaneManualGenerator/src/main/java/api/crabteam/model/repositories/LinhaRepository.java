package api.crabteam.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.crabteam.model.entities.Linha;

public interface LinhaRepository extends JpaRepository<Linha, Integer> {
	
	@Query(value = "select c.nome from codelist c, codelist_linhas cl where cl.linhas_id = ?1 and c.id = cl.codelist_id", nativeQuery = true)
	String findCodelist(Integer id);

}
