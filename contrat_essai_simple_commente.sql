-- =====================================================================
-- CONTRAT D'ENGAGEMENT À L'ESSAI - MADAGASCAR
-- Base simple : tables et insertions uniquement
-- (les vérifications se font dans la couche métier)
--
-- Référence légale : Loi n° 2003-044 portant Code du travail
--
-- Articles du Code utilisés dans ce script :
--   Art. 6   : le contrat est écrit et mentionne au moins la fonction, la
--              catégorie professionnelle, l'indice minimum de classification,
--              le salaire et la date d'effet. Il est rédigé en malgache ou
--              en français, en double exemplaire (un pour l'employeur, un
--              remis au travailleur). Exempt de timbre et d'enregistrement.
--   Art. 9   : si aucune partie ne manifeste la volonté de cesser la
--              relation à la fin de l'essai, le contrat est réputé conclu
--              pour une durée indéterminée.
--   Art. 35  : l'essai (et son renouvellement) est écrit et précise au moins
--              le poste, la durée, le salaire et la catégorie
--              professionnelle. Un essai non écrit est réputé inexistant.
--   Art. 36  : l'essai ne peut pas excéder 6 mois et n'est renouvelable
--              qu'une seule fois.
--   Art. 37  : un décret fixe la forme, les modalités et la durée de l'essai
--              (à vérifier : mentions supplémentaires éventuelles).
--   Art. 38  : le travailleur à l'essai a les mêmes conditions de travail et
--              de protection sociale qu'un travailleur confirmé au même poste.
--   Art. 39  : certains articles (10, 11, 15, 16, 18) ne s'appliquent pas à
--              l'essai, sans indemnité pour l'une ou l'autre partie, sauf
--              convention contraire. Suspension en cas de maladie.
--   Art. 43  : un étranger doit avoir l'autorisation préalable du ministre
--              chargé de l'emploi (visa du contrat par l'inspecteur).
--   Art. 75  : durée légale du travail : 173,33 heures par mois.
--   Art. 94  : l'état de grossesse ne doit pas motiver la rupture pendant
--              l'essai.
--   Art. 100 : âge minimum d'accès à l'emploi : 15 ans.
--   Art. 208 : le tribunal compétent est celui du lieu de travail.
-- =====================================================================


-- =====================================================================
-- PARTIE 1 : TABLES DE RÉFÉRENCE (alimentent les listes déroulantes)
-- =====================================================================

-- Civilité du représentant de l'employeur et du travailleur
-- (contrat : "Représentée par M. ..." et "2) M. ...")
CREATE TABLE civilite (
    id_civilite SERIAL PRIMARY KEY,
    libelle VARCHAR(20)                      -- M., Mme, Mlle
);

-- Situation de famille du travailleur
-- (contrat : "Situation de famille : ...")
CREATE TABLE situation_famille (
    id_situation_famille SERIAL PRIMARY KEY,
    libelle VARCHAR(50)                      -- Célibataire, Marié(e), ...
);

-- Nationalité du travailleur
-- (contrat : "Nationalité : ...")
-- Art. 43 : si la nationalité n'est pas malgache, une autorisation
-- d'emploi est nécessaire (à contrôler dans le métier)
CREATE TABLE nationalite (
    id_nationalite SERIAL PRIMARY KEY,
    libelle VARCHAR(80)
);

-- Convention collective applicable
-- (contrat : "... de la convention ..." dans l'article Fonctions)
-- Facultative : toutes les entreprises n'en ont pas
CREATE TABLE convention_collective (
    id_convention SERIAL PRIMARY KEY,
    libelle VARCHAR(200)
);

-- Catégorie professionnelle
-- Art. 6 et 35 : mention obligatoire dans le contrat d'essai
CREATE TABLE categorie_professionnelle (
    id_categorie SERIAL PRIMARY KEY,
    code VARCHAR(10),                        -- ex : M1, OS1, OP1, HC1
    libelle VARCHAR(100)                     -- intitulé complet
);

-- Langue de rédaction du contrat
-- Art. 6 : malgache ou français
CREATE TABLE langue_contrat (
    id_langue SERIAL PRIMARY KEY,
    libelle VARCHAR(20)
);

-- Statut du dossier (suivi interne, absent du contrat papier)
-- "Transformé en CDI" : voir Art. 9
CREATE TABLE statut_contrat (
    id_statut SERIAL PRIMARY KEY,
    libelle VARCHAR(30)
);


-- =====================================================================
-- PARTIE 2 : TABLES PRINCIPALES
-- =====================================================================

-- ---------------------------------------------------------------------
-- EMPLOYEUR
-- Contrat : "ENTRE LES SOUSSIGNÉS - 1) La société ..."
-- ---------------------------------------------------------------------
CREATE TABLE employeur (
    id_employeur SERIAL PRIMARY KEY,

    -- Identité de la société
    denomination VARCHAR(150),               -- "La société ..."
    capital_social NUMERIC(15,0),            -- "au capital social de ..." (en Ariary)
    siege_social VARCHAR(255),               -- "Siège social : ..."

    -- Immatriculations
    numero_rcs VARCHAR(60),                  -- "Immatriculée au Registre du Commerce sous le numéro ..."
    numero_nif VARCHAR(30),                  -- Numéro d'identification fiscale
    numero_stat VARCHAR(30),                 -- Numéro statistique (facultatif)

    -- Représentant légal
    -- Contrat : "Représentée par M. ..., agissant en sa qualité de ..."
    id_civilite_representant INTEGER NOT NULL REFERENCES civilite(id_civilite),
    nom_representant VARCHAR(150),
    qualite_representant VARCHAR(80)         -- ex : Directeur Général
);

-- ---------------------------------------------------------------------
-- TRAVAILLEUR
-- Contrat : "ET 2) M. ... Ci-après dénommé le Travailleur"
-- ---------------------------------------------------------------------
CREATE TABLE travailleur (
    id_travailleur SERIAL PRIMARY KEY,

    -- Identité
    id_civilite INTEGER NOT NULL REFERENCES civilite(id_civilite),
    nom_prenoms VARCHAR(150),                -- "2) M. ..."
    date_naissance DATE,                     -- "Né le ..." (Art. 100 : 15 ans minimum, à contrôler)
    lieu_naissance VARCHAR(100),             -- facultatif

    -- Situation
    id_nationalite INTEGER NOT NULL REFERENCES nationalite(id_nationalite),
    id_situation_famille INTEGER NOT NULL REFERENCES situation_famille(id_situation_famille),
    residence_habituelle VARCHAR(255),       -- "Résidence habituelle : ..."

    -- Pièces d'identité et protection sociale (facultatifs)
    numero_cin VARCHAR(30),                  -- Carte d'identité nationale
    numero_cnaps VARCHAR(30)                 -- Immatriculation CNaPS, si déjà attribuée
);

-- ---------------------------------------------------------------------
-- CONTRAT D'ESSAI
-- Contrat : articles 1 à 11 du modèle
-- ---------------------------------------------------------------------
CREATE TABLE contrat_essai (
    id_contrat SERIAL PRIMARY KEY,

    -- Les deux parties (en-tête du contrat "ENTRE LES SOUSSIGNÉS")
    id_employeur INTEGER NOT NULL REFERENCES employeur(id_employeur) ON DELETE CASCADE,
    id_travailleur INTEGER NOT NULL REFERENCES travailleur(id_travailleur) ON DELETE CASCADE,

    -- Renouvellement
    -- Art. 36 : renouvelable une seule fois.
    -- NULL = contrat initial ; sinon = renouvellement du contrat indiqué.
    -- À contrôler dans le métier : un renouvellement n'est pas renouvelable.
    id_contrat_initial INTEGER REFERENCES contrat_essai(id_contrat) ON DELETE CASCADE,

    -- -----------------------------------------------------------------
    -- Article 1 du modèle : DÉFINITION DES FONCTIONS
    -- Art. 6 et 35 : poste, catégorie et indice de classification
    -- sont des mentions obligatoires
    -- -----------------------------------------------------------------
    poste VARCHAR(150),                      -- "engagé à l'essai en qualité de ..."
    id_categorie INTEGER NOT NULL REFERENCES categorie_professionnelle(id_categorie),
                                             -- "classé en catégorie ..."
    indice_classification VARCHAR(30),       -- Art. 6 : indice minimum de classification
    id_convention INTEGER REFERENCES convention_collective(id_convention),
                                             -- "de la convention ..." (facultatif)
    missions TEXT,                           -- "il / elle aura notamment pour mission : ..."

    -- -----------------------------------------------------------------
    -- Article 3 du modèle : LIEU D'EMPLOI
    -- Art. 208 : le tribunal compétent est celui du lieu de travail
    -- (Art. 11 du modèle : attribution de juridiction, texte fixe)
    -- -----------------------------------------------------------------
    lieu_travail VARCHAR(150),               -- "Le travailleur exercera ses fonctions à ..."

    -- -----------------------------------------------------------------
    -- Article 4 du modèle : DURÉE DU CONTRAT
    -- Art. 35 : la durée est obligatoire
    -- Art. 36 : 6 mois maximum (à contrôler dans le métier)
    -- Art. 6  : la date d'effet est obligatoire
    -- Art. 75 : durée légale 173,33 h/mois, soit environ 40 h/semaine
    -- Art. 9  : sans volonté de rompre à l'échéance, devient un CDI
    --           (date de fin = date_debut + duree_mois, à calculer dans le métier)
    -- -----------------------------------------------------------------
    duree_mois SMALLINT,                     -- "conclu à l'essai pour une durée de ... mois"
    date_debut DATE,                         -- "à compter du ..." (date d'effet)
    heures_hebdo NUMERIC(4,1),               -- "durée hebdomadaire de travail de ... heures"

    -- -----------------------------------------------------------------
    -- Article 7 du modèle : RÉMUNÉRATION
    -- Art. 6 et 35 : le salaire est une mention obligatoire
    -- Art. 38 : mêmes conditions de travail et de protection sociale
    --           qu'un travailleur confirmé au même poste
    -- -----------------------------------------------------------------
    remuneration_nette NUMERIC(12,0),        -- "rémunération fixe mensuelle nette de ..." (en Ariary)

    -- -----------------------------------------------------------------
    -- Forme et signature (bas du contrat : "Fait à ..., le ... en ... originaux")
    -- Art. 6 : malgache ou français, double exemplaire minimum,
    --          exempt de droit de timbre et d'enregistrement
    -- -----------------------------------------------------------------
    id_langue INTEGER NOT NULL REFERENCES langue_contrat(id_langue),
    lieu_signature VARCHAR(100),             -- "Fait à ..."
    date_signature DATE,                     -- "le ..."
    nb_exemplaires SMALLINT,                 -- minimum 2 (un employeur, un travailleur)

    -- -----------------------------------------------------------------
    -- Suivi interne (hors modèle de contrat)
    -- -----------------------------------------------------------------
    id_statut INTEGER NOT NULL REFERENCES statut_contrat(id_statut),
    date_creation TIMESTAMP DEFAULT now()
);

-- Articles du modèle sans champ de saisie (textes fixes dans le document) :
--   Art. 2 du modèle  : Consentement
--   Art. 5 du modèle  : Obligation de loyauté
--   Art. 6 du modèle  : Clause de confidentialité
--   Art. 8 du modèle  : Maladie - accident (voir Art. 39 du Code : suspension
--                       de l'essai en cas de maladie, indemnité jusqu'à 1 mois)
--   Art. 9 du modèle  : Renouvellement (voir Art. 36 du Code : une seule fois)
--   Art. 10 du modèle : Résiliation (voir Art. 39 du Code : pas d'indemnité)
--   Art. 11 du modèle : Attribution de juridiction (voir Art. 208 du Code)


-- =====================================================================
-- PARTIE 3 : INSERTIONS (valeurs des listes déroulantes)
-- =====================================================================

INSERT INTO civilite (libelle) VALUES
    ('M.'), ('Mme'), ('Mlle');

INSERT INTO situation_famille (libelle) VALUES
    ('Célibataire'), ('Marié(e)'), ('Divorcé(e)'), ('Veuf/Veuve'), ('Union libre');

INSERT INTO nationalite (libelle) VALUES
    ('Malgache'), ('Française'), ('Comorienne'), ('Mauricienne'),
    ('Chinoise'), ('Indienne'), ('Autre');

-- Exemples à remplacer par les conventions réellement applicables
INSERT INTO convention_collective (libelle) VALUES
    ('Aucune convention collective applicable'),
    ('Convention collective de branche (à préciser)'),
    ('Accord d''établissement');

-- Exemples à vérifier et remplacer par votre grille de classification
INSERT INTO categorie_professionnelle (code, libelle) VALUES
    ('M1', 'Manœuvre 1'), ('M2', 'Manœuvre 2'),
    ('OS1', 'Ouvrier spécialisé 1'), ('OS2', 'Ouvrier spécialisé 2'), ('OS3', 'Ouvrier spécialisé 3'),
    ('OP1', 'Ouvrier professionnel 1'), ('OP2', 'Ouvrier professionnel 2'), ('OP3', 'Ouvrier professionnel 3'),
    ('HC1', 'Hors catégorie 1'), ('HC2', 'Hors catégorie 2');

INSERT INTO langue_contrat (libelle) VALUES
    ('Français'), ('Malagasy');

INSERT INTO statut_contrat (libelle) VALUES
    ('Brouillon'), ('À signer'), ('Signé'), ('Renouvelé'),
    ('Résilié'), ('Terminé'), ('Transformé en CDI');
