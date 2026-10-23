# Fiche fonctionnelle — Intégration

## Objectif

Le module d'intégration permet au RH d'envoyer au candidat les actions à réaliser avant ou à son arrivée : formation, ouverture de compte bancaire, récupération de matériel, documents à fournir, etc. Le candidat consulte ces actions et indique celles qui sont terminées.

## Parcours utilisateur

```text
RH → crée et envoie une consigne → candidat
                                  ↓
                    marque la consigne faite / à faire
                                  ↓
                         RH consulte l'avancement
```

### Côté RH

1. Depuis la liste des candidats, le RH ouvre **Intégration** pour un candidat.
2. Il choisit le type de consigne, renseigne un titre (obligatoire), et peut ajouter une description, une échéance et le contrat concerné.
3. La consigne est enregistrée avec le RH émetteur et la date d'envoi.
4. Le RH peut consulter l'état des consignes et retirer une consigne.

| Fonction | URL | Emplacement du code |
|---|---|---|
| Afficher l'intégration d'un candidat | `GET /admin/candidats/{id}/integration` | `src/main/java/mg/grh/controller/web/AdminIntegrationWebController.java` — `consignes` |
| Envoyer une consigne | `POST /admin/candidats/{id}/integration` | même fichier — `envoyer` |
| Retirer une consigne | `POST /admin/integration/{consigneId}/supprimer` | même fichier — `supprimer` |
| Formulaire et liste RH | — | `src/main/resources/templates/admin/integration.html` |

### Côté candidat

1. Le candidat ouvre **Mon intégration**.
2. Les consignes sont séparées entre **à faire** et **faites**.
3. Il peut marquer une consigne comme faite, en ajoutant éventuellement un commentaire.
4. Il peut aussi remettre une consigne à faire.

| Fonction | URL | Emplacement du code |
|---|---|---|
| Voir ses consignes | `GET /candidat/integration` | `src/main/java/mg/grh/controller/web/CandidatIntegrationWebController.java` — `mesConsignes` |
| Marquer comme faite | `POST /candidat/integration/{id}/faite` | même fichier — `faite` |
| Remettre à faire | `POST /candidat/integration/{id}/rouvrir` | même fichier — `rouvrir` |
| Page candidat | — | `src/main/resources/templates/candidat/integration.html` |

## Règles métier et sécurité

- Le titre est obligatoire.
- Le contrat est facultatif, mais s'il est sélectionné il doit appartenir au candidat choisi.
- Une consigne ne peut être validée ou rouverte que par son destinataire ; toute tentative sur la consigne d'un autre candidat lève une erreur de sécurité.
- Les URLs RH exigent le rôle `RH` et les URLs candidat le rôle `CANDIDAT`.

| Responsabilité | Fichier |
|---|---|
| Création, contrôle du contrat, réalisation et réouverture | `src/main/java/mg/grh/service/ConsigneIntegrationService.java` |
| Contrôle des rôles et de la session | `src/main/java/mg/grh/web/AuthInterceptor.java` |
| Déclaration des chemins protégés | `src/main/java/mg/grh/config/WebConfig.java` |

## Données enregistrées

L'entité `ConsigneIntegration` est définie dans `src/main/java/mg/grh/model/ConsigneIntegration.java` et est stockée dans `consigne_integration`.

| Champ | Utilité |
|---|---|
| `id_candidat` | Destinataire de la consigne |
| `id_contrat_candidat` | Contrat lié, facultatif |
| `id_type_consigne` | Catégorie : formation, matériel, document, etc. |
| `titre`, `description`, `date_limite` | Contenu et échéance |
| `id_rh_emetteur`, `date_envoi` | Traçabilité de l'envoi |
| `date_realisation`, `commentaire_candidat` | Avancement et retour du candidat |

Les types configurables sont dans `type_consigne`, via `src/main/java/mg/grh/model/TypeConsigne.java` et `src/main/java/mg/grh/service/TypeConsigneService.java`.

## API REST

Les contrôleurs REST génériques associés sont `src/main/java/mg/grh/controller/ConsigneIntegrationController.java` et `src/main/java/mg/grh/controller/TypeConsigneController.java`. Les routes `/api/**` sont protégées par l'intercepteur, sauf les routes publiques explicitement exclues dans `WebConfig`.
