DO $$
BEGIN
    IF NOT EXISTS (
        SELECT FROM pg_database
        WHERE datname = 'academy'
    ) THEN
        PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE academy');
END IF;
END $$;
