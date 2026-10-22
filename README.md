# grh-backend (Java 17 + Spring Boot 3 + PostgreSQL)

## Lancer
1. psql -U postgres -f db/grh_schema.sql
<<<<<<< HEAD
2. psql -U postgres -d grh -f db/grh_donnees_test.sql
3. psql -U postgres -d grh -f db/grh_donnees_complement.sql
3 bis. psql -U postgres -d grh -f db/base_miantra.sql              (besoins, offres, candidatures, propositions)
3 ter. psql -U postgres -d grh -f db/donnee_test_acceptation.sql
3 quater. psql -U postgres -d grh -f db/grh_embauche.sql           (lien contrat -> besoin)
3 quinquies. psql -U postgres -d grh -f db/donnee_test_embauche.sql  (besoin REMPLACEMENT de test)
3 sexies. psql -U postgres -d grh -f db/grh_integration.sql        (consignes d'intégration RH -> candidat)
3 septies. psql -U postgres -d grh -f db/grh_mots_de_passe_dev.sql (vrais hash BCrypt, mot de passe de test : motdepasse123)
=======
2. psql -U postgres -f db/grh_donnees_test.sql
3. psql -U postgres -f db/grh_donnees_complement.sql
3 bis. psql -U postgres -f db/grh_mots_de_passe_dev.sql   (vrais hash BCrypt, mot de passe de test : motdepasse123)
3 ter. psql -U postgres -f db/grh_integration.sql        (consignes d'intégration RH -> candidat)
3 quater. psql -U postgres -f db/base_miantra.sql        (offres, candidatures, suivi et propositions d'embauche)
3 quinquies. psql -U postgres -f db/grh_offres_exemple.sql (offres de démonstration visibles sans connexion)
3 sexies. psql -U postgres -f db/migration_validation_test.sql (migration pour les réponses et décisions sur les tests)
>>>>>>> 33e03d0c11dfb58f49c3361d50f3c1173277bb77
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
controller/web/   AuthWeb, AdminContratWeb, AdminEmployeWeb, AdminIntegrationWeb,
                  CandidatContratWeb, CandidatIntegrationWeb, DgContratWeb (@Controller)
web/              SessionUser + AuthInterceptor (accès par rôle)
templates/        login, fragments (en-tête, menu par rôle, messages, badges, confirmation), contrat-detail (commun),
                  admin/{candidats,contrats,contrat-form,travailleur-form,employes,historique-employes,integration},
                  candidat/{contrats,integration}, dg/contrats, proposition/*
static/css/       app.css  (Bootstrap 5 + Open Sans + Font Awesome, chargés par CDN dans fragments.html)
static/js/        app.js   (confirmations, recherche dans les tableaux, validation)

## Architecture (par table : 5 fichiers)
model/       entité JPA (@Entity)            -> 46 classes + Identifiable
repository/  Spring Data JpaRepository
dao/         DAO générique (EntityManager / JPQL) + 1 DAO par entité
service/     GenericService (CRUD) + 1 service par entité
controller/  GenericController (REST CRUD /api/<table-en-kebab-case>)
exception/   404 / 409 / 403 / 400 centralisés
config/      CORS

Écrits à la main : service/ContratCandidatService + controller/ContratCandidatController
(workflow Créé -> Signé -> Validé) et service/EmbaucheService (embauche à la validation du contrat :
création -> le candidat devient employé ; remplacement -> l'occupant est suspendu et archivé, le candidat prend sa place).
