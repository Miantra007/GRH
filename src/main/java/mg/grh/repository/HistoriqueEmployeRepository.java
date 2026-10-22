package mg.grh.repository;

import mg.grh.model.HistoriqueEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueEmployeRepository extends JpaRepository<HistoriqueEmploye, Integer> {
    List<HistoriqueEmploye> findAllByOrderByDateDebutDescIdDesc();
}
