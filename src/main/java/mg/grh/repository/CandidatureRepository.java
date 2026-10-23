package mg.grh.repository;

import mg.grh.model.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Integer> {
    long countByOffreId(Integer offreId);
    boolean existsByOffreIdAndCandidatId(Integer offreId, Integer candidatId);
    List<Candidature> findByCandidatIdOrderByDateCandidatureDesc(Integer candidatId);
}
