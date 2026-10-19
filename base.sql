CREATE DATABASE grh;

\ c grh;

-- Rôles des responsables : Manager, Directeur, RH, Admin...
CREATE TABLE role(
    id_role SERIAL PRIMARY KEY,
    libelle VARCHAR(50)
);

-- Les responsables (managers et directeurs) : ce sont eux qui se connectent
CREATE TABLE responsable(
    id_responsable SERIAL PRIMARY KEY,
    id_role INTEGER REFERENCES role(id_role),
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    date_embauche DATE
);

-- NOUVEAU : une direction regroupe les postes de même famille
-- ex : Direction des Systèmes d'Information, Direction Financière
-- Son directeur est commun à tous les départements
CREATE TABLE direction(
    id_direction SERIAL PRIMARY KEY,
    nom_direction VARCHAR(100),
    id_role INTEGER REFERENCES role(id_role),
    --directeur
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    date_embauche DATE
);

-- Chaque département a son propre manager
CREATE TABLE departement(
    id_dept SERIAL PRIMARY KEY,
    nom_dept VARCHAR(50),
    id_responsable INTEGER REFERENCES responsable(id_responsable) -- manager du département
);

-- Poste générique (ex : Dev junior), rattaché à une direction
-- id_responsable de votre version est remplacé par id_direction :
-- le directeur se retrouve via direction.id_directeur
CREATE TABLE poste(
    id_poste SERIAL PRIMARY KEY,
    libelle VARCHAR(50),
    id_direction INTEGER REFERENCES direction(id_direction),
    date_creation DATE
);

CREATE TABLE statut_poste(
    id_statut_poste SERIAL PRIMARY KEY,
    libelle VARCHAR(20) -- ex : Vacant, Occupé, Gelé
);

-- Un poste dans un département précis (ex : Dev junior dans Finance)
CREATE TABLE poste_fille(
    id_poste_fille SERIAL PRIMARY KEY,
    id_poste INTEGER NOT NULL REFERENCES poste(id_poste),
    id_dept INTEGER NOT NULL REFERENCES departement(id_dept),
    id_statut_poste INTEGER REFERENCES statut_poste(id_statut_poste)
);

CREATE TABLE employe(
    id_employe SERIAL PRIMARY KEY,
    nom_employe VARCHAR(50),
    id_poste_fille INTEGER REFERENCES poste_fille(id_poste_fille),
    date_embauche DATE
);

CREATE Table poste(
    id_poste serial PRIMARY key,
    libelle VARCHAR(50),
    id_responsable INTEGER REFERENCES responsable(id_responsable),
    date_creation date
);

CREATE TABLE poste_fille(
    id_poste_fille serial PRIMARY key,
    id_poste INTEGER REFERENCES poste(id_poste),
    id_dept INTEGER REFERENCES departement(id_dept),
    id_statut_poste INTEGER REFERENCES statut_poste(id_statut_poste)
);

CREATE TABLE statut_poste(
    id_statut_poste serial PRIMARY key,
    libelle VARCHAR(20)
);

CREATE Table candidat(
    id_candidat serial PRIMARY key,
    nom_candidat VARCHAR(50),
    prenom_candidat VARCHAR(50),
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    dtn date
);

-------------------------------CV----------------------------------------------------------
CREATE TABLE cv (
    id_cv SERIAL PRIMARY KEY,
    id_candidat INTEGER NOT NULL REFERENCES candidat(id_candidat) ON DELETE CASCADE,
    titre_cv VARCHAR(150),
    profil_professionnel TEXT,
    adresse VARCHAR(255),
    id_ville INTEGER REFERENCES ville(id_ville),
    photo_url TEXT,
    linkedin_url TEXT,
    github_url TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pays (
    id_pays SERIAL PRIMARY KEY,
    nom_pays VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE ville (
    id_ville SERIAL PRIMARY KEY,
    nom_ville VARCHAR(100) NOT NULL,
    id_pays INTEGER NOT NULL REFERENCES pays(id_pays)
);

CREATE TABLE experience_professionnelle (
    id_experience SERIAL PRIMARY KEY,
    id_cv INTEGER NOT NULL REFERENCES cv(id_cv) ON DELETE CASCADE,
    poste VARCHAR(150) NOT NULL,
    entreprise VARCHAR(150),
    type_contrat VARCHAR(50),
    lieu VARCHAR(150),
    date_debut DATE,
    date_fin DATE,
    description TEXT
);

CREATE TABLE formation (
    id_formation SERIAL PRIMARY KEY,
    id_cv INTEGER NOT NULL REFERENCES cv(id_cv) ON DELETE CASCADE,
    nom_formation VARCHAR(100) NOT NULL,
    etablissement VARCHAR(200),
    date_debut DATE,
    date_fin DATE,
    description TEXT
);

CREATE TABLE competence (
    id_competence SERIAL PRIMARY KEY,
    id_cv INTEGER NOT NULL REFERENCES cv(id_cv) ON DELETE CASCADE,
    nom_competence VARCHAR(100) NOT NULL,
    id_niveau_competence INTEGER NOT NULL REFERENCES niveau_competence(id_niveau_competence)
);

CREATE TABLE niveau_competence (
    id_niveau_competence SERIAL PRIMARY KEY,
    libelle_niveau VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE education (
    id_education SERIAL PRIMARY KEY,
    id_cv INTEGER NOT NULL REFERENCES cv(id_cv) ON DELETE CASCADE,
    diplome VARCHAR(150) NOT NULL,
    etablissement VARCHAR(150),
    domaine_etude VARCHAR(150),
    niveau VARCHAR(50),
    date_debut DATE,
    date_fin DATE,
    description TEXT
);

CREATE TABLE langue (
    id_langue SERIAL PRIMARY KEY,
    nom_langue VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE niveau_langue (
    id_niveau_langue SERIAL PRIMARY KEY,
    libelle_niveau VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE cv_langue (
    id_cv INTEGER NOT NULL REFERENCES cv(id_cv) ON DELETE CASCADE,
    id_langue INTEGER NOT NULL REFERENCES langue(id_langue),
    id_niveau_langue INTEGER NOT NULL REFERENCES niveau_langue(id_niveau_langue)
);

CREATE TABLE centre_interet (
    id_centre_interet SERIAL PRIMARY KEY,
    id_cv INTEGER NOT NULL REFERENCES cv(id_cv) ON DELETE CASCADE,
    nom_centre_interet VARCHAR(150) NOT NULL,
    description TEXT
);

-------------------------------CV----------------------------------------------------------
CREATE Table test_technique(
    id_test_technique serial PRIMARY key,
    description VARCHAR(50),
    id_candidat INTEGER NOT NULL REFERENCES candidat(id_candidat) ON DELETE CASCADE,
    date_test date
);

CREATE Table entretien(
    id_entretien serial PRIMARY key,
    description VARCHAR(50),
    id_candidat INTEGER NOT NULL REFERENCES candidat(id_candidat) ON DELETE CASCADE,
    date_entretien date
);

---------------------------------------------CONTRAT---------------------------------------------------
CREATE Table contrat_candidat(
    id_contrat_candidat serial PRIMARY key,
    id INTEGER REFERENCES contrat_ce(id),
    id INTEGER REFERENCES contrat_cdd(id),
    id INTEGER REFERENCES contrat_cdi(id),
    id_candidat INTEGER REFERENCES candidat(id_candidat),
);

CREATE Table historique_contrat_employe(
    historique_contrat_employe serial PRIMARY key,
    id_employe INTEGER REFERENCES employe(id_employe),
    id_contrat_candidat INTEGER REFERENCES contrat_candidat(id_contrat_candidat),
    date_embauche date
);

CREATE TABLE historique_statut_contrat(
    id_historique_statut_contrat serial PRIMARY key,
    id_statut_contrat INTEGER REFERENCES statut_contrat(id_statut_contrat),
    id_contrat_candidat INTEGER REFERENCES contrat_candidat(id_contrat_candidat),
    date date
);

---------------------------------------------CONTRAT---------------------------------------------------
CREATE TABLE civilite (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE situation_famille (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE nationalite (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE convention_collective (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE categorie_professionnelle (
    id SERIAL PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    libelle VARCHAR(100) NOT NULL,
    indice VARCHAR(100)
);

CREATE TABLE poste (
    id SERIAL PRIMARY KEY,
    intitule VARCHAR(100) NOT NULL,
    categorie_id INT REFERENCES categorie_professionnelle(id)
);

CREATE TABLE langue_contrat (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE statut_contrat (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

-- =====================================================================
-- PARTIE 2 : PARTIES AU CONTRAT (communes aux 3 contrats)
-- =====================================================================
CREATE TABLE employeur (
    id SERIAL PRIMARY KEY,
    raison_sociale VARCHAR(100) NOT NULL,
    forme_juridique VARCHAR(100),
    capital_social NUMERIC(15, 0),
    adresse_siege VARCHAR(100),
    nif VARCHAR(100),
    stat VARCHAR(100),
    rcs VARCHAR(100),
    numero_cnaps VARCHAR(100),
    organisme_sante VARCHAR(100),
    numero_organisme_sante VARCHAR(100),
    telephone VARCHAR(100),
    email VARCHAR(100),
    civilite_id INT REFERENCES civilite(id),
    representant_nom VARCHAR(100) NOT NULL,
    representant_fonction VARCHAR(100) NOT NULL
);

CREATE TABLE travailleur (
    id SERIAL PRIMARY KEY,
    civilite_id INT REFERENCES civilite(id),
    nom_prenoms VARCHAR(100) NOT NULL,
    date_naissance DATE NOT NULL,
    lieu_naissance VARCHAR(100) NOT NULL,
    fils_de VARCHAR(100),
    nationalite_id INT NOT NULL REFERENCES nationalite(id),
    situation_famille_id INT REFERENCES situation_famille(id),
    nombre_enfants INT,
    adresse_residence VARCHAR(100),
    profession VARCHAR(100),
    telephone VARCHAR(100),
    email VARCHAR(100),
    cin_numero VARCHAR(100),
    cin_date_delivrance DATE,
    cin_lieu_delivrance VARCHAR(100),
    numero_cnaps VARCHAR(100)
);

-- =====================================================================
-- PARTIE 3 : CONTRAT D'ESSAI (CE)
-- =====================================================================
CREATE TABLE contrat_ce (
    id SERIAL PRIMARY KEY,
    employeur_id INT NOT NULL REFERENCES employeur(id),
    travailleur_id INT NOT NULL REFERENCES travailleur(id),
    statut_id INT NOT NULL REFERENCES statut_contrat(id),
    langue_id INT NOT NULL REFERENCES langue_contrat(id),
    -- Renouvellement : NULL = contrat initial, sinon renouvellement du contrat indiqué
    contrat_initial_id INT REFERENCES contrat_ce(id) ON DELETE CASCADE,
    -- Article 1 : Fonctions
    poste VARCHAR(100),
    categorie_id INT NOT NULL REFERENCES categorie_professionnelle(id),
    indice_classification VARCHAR(100),
    convention_id INT REFERENCES convention_collective(id),
    missions TEXT,
    -- Article 3 : Lieu d'emploi
    lieu_travail VARCHAR(100),
    -- Article 4 : Durée
    duree_mois INT,
    date_debut DATE,
    heures_hebdo NUMERIC(4, 1),
    -- Article 7 : Rémunération
    remuneration_nette NUMERIC(12, 0),
    -- Signature
    lieu_signature VARCHAR(100),
    date_signature DATE,
    nb_exemplaires INT DEFAULT 2
);

-- =====================================================================
-- PARTIE 4 : CONTRAT À DURÉE DÉTERMINÉE (CDD)
-- =====================================================================
CREATE TABLE motif_cdd (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE type_avenant (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE type_rupture (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE contrat_cdd (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(100) UNIQUE,
    employeur_id INT NOT NULL REFERENCES employeur(id),
    travailleur_id INT NOT NULL REFERENCES travailleur(id),
    poste_id INT NOT NULL REFERENCES poste(id),
    categorie_id INT REFERENCES categorie_professionnelle(id),
    convention_id INT REFERENCES convention_collective(id),
    motif_id INT NOT NULL REFERENCES motif_cdd(id),
    statut_id INT NOT NULL REFERENCES statut_contrat(id),
    motif_precision TEXT,
    regime_juridique VARCHAR(100) DEFAULT 'Code du travail de Madagascar',
    -- Durée
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    -- Période d'essai
    essai_duree INT,
    essai_date_fin DATE,
    essai_resultat VARCHAR(100),
    -- Temps de travail
    duree_hebdo_heures NUMERIC(4, 1) DEFAULT 40,
    horaire_travail VARCHAR(100),
    jours_travail_semaine INT,
    lieu_travail VARCHAR(100),
    -- Rémunération
    salaire_base NUMERIC(12, 0) NOT NULL,
    devise CHAR(3) NOT NULL DEFAULT 'MGA',
    periodicite_paiement VARCHAR(100) DEFAULT 'Mensuelle',
    indice_classification VARCHAR(100),
    -- Congés
    conge_jours_par_mois NUMERIC(3, 1),
    -- Conclusion
    date_conclusion DATE,
    lieu_conclusion VARCHAR(100),
    nombre_exemplaires INT DEFAULT 2,
    date_visa_inspection DATE,
    tribunal_competent VARCHAR(100) DEFAULT 'Tribunal du travail'
);

-- Primes, indemnités, avantages en nature
CREATE TABLE element_salaire_cdd (
    id SERIAL PRIMARY KEY,
    contrat_id INT NOT NULL REFERENCES contrat_cdd(id) ON DELETE CASCADE,
    libelle VARCHAR(100) NOT NULL,
    montant NUMERIC(12, 0) NOT NULL,
    nature VARCHAR(100)
);

-- Renouvellements et modifications
CREATE TABLE avenant_cdd (
    id SERIAL PRIMARY KEY,
    contrat_id INT NOT NULL REFERENCES contrat_cdd(id) ON DELETE CASCADE,
    type_avenant_id INT NOT NULL REFERENCES type_avenant(id),
    numero INT NOT NULL,
    date_avenant DATE NOT NULL,
    nouvelle_date_fin DATE,
    nouveau_salaire NUMERIC(12, 0),
    objet TEXT
);

-- Fin du contrat
CREATE TABLE rupture_cdd (
    id SERIAL PRIMARY KEY,
    contrat_id INT NOT NULL UNIQUE REFERENCES contrat_cdd(id) ON DELETE CASCADE,
    type_rupture_id INT NOT NULL REFERENCES type_rupture(id),
    date_rupture DATE NOT NULL,
    motif VARCHAR(100),
    observations TEXT
);

-- =====================================================================
-- PARTIE 5 : CONTRAT À DURÉE INDÉTERMINÉE (CDI)
-- =====================================================================
CREATE TABLE contrat_cdi (
    id SERIAL PRIMARY KEY,
    employeur_id INT NOT NULL REFERENCES employeur(id),
    travailleur_id INT NOT NULL REFERENCES travailleur(id),
    statut_id INT NOT NULL REFERENCES statut_contrat(id),
    -- Article 1 : Engagement
    date_prise_fonction DATE NOT NULL,
    emploi VARCHAR(100) NOT NULL,
    non_lie_autre_entreprise BOOLEAN NOT NULL DEFAULT FALSE,
    -- Article 3 : Lieu de travail
    etablissement VARCHAR(100),
    region VARCHAR(100),
    district_commune VARCHAR(100),
    -- Article 5 : Période d'essai
    duree_essai VARCHAR(100),
    article_reference_essai VARCHAR(100),
    source_essai VARCHAR(100) CHECK (
        source_essai IN (
            'CODE_TRAVAIL',
            'CONVENTION_COLLECTIVE',
            'ACCORD_ETABLISSEMENT'
        )
    ),
    -- Article 6 : Classement et rémunération
    categorie_id INT REFERENCES categorie_professionnelle(id),
    convention_id INT REFERENCES convention_collective(id),
    salaire_brut_mensuel NUMERIC(12, 0),
    -- Article 7 : Durée et horaires
    duree_hebdo_heures NUMERIC(4, 1),
    horaires_fixes_par VARCHAR(100) DEFAULT 'Règlement intérieur de l''entreprise',
    -- Article 8 : Congés payés
    lieu_recrutement VARCHAR(100),
    -- Articles 11 et 12 : Accidents et maladies
    regime_accident_travail VARCHAR(100) DEFAULT 'Code de prévoyance sociale',
    regime_maladie_non_pro VARCHAR(100),
    -- Article 12 : Confidentialité
    entreprise_confidentialite VARCHAR(100),
    -- Article 17 : Dispositions finales
    direction_regionale_travail VARCHAR(100),
    nb_exemplaires INT NOT NULL DEFAULT 4,
    lieu_signature VARCHAR(100),
    date_signature DATE
);

-- Les 4 signataires du contrat
CREATE TABLE signature_cdi (
    id SERIAL PRIMARY KEY,
    contrat_id INT NOT NULL REFERENCES contrat_cdi(id) ON DELETE CASCADE,
    role VARCHAR(100) NOT NULL CHECK (
        role IN (
            'EMPLOYEUR',
            'TRAVAILLEUR',
            'DIRECTEUR_REGIONAL',
            'MEDECIN'
        )
    ),
    signe BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (contrat_id, role)
);

-- Textes légaux cités à l'article 14
CREATE TABLE texte_legal (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(100) NOT NULL,
    date_texte DATE,
    intitule VARCHAR(100) NOT NULL
);

