package mg.grh.repository;

import mg.grh.model.TypeBesoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeBesoinRepository extends JpaRepository<TypeBesoin, Integer> {
}
