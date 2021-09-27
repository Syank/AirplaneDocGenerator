package api.crabteam.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import api.crabteam.model.entities.Codelist;

public interface CodelistRepository extends JpaRepository<Codelist, Integer> {

}
