package mg.grh.repository;

import mg.grh.model.NiveauLangue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NiveauLangueRepository extends JpaRepository<NiveauLangue, Integer> {
}
