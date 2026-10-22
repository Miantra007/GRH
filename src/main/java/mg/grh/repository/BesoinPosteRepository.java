package mg.grh.repository;

import mg.grh.model.BesoinPoste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BesoinPosteRepository extends JpaRepository<BesoinPoste, Integer> {
}
