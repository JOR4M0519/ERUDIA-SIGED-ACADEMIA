CREATE OR REPLACE FUNCTION associate_knowledge_func()
RETURNS TRIGGER AS $$
    BEGIN
        insert into subject_knowledge (id_subject, id_knowledge)
        SELECT NEW.id, id FROM knowledge;

        RETURN NEW;
    end;
$$ LANGUAGE plpgsql
