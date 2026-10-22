package mg.grh.repository;

import mg.grh.model.StatutBesoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutBesoinRepository extends JpaRepository<StatutBesoin, Integer> {
}
