package mg.grh.repository;

import mg.grh.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer> {

    /** Employés qui occupent actuellement un poste (siège). */
    List<Employe> findByPosteFilleIsNotNullOrderByIdDesc();

    List<Employe> findAllByOrderByIdDesc();

    /** Occupants d'un poste dans un département (un siège = au plus un employé rattaché). */
    List<Employe> findByPosteFillePosteIdAndPosteFilleDeptId(Integer posteId, Integer deptId);

    boolean existsByPosteFilleId(Integer posteFilleId);
}
