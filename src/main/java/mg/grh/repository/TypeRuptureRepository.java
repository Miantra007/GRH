package mg.grh.repository;

import mg.grh.model.TypeRupture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRuptureRepository extends JpaRepository<TypeRupture, Integer> {
}
