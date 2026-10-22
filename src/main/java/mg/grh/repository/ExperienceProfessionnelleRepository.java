package mg.grh.repository;

import mg.grh.model.ExperienceProfessionnelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExperienceProfessionnelleRepository extends JpaRepository<ExperienceProfessionnelle, Integer> {
    List<ExperienceProfessionnelle> findByCvId(Integer cvId);
    void deleteByCvId(Integer cvId);
}
