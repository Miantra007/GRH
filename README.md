# grh-backend (Java 17 + Spring Boot 3 + PostgreSQL)

## Lancer
1. psql -U postgres -f db/grh_schema.sql
2. psql -U postgres -f db/grh_donnees_test.sql
3. psql -U postgres -f db/grh_donnees_complement.sql
3 bis. psql -U postgres -f db/grh_mots_de_passe_dev.sql   (vrais hash BCrypt, mot de passe de test : motdepasse123)
3 ter. psql -U postgres -f db/grh_integration.sql        (consignes d'intégration RH -> candidat)
3 quater. psql -U postgres -f db/base_miantra.sql        (offres, candidatures, suivi et propositions d'embauche)
3 quinquies. psql -U postgres -f db/grh_offres_exemple.sql (offres de démonstration visibles sans connexion)
3 sexies. psql -U postgres -f db/migration_validation_test.sql (migration pour les réponses et décisions sur les tests)
4. Mettre ton mot de passe dans src/main/resources/application.properties
5. mvn spring-boot:run   (IDE : activer le plugin Lombok + "annotation processing")
6. Ouvrir http://localhost:8080  (interface Thymeleaf)
7. API REST testable avec requests.http

Détails sur la connexion et la base : voir AUTHENTIFICATION_ET_BASE.md

## Comptes de test (mot de passe : motdepasse123)
rh@entreprise.mg          -> Administrateur RH : crée les contrats      (/admin/...)
hery.rabe@mail.mg         -> Candidat          : consulte et signe      (/candidat/...)
dg@entreprise.mg          -> Direction générale: valide les contrats    (/dg/...)

## Front Thymeleaf
controller/web/   AuthWeb, AdminContratWeb, CandidatContratWeb, DgContratWeb (@Controller)
web/              SessionUser + AuthInterceptor (accès par rôle)
templates/        login, fragments (nav/messages), contrat-detail (commun),
                  admin/{candidats,contrats,contrat-form,travailleur-form}, candidat/contrats, dg/contrats
static/css/       style.css

## Architecture (par table : 5 fichiers)
model/       entité JPA (@Entity)            -> 46 classes + Identifiable
repository/  Spring Data JpaRepository
dao/         DAO générique (EntityManager / JPQL) + 1 DAO par entité
service/     GenericService (CRUD) + 1 service par entité
controller/  GenericController (REST CRUD /api/<table-en-kebab-case>)
exception/   404 / 409 / 403 / 400 centralisés
config/      CORS

Écrits à la main : service/ContratCandidatService + controller/ContratCandidatController
(workflow Créé -> Signé -> Validé).
