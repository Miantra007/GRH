-- =====================================================================
-- GRH : embauche à la validation d'un contrat
--
-- Un contrat doit savoir à quel besoin il répond (création ou remplacement
-- d'un poste) pour que sa validation mette à jour les employés.
--
-- À exécuter APRÈS grh_schema.sql et base_miantra.sql :
--   psql -U postgres -d grh -f db/grh_embauche.sql
-- =====================================================================
\c grh

-- Candidature (donc offre d'emploi, poste et besoin) qui a abouti à ce contrat.
-- NULL pour un contrat créé sans besoin (anciens contrats) : sa validation ne touche pas aux employés.
ALTER TABLE contrat_candidat
    ADD COLUMN IF NOT EXISTS id_candidature INTEGER REFERENCES candidature(id_candidature);

CREATE INDEX IF NOT EXISTS idx_contrat_candidat_candidature ON contrat_candidat(id_candidature);
