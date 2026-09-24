# RepairConnect database

Start MySQL from XAMPP, then create `repairconnect_db` in phpMyAdmin. Spring Boot uses Hibernate with `ddl-auto=update` to create the schema and seeds development records on the first empty database.

For a non-default XAMPP setup, set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` before running the backend.