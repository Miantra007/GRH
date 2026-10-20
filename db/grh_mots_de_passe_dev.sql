-- DEV UNIQUEMENT : remplace les faux hash du jeu de test par un vrai hash BCrypt.
-- Mot de passe de TOUS les comptes de test : motdepasse123
\c grh
UPDATE responsable SET mot_de_passe = '$2b$12$AL9SLkK5ftyES4VMS/kY7u6HFPsSRN5kwJzOlR/H7jOfXsr0ylsWG';
UPDATE candidat SET mot_de_passe = '$2b$12$AL9SLkK5ftyES4VMS/kY7u6HFPsSRN5kwJzOlR/H7jOfXsr0ylsWG';



CREATE TABLE besoin (
    id_besoin SERIAL PRIMARY KEY,

   id_direction INTEGER NOT NULL REFERENCES direction(id_direction),
id_dept INTEGER NOT NULL REFERENCES departement(id_dept),
id_directeur_demandeur INTEGER NOT NULL REFERENCES responsable(id_responsable),

id_poste INTEGER NOT NULL REFERENCES poste(id_poste),
nombre_postes INTEGER NOT NULL CHECK (nombre_postes > 0),

motif TEXT,
missions TEXT,
type_contrat VARCHAR(3) CHECK (type_contrat IN ('CE', 'CDD', 'CDI')),

salaire_propose_rh NUMERIC(12,0) CHECK (salaire_propose_rh >= 0),
salaire_valide_finance NUMERIC(12,0) CHECK (salaire_valide_finance >= 0),

id_statut_besoin INTEGER NOT NULL REFERENCES statut_besoin(id_statut_besoin),

id_rh_traitant INTEGER REFERENCES responsable(id_responsable),
id_finance_validateur INTEGER REFERENCES responsable(id_responsable),
id_dg_validateur INTEGER REFERENCES responsable(id_responsable),

date_demande TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
date_traitement_rh TIMESTAMP,
date_validation_finance TIMESTAMP,
date_validation_dg TIMESTAMP,
date_publication_client TIMESTAMP,

commentaire_rh TEXT,
commentaire_finance TEXT,
commentaire_dg TEXT,

visible_client BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE statut_besoin (
id_statut_besoin SERIAL PRIMARY KEY,
code VARCHAR(30) NOT NULL UNIQUE,
libelle VARCHAR(100) NOT NULL
);

INSERT INTO statut_besoin (code, libelle) VALUES
('DEMANDE_DIRECTION', 'Demande créée par la direction'),
('TRAITE_RH', 'Traitée par RH'),
('VALIDE_FINANCE', 'Validée par le responsable financier'),
('VALIDE_DG', 'Validée par le directeur général'),
('PUBLIE_CLIENT', 'Publiée côté client'),
('REJETE', 'Rejetée');