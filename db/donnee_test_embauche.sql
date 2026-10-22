-- =====================================================================
-- GRH : DONNÉES DE TEST pour l'embauche à la validation d'un contrat
--
-- Ajoute un besoin de type REMPLACEMENT : le candidat Nirina Rakoto remplace
-- Fara Randrianarisoa au poste « Comptable » du département Marketing.
-- (Le besoin CREATION est celui de Hery Rabe, dans donnee_test_acceptation.sql.)
--
-- À exécuter APRÈS donnee_test_acceptation.sql et grh_embauche.sql.
-- Compte : nirina.rakoto@mail.mg / motdepasse123. Sans danger si relancé.
-- =====================================================================
\c grh

BEGIN;

INSERT INTO candidat (nom_candidat, prenom_candidat, email, mot_de_passe, dtn)
VALUES ('Rakoto', 'Nirina', 'nirina.rakoto@mail.mg',
        '$2b$12$AL9SLkK5ftyES4VMS/kY7u6HFPsSRN5kwJzOlR/H7jOfXsr0ylsWG', '1995-03-21')
ON CONFLICT (email) DO NOTHING;

-- Besoin de remplacement validé, pour le poste Comptable du département Marketing
INSERT INTO besoin (id_direction, id_dept, id_directeur_demandeur, id_type_besoin, motif,
                    id_statut_besoin, id_rh_traitant, date_validation_dg, visible_client)
SELECT d.id_direction, dep.id_dept, d.id_directeur, tb.id_type_besoin,
       'Remplacement de test - Comptable', sb.id_statut_besoin, rh.id_responsable, CURRENT_TIMESTAMP, TRUE
FROM poste p
JOIN direction d ON d.id_direction = p.id_direction
JOIN departement dep ON dep.nom_dept = 'Marketing'
JOIN type_besoin tb ON tb.libelle = 'REMPLACEMENT'
JOIN statut_besoin sb ON sb.libelle = 'VALIDEE'
JOIN responsable rh ON rh.email = 'rh@entreprise.mg'
WHERE p.libelle = 'Comptable'
  AND NOT EXISTS (SELECT 1 FROM besoin WHERE motif = 'Remplacement de test - Comptable');

INSERT INTO besoin_poste (id_besoin, id_poste, nombre_postes, missions, id_type_contrat,
                          salaire_propose_rh, salaire_valide_finance, horaire_travail,
                          responsable_hierarchique, date_prise_poste)
SELECT b.id_besoin, p.id_poste, 1, 'Tenue de la comptabilité, rapprochements bancaires, clôtures mensuelles',
       tc.id_type_contrat, 1000000, 1000000, 'Du lundi au vendredi, de 8 h à 16 h',
       'Responsable financier', CURRENT_DATE + 14
FROM besoin b
JOIN poste p ON p.libelle = 'Comptable'
JOIN type_contrat tc ON tc.libelle = 'CDI'
WHERE b.motif = 'Remplacement de test - Comptable'
ON CONFLICT (id_besoin, id_poste) DO NOTHING;

INSERT INTO offre_emploi (id_besoin_poste, titre, description, profil_recherche, date_publication,
                          date_limite, id_statut_offre, visible_client)
SELECT bp.id_besoin_poste, 'Remplacement de test - Comptable', 'Offre de test pour un remplacement.',
       'Diplôme en comptabilité, 2 ans d''expérience.', CURRENT_TIMESTAMP, CURRENT_DATE + 30, so.id_statut_offre, TRUE
FROM besoin_poste bp
JOIN besoin b ON b.id_besoin = bp.id_besoin
JOIN statut_offre so ON so.libelle = 'PUBLIEE'
WHERE b.motif = 'Remplacement de test - Comptable'
  AND NOT EXISTS (SELECT 1 FROM offre_emploi o WHERE o.id_besoin_poste = bp.id_besoin_poste);

INSERT INTO candidature (id_offre, id_candidat, id_statut_candidature)
SELECT o.id_offre, c.id_candidat, sc.id_statut_candidature
FROM offre_emploi o
JOIN candidat c ON c.email = 'nirina.rakoto@mail.mg'
JOIN statut_candidature sc ON sc.libelle = 'RETENUE'
WHERE o.titre = 'Remplacement de test - Comptable'
  AND NOT EXISTS (SELECT 1 FROM candidature ca WHERE ca.id_offre = o.id_offre AND ca.id_candidat = c.id_candidat);

-- Proposition déjà acceptée : le RH peut créer le contrat tout de suite
INSERT INTO proposition_embauche (id_candidature, id_reponse_candidat, date_reponse_candidat,
                                  salaire_final, id_statut_proposition)
SELECT ca.id_candidature, rc.id_reponse_candidat, CURRENT_TIMESTAMP, 1000000, sp.id_statut_proposition
FROM candidature ca
JOIN candidat c ON c.id_candidat = ca.id_candidat AND c.email = 'nirina.rakoto@mail.mg'
JOIN reponse_candidat rc ON rc.libelle = 'OUI'
JOIN statut_proposition sp ON sp.libelle = 'ACCORD_FINAL'
ON CONFLICT (id_candidature) DO NOTHING;

COMMIT;
