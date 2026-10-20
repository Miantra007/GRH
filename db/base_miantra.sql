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

CREATE TABLE besoin_poste (
    id_besoin_poste SERIAL PRIMARY KEY,
    id_besoin INTEGER NOT NULL REFERENCES besoin(id_besoin) ON DELETE CASCADE,
    id_poste INTEGER NOT NULL REFERENCES poste(id_poste),
    nombre_postes INTEGER NOT NULL CHECK (nombre_postes > 0),
    missions TEXT,
    type_contrat VARCHAR(3) CHECK (type_contrat IN ('CE', 'CDD', 'CDI')),
    salaire_propose_rh NUMERIC(12, 0) CHECK (salaire_propose_rh >= 0),
    salaire_valide_finance NUMERIC(12, 0) CHECK (salaire_valide_finance >= 0),
    UNIQUE (id_besoin, id_poste)
);

CREATE TABLE offre_emploi (
    id_offre SERIAL PRIMARY KEY,
    id_besoin_poste INTEGER NOT NULL REFERENCES besoin_poste(id_besoin_poste),
    titre VARCHAR(150) NOT NULL,
    description TEXT,
    profil_recherche TEXT,
    date_publication TIMESTAMP,
    date_limite DATE,
    statut VARCHAR(20) NOT NULL DEFAULT 'BROUILLON' CHECK (
        statut IN (
            'BROUILLON',
            'PUBLIEE',
            'FERMEE',
            'ANNULEE'
        )
    ),
    visible_client BOOLEAN NOT NULL DEFAULT FALSE,
    CHECK (
        date_limite IS NULL
        OR date_publication IS NULL
        OR date_limite >= date_publication :: DATE
    )
);

CREATE TABLE candidature (
    id_candidature SERIAL PRIMARY KEY,
    id_offre INTEGER NOT NULL REFERENCES offre_emploi(id_offre),
    id_candidat INTEGER NOT NULL REFERENCES candidat(id_candidat),
    date_candidature TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(30) NOT NULL DEFAULT 'RECUE' CHECK (
        statut IN (
            'RECUE',
            'DOSSIER_RETENU',
            'DOSSIER_REJETE',
            'TEST_TECHNIQUE',
            'ENTRETIEN',
            'RETENUE',
            'NON_RETENUE'
        )
    ),
    UNIQUE (id_offre, id_candidat)
);

CREATE TABLE proposition_embauche (
    id_proposition SERIAL PRIMARY KEY,
    id_candidature INTEGER NOT NULL UNIQUE REFERENCES candidature(id_candidature),
    -- Proposition initiale du RH
    salaire_propose NUMERIC(12, 0) NOT NULL CHECK (salaire_propose >= 0),
    date_envoi TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Réponse du candidat
    reponse_candidat VARCHAR(10) CHECK (
        reponse_candidat IN ('OUI', 'NON', 'AUTRE')
    ),
    commentaire_candidat TEXT,
    -- Informations de la contre-proposition
    contre_proposition TEXT,
    salaire_demande NUMERIC(12, 0) CHECK (salaire_demande >= 0),
    date_reponse_candidat TIMESTAMP,
    -- Décision du RH concernant la contre-proposition
    decision_rh VARCHAR(10) CHECK (
        decision_rh IN ('ACCEPTEE', 'REFUSEE')
    ),
    commentaire_rh TEXT,
    date_decision_rh TIMESTAMP,
    -- Conditions finalement convenues
    salaire_final NUMERIC(12, 0) CHECK (salaire_final >= 0),
    statut VARCHAR(30) NOT NULL DEFAULT 'EN_ATTENTE_CANDIDAT' CHECK (
        statut IN (
            'EN_ATTENTE_CANDIDAT',
            'EN_ATTENTE_RH',
            'ACCORD_FINAL',
            'REFUSEE_CANDIDAT',
            'REFUSEE_RH'
        )
    ),
    -- Une contre-proposition doit contenir une explication
    -- ou un salaire demandé
    CHECK (
        reponse_candidat IS DISTINCT
        FROM
            'AUTRE'
            OR (
                NULLIF(BTRIM(contre_proposition), '') IS NOT NULL
                OR salaire_demande IS NOT NULL
            )
    ),
    -- Une décision RH ne concerne que la contre-proposition
    CHECK (
        decision_rh IS NULL
        OR reponse_candidat = 'AUTRE'
    )
);

ALTER TABLE
    test_technique
ADD
    COLUMN id_candidature INTEGER REFERENCES candidature(id_candidature);

ALTER TABLE
    entretien
ADD
    COLUMN id_candidature INTEGER REFERENCES candidature(id_candidature);

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