-- grh_integration.sql
-- =====================================================================
-- GRH : consignes d'intégration envoyées par RH au candidat
--   (formations à suivre, compte bancaire à créer, matériel à utiliser, ...)
--
-- À exécuter APRÈS grh_schema.sql :
--   psql -U postgres -d grh -f db/grh_integration.sql
-- =====================================================================
\c grh

-- ---------- RÉFÉRENTIEL : types de consigne ----------
CREATE TABLE type_consigne (
    id_type_consigne SERIAL PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    libelle VARCHAR(100) NOT NULL
);

INSERT INTO type_consigne (code, libelle) VALUES
    ('FORMATION', 'Formation à suivre'),
    ('COMPTE_BANCAIRE', 'Compte bancaire à créer'),
    ('MATERIEL', 'Matériel à utiliser'),
    ('DOCUMENT', 'Document à fournir'),
    ('AUTRE', 'Autre consigne');

-- ---------- CONSIGNES ----------
-- Une ligne = une chose que le candidat doit faire.
-- date_realisation NULL = à faire ; renseignée = le candidat l'a marquée comme faite.
CREATE TABLE consigne_integration (
    id_consigne SERIAL PRIMARY KEY,
    id_candidat INTEGER NOT NULL REFERENCES candidat(id_candidat) ON DELETE CASCADE,
    id_contrat_candidat INTEGER REFERENCES contrat_candidat(id_contrat_candidat) ON DELETE SET NULL,
    id_type_consigne INTEGER NOT NULL REFERENCES type_consigne(id_type_consigne),
    titre VARCHAR(150) NOT NULL,
    description TEXT,
    date_limite DATE,
    id_rh_emetteur INTEGER REFERENCES responsable(id_responsable),
    date_envoi TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_realisation TIMESTAMP,
    commentaire_candidat TEXT
);

CREATE INDEX idx_consigne_candidat ON consigne_integration(id_candidat);

-- ---------- DONNÉES DE TEST (candidat hery.rabe@mail.mg, envoyées par rh@entreprise.mg) ----------
-- N'insère rien si ces comptes n'existent pas (jeu de test non chargé).
INSERT INTO consigne_integration (id_candidat, id_type_consigne, titre, description, date_limite, id_rh_emetteur)
SELECT c.id_candidat, t.id_type_consigne, v.titre, v.description, CURRENT_DATE + v.delai, r.id_responsable
FROM (VALUES
        ('FORMATION', 'Formation sécurité au travail', 'Session de 2 h en salle B, le matin du premier jour.', 7),
        ('COMPTE_BANCAIRE', 'Ouvrir un compte bancaire', 'Le salaire est versé par virement : transmettez votre RIB au service RH.', 14),
        ('MATERIEL', 'Ordinateur portable et badge', 'À récupérer auprès du service informatique, bureau 104.', 3)
     ) AS v(code, titre, description, delai)
JOIN type_consigne t ON t.code = v.code
JOIN candidat c ON c.email = 'hery.rabe@mail.mg'
JOIN responsable r ON r.email = 'rh@entreprise.mg';
