package mg.grh.repository;

import mg.grh.model.Langue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LangueRepository extends JpaRepository<Langue, Integer> {
}
