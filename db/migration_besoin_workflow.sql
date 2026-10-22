-- À exécuter une fois sur les bases déjà créées.
ALTER TABLE besoin_poste ADD COLUMN IF NOT EXISTS decision_finance VARCHAR(10);
ALTER TABLE besoin_poste ADD COLUMN IF NOT EXISTS commentaire_finance TEXT;
ALTER TABLE besoin_poste ADD COLUMN IF NOT EXISTS decision_dg VARCHAR(10);
ALTER TABLE besoin_poste DROP CONSTRAINT IF EXISTS ck_besoin_poste_decision_finance;
ALTER TABLE besoin_poste ADD CONSTRAINT ck_besoin_poste_decision_finance
    CHECK (decision_finance IS NULL OR decision_finance IN ('ACCEPTEE', 'REFUSEE'));
ALTER TABLE besoin_poste DROP CONSTRAINT IF EXISTS ck_besoin_poste_decision_dg;
ALTER TABLE besoin_poste ADD CONSTRAINT ck_besoin_poste_decision_dg
    CHECK (decision_dg IS NULL OR decision_dg = 'VALIDEE');
