-- Données de test pour la base grh
-- À exécuter après grh.sql, sur une base vide (les id commencent à 1)
-- Les mots de passe sont factices : ne jamais utiliser ce format en production

\c grh;

-- ---------- RÔLES ----------
INSERT INTO role (libelle) VALUES
    ('Manager'),            -- id 1
    ('Directeur'),          -- id 2
    ('Administrateur RH');  -- id 3

-- ---------- RESPONSABLES ----------
INSERT INTO responsable (id_role, email, mot_de_passe, date_embauche) VALUES
    (2, 'directeur.si@entreprise.mg',    'hash_factice_1', '2015-03-02'),  -- id 1 : directeur SI
    (2, 'directeur.finance@entreprise.mg','hash_factice_2', '2014-09-15'), -- id 2 : directeur financier
    (1, 'manager.finance@entreprise.mg', 'hash_factice_3', '2018-01-08'),  -- id 3 : manager Finance
    (1, 'manager.marketing@entreprise.mg','hash_factice_4','2019-06-17'),  -- id 4 : manager Marketing
    (1, 'manager.production@entreprise.mg','hash_factice_5','2017-11-20'), -- id 5 : manager Production
    (3, 'rh@entreprise.mg',              'hash_factice_6', '2016-04-04');  -- id 6 : administrateur RH

-- ---------- DIRECTIONS (regroupent les postes de même famille) ----------
INSERT INTO direction (nom_direction, id_directeur) VALUES
    ('Direction des Systèmes d''Information', 1),   -- id 1
    ('Direction Financière', 2);                    -- id 2

-- ---------- DÉPARTEMENTS (chacun a son propre manager) ----------
INSERT INTO departement (nom_dept, id_responsable) VALUES
    ('Finance', 3),      -- id 1
    ('Marketing', 4),    -- id 2
    ('Production', 5);   -- id 3

-- ---------- POSTES (génériques, rattachés à une direction) ----------
INSERT INTO poste (libelle, id_direction, date_creation) VALUES
    ('Développeur junior', 1, '2020-01-10'),        -- id 1
    ('Développeur senior', 1, '2020-01-10'),        -- id 2
    ('Administrateur réseau', 1, '2020-02-03'),     -- id 3
    ('Comptable', 2, '2019-05-20'),                 -- id 4
    ('Analyste financier', 2, '2021-03-15');        -- id 5

-- ---------- STATUTS DE POSTE ----------
INSERT INTO statut_poste (libelle) VALUES
    ('Occupé'),   -- id 1
    ('Vacant'),   -- id 2
    ('Gelé');     -- id 3

-- ---------- POSTES PAR DÉPARTEMENT ----------
-- (id_poste, id_dept, id_statut_poste)
INSERT INTO poste_fille (id_poste, id_dept, id_statut_poste) VALUES
    (1, 1, 1),   -- id 1 : Dév. junior dans Finance, occupé
    (1, 2, 1),   -- id 2 : Dév. junior dans Marketing, occupé
    (1, 3, 2),   -- id 3 : Dév. junior dans Production, vacant
    (2, 1, 1),   -- id 4 : Dév. senior dans Finance, occupé
    (3, 3, 1),   -- id 5 : Admin. réseau dans Production, occupé
    (4, 1, 1),   -- id 6 : Comptable dans Finance, occupé
    (5, 1, 2),   -- id 7 : Analyste financier dans Finance, vacant
    (4, 2, 1);   -- id 8 : Comptable dans Marketing, occupé

-- ---------- EMPLOYÉS ----------
INSERT INTO employe (nom_employe, id_poste_fille, date_embauche) VALUES
    ('Rakoto Jean',      1, '2022-02-01'),
    ('Rabe Hery',        2, '2023-05-15'),
    ('Randria Mialy',    4, '2019-09-02'),
    ('Rasoa Voahangy',   5, '2021-07-12'),
    ('Andry Tiana',      6, '2020-10-05'),
    ('Fanja Nirina',     8, '2024-01-08');

-- =====================================================================
-- REQUÊTES POUR COMPRENDRE
-- =====================================================================

-- 1) Pour chaque employé : son manager (département) et son directeur (direction)
SELECT e.nom_employe,
       p.libelle          AS poste,
       d.nom_dept         AS departement,
       m.email            AS manager,
       dir.nom_direction  AS direction,
       dr.email           AS directeur
FROM employe e
JOIN poste_fille pf ON pf.id_poste_fille = e.id_poste_fille
JOIN poste p        ON p.id_poste = pf.id_poste
JOIN departement d  ON d.id_dept = pf.id_dept
JOIN responsable m  ON m.id_responsable = d.id_responsable
JOIN direction dir  ON dir.id_direction = p.id_direction
JOIN responsable dr ON dr.id_responsable = dir.id_directeur
ORDER BY dir.nom_direction, p.libelle;

-- 2) Tous les employés d'une même direction, quel que soit leur département
--    (ici : Systèmes d'Information)
SELECT dir.nom_direction, p.libelle AS poste, d.nom_dept AS departement, e.nom_employe
FROM direction dir
JOIN poste p        ON p.id_direction = dir.id_direction
JOIN poste_fille pf ON pf.id_poste = p.id_poste
JOIN departement d  ON d.id_dept = pf.id_dept
LEFT JOIN employe e ON e.id_poste_fille = pf.id_poste_fille
WHERE dir.id_direction = 1
ORDER BY p.libelle, d.nom_dept;

-- 3) Postes vacants, avec le manager et le directeur à contacter
SELECT p.libelle AS poste, d.nom_dept AS departement,
       m.email AS manager, dr.email AS directeur
FROM poste_fille pf
JOIN statut_poste s ON s.id_statut_poste = pf.id_statut_poste
JOIN poste p        ON p.id_poste = pf.id_poste
JOIN departement d  ON d.id_dept = pf.id_dept
JOIN responsable m  ON m.id_responsable = d.id_responsable
JOIN direction dir  ON dir.id_direction = p.id_direction
JOIN responsable dr ON dr.id_responsable = dir.id_directeur
WHERE s.libelle = 'Vacant';

-- 4) Nombre d'employés par direction
SELECT dir.nom_direction, COUNT(e.id_employe) AS nb_employes
FROM direction dir
LEFT JOIN poste p        ON p.id_direction = dir.id_direction
LEFT JOIN poste_fille pf ON pf.id_poste = p.id_poste
LEFT JOIN employe e      ON e.id_poste_fille = pf.id_poste_fille
GROUP BY dir.nom_direction;
