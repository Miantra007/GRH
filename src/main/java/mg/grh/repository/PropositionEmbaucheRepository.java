package mg.grh.repository;

import mg.grh.model.PropositionEmbauche;

import mg.grh.model.PropositionInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PropositionEmbaucheRepository extends JpaRepository<PropositionEmbauche, Integer> {

    @Query("select p from PropositionEmbauche p where p.idCandidature = :candidatureId")
    Optional<PropositionEmbauche> findByCandidatureId(@Param("candidatureId") Integer candidatureId);
    
    @Query(nativeQuery = true, value = """
            SELECT pe.id_proposition  AS "idProposition",
                   c.id_candidat      AS "idCandidat",
                   c.nom_candidat     AS "nomCandidat",
                   c.prenom_candidat  AS "prenomCandidat",
                   o.titre            AS "titreOffre",
                   b.id_rh_traitant   AS "idRhTraitant",
                   COALESCE(bp.salaire_valide_finance, bp.salaire_propose_rh) AS "salaireInitial",
                   bp.horaire_travail            AS "horaireTravail",
                   bp.responsable_hierarchique   AS "responsableHierarchique",
                   bp.missions                   AS "missions",
                   to_char(bp.date_prise_poste, 'DD/MM/YYYY') AS "datePrisePoste"
            FROM proposition_embauche pe
            JOIN candidature ca  ON ca.id_candidature = pe.id_candidature
            JOIN candidat c      ON c.id_candidat = ca.id_candidat
            JOIN offre_emploi o  ON o.id_offre = ca.id_offre
            JOIN besoin_poste bp ON bp.id_besoin_poste = o.id_besoin_poste
            JOIN besoin b        ON b.id_besoin = bp.id_besoin
            WHERE pe.id_proposition = :id
            """)
    Optional<PropositionInfo> findInfo(@Param("id") Integer id);

}
