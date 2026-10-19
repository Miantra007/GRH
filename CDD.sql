-- =====================================================================
--  CONTRAT DE TRAVAIL À DURÉE DÉTERMINÉE (CDD) - MADAGASCAR
--  Base simple : tables, clés primaires, clés étrangères uniquement.
--  Les contrôles (durée max, renouvellements, essai, statuts...) sont
--  à faire côté application (métier).
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1. TABLES DE RÉFÉRENCE (listes de valeurs)
-- ---------------------------------------------------------------------
CREATE TABLE civilite (
    id      SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR(20) NOT NULL UNIQUE              -- Monsieur, Madame, Mademoiselle
);

CREATE TABLE statut_contrat (
    id      SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE              -- Brouillon, En cours, Terminé...
);

CREATE TABLE type_avenant (
    id      SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE              -- Renouvellement, Modification
);

CREATE TABLE type_rupture (
    id      SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE              -- Terme, Essai, Démission...
);

CREATE TABLE motif_cdd (
    id      SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR(200) NOT NULL UNIQUE             -- cas de recours au CDD
);

CREATE TABLE categorie_professionnelle (
    id      SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code    VARCHAR(20)  NOT NULL UNIQUE,            -- code de la catégorie
    libelle VARCHAR(100) NOT NULL,
    indice  VARCHAR(20)                              -- indice minimum de classification
);

CREATE TABLE poste (
    id           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    intitule     VARCHAR(150) NOT NULL,
    categorie_id SMALLINT REFERENCES categorie_professionnelle(id)
);

-- ---------------------------------------------------------------------
-- 2. PARTIES AU CONTRAT
-- ---------------------------------------------------------------------
CREATE TABLE employeur (
    id                     INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    raison_sociale         VARCHAR(200) NOT NULL,
    forme_juridique        VARCHAR(50),
    capital                NUMERIC(16,2),
    adresse_siege          TEXT NOT NULL,
    nif                    VARCHAR(30),              -- numéro d'identification fiscale
    stat                   VARCHAR(30),              -- numéro statistique
    rcs                    VARCHAR(30),              -- registre du commerce
    numero_cnaps           VARCHAR(30),              -- employeur affilié à la CNaPS
    organisme_sante        VARCHAR(100),             -- ex. OSTIE
    numero_organisme_sante VARCHAR(30),
    telephone              VARCHAR(30),
    email                  VARCHAR(150),
    representant_nom       VARCHAR(150) NOT NULL,    -- personne qui signe pour l'employeur
    representant_fonction  VARCHAR(100)              -- Gérant, DG, DRH...
);

CREATE TABLE salarie (
    id                  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    civilite_id         SMALLINT REFERENCES civilite(id),
    nom                 VARCHAR(100) NOT NULL,
    prenoms             VARCHAR(150),
    date_naissance      DATE,
    lieu_naissance      VARCHAR(100),
    nationalite         VARCHAR(50) DEFAULT 'Malagasy',
    cin_numero          VARCHAR(30),                 -- carte d'identité nationale
    cin_date_delivrance DATE,
    cin_lieu_delivrance VARCHAR(100),
    adresse             TEXT,
    telephone           VARCHAR(30),
    email               VARCHAR(150),
    situation_familiale VARCHAR(30),
    nombre_enfants      SMALLINT,
    numero_cnaps        VARCHAR(30)
);

-- ---------------------------------------------------------------------
-- 3. LE CONTRAT
-- ---------------------------------------------------------------------
CREATE TABLE contrat_travail (
    id                      INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    reference               VARCHAR(30) UNIQUE,

    employeur_id            INT NOT NULL REFERENCES employeur(id),
    salarie_id              INT NOT NULL REFERENCES salarie(id),
    poste_id                INT NOT NULL REFERENCES poste(id),
    categorie_id            SMALLINT REFERENCES categorie_professionnelle(id),
    motif_id                SMALLINT NOT NULL REFERENCES motif_cdd(id),
    statut_id               SMALLINT NOT NULL REFERENCES statut_contrat(id),

    motif_precision         TEXT,                    -- détail du motif du CDD
    regime_juridique        VARCHAR(200) DEFAULT 'Code du travail de Madagascar',
    convention_collective   VARCHAR(200),

    -- Durée
    date_debut              DATE NOT NULL,
    date_fin                DATE NOT NULL,           -- date de fin courante

    -- Période d'essai
    essai_duree             SMALLINT,                -- unité à définir côté métier (jours ou mois)
    essai_date_fin          DATE,
    essai_resultat          VARCHAR(30),             -- en cours, concluant, non concluant

    -- Temps de travail
    duree_hebdo_heures      NUMERIC(4,1) DEFAULT 40,
    horaire_travail         VARCHAR(200),            -- ex. 08h-12h / 14h-18h
    jours_travail_semaine   SMALLINT,
    lieu_travail            VARCHAR(200),

    -- Rémunération
    salaire_base            NUMERIC(14,2) NOT NULL,
    devise                  CHAR(3) NOT NULL DEFAULT 'MGA',   -- Ariary
    periodicite_paiement    VARCHAR(20) DEFAULT 'Mensuelle',
    indice_classification   VARCHAR(20),

    -- Congés
    conge_jours_par_mois    NUMERIC(3,1),

    -- Conclusion
    date_conclusion         DATE,
    lieu_conclusion         VARCHAR(100),
    nombre_exemplaires      SMALLINT DEFAULT 2,
    date_visa_inspection    DATE,                    -- visa de l'inspection du travail si applicable
    tribunal_competent      VARCHAR(200) DEFAULT 'Tribunal du travail',

    created_at              TIMESTAMP NOT NULL DEFAULT now()
);

-- Primes, indemnités, avantages en nature
CREATE TABLE element_salaire (
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    contrat_id  INT NOT NULL REFERENCES contrat_travail(id) ON DELETE CASCADE,
    libelle     VARCHAR(100) NOT NULL,               -- prime de transport, logement...
    montant     NUMERIC(14,2) NOT NULL,
    nature      VARCHAR(30)                          -- prime, indemnité, avantage en nature
);

-- Renouvellements et modifications
CREATE TABLE avenant (
    id                INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    contrat_id        INT NOT NULL REFERENCES contrat_travail(id) ON DELETE CASCADE,
    type_avenant_id   SMALLINT NOT NULL REFERENCES type_avenant(id),
    numero            SMALLINT NOT NULL,
    date_avenant      DATE NOT NULL,
    nouvelle_date_fin DATE,                          -- si renouvellement
    nouveau_salaire   NUMERIC(14,2),                 -- si changement de salaire
    objet             TEXT
);

-- Fin du contrat
CREATE TABLE rupture_contrat (
    id               INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    contrat_id       INT NOT NULL UNIQUE REFERENCES contrat_travail(id) ON DELETE CASCADE,
    type_rupture_id  SMALLINT NOT NULL REFERENCES type_rupture(id),
    date_rupture     DATE NOT NULL,
    motif            TEXT,
    observations     TEXT
);

-- ---------------------------------------------------------------------
-- 4. INDEX SUR LES CLÉS ÉTRANGÈRES PRINCIPALES
-- ---------------------------------------------------------------------
CREATE INDEX ix_contrat_salarie   ON contrat_travail (salarie_id);
CREATE INDEX ix_contrat_employeur ON contrat_travail (employeur_id);
CREATE INDEX ix_avenant_contrat   ON avenant (contrat_id);
CREATE INDEX ix_element_contrat   ON element_salaire (contrat_id);

-- ---------------------------------------------------------------------
-- 5. DONNÉES DE RÉFÉRENCE DE DÉPART (à adapter)
-- ---------------------------------------------------------------------
INSERT INTO civilite (libelle) VALUES ('Monsieur'), ('Madame'), ('Mademoiselle');

INSERT INTO statut_contrat (libelle) VALUES
  ('Brouillon'), ('Signé'), ('En cours'), ('Renouvelé'), ('Terminé'), ('Rompu'), ('Annulé');

INSERT INTO type_avenant (libelle) VALUES ('Renouvellement'), ('Modification');

INSERT INTO type_rupture (libelle) VALUES
  ('Arrivée du terme'), ('Rupture pendant l''essai'), ('Démission'),
  ('Licenciement'), ('Accord des parties'), ('Autre');

-- Exemples de motifs : à remplacer par la liste exacte prévue par le Code du travail malgache
INSERT INTO motif_cdd (libelle) VALUES
  ('Remplacement d''un travailleur absent'),
  ('Surcroît temporaire d''activité'),
  ('Travail saisonnier'),
  ('Tâche précise et temporaire');