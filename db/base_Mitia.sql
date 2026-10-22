-- Active: 1792657013409@@127.0.0.1@5432@grh
-- ============================================================
-- BASE MIANTRA : GESTION DU RECRUTEMENT
-- Fichier : base_miantra.sql
-- ============================================================
\c grh
BEGIN;

-- ============================================================
-- 1. TABLE TYPE_CONTRAT
-- ============================================================
CREATE TABLE IF NOT EXISTS type_contrat (
    id_type_contrat SERIAL PRIMARY KEY,
    libelle VARCHAR(10) NOT NULL UNIQUE
);

-- ============================================================
-- 1 BIS. TABLE TYPE_BESOIN (CREATION ou REMPLACEMENT)
-- ============================================================
CREATE TABLE IF NOT EXISTS type_besoin (
    id_type_besoin SERIAL PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE
);

-- ============================================================
-- 2. TABLE STATUT_BESOIN
-- ============================================================
CREATE TABLE IF NOT EXISTS statut_besoin (
    id_statut_besoin SERIAL PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE
);

-- ============================================================
-- 3. TABLE STATUT_OFFRE
-- ============================================================
CREATE TABLE IF NOT EXISTS statut_offre (
    id_statut_offre SERIAL PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE
);

-- ============================================================
-- 4. TABLE STATUT_CANDIDATURE
-- ============================================================
CREATE TABLE IF NOT EXISTS statut_candidature (
    id_statut_candidature SERIAL PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE
);

-- ============================================================
-- 5. TABLE REPONSE_CANDIDAT
-- ============================================================
CREATE TABLE IF NOT EXISTS reponse_candidat (
    id_reponse_candidat SERIAL PRIMARY KEY,
    libelle VARCHAR(10) NOT NULL UNIQUE
);

-- ============================================================
-- 6. TABLE DECISION_RH
-- ============================================================
CREATE TABLE IF NOT EXISTS decision_rh (
    id_decision_rh SERIAL PRIMARY KEY,
    libelle VARCHAR(10) NOT NULL UNIQUE
);

-- ============================================================
-- 7. TABLE STATUT_PROPOSITION
-- ============================================================
CREATE TABLE IF NOT EXISTS statut_proposition (
    id_statut_proposition SERIAL PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE
);

-- ============================================================
-- 8. TABLE BESOIN
-- Dépend de :
-- direction, departement, responsable, type_besoin, statut_besoin
-- ============================================================
CREATE TABLE IF NOT EXISTS besoin (
    id_besoin SERIAL PRIMARY KEY,
    id_direction INTEGER NOT NULL REFERENCES direction(id_direction),
    id_dept INTEGER NOT NULL REFERENCES departement(id_dept),
    id_directeur_demandeur INTEGER NOT NULL REFERENCES responsable(id_responsable),
    -- Type de besoin : CREATION ou REMPLACEMENT
    id_type_besoin INTEGER NOT NULL REFERENCES type_besoin(id_type_besoin),
    motif TEXT,
    id_statut_besoin INTEGER NOT NULL REFERENCES statut_besoin(id_statut_besoin),
    id_rh_traitant INTEGER REFERENCES responsable(id_responsable),
    id_finance_validateur INTEGER REFERENCES responsable(id_responsable),
    id_dg_validateur INTEGER REFERENCES responsable(id_responsable),
    date_demande TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_traitement_rh TIMESTAMP,
    date_validation_finance TIMESTAMP,
    date_validation_dg TIMESTAMP,
    date_publication_client TIMESTAMP,
    commentaire_rh TEXT,
    commentaire_finance TEXT,
    commentaire_dg TEXT,
    visible_client BOOLEAN NOT NULL DEFAULT FALSE
);

-- ============================================================
-- 9. TABLE BESOIN_POSTE
-- Dépend de : besoin, poste, type_contrat
-- ============================================================
CREATE TABLE IF NOT EXISTS besoin_poste (
    id_besoin_poste SERIAL PRIMARY KEY,
    id_besoin INTEGER NOT NULL REFERENCES besoin(id_besoin) ON DELETE CASCADE,
    id_poste INTEGER NOT NULL REFERENCES poste(id_poste),
    nombre_postes INTEGER NOT NULL CHECK (nombre_postes > 0),
    missions TEXT,
    id_type_contrat INTEGER REFERENCES type_contrat(id_type_contrat),
    salaire_propose_rh NUMERIC(12, 0) CHECK (salaire_propose_rh >= 0),
    salaire_valide_finance NUMERIC(12, 0) CHECK (salaire_valide_finance >= 0),
    -- Conditions décidées par le RH lors du besoin
    horaire_travail VARCHAR(150),
    responsable_hierarchique VARCHAR(150),
    date_prise_poste DATE,
    UNIQUE (id_besoin, id_poste)
);

-- ============================================================
-- 10. TABLE OFFRE_EMPLOI
-- Dépend de : besoin_poste, statut_offre
-- ============================================================
CREATE TABLE IF NOT EXISTS offre_emploi (
    id_offre SERIAL PRIMARY KEY,
    id_besoin_poste INTEGER NOT NULL REFERENCES besoin_poste(id_besoin_poste),
    titre VARCHAR(150) NOT NULL,
    description TEXT,
    profil_recherche TEXT,
    date_publication TIMESTAMP,
    date_limite DATE,
    id_statut_offre INTEGER NOT NULL REFERENCES statut_offre(id_statut_offre),
    visible_client BOOLEAN NOT NULL DEFAULT FALSE,
    CHECK (
        date_limite IS NULL
        OR date_publication IS NULL
        OR date_limite >= date_publication :: DATE
    )
);

-- ============================================================
-- 11. TABLE CANDIDATURE
-- Dépend de : offre_emploi, candidat, statut_candidature
-- ============================================================
CREATE TABLE IF NOT EXISTS candidature (
    id_candidature SERIAL PRIMARY KEY,
    id_offre INTEGER NOT NULL REFERENCES offre_emploi(id_offre),
    id_candidat INTEGER NOT NULL REFERENCES candidat(id_candidat),
    date_candidature TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_statut_candidature INTEGER NOT NULL REFERENCES statut_candidature(id_statut_candidature),
    code_suivi VARCHAR(64) NOT NULL UNIQUE DEFAULT md5(random()::text || clock_timestamp()::text || pg_backend_pid()::text)
        || md5(random()::text || clock_timestamp()::text || pg_backend_pid()::text),
    UNIQUE (id_offre, id_candidat)
);

-- Le candidat suit sa candidature avec un code aléatoire, sans créer de compte.
ALTER TABLE candidature ADD COLUMN IF NOT EXISTS code_suivi VARCHAR(64);
UPDATE candidature SET code_suivi = md5(random()::text || clock_timestamp()::text || id_candidature::text)
    || md5(random()::text || clock_timestamp()::text || id_candidature::text || pg_backend_pid()::text)
WHERE code_suivi IS NULL OR length(code_suivi) <> 64;
ALTER TABLE candidature ALTER COLUMN code_suivi SET DEFAULT md5(random()::text || clock_timestamp()::text || pg_backend_pid()::text)
    || md5(random()::text || clock_timestamp()::text || pg_backend_pid()::text);
ALTER TABLE candidature ALTER COLUMN code_suivi SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS uk_candidature_code_suivi ON candidature(code_suivi);

-- Les informations et justificatifs du dossier sont liés à la candidature finale.
CREATE TABLE IF NOT EXISTS dossier_candidature (
    id_dossier SERIAL PRIMARY KEY,
    id_candidature INTEGER NOT NULL UNIQUE REFERENCES candidature(id_candidature) ON DELETE CASCADE,
    lettre_motivation TEXT NOT NULL,
    cv_importe VARCHAR(255),
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS document_dossier (
    id_document SERIAL PRIMARY KEY,
    id_dossier INTEGER NOT NULL REFERENCES dossier_candidature(id_dossier) ON DELETE CASCADE,
    nom_fichier VARCHAR(255) NOT NULL,
    chemin_fichier VARCHAR(500) NOT NULL,
    type_contenu VARCHAR(120),
    taille_octets BIGINT NOT NULL CHECK (taille_octets >= 0)
);

-- ============================================================
-- 12. TABLE TEST_TECHNIQUE
-- Dépend de : candidature
-- ============================================================
CREATE TABLE IF NOT EXISTS test_technique (
    id_test_technique SERIAL PRIMARY KEY,
    description VARCHAR(50),
    id_candidature INTEGER NOT NULL REFERENCES candidature(id_candidature) ON DELETE CASCADE,
    date_test DATE,
    heure_test TIME,
    lieu TEXT,
    consignes TEXT,
    message_candidat TEXT
);
ALTER TABLE test_technique ADD COLUMN IF NOT EXISTS heure_test TIME;
ALTER TABLE test_technique ADD COLUMN IF NOT EXISTS lieu TEXT;
ALTER TABLE test_technique ADD COLUMN IF NOT EXISTS consignes TEXT;
ALTER TABLE test_technique ADD COLUMN IF NOT EXISTS message_candidat TEXT;
ALTER TABLE test_technique ADD COLUMN IF NOT EXISTS accepte_candidat BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE test_technique ADD COLUMN IF NOT EXISTS resultat_accepte BOOLEAN;

-- ============================================================
-- 13. TABLE ENTRETIEN
-- Dépend de : candidature
-- ============================================================
CREATE TABLE IF NOT EXISTS entretien (
    id_entretien SERIAL PRIMARY KEY,
    description VARCHAR(50),
    id_candidature INTEGER NOT NULL REFERENCES candidature(id_candidature) ON DELETE CASCADE,
    date_entretien DATE,
    heure_entretien TIME,
    lieu TEXT,
    consignes TEXT,
    message_candidat TEXT
);
ALTER TABLE entretien ADD COLUMN IF NOT EXISTS heure_entretien TIME;
ALTER TABLE entretien ADD COLUMN IF NOT EXISTS lieu TEXT;
ALTER TABLE entretien ADD COLUMN IF NOT EXISTS consignes TEXT;
ALTER TABLE entretien ADD COLUMN IF NOT EXISTS message_candidat TEXT;

-- ============================================================
-- 14. TABLE PROPOSITION_EMBAUCHE
-- Dépend de :
-- candidature, reponse_candidat, decision_rh,
-- statut_proposition
-- Le salaire et les conditions viennent de besoin_poste
-- ============================================================
CREATE TABLE IF NOT EXISTS proposition_embauche (
    id_proposition SERIAL PRIMARY KEY,
    id_candidature INTEGER NOT NULL UNIQUE REFERENCES candidature(id_candidature),
    -- Date d'envoi de la proposition
    date_envoi TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Réponse générale du candidat
    id_reponse_candidat INTEGER REFERENCES reponse_candidat(id_reponse_candidat),
    commentaire_candidat TEXT,
    contre_proposition TEXT,
    -- Négociation salariale
    salaire_demande NUMERIC(12, 0) CHECK (salaire_demande >= 0),
    date_reponse_candidat TIMESTAMP,
    -- Décision du RH
    id_decision_rh INTEGER REFERENCES decision_rh(id_decision_rh),
    commentaire_rh TEXT,
    date_decision_rh TIMESTAMP,
    -- Salaire finalement retenu
    salaire_final NUMERIC(12, 0) CHECK (salaire_final >= 0),
    -- Statut général de la proposition
    id_statut_proposition INTEGER NOT NULL REFERENCES statut_proposition(id_statut_proposition)
);

-- ============================================================
-- 15. TABLE NOTIFICATION
-- Dépend de : candidat, responsable, proposition_embauche
-- ============================================================
CREATE TABLE IF NOT EXISTS notification (
    id_notification SERIAL PRIMARY KEY,
    id_candidat INTEGER REFERENCES candidat(id_candidat),
    id_candidature INTEGER REFERENCES candidature(id_candidature) ON DELETE CASCADE,
    id_responsable INTEGER REFERENCES responsable(id_responsable),
    id_proposition INTEGER REFERENCES proposition_embauche(id_proposition) ON DELETE
    SET
        NULL,
        titre VARCHAR(150) NOT NULL,
        message TEXT NOT NULL,
        date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        lu BOOLEAN NOT NULL DEFAULT FALSE,
        CHECK (
            (
                id_candidat IS NOT NULL
                AND id_responsable IS NULL
            )
            OR (
                id_candidat IS NULL
                AND id_responsable IS NOT NULL
            )
        )
);
ALTER TABLE notification ADD COLUMN IF NOT EXISTS id_candidature INTEGER REFERENCES candidature(id_candidature) ON DELETE CASCADE;

-- ============================================================
-- 16. TABLE TYPE_CRITERE_PROPOSITION
-- ============================================================
CREATE TABLE IF NOT EXISTS type_critere_proposition (
    id_type_critere SERIAL PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE
);

-- ============================================================
-- 17. TABLE REPONSE_CRITERE
-- (référencée par critere_proposition)
-- ============================================================
CREATE TABLE IF NOT EXISTS reponse_critere (
    id_reponse_critere SERIAL PRIMARY KEY,
    libelle VARCHAR(10) NOT NULL UNIQUE
);

-- ============================================================
-- 18. TABLE CRITERE_PROPOSITION
-- Une réponse indépendante pour chaque critère
-- ============================================================
CREATE TABLE IF NOT EXISTS critere_proposition (
    id_critere_proposition SERIAL PRIMARY KEY,
    id_proposition INTEGER NOT NULL REFERENCES proposition_embauche(id_proposition) ON DELETE CASCADE,
    id_type_critere INTEGER NOT NULL REFERENCES type_critere_proposition(id_type_critere),
    id_reponse_critere INTEGER REFERENCES reponse_critere(id_reponse_critere),
    contre_proposition TEXT,
    commentaire_candidat TEXT,
    date_reponse TIMESTAMP,
    UNIQUE (id_proposition, id_type_critere)
);

-- Référentiels minimaux du workflow, insérés sans créer de doublons.
INSERT INTO statut_offre (libelle) VALUES ('BROUILLON'), ('PUBLIEE'), ('FERMEE'), ('ANNULEE') ON CONFLICT (libelle) DO NOTHING;
INSERT INTO statut_candidature (libelle) VALUES ('RECUE'), ('DOSSIER_RETENU'), ('DOSSIER_REJETE'), ('TEST_TECHNIQUE'), ('ENTRETIEN'), ('RETENUE'), ('NON_RETENUE') ON CONFLICT (libelle) DO NOTHING;
INSERT INTO reponse_candidat (libelle) VALUES ('OUI'), ('NON'), ('AUTRE') ON CONFLICT (libelle) DO NOTHING;
INSERT INTO decision_rh (libelle) VALUES ('ACCEPTEE'), ('REFUSEE') ON CONFLICT (libelle) DO NOTHING;
INSERT INTO statut_proposition (libelle) VALUES ('EN_ATTENTE_CANDIDAT'), ('EN_ATTENTE_RH'), ('ACCORD_FINAL'), ('REFUSEE_CANDIDAT'), ('REFUSEE_RH') ON CONFLICT (libelle) DO NOTHING;
INSERT INTO type_critere_proposition (libelle) VALUES ('SALAIRE'), ('HORAIRE'), ('RESPONSABLE'), ('TRAVAIL'), ('DATE_PRISE_POSTE') ON CONFLICT (libelle) DO NOTHING;
INSERT INTO reponse_critere (libelle) VALUES ('OUI'), ('NON'), ('AUTRE') ON CONFLICT (libelle) DO NOTHING;

COMMIT;

-- ============================================================
-- 19. VERIFICATION DES TABLES
-- ============================================================
SELECT
    table_name
FROM
    information_schema.tables
WHERE
    table_schema = 'public'
    AND table_name IN (
        'type_contrat',
        'type_besoin',
        'statut_besoin',
        'statut_offre',
        'statut_candidature',
        'reponse_candidat',
        'decision_rh',
        'statut_proposition',
        'besoin',
        'besoin_poste',
        'offre_emploi',
        'candidature',
        'dossier_candidature',
        'document_dossier',
        'test_technique',
        'entretien',
        'proposition_embauche',
        'notification',
        'type_critere_proposition',
        'reponse_critere',
        'critere_proposition'
    )
ORDER BY
    table_name;
