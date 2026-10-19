package mg.grh.repository;

import mg.grh.model.CategorieProfessionnelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieProfessionnelleRepository extends JpaRepository<CategorieProfessionnelle, Integer> {
}
