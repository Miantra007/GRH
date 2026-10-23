-- migration_langue_nullable.sql
-- Rendre langue_id nullable dans contrat_ce (seul contrat qui a cette colonne)

\c grh

ALTER TABLE contrat_ce ALTER COLUMN langue_id DROP NOT NULL;
