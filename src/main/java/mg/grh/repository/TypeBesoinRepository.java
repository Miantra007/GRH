package mg.grh.repository;

import mg.grh.model.TypeBesoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeBesoinRepository extends JpaRepository<TypeBesoin, Integer> {
    Optional<TypeBesoin> findByLibelle(String libelle);
}
