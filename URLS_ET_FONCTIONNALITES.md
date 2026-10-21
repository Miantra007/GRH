# URLs et fonctionnalités de l’application

Base locale : `http://localhost:8080`.

Les routes `/admin/**`, `/candidat/**` et `/dg/**` sont des pages protégées par connexion et rôle. Les pages publiques du parcours candidat commencent par `/api/candidat/**`. Les routes `/api/<ressource>` sont des API REST.

## Accueil et connexion

| Méthode | URL | Fonction |
|---|---|---|
| GET | `/login` | Affiche la connexion. |
| POST | `/login` | Connecte un compte avec `email` et `motDePasse`. |
| POST | `/logout` | Ferme la session. |

## Côté candidat / client

Le candidat peut consulter les offres sans compte. Le parcours de candidature conserve les informations entre les étapes dans la session du navigateur.

| Méthode | URL | Fonction |
|---|---|---|
| GET | `/api/candidat` | Redirige vers la liste des offres. |
| GET | `/api/candidat/offres` | Liste les offres publiées. Filtre optionnel : `?recherche=java`. |
| GET | `/api/candidat/offres/{offreId}` | Affiche le détail d’une offre publiée. |
| GET | `/api/candidat/offres/{offreId}/postuler` | Démarre une candidature pour l’offre choisie. |
| GET | `/api/candidat/etape/1` | Affiche les informations personnelles et coordonnées. |
| POST | `/api/candidat/etape/1` | Enregistre le candidat et passe à l’étape CV. |
| GET | `/api/candidat/etape/2` | Affiche le CV : formations, expériences, compétences et langues. |
| POST | `/api/candidat/etape/2` | Enregistre le CV et permet l’import du fichier CV. Requête `multipart/form-data`. |
| GET | `/api/candidat/etape/3` | Affiche la lettre et les justificatifs à joindre. |
| POST | `/api/candidat/etape/3` | Enregistre le dossier et ses fichiers. Requête `multipart/form-data`. |
| GET | `/api/candidat/confirmation/{code}` | Affiche la confirmation associée au code de suivi. |
| GET | `/api/candidat/suivi?code=...` | Ouvre le suivi avec le code reçu après candidature. |
| GET | `/api/candidat/suivi/{code}` | Consulte l’avancement et les notifications de la candidature. |
| POST | `/api/candidat/suivi/{code}/proposition` | Répond à une proposition d’embauche (acceptation, refus ou contre-proposition selon le formulaire). |

Les boutons **Précédent** et **Suivant** du formulaire permettent de naviguer dans les étapes. Le fichier CV et les pièces justificatives sont envoyés avec les formulaires d’upload.

## Côté responsable RH / administrateur RH

Les pages RH nécessitent une session avec le rôle RH. Elles permettent de suivre les candidatures et de gérer le parcours jusqu’à la proposition d’embauche, ainsi que les contrats et consignes d’intégration.

### Candidatures

| Méthode | URL | Fonction |
|---|---|---|
| GET | `/admin/candidatures` | Liste les candidatures reçues. |
| GET | `/admin/candidatures/{id}` | Consulte le dossier, le CV, les justificatifs, les tests, entretiens et proposition. |
| GET | `/admin/candidatures/{id}/documents/{documentId}` | Télécharge un justificatif du dossier. |
| POST | `/admin/candidatures/{id}/statut` | Modifie le statut de recrutement (`statut`). |
| POST | `/admin/candidatures/{id}/test` | Planifie ou met à jour un test technique (`date`, `heure`, `lieu`, `consignes`, `message` facultatif). |
| POST | `/admin/candidatures/{id}/entretien` | Planifie ou met à jour un entretien (mêmes champs que le test). |
| POST | `/admin/candidatures/{id}/proposition` | Envoie une proposition après avoir retenu la candidature (`salaire`, autres champs facultatifs). |
| POST | `/admin/candidatures/{id}/proposition/decision` | Enregistre la décision RH après la réponse du candidat (`decision`, `commentaire` facultatif). |

### Contrats et intégration

| Méthode | URL | Fonction |
|---|---|---|
| GET | `/admin/candidats` | Liste les candidats enregistrés. |
| GET | `/admin/contrats` | Liste les contrats; filtre facultatif `?statut=Créé`. |
| GET | `/admin/contrats/{id}` | Consulte le contrat et son historique. |
| GET | `/admin/contrats/nouveau?candidatId={id}&type=CE` | Ouvre le formulaire de création de contrat (`CE`, `CDD` ou `CDI`). |
| POST | `/admin/contrats/ce` | Crée un contrat CE. |
| POST | `/admin/contrats/cdd` | Crée un contrat CDD. |
| POST | `/admin/contrats/cdi` | Crée un contrat CDI. |
| GET | `/admin/travailleurs/nouveau` | Affiche le formulaire de création d’un travailleur. |
| POST | `/admin/travailleurs` | Enregistre un travailleur. |
| GET | `/admin/candidats/{id}/integration` | Consulte les consignes d’intégration d’un candidat. |
| POST | `/admin/candidats/{id}/integration` | Ajoute une consigne d’intégration. |
| POST | `/admin/integration/{consigneId}/supprimer` | Supprime une consigne d’intégration. |

## Côté candidat connecté : contrats et intégration

Ces pages nécessitent le rôle candidat et une session ouverte.

| Méthode | URL | Fonction |
|---|---|---|
| GET | `/candidat/contrats` | Consulte ses contrats. |
| GET | `/candidat/contrats/{id}` | Consulte le détail d’un contrat. |
| POST | `/candidat/contrats/{id}/signer` | Signe le contrat. |
| GET | `/candidat/integration` | Consulte ses consignes d’intégration. |
| POST | `/candidat/integration/{id}/faite` | Marque une consigne comme faite; `commentaire` est facultatif. |
| POST | `/candidat/integration/{id}/rouvrir` | Remet une consigne à faire. |

## Côté direction générale

Ces pages nécessitent le rôle DG.

| Méthode | URL | Fonction |
|---|---|---|
| GET | `/dg/contrats` | Liste les contrats à valider. Paramètre `?tous=true` pour afficher tous les contrats. |
| GET | `/dg/contrats/{id}` | Consulte un contrat et son historique. |
| POST | `/dg/contrats/{id}/valider` | Valide un contrat signé. |

## API REST de données

Les contrôleurs génériques exposent les opérations CRUD suivantes pour les ressources indiquées ci-dessous : `GET /api/{ressource}` (liste), `GET /api/{ressource}/{id}` (détail), `POST /api/{ressource}` (création JSON), `PUT /api/{ressource}/{id}` (mise à jour JSON) et `DELETE /api/{ressource}/{id}` (suppression).

Exemples : `/api/responsable`, `/api/candidat`, `/api/cv`, `/api/formation`, `/api/experience-professionnelle`, `/api/competence`, `/api/direction`, `/api/departement`, `/api/poste`, `/api/employe`, `/api/contrat-cdi`, `/api/contrat-cdd`, `/api/contrat-ce`.

Autres API dédiées :

| URL de base | Fonction |
|---|---|
| `/api/contrats-candidat` | Gestion du workflow des contrats et historique (création, signature, validation). |
| `/api/signature-cdi` | Opérations de signature de CDI. |

Les routes ci-dessus sont celles déclarées dans les contrôleurs du projet. Pour les paramètres complets d’une ressource CRUD, consulter son entité Java.
