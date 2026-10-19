package mg.grh.repository;

import mg.grh.model.Travailleur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravailleurRepository extends JpaRepository<Travailleur, Integer> {
}
