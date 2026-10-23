-- migration_langue_nullable.sql
-- Rendre langue_id nullable dans tous les contrats

\c grh

-- Rendre langue_id nullable dans contrat_ce
ALTER TABLE contrat_ce ALTER COLUMN langue_id DROP NOT NULL;

-- Rendre langue_id nullable dans contrat_cdd si la colonne existe
ALTER TABLE contrat_cdd ALTER COLUMN langue_id DROP NOT NULL;

-- Rendre langue_id nullable dans contrat_cdi si la colonne existe
ALTER TABLE contrat_cdi ALTER COLUMN langue_id DROP NOT NULL;
