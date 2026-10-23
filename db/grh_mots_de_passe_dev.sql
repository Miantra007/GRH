-- DEV UNIQUEMENT : remplace les faux hash du jeu de test par un vrai hash BCrypt.
-- Mot de passe de TOUS les comptes de test : motdepasse123
\c grh
UPDATE
    responsable
SET
    mot_de_passe = '$2b$12$AL9SLkK5ftyES4VMS/kY7u6HFPsSRN5kwJzOlR/H7jOfXsr0ylsWG';

UPDATE
    candidat
SET
    mot_de_passe = '$2b$12$AL9SLkK5ftyES4VMS/kY7u6HFPsSRN5kwJzOlR/H7jOfXsr0ylsWG';

