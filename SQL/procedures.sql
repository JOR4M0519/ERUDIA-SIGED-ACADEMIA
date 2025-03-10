--Ver para implementar reglas

CREATE PROCEDURE recover_student(idStudent int, idSubject int, idPeriod int, newScore NUMERIC(5,2))
    LANGUAGE plpgsql
AS $$
DECLARE
    previousScore NUMERIC(5,2);
    subjectGradeId int;
BEGIN
    select sg.total_score from subject_grade sg where sg.student_id = idStudent and sg.subject_id = idSubject and sg.period_id = idPeriod into previousScore;

    ---Si saco la misma nota o menor en la recuperación.
    IF previousScore > newScore OR previousScore = newScore THEN
        update subject_grade sg set recovered = 'N' where sg.student_id = idStudent and sg.subject_id = idSubject and sg.period_id = idPeriod;
        RETURN;
    end if;

    --- Si el estudiante si paso.
    update subject_grade sg set recovered = 'R', total_score = newScore where sg.subject_id = idSubject and student_id = idStudent and sg.period_id = idPeriod returning id into subjectGradeId;
    insert into recovery_period (subject_grade, previous_score) values (subjectGradeId, previousScore);
end;
$$;

CREATE PROCEDURE insert_family(idStudent int, idUser int, relationship int)
LANGUAGE plpgsql
AS $$
    BEGIN
        insert into family (student_id, user_id, relationship_id) values (idStudent, idUser, relationship);
    end;
$$

CREATE OR REPLACE FUNCTION obtener_familias()
RETURNS TABLE(
    codigo TEXT,
    familia TEXT,
    miembros INTEGER,
    estudiantes_activos INTEGER,
    ids_estudiantes INTEGER[]
) AS $$
BEGIN
RETURN QUERY
    -- Identificar todos los estudiantes
    WITH estudiantes AS (
        SELECT DISTINCT student_id FROM family
    ),

    -- Crear pares de estudiantes conectados (comparten un familiar)
    conexiones AS (
        SELECT DISTINCT
            f1.student_id AS estudiante1,
            f2.student_id AS estudiante2
        FROM
            family f1
        JOIN
            family f2 ON f1.user_id = f2.user_id AND f1.student_id != f2.student_id
    ),

    -- Inicializar cada estudiante como su propia familia
    inicializacion AS (
        SELECT
            student_id,
            student_id AS familia_id
        FROM
            estudiantes
    ),

    -- Iteración 1: Propagación inicial de IDs
    iteracion1 AS (
        SELECT
            i.student_id,
            LEAST(i.familia_id, MIN(COALESCE(i2.familia_id, i.familia_id))) AS familia_id
        FROM
            inicializacion i
        LEFT JOIN
            conexiones c ON i.student_id = c.estudiante1 OR i.student_id = c.estudiante2
        LEFT JOIN
            inicializacion i2 ON
                (c.estudiante1 = i2.student_id AND i.student_id = c.estudiante2) OR
                (c.estudiante2 = i2.student_id AND i.student_id = c.estudiante1)
        GROUP BY
            i.student_id, i.familia_id
    ),

    -- Iteración 2: Segunda propagación para conectar grupos indirectos
    iteracion2 AS (
        SELECT
            i.student_id,
            LEAST(i.familia_id, MIN(COALESCE(i2.familia_id, i.familia_id))) AS familia_id
        FROM
            iteracion1 i
        LEFT JOIN
            conexiones c ON i.student_id = c.estudiante1 OR i.student_id = c.estudiante2
        LEFT JOIN
            iteracion1 i2 ON
                (c.estudiante1 = i2.student_id AND i.student_id = c.estudiante2) OR
                (c.estudiante2 = i2.student_id AND i.student_id = c.estudiante1)
        GROUP BY
            i.student_id, i.familia_id
    ),

    -- Iteración 3: Tercera propagación para garantizar convergencia
    iteracion3 AS (
        SELECT
            i.student_id,
            LEAST(i.familia_id, MIN(COALESCE(i2.familia_id, i.familia_id))) AS familia_id
        FROM
            iteracion2 i
        LEFT JOIN
            conexiones c ON i.student_id = c.estudiante1 OR i.student_id = c.estudiante2
        LEFT JOIN
            iteracion2 i2 ON
                (c.estudiante1 = i2.student_id AND i.student_id = c.estudiante2) OR
                (c.estudiante2 = i2.student_id AND i.student_id = c.estudiante1)
        GROUP BY
            i.student_id, i.familia_id
    ),

    -- Normalizar los IDs de familia para asegurarnos que sean los mínimos
    familia_final AS (
        SELECT
            student_id,
            MIN(familia_id) OVER (PARTITION BY familia_id) AS familia_id
        FROM
            iteracion3
    ),

    -- Agrupar estudiantes por familia
    familias_agrupadas AS (
        SELECT
            familia_id,
            ARRAY_AGG(student_id ORDER BY student_id) AS estudiantes
        FROM
            familia_final
        GROUP BY
            familia_id
    ),

    -- Calcular datos para cada familia
    estadisticas_familia AS (
        SELECT
            f.familia_id,
            f.estudiantes,
            -- Total miembros: estudiantes + familiares únicos
            ARRAY_LENGTH(f.estudiantes, 1) + (
                SELECT COUNT(DISTINCT user_id)
                FROM family
                WHERE student_id = ANY(f.estudiantes)
            ) AS total_miembros,
            -- Estudiantes activos
            (
                SELECT COUNT(*)
                FROM unnest(f.estudiantes) AS est_id
                JOIN users u ON est_id = u.id
                WHERE u.status = 'A'
            ) AS estudiantes_activos,
            -- Nombre del representante
            (
                SELECT first_name
                FROM users
                WHERE id = (SELECT MIN(student_id) FROM unnest(f.estudiantes) AS s(student_id))
            ) AS nombre_representante
        FROM
            familias_agrupadas f
    )

-- Formateo final para mostrar resultados
SELECT
    'F' || UPPER(LEFT(e.nombre_representante, 1)) || '-' ||
    LPAD(ROW_NUMBER() OVER (PARTITION BY UPPER(LEFT(e.nombre_representante, 1))
              ORDER BY e.nombre_representante)::TEXT, 3, '0') AS codigo,
    'Familia ' || e.nombre_representante AS familia,
    e.total_miembros AS miembros,
    e.estudiantes_activos,
    e.estudiantes AS ids_estudiantes
FROM
    estadisticas_familia e
ORDER BY
    codigo;
END;
$$ LANGUAGE plpgsql;
