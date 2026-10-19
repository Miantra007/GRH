create DATABASE grh;

\ c grh;

CREATE TABLE role(
    id_role serial PRIMARY key,
    libelle VARCHAR(50)
);

CREATE TABLE responsable(
    id_responsable serial PRIMARY key,
    id_role INTEGER REFERENCES role(id_role),
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    date_embauche DATE
);

CREATE Table departement(
    id_dept serial PRIMARY key,
    nom_dept VARCHAR(50),
    id_responsable INTEGER REFERENCES responsable(id_responsable)
);

CREATE TABLE employe(
    id_employe serial PRIMARY key,
    nom_employe VARCHAR(50),
    id_poste_fille INTEGER REFERENCES poste_fille(id_poste_fille),
    date_embauche date
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
CREATE Table statut_contrat(
    id_statut_contrat serial PRIMARY key,
    libelle VARCHAR(20)
);

CREATE Table contrat_candidat(
    id_contrat_candidat serial PRIMARY key,
    id_ce INTEGER REFERENCES ce(id_ce),
    id_cdd INTEGER REFERENCES cdd(id_cdd),
    id_cdi INTEGER REFERENCES cdi(id_cdi),
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