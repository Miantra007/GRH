package mg.grh.repository;

import mg.grh.model.TypeConsigne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeConsigneRepository extends JpaRepository<TypeConsigne, Integer> {
}
