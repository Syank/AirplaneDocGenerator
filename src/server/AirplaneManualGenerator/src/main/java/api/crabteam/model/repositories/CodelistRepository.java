package api.crabteam.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.crabteam.model.entities.Codelist;

public interface CodelistRepository extends JpaRepository<Codelist, Integer> {

	@Query("select c from codelist c where c.nome = ?1")
	Codelist findByName(String name);
	
	@Query(value = "select id, block_name, block_number, code, file_path, section_number, subsection_number from linha l, codelist_linhas cl where cl.codelist_id = ?1 and l.id = cl.linhas_id", nativeQuery = true)
	List<?> findAllLines(Integer id);
}
