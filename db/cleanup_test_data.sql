-- cleanup_test_data.sql
-- Nettoie les données générées lors des tests (besoins, candidatures, offres, etc.)
-- Garde les données de base (responsables, directions, postes, etc.)
-- À exécuter sur la base grh

\c grh

-- Désactiver les contraintes de clés étrangères temporairement
SET session_replication_role = replica;

-- Ordre de suppression : respecter les dépendances de clés étrangères

-- Supprimer les notifications (créées lors des tests)
DELETE FROM notification WHERE id_notification > 0;

-- Supprimer les propositions d'embauche
DELETE FROM proposition_embauche WHERE id_proposition > 0;

-- Supprimer les tests techniques
DELETE FROM test_technique WHERE id_test_technique > 0;

-- Supprimer les entretiens
DELETE FROM entretien WHERE id_entretien > 0;

-- Supprimer les dossiers de candidature et documents
DELETE FROM document_dossier WHERE id_document > 0;
DELETE FROM dossier_candidature WHERE id_dossier > 0;

-- Supprimer les candidatures
DELETE FROM candidature WHERE id_candidature > 0;

-- Supprimer les offres d'emploi
DELETE FROM offre_emploi WHERE id_offre > 0;

-- Supprimer les besoins et postes associés
DELETE FROM besoin_poste WHERE id_besoin_poste > 0;
DELETE FROM besoin WHERE id_besoin > 0;

-- Réactiver les contraintes de clés étrangères
SET session_replication_role = default;

-- Afficher les statistiques
SELECT 'Nettoyage effectué!' as message;
SELECT 'Besoins restants:' as type, COUNT(*) as nombre FROM besoin;
SELECT 'Candidatures restantes:' as type, COUNT(*) as nombre FROM candidature;
SELECT 'Offres restantes:' as type, COUNT(*) as nombre FROM offre_emploi;
SELECT 'Propositions restantes:' as type, COUNT(*) as nombre FROM proposition_embauche;
SELECT 'Notifications restantes:' as type, COUNT(*) as nombre FROM notification;
