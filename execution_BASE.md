psql -U postgres -c "DROP DATABASE grh;"


psql -U postgres -f db/grh_schema.sql
psql -U postgres -f db/grh_donnees_test.sql
psql -U postgres -f db/grh_donnees_complement.sql
psql -U postgres -f db/grh_mots_de_passe_dev.sql
psql -U postgres -f db/grh_integration.sql
psql -U postgres -f db/base_miantra.sql
