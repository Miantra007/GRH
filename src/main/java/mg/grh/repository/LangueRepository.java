package mg.grh.repository;

import mg.grh.model.Langue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LangueRepository extends JpaRepository<Langue, Integer> {
    Optional<Langue> findByNomLangueIgnoreCase(String nomLangue);
}
