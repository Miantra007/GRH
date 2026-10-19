-- Données de test pour la base grh
-- À exécuter après grh.sql, sur une base vide (les id commencent à 1)
-- Les mots de passe sont factices : ne jamais utiliser ce format en production
\ c grh;

-- ---------- RÔLES ----------
INSERT INTO
    role (libelle)
VALUES
    ('Manager'),
    -- id 1
    ('Directeur'),
    -- id 2
    ('Administrateur RH');

-- id 3
-- ---------- RESPONSABLES ----------
INSERT INTO
    responsable (id_role, email, mot_de_passe, date_embauche)
VALUES
    (
        2,
        'directeur.si@entreprise.mg',
        'hash_factice_1',
        '2015-03-02'
    ),
    -- id 1 : directeur SI
    (
        2,
        'directeur.finance@entreprise.mg',
        'hash_factice_2',
        '2014-09-15'
    ),
    -- id 2 : directeur financier
    (
        1,
        'manager.finance@entreprise.mg',
        'hash_factice_3',
        '2018-01-08'
    ),
    -- id 3 : manager Finance
    (
        1,
        'manager.marketing@entreprise.mg',
        'hash_factice_4',
        '2019-06-17'
    ),
    -- id 4 : manager Marketing
    (
        1,
        'manager.production@entreprise.mg',
        'hash_factice_5',
        '2017-11-20'
    ),
    -- id 5 : manager Production
    (
        3,
        'rh@entreprise.mg',
        'hash_factice_6',
        '2016-04-04'
    );

-- id 6 : administrateur RH
-- ---------- DIRECTIONS (regroupent les postes de même famille) ----------
INSERT INTO
    direction (nom_direction, id_directeur)
VALUES
    ('Direction des Systèmes d''Information', 1),
    -- id 1
    ('Direction Financière', 2);

-- id 2
-- ---------- DÉPARTEMENTS (chacun a son propre manager) ----------
INSERT INTO
    departement (nom_dept, id_responsable)
VALUES
    ('Finance', 3),
    -- id 1
    ('Marketing', 4),
    -- id 2
    ('Production', 5);

-- id 3
-- ---------- POSTES (génériques, rattachés à une direction) ----------
INSERT INTO
    poste (libelle, id_direction, date_creation)
VALUES
    ('Développeur junior', 1, '2020-01-10'),
    -- id 1
    ('Développeur senior', 1, '2020-01-10'),
    -- id 2
    ('Administrateur réseau', 1, '2020-02-03'),
    -- id 3
    ('Comptable', 2, '2019-05-20'),
    -- id 4
    ('Analyste financier', 2, '2021-03-15');

-- id 5
-- ---------- STATUTS DE POSTE ----------
INSERT INTO
    statut_poste (libelle)
VALUES
    ('Occupé'),
    -- id 1
    ('Vacant'),
    -- id 2
    ('Gelé');

-- id 3
-- ---------- POSTES PAR DÉPARTEMENT ----------
-- (id_poste, id_dept, id_statut_poste)
INSERT INTO
    poste_fille (id_poste, id_dept, id_statut_poste)
VALUES
    (1, 1, 1),
    -- id 1 : Dév. junior dans Finance, occupé
    (1, 2, 1),
    -- id 2 : Dév. junior dans Marketing, occupé
    (1, 3, 2),
    -- id 3 : Dév. junior dans Production, vacant
    (2, 1, 1),
    -- id 4 : Dév. senior dans Finance, occupé
    (3, 3, 1),
    -- id 5 : Admin. réseau dans Production, occupé
    (4, 1, 1),
    -- id 6 : Comptable dans Finance, occupé
    (5, 1, 2),
    -- id 7 : Analyste financier dans Finance, vacant
    (4, 2, 1);

-- id 8 : Comptable dans Marketing, occupé
-- ---------- EMPLOYÉS ----------
-- =====================================================================
-- PARTIE 6 : DONNÉES DE RÉFÉRENCE
-- =====================================================================
INSERT INTO
    civilite (libelle)
VALUES
    ('M.'),
    ('Mme'),
    ('Mlle');

INSERT INTO
    situation_famille (libelle)
VALUES
    ('Célibataire'),
    ('Marié(e)'),
    ('Divorcé(e)'),
    ('Veuf/Veuve'),
    ('Union libre');

INSERT INTO
    nationalite (libelle)
VALUES
    ('Malagasy'),
    ('Française'),
    ('Comorienne'),
    ('Mauricienne'),
    ('Chinoise'),
    ('Indienne'),
    ('Autre');

INSERT INTO
    convention_collective (libelle)
VALUES
    ('Aucune convention collective applicable'),
    ('Convention collective de branche (à préciser)'),
    ('Accord d''établissement');

INSERT INTO
    categorie_professionnelle (code, libelle)
VALUES
    ('M1', 'Manœuvre 1'),
    ('M2', 'Manœuvre 2'),
    ('OS1', 'Ouvrier spécialisé 1'),
    ('OS2', 'Ouvrier spécialisé 2'),
    ('OS3', 'Ouvrier spécialisé 3'),
    ('OP1', 'Ouvrier professionnel 1'),
    ('OP2', 'Ouvrier professionnel 2'),
    ('OP3', 'Ouvrier professionnel 3'),
    ('HC1', 'Hors catégorie 1'),
    ('HC2', 'Hors catégorie 2');

INSERT INTO
    langue_contrat (libelle)
VALUES
    ('Français'),
    ('Malagasy');

INSERT INTO
    statut_contrat (libelle)
VALUES
    ('Créé'),
    ('Signé'),
    ('Validé');

INSERT INTO
    motif_cdd (libelle)
VALUES
    ('Remplacement d''un travailleur absent'),
    ('Surcroît temporaire d''activité'),
    ('Travail saisonnier'),
    ('Tâche précise et temporaire');

INSERT INTO
    type_avenant (libelle)
VALUES
    ('Renouvellement'),
    ('Modification');

INSERT INTO
    type_rupture (libelle)
VALUES
    ('Arrivée du terme'),
    ('Rupture pendant l''essai'),
    ('Démission'),
    ('Licenciement'),
    ('Accord des parties'),
    ('Autre');

INSERT INTO
    texte_legal (reference, date_texte, intitule)
VALUES
    (
        'Loi n° 020',
        '1992-09-23',
        'portant code du travail en République du Mali'
    ),
    (
        'Loi n° 99-041',
        '1999-08-12',
        'instituant un Code de Prévoyance sociale'
    ),
    (
        'Loi n° 09-015',
        '2009-06-26',
        'instituant l''Assurance Maladie Obligatoire'
    );