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
    id_role INTEGER REFERENCES role(id_role), --directeur
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

-- Exemple de requête : manager et directeur de chaque employé
-- SELECT e.nom_employe,
--        p.libelle        AS poste,
--        d.nom_dept       AS departement,
--        m.email          AS manager,
--        dir.nom_direction,
--        dr.email         AS directeur
-- FROM employe e
-- JOIN poste_fille pf ON pf.id_poste_fille = e.id_poste_fille
-- JOIN poste p        ON p.id_poste = pf.id_poste
-- JOIN departement d  ON d.id_dept = pf.id_dept
-- JOIN responsable m  ON m.id_responsable = d.id_responsable
-- JOIN direction dir  ON dir.id_direction = p.id_direction
-- JOIN responsable dr ON dr.id_responsable = dir.id_directeur;