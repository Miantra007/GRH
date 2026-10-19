CREATE TABLE statut_contrat (
    id       SERIAL        PRIMARY KEY,
    code     VARCHAR(100)  NOT NULL UNIQUE,
    libelle  VARCHAR(100)  NOT NULL
);

INSERT INTO statut_contrat (code, libelle) VALUES
    ('BROUILLON', 'Brouillon'),
    ('GENERE',    'Contrat généré'),
    ('SIGNE',     'Signé par les parties'),
    ('VISE',      'Visé par la Direction du travail'),
    ('EN_COURS',  'En cours d''exécution'),
    ('RESILIE',   'Résilié'),
    ('ANNULE',    'Annulé');

CREATE TABLE employeur (
    id                     SERIAL        PRIMARY KEY,
    raison_sociale         VARCHAR(100)  NOT NULL,
    adresse_siege          VARCHAR(100),
    representant_nom       VARCHAR(100)  NOT NULL,
    representant_fonction  VARCHAR(100)  NOT NULL
);

CREATE TABLE travailleur (
    id                 SERIAL        PRIMARY KEY,
    nom_prenoms        VARCHAR(100)  NOT NULL,
    date_naissance     DATE          NOT NULL,
    lieu_naissance     VARCHAR(100)  NOT NULL,
    fils_de            VARCHAR(100),
    adresse_residence  VARCHAR(100),
    profession         VARCHAR(100),
    nationalite        VARCHAR(100)  NOT NULL DEFAULT 'Malagasy'
);

CREATE TABLE contrat (
    id                           SERIAL        PRIMARY KEY,
    employeur_id                 INT           NOT NULL REFERENCES employeur(id),
    travailleur_id               INT           NOT NULL REFERENCES travailleur(id),
    statut_id                    INT           NOT NULL REFERENCES statut_contrat(id),

    -- Article 1 : Engagement
    date_prise_fonction          DATE          NOT NULL,
    emploi                       VARCHAR(100)  NOT NULL,
    non_lie_autre_entreprise     BOOLEAN       NOT NULL DEFAULT FALSE,

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
    categorie                    VARCHAR(100),
    convention_collective        VARCHAR(100),
    salaire_brut_mensuel         NUMERIC(12,0),

    -- Article 7 : Durée et horaires
    duree_hebdo_heures           NUMERIC(4,1),
    horaires_fixes_par           VARCHAR(100)  DEFAULT 'Règlement intérieur de l''entreprise',

    -- Article 8 : Congés payés
    lieu_recrutement             VARCHAR(100),

    -- Articles 11 et 12 : Accidents et maladies
    regime_accident_travail      VARCHAR(100)  DEFAULT 'Code de prévoyance sociale',
    regime_maladie_non_pro       VARCHAR(100),

    -- Article 12 : Confidentialité
    entreprise_confidentialite   VARCHAR(100),

    -- Article 17 : Dispositions finales
    direction_regionale_travail  VARCHAR(100),
    nb_exemplaires               INT           NOT NULL DEFAULT 4,
    lieu_signature               VARCHAR(100),
    date_signature               DATE
);

-- Les 4 signataires du contrat
CREATE TABLE signature_contrat (
    id          SERIAL        PRIMARY KEY,
    contrat_id  INT           NOT NULL REFERENCES contrat(id) ON DELETE CASCADE,
    role        VARCHAR(100)  NOT NULL
        CHECK (role IN ('EMPLOYEUR', 'TRAVAILLEUR', 'DIRECTEUR_REGIONAL', 'MEDECIN')),
    signe       BOOLEAN       NOT NULL DEFAULT FALSE,
    UNIQUE (contrat_id, role)
);

-- Textes légaux cités à l'article 14
CREATE TABLE texte_legal (
    id          SERIAL  PRIMARY KEY,
    reference   VARCHAR(100)  NOT NULL,
    date_texte  DATE,
    intitule    VARCHAR(100)  NOT NULL
);

INSERT INTO texte_legal (reference, date_texte, intitule) VALUES
    ('Loi n° 020',    '1992-09-23', 'portant code du travail en République du Mali'),
    ('Loi n° 99-041', '1999-08-12', 'instituant un Code de Prévoyance sociale'),
    ('Loi n° 09-015', '2009-06-26', 'instituant l''Assurance Maladie Obligatoire');