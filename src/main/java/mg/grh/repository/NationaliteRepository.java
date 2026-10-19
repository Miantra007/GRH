package mg.grh.repository;

import mg.grh.model.Nationalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationaliteRepository extends JpaRepository<Nationalite, Integer> {
}
