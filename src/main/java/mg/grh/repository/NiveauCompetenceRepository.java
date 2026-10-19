package mg.grh.repository;

import mg.grh.model.NiveauCompetence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NiveauCompetenceRepository extends JpaRepository<NiveauCompetence, Integer> {
}
