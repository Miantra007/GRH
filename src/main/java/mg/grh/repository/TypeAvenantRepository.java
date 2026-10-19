package mg.grh.repository;

import mg.grh.model.TypeAvenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeAvenantRepository extends JpaRepository<TypeAvenant, Integer> {
}
