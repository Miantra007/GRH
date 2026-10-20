-- ============================================================
-- GRH : DONNEES DE TEST POUR LA PROPOSITION D'EMBAUCHE
-- Base de données : grh
-- ============================================================

BEGIN;

-- ============================================================
-- 1. AJOUTER LES STATUTS NECESSAIRES
-- ============================================================

-- Statuts du besoin
INSERT INTO statut_besoin (libelle)
VALUES
    ('EN_ATTENTE_RH'),
    ('EN_ATTENTE_FINANCE'),
    ('EN_ATTENTE_DG'),
    ('VALIDEE'),
    ('REFUSEE')
ON CONFLICT (libelle) DO NOTHING;


-- Statuts de l'offre d'emploi
INSERT INTO statut_offre (libelle)
VALUES
    ('BROUILLON'),
    ('PUBLIEE'),
    ('FERMEE'),
    ('ANNULEE')
ON CONFLICT (libelle) DO NOTHING;


-- Statuts de la candidature
INSERT INTO statut_candidature (libelle)
VALUES
    ('RECUE'),
    ('DOSSIER_RETENU'),
    ('DOSSIER_REJETE'),
    ('TEST_TECHNIQUE'),
    ('ENTRETIEN'),
    ('RETENUE'),
    ('NON_RETENUE')
ON CONFLICT (libelle) DO NOTHING;


-- Statuts de la proposition d'embauche
INSERT INTO statut_proposition (libelle)
VALUES
    ('EN_ATTENTE_CANDIDAT'),
    ('EN_ATTENTE_RH'),
    ('ACCORD_FINAL'),
    ('REFUSEE_CANDIDAT'),
    ('REFUSEE_RH')
ON CONFLICT (libelle) DO NOTHING;


-- Réponses possibles du candidat
INSERT INTO reponse_candidat (libelle)
VALUES
    ('OUI'),
    ('NON'),
    ('AUTRE')
ON CONFLICT (libelle) DO NOTHING;


-- Décisions possibles des RH
INSERT INTO decision_rh (libelle)
VALUES
    ('ACCEPTEE'),
    ('REFUSEE')
ON CONFLICT (libelle) DO NOTHING;


-- Types de contrat
INSERT INTO type_contrat (libelle)
VALUES
    ('CE'),
    ('CDD'),
    ('CDI')
ON CONFLICT (libelle) DO NOTHING;


-- ============================================================
-- 2. CREER LE BESOIN DE RECRUTEMENT
-- ============================================================

DO $$
DECLARE
    v_id_direction INTEGER;
    v_id_dept INTEGER;
    v_id_directeur INTEGER;
    v_id_rh INTEGER;
    v_id_statut INTEGER;
    v_id_besoin INTEGER;
    v_id_poste INTEGER;
    v_id_type_contrat INTEGER;
BEGIN

    -- --------------------------------------------------------
    -- 2.1. Rechercher la direction
    -- --------------------------------------------------------

    SELECT id_direction
    INTO v_id_direction
    FROM direction
    WHERE nom_direction = 'Direction des Systèmes d''Information'
    LIMIT 1;


    -- --------------------------------------------------------
    -- 2.2. Rechercher le département
    -- --------------------------------------------------------

    SELECT id_dept
    INTO v_id_dept
    FROM departement
    WHERE nom_dept = 'Marketing'
    LIMIT 1;


    -- --------------------------------------------------------
    -- 2.3. Rechercher le directeur demandeur
    -- --------------------------------------------------------

    SELECT id_responsable
    INTO v_id_directeur
    FROM responsable
    WHERE email = 'directeur.si@entreprise.mg'
    LIMIT 1;


    -- --------------------------------------------------------
    -- 2.4. Rechercher le responsable RH
    -- --------------------------------------------------------

    SELECT id_responsable
    INTO v_id_rh
    FROM responsable
    WHERE email = 'rh@entreprise.mg'
    LIMIT 1;


    -- --------------------------------------------------------
    -- 2.5. Rechercher le statut VALIDEE
    -- --------------------------------------------------------

    SELECT id_statut_besoin
    INTO v_id_statut
    FROM statut_besoin
    WHERE libelle = 'VALIDEE'
    LIMIT 1;


    -- --------------------------------------------------------
    -- 2.6. Vérifier les données nécessaires
    -- --------------------------------------------------------

    IF v_id_direction IS NULL THEN
        RAISE EXCEPTION
            'Direction introuvable : Direction des Systèmes d''Information';
    END IF;

    IF v_id_dept IS NULL THEN
        RAISE EXCEPTION
            'Département introuvable : Marketing';
    END IF;

    IF v_id_directeur IS NULL THEN
        RAISE EXCEPTION
            'Directeur introuvable : directeur.si@entreprise.mg';
    END IF;

    IF v_id_rh IS NULL THEN
        RAISE EXCEPTION
            'Responsable RH introuvable : rh@entreprise.mg';
    END IF;

    IF v_id_statut IS NULL THEN
        RAISE EXCEPTION
            'Statut du besoin introuvable : VALIDEE';
    END IF;


    -- --------------------------------------------------------
    -- 2.7. Rechercher le besoin de test existant
    -- --------------------------------------------------------

    SELECT id_besoin
    INTO v_id_besoin
    FROM besoin
    WHERE motif = 'Recrutement de test - Développeur junior'
    LIMIT 1;


    -- --------------------------------------------------------
    -- 2.8. Créer le besoin s'il n'existe pas
    -- --------------------------------------------------------

    IF v_id_besoin IS NULL THEN

        INSERT INTO besoin (
            id_direction,
            id_dept,
            id_directeur_demandeur,
            motif,
            id_statut_besoin,
            id_rh_traitant,
            date_demande,
            date_traitement_rh,
            date_validation_finance,
            date_validation_dg,
            visible_client
        )
        VALUES (
            v_id_direction,
            v_id_dept,
            v_id_directeur,
            'Recrutement de test - Développeur junior',
            v_id_statut,
            v_id_rh,
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP,
            TRUE
        )
        RETURNING id_besoin INTO v_id_besoin;

    END IF;


    -- ========================================================
    -- 3. ASSOCIER LE BESOIN AU POSTE
    -- ========================================================

    -- Rechercher le poste
    SELECT id_poste
    INTO v_id_poste
    FROM poste
    WHERE libelle = 'Développeur junior'
    LIMIT 1;


    IF v_id_poste IS NULL THEN
        RAISE EXCEPTION
            'Poste introuvable : Développeur junior';
    END IF;


    -- Rechercher le type de contrat CDI
    SELECT id_type_contrat
    INTO v_id_type_contrat
    FROM type_contrat
    WHERE libelle = 'CDI'
    LIMIT 1;


    IF v_id_type_contrat IS NULL THEN
        RAISE EXCEPTION
            'Type de contrat introuvable : CDI';
    END IF;


    -- Créer l'association si elle n'existe pas
    IF NOT EXISTS (
        SELECT 1
        FROM besoin_poste bp
        WHERE bp.id_besoin = v_id_besoin
          AND bp.id_poste = v_id_poste
    ) THEN

        INSERT INTO besoin_poste (
            id_besoin,
            id_poste,
            nombre_postes,
            missions,
            id_type_contrat,
            salaire_propose_rh,
            salaire_valide_finance
        )
        VALUES (
            v_id_besoin,
            v_id_poste,
            1,
            'Développement et maintenance des applications Java.',
            v_id_type_contrat,
            1200000,
            1200000
        );

    END IF;


    -- Vérifier que le besoin possède bien un poste
    IF NOT EXISTS (
        SELECT 1
        FROM besoin_poste bp
        WHERE bp.id_besoin = v_id_besoin
    ) THEN
        RAISE EXCEPTION
            'Aucun poste associé au besoin de recrutement.';
    END IF;

END;
$$;


-- ============================================================
-- 4. CREER L'OFFRE D'EMPLOI
-- ============================================================

INSERT INTO offre_emploi (
    id_besoin_poste,
    titre,
    description,
    profil_recherche,
    date_publication,
    date_limite,
    id_statut_offre,
    visible_client
)
SELECT
    bp.id_besoin_poste,
    'Recrutement de test - Développeur junior',
    'Offre de test pour la proposition d''embauche.',
    'Connaissances en Java et en développement web.',
    CURRENT_TIMESTAMP,
    CURRENT_DATE + 30,
    so.id_statut_offre,
    TRUE
FROM besoin_poste bp
JOIN besoin b
    ON b.id_besoin = bp.id_besoin
JOIN poste p
    ON p.id_poste = bp.id_poste
JOIN statut_offre so
    ON so.libelle = 'PUBLIEE'
WHERE b.motif = 'Recrutement de test - Développeur junior'
  AND p.libelle = 'Développeur junior'
  AND NOT EXISTS (
      SELECT 1
      FROM offre_emploi o
      WHERE o.id_besoin_poste = bp.id_besoin_poste
        AND o.titre = 'Recrutement de test - Développeur junior'
  );


-- ============================================================
-- 5. CREER LA CANDIDATURE DE HERY RABE
-- ============================================================

INSERT INTO candidature (
    id_offre,
    id_candidat,
    date_candidature,
    id_statut_candidature
)
SELECT
    o.id_offre,
    c.id_candidat,
    CURRENT_TIMESTAMP,
    sc.id_statut_candidature
FROM offre_emploi o
JOIN besoin_poste bp
    ON bp.id_besoin_poste = o.id_besoin_poste
JOIN besoin b
    ON b.id_besoin = bp.id_besoin
JOIN candidat c
    ON c.email = 'hery.rabe@mail.mg'
JOIN statut_candidature sc
    ON sc.libelle = 'RETENUE'
WHERE o.titre = 'Recrutement de test - Développeur junior'
  AND b.motif = 'Recrutement de test - Développeur junior'
  AND NOT EXISTS (
      SELECT 1
      FROM candidature ca
      WHERE ca.id_offre = o.id_offre
        AND ca.id_candidat = c.id_candidat
  );


-- ============================================================
-- 6. CREER LA PROPOSITION D'EMBAUCHE
-- ============================================================

INSERT INTO proposition_embauche (
    id_candidature,
    salaire_propose,
    date_envoi,
    id_statut_proposition
)
SELECT
    ca.id_candidature,
    1200000,
    CURRENT_TIMESTAMP,
    sp.id_statut_proposition
FROM candidature ca
JOIN candidat c
    ON c.id_candidat = ca.id_candidat
JOIN offre_emploi o
    ON o.id_offre = ca.id_offre
JOIN statut_proposition sp
    ON sp.libelle = 'EN_ATTENTE_CANDIDAT'
WHERE c.email = 'hery.rabe@mail.mg'
  AND o.titre = 'Recrutement de test - Développeur junior'
  AND NOT EXISTS (
      SELECT 1
      FROM proposition_embauche pe
      WHERE pe.id_candidature = ca.id_candidature
  );


-- ============================================================
-- 7. VERIFIER LES DONNEES CREEES
-- ============================================================

COMMIT;


SELECT
    c.id_candidat,
    c.nom_candidat,
    c.prenom_candidat,
    c.email,
    o.titre AS offre,
    p.libelle AS poste,
    bp.salaire_valide_finance AS salaire_valide_finance,
    pe.salaire_propose AS salaire_propose_candidat,
    sc.libelle AS statut_candidature,
    sprop.libelle AS statut_proposition,
    pe.date_envoi
FROM proposition_embauche pe
JOIN candidature ca
    ON ca.id_candidature = pe.id_candidature
JOIN candidat c
    ON c.id_candidat = ca.id_candidat
JOIN offre_emploi o
    ON o.id_offre = ca.id_offre
JOIN besoin_poste bp
    ON bp.id_besoin_poste = o.id_besoin_poste
JOIN poste p
    ON p.id_poste = bp.id_poste
JOIN statut_candidature sc
    ON sc.id_statut_candidature = ca.id_statut_candidature
JOIN statut_proposition sprop
    ON sprop.id_statut_proposition = pe.id_statut_proposition
WHERE c.email = 'hery.rabe@mail.mg'
  AND o.titre = 'Recrutement de test - Développeur junior';