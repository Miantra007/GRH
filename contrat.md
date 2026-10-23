# Fiche fonctionnelle — Contrats et embauche

## Objectif

Le module gère les contrats d'essai (CE), les CDD et les CDI. Il suit leur cycle de vie, conserve l'historique des changements et, à la validation, met à jour les employés selon le besoin de recrutement associé.

## Cycle de vie

```text
Créé → Signé → Validé → embauche ou remplacement enregistré
  RH       Candidat     RH
```

Chaque transition est ajoutée à l'historique du contrat. Un contrat doit être **signé** avant de pouvoir être validé.

## Fonctions par rôle

### RH

Le RH consulte les candidats, crée un CE, CDD ou CDI, consulte les détails et l'historique, puis valide le contrat signé. Pour une validation liée à une proposition, seul le RH traitant de cette proposition peut valider.

| Fonction | URL | Emplacement du code |
|---|---|---|
| Liste des candidats | `GET /admin/candidats` | `src/main/java/mg/grh/controller/web/AdminContratWebController.java` — `candidats` |
| Liste et filtre des contrats | `GET /admin/contrats` | même fichier — `liste` |
| Détail, historique et aperçu d'embauche | `GET /admin/contrats/{id}` | même fichier — `detail` |
| Formulaire de création | `GET /admin/contrats/nouveau` | même fichier — `nouveau` |
| Créer CE / CDD / CDI | `POST /admin/contrats/ce`, `/cdd`, `/cdi` | même fichier — `creerCe`, `creerCdd`, `creerCdi` |
| Valider un contrat signé | `POST /admin/contrats/{id}/valider` | même fichier — `valider` |
| Créer un travailleur | `GET /admin/travailleurs/nouveau`, `POST /admin/travailleurs` | même fichier — `nouveauTravailleur`, `creerTravailleur` |

Les vues se trouvent dans `src/main/resources/templates/admin/contrats.html`, `admin/contrat-form.html` et `contrat-detail.html`.

### Candidat

Le candidat peut consulter ses contrats, consulter leur détail et signer uniquement ses propres contrats. Il peut aussi créer un contrat depuis une proposition à l'état d'accord final.

| Fonction | URL | Emplacement du code |
|---|---|---|
| Liste de ses contrats | `GET /candidat/contrats` | `src/main/java/mg/grh/controller/web/CandidatContratWebController.java` — `mesContrats` |
| Détail d'un contrat | `GET /candidat/contrats/{id}` | même fichier — `detail` |
| Signer | `POST /candidat/contrats/{id}/signer` | même fichier — `signer` |
| Formulaire depuis une proposition | `GET /candidat/contrats/nouveau` | même fichier — `nouveau` |
| Créer CE / CDD / CDI depuis cette proposition | `POST /candidat/contrats/ce`, `/cdd`, `/cdi` | même fichier |

Les vues sont `src/main/resources/templates/candidat/contrats.html` et `contrat-detail.html`.

### Direction générale

La DG dispose d'une consultation des contrats à valider ou de tous les contrats, avec le besoin associé et le résultat de l'embauche. Dans le code actuel, elle ne réalise pas la validation : celle-ci est effectuée par le RH.

| Fonction | URL | Emplacement du code |
|---|---|---|
| Liste des contrats | `GET /dg/contrats` | `src/main/java/mg/grh/controller/web/DgContratWebController.java` — `liste` |
| Détail et aperçu d'embauche | `GET /dg/contrats/{id}` | même fichier — `detail` |

Vue : `src/main/resources/templates/dg/contrats.html`.

## Règles métier principales

| Règle | Emplacement |
|---|---|
| Création des contrats et statut initial `Créé` | `src/main/java/mg/grh/service/ContratCandidatService.java` — `creerCe`, `creerCdd`, `creerCdi` |
| Vérification que la proposition est en accord final | même fichier — `propositionAcceptee` |
| Signature par le seul candidat concerné, passage à `Signé` | même fichier — `signer` |
| Validation uniquement depuis `Signé`, passage à `Validé` | même fichier — `valider` |
| Historisation de chaque changement | même fichier — `changerStatut` et `historique` |
| Notification après signature ou validation | même fichier |

Les classes métier principales sont :

- `src/main/java/mg/grh/model/ContratCandidat.java` : lien entre le candidat, le contrat concret, le statut courant et la candidature.
- `src/main/java/mg/grh/model/ContratCe.java`, `ContratCdd.java`, `ContratCdi.java` : données propres à chaque type.
- `src/main/java/mg/grh/model/HistoriqueStatutContrat.java` : trace des statuts.
- `src/main/java/mg/grh/service/HistoriqueStatutContratService.java` : service de l'historique.

## Effet de la validation : embauche

`EmbaucheService` est appelé dans la même transaction que la validation. Il suit le lien :

```text
contrat_candidat → candidature → offre_emploi → besoin_poste → besoin
```

| Type de besoin | Résultat |
|---|---|
| `CREATION` | Création ou occupation d'un siège pour le candidat, avec statut employé actif. |
| `REMPLACEMENT` | L'occupant est suspendu, ajouté à l'historique des employés, puis le candidat prend le siège. |
| Aucun besoin lié | Le contrat est validé sans modifier les employés. |

Le comportement est centralisé dans `src/main/java/mg/grh/service/EmbaucheService.java` : `apercu`, `embaucher`, `creer`, `remplacer` et `lierContrat`. Les résultats utilisent `src/main/java/mg/grh/dto/ApercuEmbauche.java` et `ResultatEmbauche.java`.

## API REST

Les contrôleurs REST liés au domaine sont dans `src/main/java/mg/grh/controller/` : `ContratCandidatController`, `ContratCeController`, `ContratCddController`, `ContratCdiController`, `HistoriqueStatutContratController` et `SignatureCdiController`. Ils sont derrière `/api/**`, protégé par la configuration de `WebConfig`.
