package mg.grh.repository;

import mg.grh.model.ConsigneIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsigneIntegrationRepository extends JpaRepository<ConsigneIntegration, Integer> {
    List<ConsigneIntegration> findByCandidatIdOrderByDateEnvoiDesc(Integer candidatId);
}
