package mg.grh.repository;

import mg.grh.model.ReponseCritere;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReponseCritereRepository extends JpaRepository<ReponseCritere, Integer> {
    Optional<ReponseCritere> findByLibelle(String libelle);
}