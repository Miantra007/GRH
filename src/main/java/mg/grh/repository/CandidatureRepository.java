package mg.grh.repository;

import mg.grh.model.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Integer> {
    long countByOffreId(Integer offreId);
    boolean existsByOffreIdAndCandidatId(Integer offreId, Integer candidatId);
    List<Candidature> findByCandidatIdOrderByDateCandidatureDesc(Integer candidatId);

    /** Candidats retenus pour l'offre dont la proposition n'a pas été refusée (ils occupent un poste). */
    @org.springframework.data.jpa.repository.Query("""
            select count(c) from Candidature c
            where c.offre.id = :offreId and c.statut.libelle = 'RETENUE'
              and not exists (select p from PropositionEmbauche p
                              where p.idCandidature = c.id and p.statut.libelle in ('REFUSEE_CANDIDAT', 'REFUSEE_RH'))
            """)
    long countRetenusActifs(@org.springframework.data.repository.query.Param("offreId") Integer offreId);
}
