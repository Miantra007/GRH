CREATE TABLE civilite (
    id       SERIAL        PRIMARY KEY,
    libelle  VARCHAR(100)  NOT NULL UNIQUE
);

CREATE TABLE situation_famille (
    id       SERIAL        PRIMARY KEY,
    libelle  VARCHAR(100)  NOT NULL UNIQUE
);

CREATE TABLE nationalite (
    id       SERIAL        PRIMARY KEY,
    libelle  VARCHAR(100)  NOT NULL UNIQUE
);

CREATE TABLE convention_collective (
    id       SERIAL        PRIMARY KEY,
    libelle  VARCHAR(100)  NOT NULL
);

CREATE TABLE categorie_professionnelle (
    id       SERIAL        PRIMARY KEY,
    code     VARCHAR(100)  NOT NULL UNIQUE,
    libelle  VARCHAR(100)  NOT NULL,
    indice   VARCHAR(100)
);

CREATE TABLE poste (
    id            SERIAL        PRIMARY KEY,
    intitule      VARCHAR(100)  NOT NULL,
    categorie_id  INT           REFERENCES categorie_professionnelle(id)
);

CREATE TABLE langue_contrat (
    id       SERIAL        PRIMARY KEY,
    libelle  VARCHAR(100)  NOT NULL UNIQUE
);

CREATE TABLE statut_contrat (
    id       SERIAL        PRIMARY KEY,
    libelle  VARCHAR(100)  NOT NULL UNIQUE
);


-- =====================================================================
-- PARTIE 2 : PARTIES AU CONTRAT (communes aux 3 contrats)
-- =====================================================================

CREATE TABLE employeur (
    id                      SERIAL         PRIMARY KEY,
    raison_sociale          VARCHAR(100)   NOT NULL,
    forme_juridique         VARCHAR(100),
    capital_social          NUMERIC(15,0),
    adresse_siege           VARCHAR(100),
    nif                     VARCHAR(100),
    stat                    VARCHAR(100),
    rcs                     VARCHAR(100),
    numero_cnaps            VARCHAR(100),
    organisme_sante         VARCHAR(100),
    numero_organisme_sante  VARCHAR(100),
    telephone               VARCHAR(100),
    email                   VARCHAR(100),
    civilite_id             INT            REFERENCES civilite(id),
    representant_nom        VARCHAR(100)   NOT NULL,
    representant_fonction   VARCHAR(100)   NOT NULL
);

CREATE TABLE travailleur (
    id                    SERIAL        PRIMARY KEY,
    civilite_id           INT           REFERENCES civilite(id),
    nom_prenoms           VARCHAR(100)  NOT NULL,
    date_naissance        DATE          NOT NULL,
    lieu_naissance        VARCHAR(100)  NOT NULL,
    fils_de               VARCHAR(100),
    nationalite_id        INT           NOT NULL REFERENCES nationalite(id),
    situation_famille_id  INT           REFERENCES situation_famille(id),
    nombre_enfants        INT,
    adresse_residence     VARCHAR(100),
    profession            VARCHAR(100),
    telephone             VARCHAR(100),
    email                 VARCHAR(100),
    cin_numero            VARCHAR(100),
    cin_date_delivrance   DATE,
    cin_lieu_delivrance   VARCHAR(100),
    numero_cnaps          VARCHAR(100)
);


-- =====================================================================
-- PARTIE 3 : CONTRAT D'ESSAI (CE)
-- =====================================================================

CREATE TABLE contrat_ce (
    id                     SERIAL         PRIMARY KEY,
    employeur_id           INT            NOT NULL REFERENCES employeur(id),
    travailleur_id         INT            NOT NULL REFERENCES travailleur(id),
    statut_id              INT            NOT NULL REFERENCES statut_contrat(id),
    langue_id              INT            NOT NULL REFERENCES langue_contrat(id),

    -- Renouvellement : NULL = contrat initial, sinon renouvellement du contrat indiqué
    contrat_initial_id     INT            REFERENCES contrat_ce(id) ON DELETE CASCADE,

    -- Article 1 : Fonctions
    poste                  VARCHAR(100),
    categorie_id           INT            NOT NULL REFERENCES categorie_professionnelle(id),
    indice_classification  VARCHAR(100),
    convention_id          INT            REFERENCES convention_collective(id),
    missions               TEXT,

    -- Article 3 : Lieu d'emploi
    lieu_travail           VARCHAR(100),

    -- Article 4 : Durée
    duree_mois             INT,
    date_debut             DATE,
    heures_hebdo           NUMERIC(4,1),

    -- Article 7 : Rémunération
    remuneration_nette     NUMERIC(12,0),

    -- Signature
    lieu_signature         VARCHAR(100),
    date_signature         DATE,
    nb_exemplaires         INT            DEFAULT 2
);


-- =====================================================================
-- PARTIE 4 : CONTRAT À DURÉE DÉTERMINÉE (CDD)
-- =====================================================================

CREATE TABLE motif_cdd (
    id       SERIAL        PRIMARY KEY,
    libelle  VARCHAR(100)  NOT NULL UNIQUE
);

CREATE TABLE type_avenant (
    id       SERIAL        PRIMARY KEY,
    libelle  VARCHAR(100)  NOT NULL UNIQUE
);

CREATE TABLE type_rupture (
    id       SERIAL        PRIMARY KEY,
    libelle  VARCHAR(100)  NOT NULL UNIQUE
);

CREATE TABLE contrat_cdd (
    id                     SERIAL         PRIMARY KEY,
    reference              VARCHAR(100)   UNIQUE,

    employeur_id           INT            NOT NULL REFERENCES employeur(id),
    travailleur_id         INT            NOT NULL REFERENCES travailleur(id),
    poste_id               INT            NOT NULL REFERENCES poste(id),
    categorie_id           INT            REFERENCES categorie_professionnelle(id),
    convention_id          INT            REFERENCES convention_collective(id),
    motif_id               INT            NOT NULL REFERENCES motif_cdd(id),
    statut_id              INT            NOT NULL REFERENCES statut_contrat(id),

    motif_precision        TEXT,
    regime_juridique       VARCHAR(100)   DEFAULT 'Code du travail de Madagascar',

    -- Durée
    date_debut             DATE           NOT NULL,
    date_fin               DATE           NOT NULL,

    -- Période d'essai
    essai_duree            INT,
    essai_date_fin         DATE,
    essai_resultat         VARCHAR(100),

    -- Temps de travail
    duree_hebdo_heures     NUMERIC(4,1)   DEFAULT 40,
    horaire_travail        VARCHAR(100),
    jours_travail_semaine  INT,
    lieu_travail           VARCHAR(100),

    -- Rémunération
    salaire_base           NUMERIC(12,0)  NOT NULL,
    devise                 CHAR(3)        NOT NULL DEFAULT 'MGA',
    periodicite_paiement   VARCHAR(100)   DEFAULT 'Mensuelle',
    indice_classification  VARCHAR(100),

    -- Congés
    conge_jours_par_mois   NUMERIC(3,1),

    -- Conclusion
    date_conclusion        DATE,
    lieu_conclusion        VARCHAR(100),
    nombre_exemplaires     INT            DEFAULT 2,
    date_visa_inspection   DATE,
    tribunal_competent     VARCHAR(100)   DEFAULT 'Tribunal du travail'
);

-- Primes, indemnités, avantages en nature
CREATE TABLE element_salaire_cdd (
    id          SERIAL         PRIMARY KEY,
    contrat_id  INT            NOT NULL REFERENCES contrat_cdd(id) ON DELETE CASCADE,
    libelle     VARCHAR(100)   NOT NULL,
    montant     NUMERIC(12,0)  NOT NULL,
    nature      VARCHAR(100)
);

-- Renouvellements et modifications
CREATE TABLE avenant_cdd (
    id                 SERIAL         PRIMARY KEY,
    contrat_id         INT            NOT NULL REFERENCES contrat_cdd(id) ON DELETE CASCADE,
    type_avenant_id    INT            NOT NULL REFERENCES type_avenant(id),
    numero             INT            NOT NULL,
    date_avenant       DATE           NOT NULL,
    nouvelle_date_fin  DATE,
    nouveau_salaire    NUMERIC(12,0),
    objet              TEXT
);

-- Fin du contrat
CREATE TABLE rupture_cdd (
    id               SERIAL        PRIMARY KEY,
    contrat_id       INT           NOT NULL UNIQUE REFERENCES contrat_cdd(id) ON DELETE CASCADE,
    type_rupture_id  INT           NOT NULL REFERENCES type_rupture(id),
    date_rupture     DATE          NOT NULL,
    motif            VARCHAR(100),
    observations     TEXT
);


-- =====================================================================
-- PARTIE 5 : CONTRAT À DURÉE INDÉTERMINÉE (CDI)
-- =====================================================================

CREATE TABLE contrat_cdi (
    id                           SERIAL         PRIMARY KEY,
    employeur_id                 INT            NOT NULL REFERENCES employeur(id),
    travailleur_id               INT            NOT NULL REFERENCES travailleur(id),
    statut_id                    INT            NOT NULL REFERENCES statut_contrat(id),

    -- Article 1 : Engagement
    date_prise_fonction          DATE           NOT NULL,
    emploi                       VARCHAR(100)   NOT NULL,
    non_lie_autre_entreprise     BOOLEAN        NOT NULL DEFAULT FALSE,

    -- Article 3 : Lieu de travail
    etablissement                VARCHAR(100),
    region                       VARCHAR(100),
    district_commune             VARCHAR(100),

    -- Article 5 : Période d'essai
    duree_essai                  VARCHAR(100),
    article_reference_essai      VARCHAR(100),
    source_essai                 VARCHAR(100)
        CHECK (source_essai IN ('CODE_TRAVAIL', 'CONVENTION_COLLECTIVE', 'ACCORD_ETABLISSEMENT')),

    -- Article 6 : Classement et rémunération
    categorie_id                 INT            REFERENCES categorie_professionnelle(id),
    convention_id                INT            REFERENCES convention_collective(id),
    salaire_brut_mensuel         NUMERIC(12,0),

    -- Article 7 : Durée et horaires
    duree_hebdo_heures           NUMERIC(4,1),
    horaires_fixes_par           VARCHAR(100)   DEFAULT 'Règlement intérieur de l''entreprise',

    -- Article 8 : Congés payés
    lieu_recrutement             VARCHAR(100),

    -- Articles 11 et 12 : Accidents et maladies
    regime_accident_travail      VARCHAR(100)   DEFAULT 'Code de prévoyance sociale',
    regime_maladie_non_pro       VARCHAR(100),

    -- Article 12 : Confidentialité
    entreprise_confidentialite   VARCHAR(100),

    -- Article 17 : Dispositions finales
    direction_regionale_travail  VARCHAR(100),
    nb_exemplaires               INT            NOT NULL DEFAULT 4,
    lieu_signature               VARCHAR(100),
    date_signature               DATE
);

-- Les 4 signataires du contrat
CREATE TABLE signature_cdi (
    id          SERIAL        PRIMARY KEY,
    contrat_id  INT           NOT NULL REFERENCES contrat_cdi(id) ON DELETE CASCADE,
    role        VARCHAR(100)  NOT NULL
        CHECK (role IN ('EMPLOYEUR', 'TRAVAILLEUR', 'DIRECTEUR_REGIONAL', 'MEDECIN')),
    signe       BOOLEAN       NOT NULL DEFAULT FALSE,
    UNIQUE (contrat_id, role)
);

-- Textes légaux cités à l'article 14
CREATE TABLE texte_legal (
    id          SERIAL        PRIMARY KEY,
    reference   VARCHAR(100)  NOT NULL,
    date_texte  DATE,
    intitule    VARCHAR(100)  NOT NULL
);


-- =====================================================================
-- PARTIE 6 : DONNÉES DE RÉFÉRENCE
-- =====================================================================

INSERT INTO civilite (libelle) VALUES ('M.'), ('Mme'), ('Mlle');

INSERT INTO situation_famille (libelle) VALUES
    ('Célibataire'), ('Marié(e)'), ('Divorcé(e)'), ('Veuf/Veuve'), ('Union libre');

INSERT INTO nationalite (libelle) VALUES
    ('Malagasy'), ('Française'), ('Comorienne'), ('Mauricienne'),
    ('Chinoise'), ('Indienne'), ('Autre');

INSERT INTO convention_collective (libelle) VALUES
    ('Aucune convention collective applicable'),
    ('Convention collective de branche (à préciser)'),
    ('Accord d''établissement');

INSERT INTO categorie_professionnelle (code, libelle) VALUES
    ('M1',  'Manœuvre 1'),            ('M2',  'Manœuvre 2'),
    ('OS1', 'Ouvrier spécialisé 1'),  ('OS2', 'Ouvrier spécialisé 2'), ('OS3', 'Ouvrier spécialisé 3'),
    ('OP1', 'Ouvrier professionnel 1'), ('OP2', 'Ouvrier professionnel 2'), ('OP3', 'Ouvrier professionnel 3'),
    ('HC1', 'Hors catégorie 1'),      ('HC2', 'Hors catégorie 2');

INSERT INTO langue_contrat (libelle) VALUES ('Français'), ('Malagasy');

INSERT INTO statut_contrat (libelle) VALUES
    ('Créé'),
    ('Signé'),
    ('Validé');

INSERT INTO motif_cdd (libelle) VALUES
    ('Remplacement d''un travailleur absent'),
    ('Surcroît temporaire d''activité'),
    ('Travail saisonnier'),
    ('Tâche précise et temporaire');

INSERT INTO type_avenant (libelle) VALUES ('Renouvellement'), ('Modification');

INSERT INTO type_rupture (libelle) VALUES
    ('Arrivée du terme'), ('Rupture pendant l''essai'), ('Démission'),
    ('Licenciement'), ('Accord des parties'), ('Autre');

INSERT INTO texte_legal (reference, date_texte, intitule) VALUES
    ('Loi n° 020',    '1992-09-23', 'portant code du travail en République du Mali'),
    ('Loi n° 99-041', '1999-08-12', 'instituant un Code de Prévoyance sociale'),
    ('Loi n° 09-015', '2009-06-26', 'instituant l''Assurance Maladie Obligatoire');
