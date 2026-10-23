-- migration_sexe.sql
-- Crée la table sexe et ajoute colonne sexe_id à travailleur

\c grh

-- Créer la table sexe
CREATE TABLE IF NOT EXISTS sexe (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

-- Insérer les données de base
INSERT INTO sexe (libelle) VALUES ('M'), ('F')
ON CONFLICT DO NOTHING;

-- Ajouter colonne sexe_id à travailleur
ALTER TABLE travailleur
ADD COLUMN IF NOT EXISTS sexe_id INT REFERENCES sexe(id);

-- Index pour la recherche
CREATE INDEX IF NOT EXISTS idx_travailleur_sexe ON travailleur(sexe_id);
