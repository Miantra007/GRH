# Travail effectué — tâche « Toi » du flux contrat

Périmètre suivi depuis `relier.md` et l’analyse jointe : raccorder l’acceptation à la création du contrat, faire signer le candidat, faire valider le contrat par le RH, envoyer les notifications et renvoyer vers le module Intégration. La création de l’employé n’a pas été modifiée.

## Changements réalisés

| Fichier | Modification et raison |
| --- | --- |
| `src/main/java/mg/grh/service/PropositionService.java` | Le fichier avait déjà une modification locale non validée qui notifie le candidat quand il accepte tous les critères. Elle a été conservée. Le lien vers le contrat est fourni par la page Notifications. |
| `src/main/resources/templates/proposition/notifications.html` | Ajout des actions « Choisir mon contrat » après accord final et « Voir Mon intégration » après validation RH. |
| `src/main/resources/templates/fragments.html` | Ajout des liens vers les notifications du candidat et du RH connecté. |
| `src/main/java/mg/grh/controller/NotificationController.java` | Les notifications ne sont accessibles qu’à leur destinataire connecté. |
| `src/main/java/mg/grh/controller/PropositionCandidatController.java` | La session candidat doit correspondre au candidat de l’URL pour consulter ou soumettre le formulaire. |
| `src/main/java/mg/grh/controller/PropositionRhController.java` | Accès RH protégé et limité au RH responsable de la proposition. |
| `src/main/java/mg/grh/service/PropositionService.java` | Ajout du contrôle permettant au contrôleur de vérifier le RH responsable. |
| `src/main/java/mg/grh/config/WebConfig.java`, `src/main/java/mg/grh/web/AuthInterceptor.java` | Protection des routes `/api/**`, `/candidats/**` et `/rh/**`, en plus des espaces déjà protégés. Les API sont réservées au RH. |
| `src/main/java/mg/grh/model/ContratCandidat.java`, `src/main/java/mg/grh/repository/ContratCandidatRepository.java`, `src/main/java/mg/grh/repository/PropositionEmbaucheRepository.java` | Ajout du lien unique entre contrat et proposition, et de la recherche d’une proposition au statut `ACCORD_FINAL`. |
| `db/grh_integration.sql` | Ajout de la colonne `contrat_candidat.id_proposition` avec clé étrangère et unicité : une proposition ne peut produire qu’un contrat. |
| `src/main/java/mg/grh/controller/web/CandidatContratWebController.java` | Ajout de l’entrée `/candidat/contrats/nouveau` et des trois formulaires de création. Le candidat est pris depuis la session, jamais depuis un identifiant envoyé par le navigateur. |
| `src/main/resources/templates/admin/contrat-form.html` | Réutilisation du formulaire pour le candidat; salaire, poste et date issus de la proposition sont affichés en lecture seule. Le candidat choisit CE, CDD ou CDI. |
| `src/main/java/mg/grh/service/ContratCandidatService.java` | Création uniquement depuis une proposition acceptée appartenant au candidat, refus des doublons, reprise côté serveur du salaire final et des conditions disponibles, vérification que le dossier travailleur correspond à l’email du candidat, signature avec notification au RH et validation par le RH responsable avec notification au candidat. |
| `src/main/java/mg/grh/repository/TravailleurRepository.java`, `src/main/java/mg/grh/repository/PosteRepository.java` | Ajout des recherches nécessaires pour vérifier le dossier travailleur et retrouver le poste de la proposition lors d’un CDD. |
| `src/main/java/mg/grh/controller/web/AdminContratWebController.java` | Ajout de la validation RH, limitée au RH associé à la proposition. |
| `src/main/java/mg/grh/controller/web/DgContratWebController.java`, `src/main/java/mg/grh/controller/ContratCandidatController.java` | Retrait des anciennes actions de validation DG et de signature candidat via l’API. La signature passe par l’espace candidat; la validation passe par l’espace RH. |
| `src/main/resources/templates/contrat-detail.html` | Affichage de l’action de validation au RH et mise à jour du texte du cycle de signature. |
| `src/main/resources/templates/admin/candidats.html` | Retrait de l’ancien raccourci RH de création de contrat sans proposition acceptée. |
| `fichier.md` | Journal demandé des changements et de leurs prérequis. |

## Parcours raccordé

1. Après accord final, le candidat ouvre ses notifications et clique sur « Choisir mon contrat ».
2. Il choisit le type de contrat. La création est refusée si la proposition n’est pas acceptée, ne lui appartient pas ou possède déjà un contrat.
3. Le candidat signe le contrat depuis « Mes contrats ».
4. Le RH responsable reçoit une notification et valide le contrat depuis sa page de détail.
5. Le candidat reçoit une notification qui renvoie vers « Mon intégration ».

## Prérequis et limites

- Le schéma doit contenir `contrat_candidat.id_proposition`. Pour une nouvelle base, exécuter les scripts dans l’ordre indiqué par `execution_BASE.md`. Pour une base existante, appliquer l’`ALTER TABLE` ajouté à `db/grh_integration.sql`; ce script contient aussi des créations de tables et données de démonstration et ne doit pas être rejoué aveuglément sur une base déjà intégrée.
- Un dossier `travailleur` avec le même email que le candidat doit déjà exister. La création automatique d’un employé est hors périmètre; la création automatique d’un dossier travailleur n’a pas été ajoutée non plus. Sans ce dossier, le candidat voit le message de prérequis et la création est refusée.
- Pour le CDD, la date de fin et les champs contractuels non contenus dans la proposition restent à renseigner dans le formulaire. Le salaire, le poste et la date de début sont contrôlés côté serveur.
- Le lien vers « Mon intégration » réutilise le module existant; les consignes restent à créer par le RH dans ce module.
- Le projet n’a pas été lancé et les scripts SQL n’ont pas été exécutés. Aucun test n’a été ajouté ou lancé.
