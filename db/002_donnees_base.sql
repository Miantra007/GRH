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
-- DONNÉES TEST (OPTIONNEL - À COMMENTER SI NON NÉCESSAIRE)
-- ============================================================

-- DIRECTION
INSERT INTO direction (nom_direction) VALUES
('Direction générale'),
('Direction technique'),
('Direction commerciale')
ON CONFLICT DO NOTHING;

-- DÉPARTEMENT
INSERT INTO departement (nom_dept) VALUES
('IT'),
('Ressources Humaines'),
('Comptabilité'),
('Ventes')
ON CONFLICT DO NOTHING;

-- POSTE
INSERT INTO poste (libelle, id_direction) VALUES
('Développeur Java', 1),
('Chef de projet', 1),
('Responsable RH', 1),
('Commercial', 1),
('Comptable', 1)
ON CONFLICT DO NOTHING;

-- STATUT EMPLOYE (si non inséré)
INSERT INTO statut_employe (libelle) VALUES
('Stagiaire'),
('CDI'),
('CDD'),
('Temporaire')
ON CONFLICT DO NOTHING;
