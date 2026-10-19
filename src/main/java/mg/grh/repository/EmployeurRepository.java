package mg.grh.repository;

import mg.grh.model.Employeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeurRepository extends JpaRepository<Employeur, Integer> {
}
