CREATE OR REPLACE FUNCTION calculateKnowledgesGradesBySubjectAndGroupAndPeriod(
    p_group_id INT,
    p_subject_id INT,
    p_period_id INT
) RETURNS VOID
LANGUAGE plpgsql
AS $$
DECLARE
v_student_id INT;
    v_current_period_name VARCHAR;
    v_period_number INT;
    v_is_last_period BOOLEAN;
    v_period_record RECORD;
    v_knowledge_record RECORD;
    v_accumulated_score NUMERIC(5,2);
    v_accumulated_percentage INT;
    v_year INT;
BEGIN
    -- Obtener información del periodo actual
SELECT name, EXTRACT(YEAR FROM start_date) INTO v_current_period_name, v_year
FROM academic_period
WHERE id = p_period_id;

-- Extraer el número de periodo (asumiendo formato "1P", "2P", etc.)
v_period_number := CAST(SUBSTRING(v_current_period_name FROM 1 FOR 1) AS INT);

    -- Determinar si es el último periodo del año
SELECT COUNT(*) = v_period_number INTO v_is_last_period
FROM academic_period
WHERE EXTRACT(YEAR FROM start_date) = v_year
  AND status = 'A';

-- Para cada estudiante en el grupo
FOR v_student_id IN
SELECT student_id
FROM group_students
WHERE group_id = p_group_id
  AND status = 'A'
    LOOP
        -- Si no es el primer periodo, calcular acumulados
        IF v_period_number > 1 THEN
            -- Inicializar acumulados
            v_accumulated_score := 0;
v_accumulated_percentage := 0;

            -- Acumular notas de periodos anteriores
FOR v_period_record IN
SELECT ap.id, ap.name, ap.percentage
FROM academic_period ap
WHERE EXTRACT(YEAR FROM ap.start_date) = v_year
  AND CAST(SUBSTRING(ap.name FROM 1 FOR 1) AS INT) < v_period_number
  AND ap.status = 'A'
ORDER BY ap.start_date
    LOOP
                -- Acumular porcentaje del periodo
                v_accumulated_percentage := v_accumulated_percentage + v_period_record.percentage;

-- Acumular nota ponderada del periodo
SELECT COALESCE(SUM(sg.total_score * v_period_record.percentage / 100), 0)
INTO v_accumulated_score
FROM subject_grade sg
WHERE sg.student_id = v_student_id
  AND sg.subject_id = p_subject_id
  AND sg.period_id = v_period_record.id;
END LOOP;
END IF;

        -- Para cada conocimiento de la materia
FOR v_knowledge_record IN
SELECT
    k.id AS knowledge_id,
    k.name AS knowledge_name,
    k.percentage,
    ag.achievement,
    sk.id AS subject_knowledge_id
FROM knowledge k
         JOIN subject_knowledge sk ON k.id = sk.id_knowledge
         LEFT JOIN achievement_groups ag ON
    sk.id = ag.subject_knowledge_id AND
    ag.group_id = p_group_id AND
    ag.period_id = p_period_id
WHERE sk.id_subject = p_subject_id
  AND k.status = 'A'
    LOOP
-- Calcular nota para este conocimiento
DECLARE
v_knowledge_score NUMERIC(5,2);
BEGIN
                -- Obtener promedio de actividades para este conocimiento
SELECT COALESCE(AVG(ag.score), 0) INTO v_knowledge_score
FROM activity_grade ag
         JOIN activity_group agr ON ag.activity_id = agr.id
         JOIN activity a ON agr.activity_id = a.id
         JOIN achievement_groups achg ON a.achievement_groups_id = achg.id
         JOIN subject_knowledge sk ON achg.subject_knowledge_id = sk.id
WHERE ag.student_id = v_student_id
  AND sk.id_subject = p_subject_id
  AND sk.id_knowledge = v_knowledge_record.knowledge_id
  AND achg.period_id = p_period_id;

-- Si es el último periodo, incluir la nota acumulada
IF v_is_last_period AND v_period_number > 1 THEN
                    v_knowledge_score := (v_knowledge_score *
                        (SELECT percentage FROM academic_period WHERE id = p_period_id) / 100)
                        + v_accumulated_score;
END IF;

                -- Actualizar o insertar la nota en subject_grade
UPDATE subject_grade
SET total_score = v_knowledge_score
WHERE student_id = v_student_id
  AND subject_id = p_subject_id
  AND period_id = p_period_id;

IF NOT FOUND THEN
                    INSERT INTO subject_grade (
                        subject_id, student_id, period_id, comment, total_score, recovered
                    ) VALUES (
                        p_subject_id, v_student_id, p_period_id,
                        'Calificación calculada por saberes', v_knowledge_score, 'N'
                    );
END IF;
END;
END LOOP;
END LOOP;
END;
$$;
