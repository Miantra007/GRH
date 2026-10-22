-- Offres de démonstration pour l'espace candidat.
-- À exécuter après db/base_miantra.sql et les données de base.
-- Le script peut être relancé : les offres portant ces titres ne sont pas dupliquées.
\c grh
BEGIN;

INSERT INTO type_besoin (libelle) VALUES ('CREATION') ON CONFLICT (libelle) DO NOTHING;
INSERT INTO statut_besoin (libelle) VALUES ('VALIDEE') ON CONFLICT (libelle) DO NOTHING;
INSERT INTO type_contrat (libelle) VALUES ('CDI'), ('CDD'), ('STAGE') ON CONFLICT (libelle) DO NOTHING;
INSERT INTO statut_offre (libelle) VALUES ('PUBLIEE') ON CONFLICT (libelle) DO NOTHING;

DO $$
DECLARE
    exemple RECORD;
    v_direction INTEGER;
    v_dept INTEGER;
    v_directeur INTEGER;
    v_poste INTEGER;
    v_besoin INTEGER;
    v_besoin_poste INTEGER;
BEGIN
    SELECT id_direction INTO v_direction FROM direction ORDER BY id_direction LIMIT 1;
    SELECT id_dept INTO v_dept FROM departement ORDER BY id_dept LIMIT 1;
    SELECT id_directeur INTO v_directeur FROM direction
      WHERE id_directeur IS NOT NULL ORDER BY id_direction LIMIT 1;
    IF v_directeur IS NULL THEN
        SELECT id_responsable INTO v_directeur FROM responsable ORDER BY id_responsable LIMIT 1;
    END IF;

    IF v_direction IS NULL OR v_dept IS NULL OR v_directeur IS NULL THEN
        RAISE EXCEPTION 'Données organisationnelles manquantes. Exécutez db/grh_donnees_test.sql avant ce script.';
    END IF;

    FOR exemple IN
        SELECT * FROM (VALUES
          ('Développeur Java', 'Développer et maintenir les applications métier.', 'Java, Spring Boot, PostgreSQL', 'CDI', CURRENT_DATE + 45),
          ('Assistant(e) ressources humaines', 'Participer au suivi administratif et au recrutement.', 'Gestion RH, communication, organisation', 'CDD', CURRENT_DATE + 45),
          ('Stagiaire support informatique', 'Accompagner les utilisateurs et documenter les incidents.', 'Support, bureautique, bases réseau', 'STAGE', CURRENT_DATE + 60)
        ) AS offres(titre, description, profil, contrat, date_limite)
    LOOP
        IF NOT EXISTS (SELECT 1 FROM offre_emploi WHERE titre = exemple.titre) THEN
            SELECT id_poste INTO v_poste FROM poste
              WHERE libelle = exemple.titre AND id_direction = v_direction
              ORDER BY id_poste LIMIT 1;
            IF v_poste IS NULL THEN
                INSERT INTO poste (libelle, id_direction, date_creation)
                VALUES (exemple.titre, v_direction, CURRENT_DATE)
                RETURNING id_poste INTO v_poste;
            END IF;

            INSERT INTO besoin (id_direction, id_dept, id_directeur_demandeur,
                id_type_besoin, motif, id_statut_besoin, visible_client, date_publication_client)
            VALUES (v_direction, v_dept, v_directeur,
                (SELECT id_type_besoin FROM type_besoin WHERE libelle = 'CREATION'),
                'Besoin de démonstration : ' || exemple.titre,
                (SELECT id_statut_besoin FROM statut_besoin WHERE libelle = 'VALIDEE'),
                TRUE, CURRENT_TIMESTAMP)
            RETURNING id_besoin INTO v_besoin;

            INSERT INTO besoin_poste (id_besoin, id_poste, nombre_postes, missions, id_type_contrat)
            VALUES (v_besoin, v_poste, 1, exemple.description,
                (SELECT id_type_contrat FROM type_contrat WHERE libelle = exemple.contrat))
            RETURNING id_besoin_poste INTO v_besoin_poste;

            INSERT INTO offre_emploi (id_besoin_poste, titre, description, profil_recherche,
                date_publication, date_limite, id_statut_offre, visible_client)
            VALUES (v_besoin_poste, exemple.titre, exemple.description, exemple.profil,
                CURRENT_TIMESTAMP, exemple.date_limite,
                (SELECT id_statut_offre FROM statut_offre WHERE libelle = 'PUBLIEE'), TRUE);
        END IF;
    END LOOP;
END $$;

COMMIT;
