package mg.grh.repository;

import mg.grh.model.StatutOffre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatutOffreRepository extends JpaRepository<StatutOffre, Integer> {
    Optional<StatutOffre> findByLibelle(String libelle);
}
