psql -U postgres -f db/grh_schema.sql

psql -U postgres -d grh -f db/grh_donnees_test.sql

psql -U postgres -d grh -f db/grh_donnees_complement.sql

psql -U postgres -d grh -f db/base_Mitia.sql

psql -U postgres -d grh -f db/donnee_test_acceptation.sql

psql -U postgres -d grh -f db/grh_embauche.sql

psql -U postgres -d grh -f db/donnee_test_embauche.sql

psql -U postgres -d grh -f db/grh_integration.sql

psql -U postgres -d grh -f db/grh_mots_de_passe_dev.sql



psql -U postgres -c "DROP DATABASE grh;"



SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'grh'