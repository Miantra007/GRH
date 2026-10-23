package mg.grh.repository;

import mg.grh.model.OffreEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface OffreEmploiRepository extends JpaRepository<OffreEmploi, Integer> {
    Optional<OffreEmploi> findByBesoinPosteId(Integer besoinPosteId);
    @Query("select o from OffreEmploi o join o.statutOffre s where o.visibleClient = true and s.libelle = 'PUBLIEE' and (o.dateLimite is null or o.dateLimite >= CURRENT_DATE) order by o.datePublication desc")
    List<OffreEmploi> findOffresPubliees();

    @Query("select o from OffreEmploi o join o.statutOffre s where o.id = :id and o.visibleClient = true and s.libelle = 'PUBLIEE' and (o.dateLimite is null or o.dateLimite >= CURRENT_DATE)")
    Optional<OffreEmploi> findOffrePubliee(@Param("id") Integer id);

    /** Candidats de cette offre dont le contrat est validé (donc en poste). */
    @Query(nativeQuery = true, value = """
            SELECT COUNT(*) FROM contrat_candidat cc
            JOIN candidature ca      ON ca.id_candidature = cc.id_candidature
            LEFT JOIN contrat_ce ce  ON ce.id = cc.id_contrat_ce
            LEFT JOIN contrat_cdd cd ON cd.id = cc.id_contrat_cdd
            LEFT JOIN contrat_cdi ci ON ci.id = cc.id_contrat_cdi
            JOIN statut_contrat sc   ON sc.id = COALESCE(ce.statut_id, cd.statut_id, ci.statut_id)
            WHERE ca.id_offre = :id AND sc.libelle = 'Validé'
            """)
    long countEmbauchesValidees(@Param("id") Integer offreId);
    /** Offres encore en brouillon dont le poste a été validé par la Direction générale. */
    @Query("select o from OffreEmploi o, BesoinPoste bp join o.statutOffre s where bp.id = o.besoinPosteId and bp.decisionDg = 'VALIDEE' and s.libelle = 'BROUILLON' order by o.id")
    List<OffreEmploi> findOffresAPublier();
}
