package mg.grh.repository;

import mg.grh.model.ReponseCandidat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReponseCandidatRepository extends JpaRepository<ReponseCandidat, Integer> {
    Optional<ReponseCandidat> findByLibelle(String libelle);
}