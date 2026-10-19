package mg.grh.repository;

import mg.grh.model.Civilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CiviliteRepository extends JpaRepository<Civilite, Integer> {
}
