package api.crabteam.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.crabteam.model.entities.Remark;

public interface RemarkRepository extends JpaRepository<Remark, Integer> {
	
	@Query(value = "select * from remark r where r.traco = ?1", nativeQuery = true)
	List<Remark> findAllRemarks(String traco);
	
}
