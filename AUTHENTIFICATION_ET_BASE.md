# Authentification et utilisation de la base

## 1. Authentification

### Qui peut se connecter

Deux tables contiennent des comptes ; l'adresse e-mail sert d'identifiant.

| Table | Profil | Rôle applicatif (`SessionUser.type`) |
|---|---|---|
| `responsable` | Personnel interne, son rôle vient de `role.libelle` | `"Administrateur RH"` → **RH**, `"Direction générale"` → **DG**, autre libellé → **AUTRE** (refusé) |
| `candidat` | Candidat recruté | **CANDIDAT** |

La correspondance libellé → rôle est codée dans `AuthService`. Les libellés de la table `role` doivent donc
être écrits **exactement** comme ci-dessus (accents compris).

### Déroulement d'une connexion

```
GET  /login            -> formulaire (templates/login.html)
POST /login            -> AuthService.authentifier(email, motDePasse)
                          1. cherche l'e-mail (en minuscules) dans responsable, puis dans candidat
                          2. compare le mot de passe au hash BCrypt de la colonne mot_de_passe
                          3. crée un SessionUser(type, id, email)
                       -> nouvel identifiant de session (protection contre la fixation de session)
                       -> session.setAttribute("user", SessionUser)
                       -> redirection vers la page d'accueil du rôle
POST /logout           -> session.invalidate() puis retour à /login
```

| Rôle | Page d'accueil après connexion |
|---|---|
| RH | `/admin/contrats` |
| DG | `/dg/contrats` |
| CANDIDAT | `/candidat/contrats` |

La session expire après **30 minutes** d'inactivité (`server.servlet.session.timeout`).

### Contrôle d'accès

`web/AuthInterceptor` (déclaré dans `config/WebConfig`) intercepte les URL suivantes :

| URL | Rôle exigé | Sinon |
|---|---|---|
| `/admin/**` | RH | `/login` si non connecté, `/login?refuse` si mauvais rôle |
| `/dg/**` | DG | idem |
| `/candidat/**` | CANDIDAT | idem |

L'intercepteur vérifie seulement le **rôle**. Pour les données personnelles, c'est le service qui vérifie que
l'élément appartient bien à l'utilisateur connecté. Par exemple, un candidat ne peut signer que ses propres
contrats (`ContratCandidatService.signer`) et ne cocher que ses propres consignes
(`ConsigneIntegrationService.marquerFaite`). Dans un contrôleur, l'utilisateur connecté se récupère ainsi :

```java
SessionUser user = (SessionUser) session.getAttribute("user");
user.getId();    // id_responsable ou id_candidat selon le rôle
```

Dans un template : `${session.user.type}`, `${session.user.email}`.

### Mots de passe

Ils sont stockés hachés en **BCrypt** (`spring-security-crypto`). Un hash invalide, par exemple
`hash_factice_1` du jeu de test, ne permet aucune connexion : c'est pour ça que
`grh_mots_de_passe_dev.sql` remplace tous les hash par celui de `motdepasse123`.

Pour créer un compte à la main, générer un hash puis l'insérer :

```bash
htpasswd -bnBC 12 "" 'MonMotDePasse' | tr -d ':\n'      # paquet apache2-utils ; donne $2y$12$...
```
```sql
INSERT INTO responsable (id_role, email, mot_de_passe)
VALUES ((SELECT id_role FROM role WHERE libelle = 'Administrateur RH'), 'nouveau.rh@entreprise.mg', '$2y$12$...');
```

L'e-mail doit être enregistré **en minuscules** : la connexion le convertit en minuscules avant la recherche.

### Comptes de test (mot de passe : `motdepasse123`)

| E-mail | Rôle |
|---|---|
| `rh@entreprise.mg` | RH |
| `dg@entreprise.mg` | DG |
| `hery.rabe@mail.mg` | Candidat |

Les autres responsables du jeu de test (managers, directeurs, financier) sont refusés : leur rôle donne **AUTRE**.

### Limites actuelles (à traiter avant une mise en production)

1. **L'API REST `/api/**` n'est pas protégée (critique).** Elle accepte toutes les requêtes sans connexion,
   en lecture comme en écriture. Les hash des mots de passe sont bien masqués dans le JSON
   (`@JsonProperty(access = WRITE_ONLY)`), mais n'importe qui peut par exemple **créer un compte RH** avec
   `POST /api/responsable` (rôle 3 et un hash BCrypt de son choix), puis se connecter avec. Vérifié sur la
   base de test. Correctif : ajouter `/api/**` aux chemins de l'intercepteur en exigeant le rôle RH, puis
   tester `requests.http` après connexion.
2. **Identifiant de session dans l'URL** : après la connexion, Tomcat ajoute `;jsessionid=…` à l'adresse.
   À désactiver avec `server.servlet.session.tracking-modes=cookie` dans `application.properties`.
3. **Pas de protection CSRF** sur les formulaires POST (Spring Security n'est pas utilisé).
4. **Le mot de passe de la base** est écrit dans `application.properties` (voir 2.3 pour le passer en variable).

---

## 2. Utilisation de la base

### 2.1 Création (base vide)

Les scripts sont dans `db/` et s'exécutent **dans cet ordre** :

| # | Script | Contenu |
|---|---|---|
| 1 | `grh_schema.sql` | `CREATE DATABASE grh` + toutes les tables |
| 2 | `grh_donnees_test.sql` | Référentiels et données de démonstration |
| 3 | `grh_donnees_complement.sql` | Compte DG, employeur, candidat et travailleur de test |
| 4 | `grh_mots_de_passe_dev.sql` | Hash BCrypt de test + tables `statut_besoin` / `besoin` |
| 5 | `grh_integration.sql` | Tables `type_consigne` / `consigne_integration` + 3 consignes de test |

```bash
psql -U postgres -f db/grh_schema.sql
psql -U postgres -f db/grh_donnees_test.sql
psql -U postgres -f db/grh_donnees_complement.sql
psql -U postgres -f db/grh_mots_de_passe_dev.sql
psql -U postgres -f db/grh_integration.sql
```

Chaque script commence par `\c grh` : inutile de préciser `-d grh`. Les identifiants des données de test
supposent une base **vide** (les commentaires `-- id 1`, `-- id 2`… en dépendent).

### 2.2 Réinitialiser la base

```bash
psql -U postgres -c "DROP DATABASE grh;"
```
puis relancer les 5 scripts. **Toutes les données sont perdues.**

### 2.3 Brancher l'application

`application.properties` contient `spring.datasource.username` / `password`. Plutôt que d'y écrire son mot
de passe (le fichier est commité), on peut le passer en variable d'environnement au lancement :

```bash
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD='mon_mot_de_passe'
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64     # le projet exige Java 17+
mvn spring-boot:run
```

`spring.jpa.hibernate.ddl-auto=none` : Hibernate **ne modifie jamais** le schéma. Toute évolution se fait dans
les scripts SQL **et** dans l'entité Java correspondante (`model/`).

### 2.4 Organisation des tables

| Domaine | Tables principales |
|---|---|
| Comptes et organisation | `role`, `responsable`, `direction`, `departement`, `poste`, `poste_fille`, `employe` |
| Recrutement | `candidat`, `cv` (+ `formation`, `education`, `competence`, `cv_langue`, `experience_professionnelle`…), `test_technique`, `entretien`, `besoin` |
| Contrats | `contrat_ce`, `contrat_cdd`, `contrat_cdi` (+ avenants, ruptures, éléments de salaire, signatures) |
| Suivi | `contrat_candidat` (lien candidat ↔ un seul contrat), `historique_statut_contrat`, `historique_contrat_employe` |
| Intégration | `type_consigne`, `consigne_integration` |
| Référentiels | `civilite`, `nationalite`, `situation_famille`, `statut_contrat`, `categorie_professionnelle`, `motif_cdd`… |

Cycle de vie d'un contrat : **Créé** (RH) → **Signé** (candidat) → **Validé** (DG). Chaque changement est
tracé dans `historique_statut_contrat`.

### 2.5 Consignes d'intégration

RH envoie au candidat ce qu'il doit faire avant ou à son arrivée : formation à suivre, compte bancaire à
créer, matériel à récupérer, document à fournir…

| Colonne | Signification |
|---|---|
| `id_candidat` | Destinataire (obligatoire) |
| `id_contrat_candidat` | Contrat concerné (facultatif) |
| `id_type_consigne` | `FORMATION`, `COMPTE_BANCAIRE`, `MATERIEL`, `DOCUMENT`, `AUTRE` (table `type_consigne`) |
| `titre`, `description`, `date_limite` | Ce que voit le candidat |
| `id_rh_emetteur`, `date_envoi` | Qui l'a envoyée, et quand |
| `date_realisation` | `NULL` = à faire ; renseignée = faite par le candidat |
| `commentaire_candidat` | Réponse facultative du candidat |

Pages :

| Qui | URL | Action |
|---|---|---|
| RH | `/admin/candidats` → bouton **Intégration** | Envoyer / retirer des consignes, voir l'avancement |
| Candidat | `/candidat/integration` (menu **Mon intégration**) | Voir ses consignes, les marquer faites ou les remettre à faire |

Un nouveau type de consigne s'ajoute directement en base, sans toucher au code :
```sql
INSERT INTO type_consigne (code, libelle) VALUES ('VISITE_MEDICALE', 'Visite médicale à passer');
```

### 2.6 Requêtes utiles

```sql
-- Comptes qui peuvent se connecter, avec leur rôle
SELECT r.email, ro.libelle FROM responsable r LEFT JOIN role ro USING (id_role)
UNION ALL SELECT email, 'Candidat' FROM candidat;

-- Avancement de l'intégration par candidat
SELECT c.email, count(*) FILTER (WHERE ci.date_realisation IS NOT NULL) AS faites, count(*) AS total
FROM candidat c JOIN consigne_integration ci USING (id_candidat)
GROUP BY c.email;

-- Consignes en retard
SELECT c.email, ci.titre, ci.date_limite
FROM consigne_integration ci JOIN candidat c USING (id_candidat)
WHERE ci.date_realisation IS NULL AND ci.date_limite < CURRENT_DATE;
```
