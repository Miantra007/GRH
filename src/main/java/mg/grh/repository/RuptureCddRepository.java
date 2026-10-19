package mg.grh.repository;

import mg.grh.model.RuptureCdd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuptureCddRepository extends JpaRepository<RuptureCdd, Integer> {
}
