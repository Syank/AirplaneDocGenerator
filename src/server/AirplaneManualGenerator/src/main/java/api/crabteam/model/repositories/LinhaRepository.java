package api.crabteam.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import api.crabteam.model.entities.Linha;

public interface LinhaRepository extends JpaRepository<Linha, Integer> {

}
