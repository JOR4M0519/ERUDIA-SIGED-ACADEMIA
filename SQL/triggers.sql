CREATE TRIGGER associate_knowledges AFTER
    INSERT ON subject
    FOR EACH ROW
    EXECUTE FUNCTION associate_knowledge_func();

-- Creamos la función del trigger correctamente
CREATE OR REPLACE FUNCTION insert_activity_grades()
RETURNS TRIGGER AS $$
BEGIN
    -- Esta función se ejecutará después de insertar en activity_group
    -- Insertamos un registro en activity_grade para cada estudiante del grupo
INSERT INTO activity_grade (student_id, activity_id, score, comment)
SELECT
    gs.student_id,  -- ID del estudiante
    NEW.id,         -- ID del activity_group recién creado
    NULL,           -- Puntuación inicial NULL
    ''              -- Comentario inicial vacío
FROM
    group_students gs
WHERE
    gs.group_id = NEW.group_id;  -- Usamos directamente el id de group_students que está en NEW.group_id

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Creamos el trigger correctamente
CREATE TRIGGER after_activity_group_insert
    AFTER INSERT ON activity_group
    FOR EACH ROW
    EXECUTE FUNCTION insert_activity_grades();