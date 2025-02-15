CREATE TRIGGER associate_knowledges AFTER
    INSERT ON subject
    FOR EACH ROW
    EXECUTE FUNCTION associate_knowledge_func();