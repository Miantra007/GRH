# GRH

Application de gestion des ressources humaines : Spring Boot 4, Thymeleaf, PostgreSQL, Bootstrap 5.

## Démarrage

1. Créer la base et l'utilisateur `grh` (mot de passe = `DB_PASSWORD` du `.env`), puis le schéma :
   ```bash
   psql -h localhost -U grh -d grh -f schema.sql
   ```
   `schema.sql` regroupe `base.sql` et `RH.sql` dans l'ordre d'exécution, avec les données de référence.
2. Lancer l'application (Spring ne lit pas le `.env` tout seul) :
   ```bash
   set -a; source .env; set +a
   ./mvnw spring-boot:run
   ```
3. Ouvrir http://localhost:8080

`HIBERNATE_DDL_AUTO=validate` : au démarrage, Hibernate vérifie que les entités correspondent aux tables.
Toute modification du schéma doit être faite dans `schema.sql` **et** dans l'entité correspondante.

## Organisation du code

| Couche | Paquet | Rôle |
|---|---|---|
| Modèle | `com.grh.model` | Une entité JPA par table, toutes implémentent `Entite` (id + libellé affiché) |
| Repository | `com.grh.repository` | Interfaces Spring Data (`JpaRepository`) |
| DAO | `com.grh.dao` | `GenericDao` (lister, obtenir, enregistrer, supprimer) + requêtes métier |
| Contrôleur | `com.grh.controller` | Un contrôleur par table, qui étend `CrudController` |
| Pages | `templates/crud/` | Liste et formulaire génériques, pilotés par la description des champs |

Pages dédiées : `templates/contrat/contrat-cdi.html` (formulaire du CDI).

## Ajouter un écran pour une nouvelle table

1. `model/MaTable.java` : entité `implements Entite`, avec `getAffichage()`.
2. `repository/MaTableRepository.java` : `extends JpaRepository<MaTable, Integer>`.
3. `dao/MaTableDao.java` : `extends GenericDao<MaTable>`.
4. `controller/MaTableController.java` : `extends CrudController<MaTable>`, avec l'`Ecran` (URL, titre, icône,
   rubrique du menu) et la liste des `Champ`. L'écran apparaît automatiquement dans le menu et le tableau de bord.

Pour une page sur mesure, redéfinir `vueFormulaire()` dans le contrôleur (exemple : `ContratCdiController`).
