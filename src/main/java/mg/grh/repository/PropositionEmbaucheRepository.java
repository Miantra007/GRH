package mg.grh.repository;

import mg.grh.model.PropositionEmbauche;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PropositionEmbaucheRepository extends JpaRepository<PropositionEmbauche, Integer> {
    Optional<PropositionEmbauche> findByCandidatureId(Integer candidatureId);
}
