package mg.grh.repository;

import mg.grh.model.Responsable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResponsableRepository extends JpaRepository<Responsable, Integer> {
    Optional<Responsable> findByEmail(String email);
}
