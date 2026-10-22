package mg.grh.repository;

import mg.grh.model.StatutEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatutEmployeRepository extends JpaRepository<StatutEmploye, Integer> {
    Optional<StatutEmploye> findByLibelle(String libelle);
}
