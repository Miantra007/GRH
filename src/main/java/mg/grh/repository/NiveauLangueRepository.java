package mg.grh.repository;

import mg.grh.model.NiveauLangue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NiveauLangueRepository extends JpaRepository<NiveauLangue, Integer> {
    Optional<NiveauLangue> findByLibelleNiveau(String libelleNiveau);
}
