-- =====================================================================
-- GRH : SCHÉMA COMPLET
-- Contient toutes les tables nécessaires
-- Exécution : psql -U postgres -f 001_schema_complet.sql
-- =====================================================================

DROP DATABASE IF EXISTS grh;
CREATE DATABASE grh;
\c grh

-- ============================================================
-- ORGANISATION
-- ============================================================

CREATE TABLE role(
    id_role SERIAL PRIMARY KEY,
    libelle VARCHAR(50)
);

CREATE TABLE statut_employe (
    id_statut_employe SERIAL PRIMARY KEY,
    libelle VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE responsable(
    id_responsable SERIAL PRIMARY KEY,
    id_role INTEGER REFERENCES role(id_role),
    id_statut_employe INTEGER REFERENCES statut_employe(id_statut_employe),
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    date_embauche DATE
);

CREATE TABLE direction(
    id_direction SERIAL PRIMARY KEY,
    nom_direction VARCHAR(100),
    id_directeur INTEGER REFERENCES responsable(id_responsable)
);

CREATE TABLE departement(
    id_dept SERIAL PRIMARY KEY,
    nom_dept VARCHAR(50),
    id_responsable INTEGER REFERENCES responsable(id_responsable)
);

CREATE TABLE poste(
    id_poste SERIAL PRIMARY KEY,
    libelle VARCHAR(50),
    id_direction INTEGER REFERENCES direction(id_direction),
    date_creation DATE
);

CREATE TABLE statut_poste(
    id_statut_poste SERIAL PRIMARY KEY,
    libelle VARCHAR(20)
);

CREATE TABLE poste_fille(
    id_poste_fille SERIAL PRIMARY KEY,
    id_poste INTEGER NOT NULL REFERENCES poste(id_poste),
    id_dept INTEGER NOT NULL REFERENCES departement(id_dept),
    id_statut_poste INTEGER REFERENCES statut_poste(id_statut_poste)
);

CREATE TABLE employe (
    id_employe SERIAL PRIMARY KEY,
    nom_employe VARCHAR(50) NOT NULL,
    id_poste_fille INTEGER REFERENCES poste_fille(id_poste_fille),
    date_embauche DATE NOT NULL,
    id_statut_employe INTEGER NOT NULL REFERENCES statut_employe(id_statut_employe)
);

CREATE TABLE historique_employe (
    id_historique_employe SERIAL PRIMARY KEY,
    id_employe INTEGER NOT NULL REFERENCES employe(id_employe),
    id_statut_employe INTEGER NOT NULL REFERENCES statut_employe(id_statut_employe),
    date_debut DATE NOT NULL,
    date_fin DATE,
    motif TEXT,
    date_enregistrement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (date_fin IS NULL OR date_fin >= date_debut)
);

-- ============================================================
-- CANDIDAT + CV
-- ============================================================

CREATE TABLE candidat(
    id_candidat SERIAL PRIMARY KEY,
    nom_candidat VARCHAR(50),
    prenom_candidat VARCHAR(50),
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    dtn DATE
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

CREATE TABLE niveau_competence (
    id_niveau_competence SERIAL PRIMARY KEY,
    libelle_niveau VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE competence (
    id_competence SERIAL PRIMARY KEY,
    id_cv INTEGER NOT NULL REFERENCES cv(id_cv) ON DELETE CASCADE,
    nom_competence VARCHAR(100) NOT NULL,
    id_niveau_competence INTEGER NOT NULL REFERENCES niveau_competence(id_niveau_competence)
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
    id_cv_langue SERIAL PRIMARY KEY,
    id_cv INTEGER NOT NULL REFERENCES cv(id_cv) ON DELETE CASCADE,
    id_langue INTEGER NOT NULL REFERENCES langue(id_langue),
    id_niveau_langue INTEGER NOT NULL REFERENCES niveau_langue(id_niveau_langue),
    UNIQUE (id_cv, id_langue)
);

CREATE TABLE centre_interet (
    id_centre_interet SERIAL PRIMARY KEY,
    id_cv INTEGER NOT NULL REFERENCES cv(id_cv) ON DELETE CASCADE,
    nom_centre_interet VARCHAR(150) NOT NULL,
    description TEXT
);

-- ============================================================
-- RÉFÉRENTIELS CONTRAT
-- ============================================================

CREATE TABLE civilite (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE sexe (
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

CREATE TABLE langue_contrat (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE statut_contrat (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

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

CREATE TABLE type_contrat (
    id_type_contrat SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE type_consigne (
    id_type_consigne SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE type_critere_proposition (
    id_type_critere SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE statut_besoin (
    id_statut_besoin SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE statut_candidature (
    id_statut_candidature SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE texte_legal (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(100) NOT NULL,
    date_texte DATE,
    intitule VARCHAR(100) NOT NULL
);

-- ============================================================
-- PARTIES AU CONTRAT
-- ============================================================

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
    sexe_id INT REFERENCES sexe(id),
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
    numero_cnaps VARCHAR(100),
    id_candidat INT REFERENCES candidat(id_candidat) ON DELETE CASCADE
);

CREATE INDEX idx_travailleur_sexe ON travailleur(sexe_id);
CREATE INDEX idx_travailleur_candidat ON travailleur(id_candidat);

-- ============================================================
-- CONTRATS
-- ============================================================

CREATE TABLE contrat_ce (
    id SERIAL PRIMARY KEY,
    employeur_id INT NOT NULL REFERENCES employeur(id),
    travailleur_id INT NOT NULL REFERENCES travailleur(id),
    statut_id INT NOT NULL REFERENCES statut_contrat(id),
    langue_id INT REFERENCES langue_contrat(id),
    contrat_initial_id INT REFERENCES contrat_ce(id) ON DELETE CASCADE,
    poste VARCHAR(100),
    categorie_id INT NOT NULL REFERENCES categorie_professionnelle(id),
    indice_classification VARCHAR(100),
    convention_id INT REFERENCES convention_collective(id),
    missions TEXT,
    lieu_travail VARCHAR(100),
    duree_mois INT,
    date_debut DATE,
    heures_hebdo NUMERIC(4, 1),
    remuneration_nette NUMERIC(12, 0),
    lieu_signature VARCHAR(100),
    date_signature DATE,
    nb_exemplaires INT DEFAULT 2
);

CREATE TABLE contrat_cdd (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(100) UNIQUE,
    employeur_id INT NOT NULL REFERENCES employeur(id),
    travailleur_id INT NOT NULL REFERENCES travailleur(id),
    poste_id INT NOT NULL REFERENCES poste(id_poste),
    categorie_id INT REFERENCES categorie_professionnelle(id),
    convention_id INT REFERENCES convention_collective(id),
    motif_id INT NOT NULL REFERENCES motif_cdd(id),
    statut_id INT NOT NULL REFERENCES statut_contrat(id),
    motif_precision TEXT,
    regime_juridique VARCHAR(100) DEFAULT 'Code du travail de Madagascar',
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    essai_duree INT,
    essai_date_fin DATE,
    essai_resultat VARCHAR(100),
    duree_hebdo_heures NUMERIC(4, 1) DEFAULT 40,
    horaire_travail VARCHAR(100),
    jours_travail_semaine INT,
    lieu_travail VARCHAR(100),
    salaire_base NUMERIC(12, 0) NOT NULL,
    devise CHAR(3) NOT NULL DEFAULT 'MGA',
    periodicite_paiement VARCHAR(100) DEFAULT 'Mensuelle',
    indice_classification VARCHAR(100),
    conge_jours_par_mois NUMERIC(3, 1),
    date_conclusion DATE,
    lieu_conclusion VARCHAR(100),
    nombre_exemplaires INT DEFAULT 2,
    date_visa_inspection DATE,
    tribunal_competent VARCHAR(100) DEFAULT 'Tribunal du travail'
);

CREATE TABLE contrat_cdi (
    id SERIAL PRIMARY KEY,
    employeur_id INT NOT NULL REFERENCES employeur(id),
    travailleur_id INT NOT NULL REFERENCES travailleur(id),
    statut_id INT NOT NULL REFERENCES statut_contrat(id),
    date_prise_fonction DATE NOT NULL,
    emploi VARCHAR(100) NOT NULL,
    non_lie_autre_entreprise BOOLEAN NOT NULL DEFAULT FALSE,
    etablissement VARCHAR(100),
    region VARCHAR(100),
    district_commune VARCHAR(100),
    duree_essai VARCHAR(100),
    article_reference_essai VARCHAR(100),
    source_essai VARCHAR(100) CHECK (
        source_essai IN ('CODE_TRAVAIL', 'CONVENTION_COLLECTIVE', 'ACCORD_ETABLISSEMENT')
    ),
    categorie_id INT REFERENCES categorie_professionnelle(id),
    convention_id INT REFERENCES convention_collective(id),
    salaire_brut_mensuel NUMERIC(12, 0),
    duree_hebdo_heures NUMERIC(4, 1),
    horaires_fixes_par VARCHAR(100) DEFAULT 'Règlement intérieur de l''entreprise',
    lieu_recrutement VARCHAR(100),
    regime_accident_travail VARCHAR(100) DEFAULT 'Code de prévoyance sociale',
    regime_maladie_non_pro VARCHAR(100),
    entreprise_confidentialite VARCHAR(100),
    direction_regionale_travail VARCHAR(100),
    nb_exemplaires INT NOT NULL DEFAULT 4,
    lieu_signature VARCHAR(100),
    date_signature DATE
);

CREATE TABLE signature_cdi (
    id SERIAL PRIMARY KEY,
    contrat_id INT NOT NULL REFERENCES contrat_cdi(id) ON DELETE CASCADE,
    role VARCHAR(100) NOT NULL CHECK (
        role IN ('EMPLOYEUR', 'TRAVAILLEUR', 'DIRECTEUR_REGIONAL', 'MEDECIN')
    ),
    signe BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (contrat_id, role)
);

-- ============================================================
-- RECRUTEMENT
-- ============================================================

CREATE TABLE type_besoin (
    id_type_besoin SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE besoin (
    id_besoin SERIAL PRIMARY KEY,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE besoin_poste (
    id_besoin_poste SERIAL PRIMARY KEY,
    id_besoin INT NOT NULL REFERENCES besoin(id_besoin),
    id_poste INT NOT NULL REFERENCES poste(id_poste),
    id_type_besoin INT REFERENCES type_besoin(id_type_besoin),
    date_limite DATE,
    description TEXT
);

CREATE TABLE candidature (
    id_candidature SERIAL PRIMARY KEY,
    id_candidat INT NOT NULL REFERENCES candidat(id_candidat),
    id_besoin_poste INT NOT NULL REFERENCES besoin_poste(id_besoin_poste),
    date_candidature TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(50) DEFAULT 'NOUVELLE'
);

CREATE TABLE statut_offre (
    id_statut_offre SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE offre_emploi (
    id_offre SERIAL PRIMARY KEY,
    id_besoin_poste INT NOT NULL REFERENCES besoin_poste(id_besoin_poste),
    id_statut_offre INT NOT NULL REFERENCES statut_offre(id_statut_offre),
    titre VARCHAR(255),
    description TEXT,
    profil_recherche TEXT,
    visible_client BOOLEAN DEFAULT FALSE,
    date_publication DATE,
    date_limite DATE
);

CREATE TABLE proposition_embauche (
    id_proposition SERIAL PRIMARY KEY,
    id_candidature INT NOT NULL REFERENCES candidature(id_candidature),
    salaire_demande NUMERIC(12, 0),
    salaire_propose_rh NUMERIC(12, 0),
    salaire_valide_finance NUMERIC(12, 0),
    salaire_final NUMERIC(12, 0),
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE_CANDIDAT',
    date_envoi TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE decision_rh (
    id_decision_rh SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE reponse_candidat (
    id_reponse_candidat SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE statut_proposition (
    id_statut_proposition SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

-- ============================================================
-- UTILISATEURS (LOGIN)
-- ============================================================

CREATE TABLE utilisateur (
    id_utilisateur SERIAL PRIMARY KEY,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

-- ============================================================
-- NOTIFICATIONS
-- ============================================================

CREATE TABLE notification (
    id_notification SERIAL PRIMARY KEY,
    id_candidat INT REFERENCES candidat(id_candidat),
    id_utilisateur INT REFERENCES utilisateur(id_utilisateur),
    titre VARCHAR(255),
    message TEXT,
    lu BOOLEAN DEFAULT FALSE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_proposition INT REFERENCES proposition_embauche(id_proposition)
);

-- ============================================================
-- CONTRAT CANDIDAT (LIEN)
-- ============================================================

CREATE TABLE contrat_candidat(
    id_contrat_candidat SERIAL PRIMARY KEY,
    id_candidat INTEGER NOT NULL REFERENCES candidat(id_candidat),
    type_contrat VARCHAR(3) NOT NULL DEFAULT 'CE' CHECK (type_contrat IN ('CE', 'CDD', 'CDI')),
    id_contrat_ce INTEGER REFERENCES contrat_ce(id),
    id_contrat_cdd INTEGER REFERENCES contrat_cdd(id),
    id_contrat_cdi INTEGER REFERENCES contrat_cdi(id),
    CHECK (num_nonnulls(id_contrat_ce, id_contrat_cdd, id_contrat_cdi) = 1)
);

CREATE TABLE historique_contrat_employe(
    id_historique_contrat_employe SERIAL PRIMARY KEY,
    id_employe INTEGER REFERENCES employe(id_employe),
    id_contrat_candidat INTEGER REFERENCES contrat_candidat(id_contrat_candidat),
    date_embauche DATE
);

CREATE TABLE historique_statut_contrat(
    id_historique_statut_contrat SERIAL PRIMARY KEY,
    id_statut_contrat INTEGER NOT NULL REFERENCES statut_contrat(id),
    id_contrat_candidat INTEGER NOT NULL REFERENCES contrat_candidat(id_contrat_candidat),
    date_changement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- DOSSIER CANDIDATURE ET DOCUMENTS
-- ============================================================

CREATE TABLE dossier_candidature (
    id_dossier SERIAL PRIMARY KEY,
    id_candidat INT NOT NULL REFERENCES candidat(id_candidat),
    id_proposition INT REFERENCES proposition_embauche(id_proposition),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE document_dossier (
    id_document SERIAL PRIMARY KEY,
    id_dossier INT NOT NULL REFERENCES dossier_candidature(id_dossier),
    nom_document VARCHAR(255),
    url_document TEXT,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- CRITERES ET REPONSES
-- ============================================================

CREATE TABLE critere_proposition (
    id_critere SERIAL PRIMARY KEY,
    id_proposition INT NOT NULL REFERENCES proposition_embauche(id_proposition),
    id_type_critere INT NOT NULL REFERENCES type_critere_proposition(id_type_critere),
    description TEXT
);

CREATE TABLE reponse_critere (
    id_reponse SERIAL PRIMARY KEY,
    id_critere INT NOT NULL REFERENCES critere_proposition(id_critere),
    reponse TEXT,
    date_reponse TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- ENTRETIENS ET TESTS
-- ============================================================

CREATE TABLE entretien (
    id_entretien SERIAL PRIMARY KEY,
    id_candidat INT NOT NULL REFERENCES candidat(id_candidat),
    id_proposition INT REFERENCES proposition_embauche(id_proposition),
    date_entretien TIMESTAMP,
    lieu VARCHAR(255),
    notes TEXT,
    resultat VARCHAR(50)
);

CREATE TABLE test_technique (
    id_test SERIAL PRIMARY KEY,
    id_candidat INT NOT NULL REFERENCES candidat(id_candidat),
    id_proposition INT REFERENCES proposition_embauche(id_proposition),
    date_test TIMESTAMP,
    titre VARCHAR(255),
    score NUMERIC(5,2),
    notes TEXT
);

-- ============================================================
-- AVENANTS ET RUPTURES CDD
-- ============================================================

CREATE TABLE avenant_cdd (
    id_avenant SERIAL PRIMARY KEY,
    id_contrat_cdd INT NOT NULL REFERENCES contrat_cdd(id),
    id_type_avenant INT REFERENCES type_avenant(id),
    date_avenant DATE,
    description TEXT
);

CREATE TABLE rupture_cdd (
    id_rupture SERIAL PRIMARY KEY,
    id_contrat_cdd INT NOT NULL REFERENCES contrat_cdd(id),
    id_type_rupture INT REFERENCES type_rupture(id),
    date_rupture DATE,
    description TEXT
);

CREATE TABLE element_salaire_cdd (
    id_element SERIAL PRIMARY KEY,
    id_contrat_cdd INT NOT NULL REFERENCES contrat_cdd(id),
    libelle VARCHAR(100),
    montant NUMERIC(12,2),
    type_element VARCHAR(50)
);

-- ============================================================
-- CONSIGNES INTEGRATION
-- ============================================================

CREATE TABLE consigne_integration (
    id_consigne SERIAL PRIMARY KEY,
    id_type_consigne INT NOT NULL REFERENCES type_consigne(id_type_consigne),
    libelle VARCHAR(255),
    description TEXT
);
