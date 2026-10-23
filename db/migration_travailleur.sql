-- migration_travailleur.sql
-- Ajoute la colonne id_candidat à la table travailleur pour lier les travailleurs aux candidats

\c grh

-- Ajouter colonne id_candidat à travailleur
ALTER TABLE travailleur
ADD COLUMN id_candidat INT REFERENCES candidat(id_candidature) ON DELETE CASCADE;

-- Index pour améliorer les requêtes par candidat
CREATE INDEX idx_travailleur_candidat ON travailleur(id_candidat);

COMMIT;
