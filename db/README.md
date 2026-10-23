# GRH - Scripts de Base de Données

## Vue d'ensemble

Ce répertoire contient les scripts SQL pour initialiser complètement la base de données GRH (Gestion des Ressources Humaines).

## Scripts disponibles

### 1. `001_schema_complet.sql`
**Crée la structure complète de la base de données**

- Supprime et recrée la base de données `grh`
- Crée toutes les tables nécessaires
- Établit toutes les relations (clés étrangères)
- Crée les index pour optimiser les requêtes

**Contenu:**
- Organisation (rôles, directions, départements, postes, employés)
- Candidats et CV (formations, expériences, langues, compétences)
- Référentiels (nationalité, civilité, sexe, situation familiale, etc.)
- Parties au contrat (employeur, travailleur)
- Contrats (CE, CDD, CDI)
- Recrutement (besoins, candidatures, offres, propositions)
- Utilisateurs et notifications

### 2. `002_donnees_base.sql`
**Insère toutes les données de base nécessaires**

- Civilité (M., Mme, Mlle)
- Sexe (M, F)
- Situation familiale (Célibataire, Marié, etc.)
- Nationalité (7 pays)
- Statut employe, poste, contrat
- Motifs CDD, types de rupture, avenants
- Catégories professionnelles
- Conventions collectives
- Utilisateurs par défaut (admin, RH, DG, Finance, Directeur)
- Données test optionnelles (Direction, département, poste)

## Installation

### Prérequis
- PostgreSQL 12+
- Accès administrateur à PostgreSQL

### Étapes

1. **Créer la structure de la base:**
```bash
psql -U postgres -f 001_schema_complet.sql
```

2. **Insérer les données de base:**
```bash
psql -U postgres -d grh -f 002_donnees_base.sql
```

### Exemple complet
```bash
cd /home/miantra/Documents/S5/Gestion-Entreprise/GRH/GRH/db
psql -U postgres -f 001_schema_complet.sql
psql -U postgres -d grh -f 002_donnees_base.sql
```

## Mots de passe par défaut

Les utilisateurs suivants sont créés avec le mot de passe: **password123**

| Email | Rôle | Utilisé pour |
|-------|------|--------------|
| admin@test.com | ADMIN | Administration générale |
| rh@test.com | RH | Ressources Humaines |
| dg@test.com | DG | Direction générale |
| finance@test.com | FINANCE | Validation des salaires |
| directeur@test.com | DIRECTEUR | Validation des besoins |

⚠️ **IMPORTANT:** Changer tous les mots de passe en production!

## Structure des tables principales

### Recrutement
- **besoin_poste**: Définit les besoins (création/remplacement)
- **candidature**: Candidat répond à un besoin
- **offre_emploi**: Offre publiée par RH
- **proposition_embauche**: Proposition au candidat retenu

### Contrats
- **contrat_ce**: Contrat d'essai
- **contrat_cdd**: Contrat à durée déterminée
- **contrat_cdi**: Contrat à durée indéterminée
- **contrat_candidat**: Lien candidat ↔ contrat

### Parties au contrat
- **travailleur**: Données du travailleur
- **employeur**: Données de l'employeur

## Notes importantes

1. **Sexe du travailleur:**
   - M: Masculin (CIN 6e chiffre = 1)
   - F: Féminin (CIN 6e chiffre = 2)

2. **CIN Format:**
   - 12 chiffres
   - 6e chiffre indique le sexe
   - Exemple femme: 102 092 047 241
   - Exemple homme: 102 091 047 241

3. **Téléphone:**
   - Format: 03XXXXXXXX
   - Commence par 03 suivi de 2, 3, 4, 7 ou 8
   - Exemple: 0323456789, 0324567890

4. **État de la base:**
   - Après script 001: base vide mais structure prête
   - Après script 002: données minimales chargées, prête à utiliser

## Nettoyage

Pour réinitialiser complètement:
```bash
psql -U postgres -c "DROP DATABASE IF EXISTS grh;"
```

## Support

Pour toute question sur la base de données, consulter les commentaires dans les scripts SQL.
