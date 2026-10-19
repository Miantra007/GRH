package mg.grh.repository;

import mg.grh.model.StatutPoste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutPosteRepository extends JpaRepository<StatutPoste, Integer> {
}
