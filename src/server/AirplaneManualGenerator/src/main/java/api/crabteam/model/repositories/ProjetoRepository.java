package api.crabteam.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import api.crabteam.model.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {

}
