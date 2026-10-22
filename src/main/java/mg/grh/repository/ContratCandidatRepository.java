package mg.grh.repository;

import mg.grh.model.BesoinInfo;
import mg.grh.model.CandidatureRetenueInfo;
import mg.grh.model.ContratCandidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContratCandidatRepository extends JpaRepository<ContratCandidat, Integer> {
    List<ContratCandidat> findByCandidatId(Integer candidatId);

    /** Besoin (type, poste, département) auquel répond une candidature. */
    @Query(nativeQuery = true, value = """
            SELECT b.id_besoin   AS "idBesoin",
                   tb.libelle    AS "typeBesoin",
                   bp.id_poste   AS "idPoste",
                   p.libelle     AS "libellePoste",
                   b.id_dept     AS "idDept",
                   d.nom_dept    AS "nomDept"
            FROM candidature ca
            JOIN offre_emploi o  ON o.id_offre = ca.id_offre
            JOIN besoin_poste bp ON bp.id_besoin_poste = o.id_besoin_poste
            JOIN besoin b        ON b.id_besoin = bp.id_besoin
            JOIN type_besoin tb  ON tb.id_type_besoin = b.id_type_besoin
            JOIN poste p         ON p.id_poste = bp.id_poste
            JOIN departement d   ON d.id_dept = b.id_dept
            WHERE ca.id_candidature = :id
            """)
    Optional<BesoinInfo> findBesoinInfo(@Param("id") Integer idCandidature);

    /** Candidatures d'un candidat dont la proposition d'embauche a reçu l'accord final. */
    @Query(nativeQuery = true, value = """
            SELECT ca.id_candidature AS "idCandidature",
                   o.titre           AS "titreOffre",
                   tb.libelle        AS "typeBesoin",
                   p.libelle         AS "libellePoste",
                   d.nom_dept        AS "nomDept"
            FROM candidature ca
            JOIN proposition_embauche pe ON pe.id_candidature = ca.id_candidature
            JOIN statut_proposition sp   ON sp.id_statut_proposition = pe.id_statut_proposition
                                        AND sp.libelle = 'ACCORD_FINAL'
            JOIN offre_emploi o          ON o.id_offre = ca.id_offre
            JOIN besoin_poste bp         ON bp.id_besoin_poste = o.id_besoin_poste
            JOIN besoin b                ON b.id_besoin = bp.id_besoin
            JOIN type_besoin tb          ON tb.id_type_besoin = b.id_type_besoin
            JOIN poste p                 ON p.id_poste = bp.id_poste
            JOIN departement d           ON d.id_dept = b.id_dept
            WHERE ca.id_candidat = :idCandidat
            ORDER BY ca.id_candidature
            """)
    List<CandidatureRetenueInfo> findCandidaturesRetenues(@Param("idCandidat") Integer idCandidat);
}
