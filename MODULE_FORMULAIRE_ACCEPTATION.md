# Module « Formulaire d'acceptation » (proposition d'embauche)

## 1. À quoi sert le module

Quand le RH retient un candidat après l'entretien, le système crée une **proposition d'embauche**.
Le candidat la reçoit sous forme d'un **formulaire de 5 critères** pré-remplis avec ce que le RH a décidé lors du besoin :

| Code critère | Libellé affiché | Valeur proposée (source : `besoin_poste`) |
|---|---|---|
| `SALAIRE` | Salaire mensuel | `salaire_valide_finance` (sinon `salaire_propose_rh`) |
| `HORAIRE` | Horaire de travail | `horaire_travail` |
| `RESPONSABLE` | Responsable hiérarchique | `responsable_hierarchique` |
| `TRAVAIL` | Travail à effectuer | `missions` |
| `DATE_PRISE_POSTE` | Date de prise de poste | `date_prise_poste` |

Pour chaque critère le candidat répond **Oui**, **Non** ou **Autre** (avec sa contre-proposition), puis choisit une action :

| Action | Condition | Résultat (statut de la proposition) |
|---|---|---|
| **Valider** | tous les critères à « Oui » | `ACCORD_FINAL` → le candidat peut choisir son contrat |
| **Attendre** | au moins un « Autre » | `EN_ATTENTE_RH` → le RH doit accepter ou refuser la contre-proposition |
| **Refuser** | au moins un « Non » et aucun « Autre » | `REFUSEE_CANDIDAT` → fin du recrutement |

Si le RH **accepte** la contre-proposition → `ACCORD_FINAL` (le salaire demandé devient le salaire final).
S'il la **refuse** → `REFUSEE_RH`.
À chaque étape, une **notification** est envoyée à l'autre partie.

## 2. Déroulement complet

```
RH : entretien → « Accepter »            (AdminCandidatureWebController.decisionEntretien)
        │  candidature = RETENUE, proposition créée (EN_ATTENTE_CANDIDAT)
        ▼
Candidat : notification « Candidature retenue » → bouton « Remplir le formulaire d'acceptation »
        │  GET /candidats/{idCandidat}/propositions/{idProposition}
        ▼
Candidat : répond aux 5 critères → Valider / Attendre / Refuser
        │  POST /candidats/{idCandidat}/propositions/{idProposition}
        ├── Valider  → ACCORD_FINAL      → notif RH + notif candidat « choisir mon contrat »
        ├── Refuser  → REFUSEE_CANDIDAT  → notif RH
        └── Attendre → EN_ATTENTE_RH     → notif RH « Contre-proposition à examiner »
                          │  GET  /rh/propositions/{idProposition}
                          │  POST /rh/propositions/{idProposition}/decision
                          ├── Valider → ACCORD_FINAL → notif candidat
                          └── Refuser → REFUSEE_RH   → notif candidat
        ▼
ACCORD_FINAL : module Contrat (/candidat/contrats/nouveau?propositionId=…)
```

## 3. Fichiers du module et leur rôle

### 3.1 Contrôleurs (pages web)

| Fichier | Rôle |
|---|---|
| `src/main/java/mg/grh/controller/PropositionCandidatController.java` | **Côté candidat.** `GET` affiche le formulaire, `POST` enregistre la réponse. Vérifie que le candidat connecté est bien le propriétaire de la proposition (sinon renvoie vers `/login?refuse`). |
| `src/main/java/mg/grh/controller/PropositionRhController.java` | **Côté RH.** `GET /rh/propositions/{id}` affiche la réponse du candidat ; `POST …/decision` enregistre la décision RH sur une contre-proposition. Seul le **RH traitant** du besoin y a accès. |
| `src/main/java/mg/grh/controller/NotificationController.java` | Pages de notifications `/candidats/{id}/notifications` et `/rh/{id}/notifications`. Marque les notifications comme lues (vide le badge du menu). |

### 3.2 Service (règles métier)

| Fichier | Rôle |
|---|---|
| `src/main/java/mg/grh/service/PropositionService.java` | Cœur du module :<br>• `creerPourCandidature` : crée la proposition (statut `EN_ATTENTE_CANDIDAT`, salaire initial du besoin) quand le RH retient le candidat ;<br>• `vueCandidat` / `vueRh` : construit l'écran (critères + valeurs proposées + réponses déjà données) ;<br>• `soumettre` : contrôle les réponses et applique Valider / Attendre / Refuser ;<br>• `decider` : décision RH sur une contre-proposition ;<br>• `assurerCriteres` : crée les 5 lignes de critères si elles n'existent pas encore ;<br>• envoi des notifications au RH et au candidat. |

### 3.3 Objets d'échange (DTO) et exception

| Fichier | Rôle |
|---|---|
| `src/main/java/mg/grh/dto/ReponseForm.java` | Données envoyées par le formulaire : `action` (VALIDER/ATTENDRE/REFUSER), `reponse[code]` (OUI/NON/AUTRE), `contre[code]` (texte de la contre-proposition). |
| `src/main/java/mg/grh/dto/FormulaireVue.java` | Tout ce que la page affiche : candidat, offre, statut lisible, formulaire modifiable ou non, attente RH, commentaire RH, liste des critères. |
| `src/main/java/mg/grh/dto/CritereLigne.java` | Une ligne du formulaire : code, libellé, valeur proposée, réponse, contre-proposition. |
| `src/main/java/mg/grh/exception/RegleMetierException.java` | Erreur métier (ex. « Valider n'est possible que si tous les critères sont acceptés ») affichée en message rouge. |

### 3.4 Modèles (entités JPA)

| Fichier | Table | Rôle |
|---|---|---|
| `model/PropositionEmbauche.java` | `proposition_embauche` | La proposition : candidature, date d'envoi, réponse globale du candidat, contre-proposition, salaire demandé, décision et commentaire RH, salaire final, statut. |
| `model/CritereProposition.java` | `critere_proposition` | Une ligne par critère et par proposition : type, réponse (OUI/NON/AUTRE), contre-proposition, date de réponse. |
| `model/TypeCritereProposition.java` | `type_critere_proposition` | Référentiel des 5 critères (SALAIRE, HORAIRE, RESPONSABLE, TRAVAIL, DATE_PRISE_POSTE). |
| `model/ReponseCritere.java` | `reponse_critere` | Référentiel OUI / NON / AUTRE pour un critère. |
| `model/ReponseCandidat.java` | `reponse_candidat` | Réponse globale du candidat : OUI / NON / AUTRE. |
| `model/StatutProposition.java` | `statut_proposition` | EN_ATTENTE_CANDIDAT, EN_ATTENTE_RH, ACCORD_FINAL, REFUSEE_CANDIDAT, REFUSEE_RH. |
| `model/DecisionRh.java` | `decision_rh` | ACCEPTEE / REFUSEE (décision du RH sur la contre-proposition). |
| `model/Notification.java` | `notification` | Message pour un candidat (`id_candidat`) ou un responsable (`id_responsable`), lié à la proposition (`id_proposition`). Méthode `Notification.pour(...)`. |
| `model/PropositionInfo.java` | — (projection) | Résultat de la requête `findInfo` : candidat, offre, RH traitant et valeurs du besoin (salaire, horaire, responsable, missions, date). |

### 3.5 Repositories (accès base)

| Fichier | Rôle |
|---|---|
| `repository/PropositionEmbaucheRepository.java` | `findByCandidatureId`, `findAccordFinal` (utilisé par le module Contrat) et `findInfo` (requête SQL qui joint proposition → candidature → candidat → offre → besoin_poste → besoin). |
| `repository/CritereRepository.java` | Critères d'une proposition triés par type. |
| `repository/TypeCritereRepository.java` | Liste des types de critère. |
| `repository/ReponseCritereRepository.java` | Recherche OUI/NON/AUTRE par libellé. |
| `repository/ReponseCandidatRepository.java` | Idem pour la réponse globale. |
| `repository/StatutPropositionRepository.java` | Recherche d'un statut par libellé. |
| `repository/DecisionRhRepository.java` | ACCEPTEE / REFUSEE. |
| `repository/NotificationRepository.java` | Notifications d'un candidat ou d'un RH + compteurs de non lues (badge). |

### 3.6 Pages (Thymeleaf)

| Fichier | Rôle |
|---|---|
| `src/main/resources/templates/proposition/formulaire.html` | Formulaire du candidat : une ligne par critère avec Oui / Non / Autre et un champ de contre-proposition qui apparaît sur « Autre ». Le JavaScript active seulement le bouton autorisé (Valider, Attendre ou Refuser) selon les réponses. Lecture seule une fois la réponse envoyée. |
| `src/main/resources/templates/proposition/rh-decision.html` | Écran du RH : réponses du candidat critère par critère, contre-propositions, et boutons Valider / Refuser avec commentaire (visible seulement si `EN_ATTENTE_RH`). |
| `src/main/resources/templates/proposition/notifications.html` | Liste des notifications avec le bouton « Ouvrir la proposition » (candidat) ou « Examiner la réponse » (RH). |

### 3.7 Base de données

| Fichier | Rôle |
|---|---|
| `db/base_miantra.sql` | Crée les tables du module : `reponse_candidat`, `decision_rh`, `statut_proposition`, `proposition_embauche`, `notification`, `type_critere_proposition`, `reponse_critere`, `critere_proposition`, et insère leurs valeurs de référence. |
| `db/donnee_test_acceptation.sql` | Jeu de test complet : besoin, offre, candidature de Hery Rabe, proposition d'embauche et ses 5 critères. |
| `db/migration_validation_test.sql` | Ajoute aussi les types de critère (SALAIRE, HORAIRE, …) s'ils manquent. |
| `db/grh_mots_de_passe_dev.sql` | Met le mot de passe `motdepasse123` sur tous les comptes de test pour pouvoir se connecter. |

## 4. Fichiers d'autres modules reliés au formulaire

| Fichier | Lien avec le module |
|---|---|
| `controller/web/AdminCandidatureWebController.java` | `decisionEntretien` (« Accepter » après entretien) passe la candidature en `RETENUE`, appelle `PropositionService.creerPourCandidature` et notifie le candidat. |
| `controller/web/CandidatNotificationWebController.java` + `templates/candidat/notifications.html` | Espace candidat : affiche le bon bouton selon l'état — « Remplir le formulaire d'acceptation » (`EN_ATTENTE_CANDIDAT`), « Choisir mon contrat » (`ACCORD_FINAL` sans contrat), « Voir mon contrat », puis « Voir mon intégration » une fois le contrat validé. |
| `controller/web/CandidatContratWebController.java`, `service/ContratCandidatService.java` | Module Contrat : n'accepte de créer un contrat que pour une proposition en `ACCORD_FINAL` (`findAccordFinal`). |
| `config/WebConfig.java` / `web/AuthInterceptor.java` | Les routes `/candidats/**` et `/rh/**` exigent une connexion. |

## 5. URLs

| URL | Qui | Effet |
|---|---|---|
| `GET /candidats/{idCandidat}/propositions/{idProposition}` | Candidat propriétaire | Voir / remplir le formulaire |
| `POST /candidats/{idCandidat}/propositions/{idProposition}` | Candidat propriétaire | Envoyer Valider / Attendre / Refuser |
| `GET /rh/propositions/{idProposition}` | RH traitant du besoin | Voir la réponse du candidat |
| `POST /rh/propositions/{idProposition}/decision` | RH traitant du besoin | Accepter / refuser la contre-proposition |
| `GET /candidats/{id}/notifications` | Candidat | Notifications du candidat |
| `GET /rh/{id}/notifications` | RH | Notifications du RH |

## 6. Règles de sécurité et de cohérence

- Un candidat ne peut ouvrir que **ses** propositions ; un RH ne peut décider que sur les propositions des besoins **qu'il traite**.
- La réponse n'est acceptée que si la proposition est encore `EN_ATTENTE_CANDIDAT` (impossible de répondre deux fois).
- La décision RH n'est acceptée que si la proposition est `EN_ATTENTE_RH`.
- Une contre-proposition sur le **salaire** doit être un nombre positif ; elle est enregistrée dans `salaire_demande`.
- Le salaire final vaut : le salaire du besoin (Valider), ou le salaire demandé si le RH accepte la contre-proposition.
