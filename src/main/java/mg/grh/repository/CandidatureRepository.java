package mg.grh.repository;

import mg.grh.model.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CandidatureRepository extends JpaRepository<Candidature, Integer> {
    long countByOffreId(Integer offreId);
    boolean existsByOffreIdAndCandidatId(Integer offreId, Integer candidatId);
    List<Candidature> findByCandidatIdOrderByDateCandidatureDesc(Integer candidatId);
    Optional<Candidature> findByCodeSuivi(String codeSuivi);
}
