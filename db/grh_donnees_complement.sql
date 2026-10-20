-- grh_donnees_complement.sql
-- À exécuter APRÈS grh_donnees_test.sql : données minimales pour tester le workflow des contrats
\c grh

-- Rôle + compte "Direction générale" (celui qui valide les contrats)
INSERT INTO role (libelle) VALUES ('Direction générale');
INSERT INTO responsable (id_role, email, mot_de_passe, date_embauche)
VALUES ((SELECT id_role FROM role WHERE libelle = 'Direction générale'),
        'dg@entreprise.mg', 'hash_factice_7', '2012-01-02');      -- id 7

INSERT INTO employeur (raison_sociale, forme_juridique, adresse_siege, representant_nom, representant_fonction, civilite_id)
VALUES ('Entreprise Demo SARL', 'SARL', 'Antananarivo', 'Rakoto Jean', 'Directeur Général', 1);   -- id 1

INSERT INTO candidat (nom_candidat, prenom_candidat, email, mot_de_passe, dtn)
VALUES ('Rabe', 'Hery', 'hery.rabe@mail.mg', 'hash_factice_8', '1999-05-12');                       -- id 1

INSERT INTO travailleur (civilite_id, nom_prenoms, date_naissance, lieu_naissance, nationalite_id, situation_famille_id, profession, email)
VALUES (1, 'Rabe Hery', '1999-05-12', 'Antananarivo', 1, 1, 'Développeur', 'hery.rabe@mail.mg');   -- id 1
