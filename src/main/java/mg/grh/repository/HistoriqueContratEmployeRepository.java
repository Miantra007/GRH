package mg.grh.repository;

import mg.grh.model.HistoriqueContratEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueContratEmployeRepository extends JpaRepository<HistoriqueContratEmploye, Integer> {
}
