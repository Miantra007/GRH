package mg.grh.repository;

import mg.grh.model.HistoriqueContratEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoriqueContratEmployeRepository extends JpaRepository<HistoriqueContratEmploye, Integer> {

    /** Employé déjà embauché pour cette candidature (par un contrat précédent, ex. essai puis CDI). */
    Optional<HistoriqueContratEmploye> findFirstByContratCandidatIdCandidatureOrderByIdAsc(Integer idCandidature);

    /** Embauche enregistrée pour ce contrat. */
    Optional<HistoriqueContratEmploye> findFirstByContratCandidatIdOrderByIdAsc(Integer idContratCandidat);
}
