package api.crabteam.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import api.crabteam.model.entities.Revisao;

public interface RevisaoRepository extends JpaRepository<Revisao, Integer> {

}
