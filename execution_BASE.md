

psql -U postgres -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'grh' AND pid <> pg_backend_pid();"
psql -U postgres -c "DROP DATABASE IF EXISTS grh;"


psql -U postgres -f db/grh_schema.sql


psql -U postgres -d grh -f db/migration_sexe.sql
psql -U postgres -d grh -f db/migration_travailleur.sql
psql -U postgres -d grh -f db/migration_langue_nullable.sql


psql -U postgres -d grh -f db/grh_donnees_test.sql
psql -U postgres -d grh -f db/grh_donnees_complement.sql


psql -U postgres -d grh -f db/base_miantra.sql


psql -U postgres -d grh -f db/grh_integration.sql
psql -U postgres -d grh -f db/grh_embauche.sql
psql -U postgres -d grh -f db/migration_validation_test.sql


psql -U postgres -d grh -f db/donnee_test_acceptation.sql
psql -U postgres -d grh -f db/donnee_test_embauche.sql
psql -U postgres -d grh -f db/grh_offres_exemple.sql


psql -U postgres -d grh -f db/grh_mots_de_passe_dev.sql



psql -U postgres -d grh -f db/cleanup_test_data.sql
