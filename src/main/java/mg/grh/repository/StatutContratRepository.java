package mg.grh.repository;

import mg.grh.model.StatutContrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatutContratRepository extends JpaRepository<StatutContrat, Integer> {
    Optional<StatutContrat> findByLibelle(String libelle);
}
