package mg.grh.repository;

import mg.grh.model.TypeContrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeContratRepository extends JpaRepository<TypeContrat, Integer> {
}
