-- ============================================================
-- 1. TABLE TYPE_CONTRAT
-- ============================================================
CREATE TABLE type_contrat (
    id_type_contrat SERIAL PRIMARY KEY,
    libelle VARCHAR(10) NOT NULL UNIQUE
);

INSERT INTO
    type_contrat (libelle)
VALUES
    ('CE'),
    ('CDD'),
    ('CDI');

-- ============================================================
-- 2. TABLE BESOIN
-- ============================================================
CREATE TABLE besoin (
    id_besoin SERIAL PRIMARY KEY,
    id_direction INTEGER NOT NULL REFERENCES direction(id_direction),
    id_dept INTEGER NOT NULL REFERENCES departement(id_dept),
    id_directeur_demandeur INTEGER NOT NULL REFERENCES responsable(id_responsable),
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
-- 3. TABLE BESOIN_POSTE
-- ============================================================
CREATE TABLE besoin_poste (
    id_besoin_poste SERIAL PRIMARY KEY,
    id_besoin INTEGER NOT NULL REFERENCES besoin(id_besoin) ON DELETE CASCADE,
    id_poste INTEGER NOT NULL REFERENCES poste(id_poste),
    nombre_postes INTEGER NOT NULL CHECK (nombre_postes > 0),
    missions TEXT,
    id_type_contrat INTEGER REFERENCES type_contrat(id_type_contrat),
    salaire_propose_rh NUMERIC(12, 0) CHECK (salaire_propose_rh >= 0),
    salaire_valide_finance NUMERIC(12, 0) CHECK (salaire_valide_finance >= 0),
    UNIQUE (id_besoin, id_poste)
);

-- ============================================================
-- 4. TABLE STATUT_OFFRE
-- ============================================================
CREATE TABLE statut_offre (
    id_statut_offre SERIAL PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE
);

INSERT INTO
    statut_offre (libelle)
VALUES
    ('BROUILLON'),
    ('PUBLIEE'),
    ('FERMEE'),
    ('ANNULEE');

-- ============================================================
-- 5. TABLE OFFRE_EMPLOI
-- ============================================================
CREATE TABLE offre_emploi (
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
-- 6. TABLE STATUT_CANDIDATURE
-- ============================================================
CREATE TABLE statut_candidature (
    id_statut_candidature SERIAL PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE
);

INSERT INTO
    statut_candidature (libelle)
VALUES
    ('RECUE'),
    ('DOSSIER_RETENU'),
    ('DOSSIER_REJETE'),
    ('TEST_TECHNIQUE'),
    ('ENTRETIEN'),
    ('RETENUE'),
    ('NON_RETENUE');

-- ============================================================
-- 7. TABLE CANDIDATURE
-- ============================================================
CREATE TABLE candidature (
    id_candidature SERIAL PRIMARY KEY,
    id_offre INTEGER NOT NULL REFERENCES offre_emploi(id_offre),
    id_candidat INTEGER NOT NULL REFERENCES candidat(id_candidat),
    date_candidature TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_statut_candidature INTEGER NOT NULL REFERENCES statut_candidature(id_statut_candidature),
    UNIQUE (id_offre, id_candidat)
);

-- ============================================================
-- 8. TABLE TEST_TECHNIQUE
-- ============================================================
CREATE TABLE test_technique (
    id_test_technique SERIAL PRIMARY KEY,
    description VARCHAR(50),
    id_candidature INTEGER NOT NULL REFERENCES candidature(id_candidature) ON DELETE CASCADE,
    date_test DATE
);

-- ============================================================
-- 9. TABLE ENTRETIEN
-- ============================================================
CREATE TABLE entretien (
    id_entretien SERIAL PRIMARY KEY,
    description VARCHAR(50),
    id_candidature INTEGER NOT NULL REFERENCES candidature(id_candidature) ON DELETE CASCADE,
    date_entretien DATE
);

-- ============================================================
-- 10. TABLE REPONSE_CANDIDAT
-- ============================================================
CREATE TABLE reponse_candidat (
    id_reponse_candidat SERIAL PRIMARY KEY,
    libelle VARCHAR(10) NOT NULL UNIQUE
);

INSERT INTO
    reponse_candidat (libelle)
VALUES
    ('OUI'),
    ('NON'),
    ('AUTRE');

-- ============================================================
-- 11. TABLE DECISION_RH
-- ============================================================
CREATE TABLE decision_rh (
    id_decision_rh SERIAL PRIMARY KEY,
    libelle VARCHAR(10) NOT NULL UNIQUE
);

INSERT INTO
    decision_rh (libelle)
VALUES
    ('ACCEPTEE'),
    ('REFUSEE');

-- ============================================================
-- 12. TABLE STATUT_PROPOSITION
-- ============================================================
CREATE TABLE statut_proposition (
    id_statut_proposition SERIAL PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE
);

INSERT INTO
    statut_proposition (libelle)
VALUES
    ('EN_ATTENTE_CANDIDAT'),
    ('EN_ATTENTE_RH'),
    ('ACCORD_FINAL'),
    ('REFUSEE_CANDIDAT'),
    ('REFUSEE_RH');

-- ============================================================
-- 13. TABLE PROPOSITION_EMBAUCHE
-- ============================================================
CREATE TABLE proposition_embauche (
    id_proposition SERIAL PRIMARY KEY,
    id_candidature INTEGER NOT NULL UNIQUE REFERENCES candidature(id_candidature),
    salaire_propose NUMERIC(12, 0) NOT NULL CHECK (salaire_propose >= 0),
    date_envoi TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_reponse_candidat INTEGER REFERENCES reponse_candidat(id_reponse_candidat),
    commentaire_candidat TEXT,
    contre_proposition TEXT,
    salaire_demande NUMERIC(12, 0) CHECK (salaire_demande >= 0),
    date_reponse_candidat TIMESTAMP,
    id_decision_rh INTEGER REFERENCES decision_rh(id_decision_rh),
    commentaire_rh TEXT,
    date_decision_rh TIMESTAMP,
    salaire_final NUMERIC(12, 0) CHECK (salaire_final >= 0),
    id_statut_proposition INTEGER NOT NULL REFERENCES statut_proposition(id_statut_proposition)
);

-- ============================================================
-- 14. TABLE NOTIFICATION
-- ============================================================
CREATE TABLE notification (
    id_notification SERIAL PRIMARY KEY,
    id_candidat INTEGER REFERENCES candidat(id_candidat),
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