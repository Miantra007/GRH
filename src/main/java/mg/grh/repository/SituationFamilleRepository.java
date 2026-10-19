package mg.grh.repository;

import mg.grh.model.SituationFamille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SituationFamilleRepository extends JpaRepository<SituationFamille, Integer> {
}
