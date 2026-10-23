-- =====================================================================
-- GRH : DONNÉES DE BASE
-- Contient toutes les données nécessaires pour démarrer
-- Exécution : psql -U postgres -d grh -f 002_donnees_base.sql
-- =====================================================================

-- ============================================================
-- RÉFÉRENTIELS
-- ============================================================

-- CIVILITÉ
INSERT INTO civilite (libelle) VALUES
('M.'),
('Mme'),
('Mlle')
ON CONFLICT DO NOTHING;

-- SEXE
INSERT INTO sexe (libelle) VALUES
('M'),
('F')
ON CONFLICT DO NOTHING;

-- SITUATION FAMILLE
INSERT INTO situation_famille (libelle) VALUES
('Célibataire'),
('Marié(e)'),
('Divorcé(e)'),
('Veuf/Veuve'),
('Union libre')
ON CONFLICT DO NOTHING;

-- NATIONALITÉ
INSERT INTO nationalite (libelle) VALUES
('Malagasy'),
('Française'),
('Comorienne'),
('Mauricienne'),
('Chinoise'),
('Indienne'),
('Autre')
ON CONFLICT DO NOTHING;

-- STATUT EMPLOYE
INSERT INTO statut_employe (libelle) VALUES
('Actif'),
('Suspendu'),
('Archivé'),
('Congé'),
('Démission')
ON CONFLICT DO NOTHING;

-- STATUT POSTE
INSERT INTO statut_poste (libelle) VALUES
('Disponible'),
('Occupé'),
('Bloqué'),
('Supprimé')
ON CONFLICT DO NOTHING;

-- STATUT CONTRAT
INSERT INTO statut_contrat (libelle) VALUES
('Créé'),
('Signé'),
('Validé'),
('Terminé'),
('Annulé')
ON CONFLICT DO NOTHING;

-- DÉCISION RH
INSERT INTO decision_rh (libelle) VALUES
('ACCEPTEE'),
('REFUSEE'),
('EN_ATTENTE')
ON CONFLICT DO NOTHING;

-- RÉPONSE CANDIDAT
INSERT INTO reponse_candidat (libelle) VALUES
('ACCEPTEE'),
('REFUSEE'),
('EN_ATTENTE'),
('ACCORD_FINAL')
ON CONFLICT DO NOTHING;

-- STATUT PROPOSITION
INSERT INTO statut_proposition (libelle) VALUES
('EN_ATTENTE_CANDIDAT'),
('ACCEPTEE_CANDIDAT'),
('REFUSEE_CANDIDAT'),
('ACCORD_FINAL')
ON CONFLICT DO NOTHING;

-- MOTIF CDD
INSERT INTO motif_cdd (libelle) VALUES
('Remplacement d''un salarié absent'),
('Accroissement temporaire de l''activité'),
('Emploi saisonnier'),
('Contrat de formation'),
('Contrat d''insertion'),
('Projet ou opération déterminée'),
('Autre motif légitime')
ON CONFLICT DO NOTHING;

-- TYPE AVENANT
INSERT INTO type_avenant (libelle) VALUES
('Modification de salaire'),
('Modification de dates'),
('Modification de durée'),
('Modification de lieu'),
('Modification de tâches'),
('Autre modification')
ON CONFLICT DO NOTHING;

-- TYPE RUPTURE
INSERT INTO type_rupture (libelle) VALUES
('Démission'),
('Licenciement'),
('Rupture d''accord'),
('Fin de contrat'),
('Décès'),
('Retraite')
ON CONFLICT DO NOTHING;

-- LANGUE CONTRAT
INSERT INTO langue_contrat (libelle) VALUES
('Français'),
('Anglais'),
('Malgache')
ON CONFLICT DO NOTHING;

-- CATÉGORIE PROFESSIONNELLE
INSERT INTO categorie_professionnelle (code, libelle, indice) VALUES
('OUV1', 'Ouvrier spécialisé 1', 'A1'),
('OUV2', 'Ouvrier spécialisé 2', 'A2'),
('EMP1', 'Employé 1', 'B1'),
('EMP2', 'Employé 2', 'B2'),
('TEC1', 'Technicien 1', 'C1'),
('TEC2', 'Technicien 2', 'C2'),
('CAD1', 'Cadre 1', 'D1'),
('CAD2', 'Cadre 2', 'D2')
ON CONFLICT DO NOTHING;

-- CONVENTION COLLECTIVE
INSERT INTO convention_collective (libelle) VALUES
('Convention nationale du secteur privé'),
('Convention de branche'),
('Accord d''entreprise'),
('Accord particulier')
ON CONFLICT DO NOTHING;

-- NIVEAU COMPETENCE
INSERT INTO niveau_competence (libelle_niveau) VALUES
('Débutant'),
('Intermédiaire'),
('Confirmé'),
('Expert')
ON CONFLICT DO NOTHING;

-- NIVEAU LANGUE
INSERT INTO niveau_langue (libelle_niveau) VALUES
('Débutant'),
('Intermédiaire'),
('Courant'),
('Bilingue'),
('Natif')
ON CONFLICT DO NOTHING;

-- PAYS
INSERT INTO pays (nom_pays) VALUES
('Madagascar'),
('France'),
('Comores'),
('Maurice'),
('Chine'),
('Inde')
ON CONFLICT DO NOTHING;

-- ============================================================
-- UTILISATEURS PAR DÉFAUT
-- ============================================================

-- Mot de passe: password123 (à changer en production!)
INSERT INTO utilisateur (email, mot_de_passe, role, active) VALUES
('admin@test.com', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', 'ADMIN', TRUE),
('rh@test.com', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', 'RH', TRUE),
('dg@test.com', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', 'DG', TRUE),
('finance@test.com', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', 'FINANCE', TRUE),
('directeur@test.com', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', 'DIRECTEUR', TRUE)
ON CONFLICT DO NOTHING;

-- ============================================================
-- ORGANISATION ET HIÉRARCHIE
-- ============================================================

-- RÔLES
INSERT INTO role (libelle) VALUES
('Directeur Général'),
('Directeur'),
('Manager'),
('Responsable RH'),
('Responsable Financier')
ON CONFLICT DO NOTHING;

-- RESPONSABLES (Directeurs et Managers)
-- Directeur Général
INSERT INTO responsable (id_role, id_statut_employe, email, mot_de_passe, date_embauche) VALUES
(1, 1, 'dg@entreprise.mg', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', '2020-01-01');

-- Directeur DSI (Direction Système Information)
INSERT INTO responsable (id_role, id_statut_employe, email, mot_de_passe, date_embauche) VALUES
(2, 1, 'directeur.dsi@entreprise.mg', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', '2020-02-01');

-- Directeur DRH (Direction Ressources Humaines)
INSERT INTO responsable (id_role, id_statut_employe, email, mot_de_passe, date_embauche) VALUES
(2, 1, 'directeur.drh@entreprise.mg', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', '2020-03-01');

-- Directeur DF (Direction Finance)
INSERT INTO responsable (id_role, id_statut_employe, email, mot_de_passe, date_embauche) VALUES
(2, 1, 'directeur.df@entreprise.mg', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', '2020-04-01');

-- Directeur DG (Direction Générale)
INSERT INTO responsable (id_role, id_statut_employe, email, mot_de_passe, date_embauche) VALUES
(2, 1, 'directeur.dg@entreprise.mg', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', '2020-05-01');

-- Manager IT (département IT)
INSERT INTO responsable (id_role, id_statut_employe, email, mot_de_passe, date_embauche) VALUES
(3, 1, 'manager.it@entreprise.mg', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', '2020-06-01');

-- Manager RH (département RH)
INSERT INTO responsable (id_role, id_statut_employe, email, mot_de_passe, date_embauche) VALUES
(4, 1, 'manager.rh@entreprise.mg', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', '2020-07-01');

-- Manager Finance (département Finance)
INSERT INTO responsable (id_role, id_statut_employe, email, mot_de_passe, date_embauche) VALUES
(5, 1, 'manager.finance@entreprise.mg', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', '2020-08-01');

-- Manager Général (département Général)
INSERT INTO responsable (id_role, id_statut_employe, email, mot_de_passe, date_embauche) VALUES
(3, 1, 'manager.general@entreprise.mg', '$2a$10$u/m4.7.OI4fADHXiRmfnWuTCdHg0qrLkXWaI5eCqkpQ.KGRwvWQv2', '2020-09-01');

-- DIRECTION (avec leurs directeurs respectifs)
INSERT INTO direction (nom_direction, id_directeur) VALUES
('DSI - Direction Système Information', 2),
('DRH - Direction Ressources Humaines', 3),
('DF - Direction Finance', 4),
('DG - Direction Générale', 5)
ON CONFLICT DO NOTHING;

-- DÉPARTEMENT (4 départements avec leurs managers)
INSERT INTO departement (nom_dept, id_responsable) VALUES
('IT', 6),                  -- Manager IT
('Ressources Humaines', 7), -- Manager RH
('Finance', 8),             -- Manager Finance
('Général', 9)              -- Manager Général
ON CONFLICT DO NOTHING;

-- POSTE (Postes rattachés à chaque direction)
INSERT INTO poste (libelle, id_direction, date_creation) VALUES
-- DSI
('Développeur Java Senior', 1, CURRENT_DATE),
('Développeur Java Junior', 1, CURRENT_DATE),
('Développeur Frontend', 1, CURRENT_DATE),
('Chef de Projet IT', 1, CURRENT_DATE),
('Administrateur Système', 1, CURRENT_DATE),
-- DRH
('Responsable Recrutement', 2, CURRENT_DATE),
('Responsable Paie', 2, CURRENT_DATE),
('Agent RH', 2, CURRENT_DATE),
-- DF
('Comptable Senior', 3, CURRENT_DATE),
('Comptable Junior', 3, CURRENT_DATE),
('Responsable Trésorier', 3, CURRENT_DATE),
-- DG
('Directeur Général', 4, CURRENT_DATE),
('Assistant Direction', 4, CURRENT_DATE),
('Agent Administratif', 4, CURRENT_DATE)
ON CONFLICT DO NOTHING;

-- STATUT POSTE
INSERT INTO statut_poste (libelle) VALUES
('Disponible'),
('Occupé'),
('Bloqué')
ON CONFLICT DO NOTHING;

-- POSTE_FILLE (Instances de postes dans chaque département)
INSERT INTO poste_fille (id_poste, id_dept, id_statut_poste) VALUES
-- IT
(1, 1, 2),  -- Dev Java Senior - IT - Occupé
(2, 1, 2),  -- Dev Java Junior - IT - Occupé
(2, 1, 2),  -- Dev Java Junior - IT - Occupé (2ème personne)
(3, 1, 1),  -- Dev Frontend - IT - Disponible
(4, 1, 1),  -- Chef de Projet - IT - Disponible
(5, 1, 1),  -- Admin Système - IT - Disponible
-- RH
(6, 2, 2),  -- Recrutement - RH - Occupé
(7, 2, 2),  -- Paie - RH - Occupé
(8, 2, 1),  -- Agent RH - RH - Disponible
-- Finance
(9, 3, 2),  -- Comptable Senior - Finance - Occupé
(10, 3, 2), -- Comptable Junior - Finance - Occupé
(11, 3, 1), -- Trésorier - Finance - Disponible
-- Général
(12, 4, 2), -- DG - Général - Occupé
(13, 4, 1), -- Assistant Direction - Général - Disponible
(14, 4, 1)  -- Agent Administratif - Général - Disponible
ON CONFLICT DO NOTHING;
