psql -U postgres -f db/001_schema_complet.sql

psql -U postgres -d grh -f db/002_donnees_base.sql




psql -U postgres -c "DROP DATABASE grh;"




//nettoyer donnee

psql -U postgres -d grh -f db/cleanup_test_data.sql




SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'grh'