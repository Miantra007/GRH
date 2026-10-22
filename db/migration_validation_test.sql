-- Migration requise pour enregistrer l'acceptation de convocation et la décision RH sur le test.
-- À exécuter une fois sur une base existante (peut être relancée sans risque).
\c grh

ALTER TABLE public.test_technique
    ADD COLUMN IF NOT EXISTS accepte_candidat BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.test_technique
    ADD COLUMN IF NOT EXISTS resultat_accepte BOOLEAN;

INSERT INTO public.type_critere_proposition (libelle)
VALUES ('SALAIRE'), ('HORAIRE'), ('RESPONSABLE'), ('TRAVAIL'), ('DATE_PRISE_POSTE')
ON CONFLICT (libelle) DO NOTHING;

INSERT INTO public.reponse_critere (libelle)
VALUES ('OUI'), ('NON'), ('AUTRE')
ON CONFLICT (libelle) DO NOTHING;
