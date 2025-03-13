CREATE OR REPLACE FUNCTION get_attendance_report()
RETURNS TABLE (
    group_id INTEGER,
    level_name TEXT,
    group_name TEXT,
    section_name TEXT,
    total_active BIGINT,
    present_count BIGINT,
    absent_count BIGINT,
    late_count BIGINT,
    last_record TIMESTAMP WITHOUT TIME ZONE  -- Especificar explícitamente sin zona horaria
) AS $$
BEGIN
RETURN QUERY
    WITH active_students AS (
        -- Obtener todos los estudiantes activos por grupo
        SELECT
            g.id AS group_id,
            el.level_name::TEXT,
            g.group_name::TEXT,
            g.group_code::TEXT AS section_name,
            COUNT(DISTINCT gs.student_id) AS total_active
        FROM groups g
        JOIN educational_level el ON g.level_id = el.id
        JOIN group_students gs ON g.id = gs.group_id
        JOIN users u ON gs.student_id = u.id
        WHERE g.status = 'A' AND u.status = 'A'
        GROUP BY g.id, el.level_name, g.group_name, g.group_code
    ),
    attendance_stats AS (
        -- Calcular estadísticas de asistencia por grupo
        SELECT
            g.id AS group_id,
            COUNT(CASE WHEN a.status = 'P' THEN 1 END) AS present_count,
            COUNT(CASE WHEN a.status = 'A' THEN 1 END) AS absent_count,
            COUNT(CASE WHEN a.status = 'T' THEN 1 END) AS late_count,
            MAX(a.recorded_at)::TIMESTAMP WITHOUT TIME ZONE AS last_record  -- Conversión explícita
        FROM groups g
        JOIN group_students gs ON g.id = gs.group_id
        LEFT JOIN attendance a ON gs.student_id = a.student_id
        WHERE g.status = 'A'
        GROUP BY g.id
    )

SELECT
    as_students.group_id,
    as_students.level_name,
    as_students.group_name,
    as_students.section_name,
    as_students.total_active,
    COALESCE(a_stats.present_count, 0) AS present_count,
    COALESCE(a_stats.absent_count, 0) AS absent_count,
    COALESCE(a_stats.late_count, 0) AS late_count,
    a_stats.last_record
FROM active_students as_students
         LEFT JOIN attendance_stats a_stats ON as_students.group_id = a_stats.group_id
ORDER BY as_students.level_name, as_students.group_name;
END;
$$ LANGUAGE plpgsql;

   ------------------------
   -----**************-----
   ------------------------





CREATE OR REPLACE FUNCTION get_academic_level_report()
RETURNS TABLE (
    group_id INTEGER,
    level_name TEXT,
    group_name TEXT,
    status_name TEXT,
    students_total BIGINT
) AS $$
BEGIN
RETURN QUERY
    WITH student_status AS (
        SELECT
            el.level_name::TEXT,
            g.id AS group_id,
            g.group_name::TEXT,
            CASE
                WHEN u.status = 'A' THEN 'Activo'::TEXT
                WHEN u.status = 'N' THEN 'Nuevo'::TEXT
                WHEN u.status = 'S' THEN 'Suspendido'::TEXT
                WHEN u.status = 'P' THEN 'Pendiente'::TEXT
                WHEN u.status = 'R' THEN 'Retirado'::TEXT
                WHEN u.status = 'I' THEN 'Inscrito'::TEXT
                WHEN u.status = 'E' THEN 'Egresado'::TEXT
                ELSE 'Otro'::TEXT
            END AS status_name
        FROM users u
        JOIN group_students gs ON u.id = gs.student_id
        JOIN groups g ON gs.group_id = g.id
        JOIN educational_level el ON g.level_id = el.id
        WHERE g.status = 'A'
    )

SELECT
    ss.group_id,
    ss.level_name,
    ss.group_name,
    ss.status_name,
    COUNT(*) AS students_total
FROM student_status ss
GROUP BY ss.group_id, ss.level_name, ss.group_name, ss.status_name
ORDER BY ss.level_name, ss.group_name,
         CASE
             WHEN ss.status_name = 'Activo' THEN 1
             WHEN ss.status_name = 'Nuevo' THEN 2
             WHEN ss.status_name = 'Suspendido' THEN 3
             WHEN ss.status_name = 'Pendiente' THEN 4
             WHEN ss.status_name = 'Retirado' THEN 5
             WHEN ss.status_name = 'Inscrito' THEN 6
             WHEN ss.status_name = 'Egresado' THEN 7
             ELSE 8
             END;
END;
$$ LANGUAGE plpgsql;

   ------------------------
   -----**************-----
   ------------------------

CREATE OR REPLACE FUNCTION get_repeating_students_report_by_group()
RETURNS TABLE (
    group_id INTEGER,
    group_name VARCHAR,
    level_name VARCHAR,
    repeating_count BIGINT
) AS $$
BEGIN
RETURN QUERY
SELECT
    g.id AS group_id,
    g.group_name,
    el.level_name,
    COUNT(DISTINCT sg.student_id) AS repeating_count
FROM groups g
         JOIN educational_level el ON g.level_id = el.id
         LEFT JOIN group_students gs ON gs.group_id = g.id
         LEFT JOIN subject_grade sg ON gs.student_id = sg.student_id
         LEFT JOIN recovery_period rp ON sg.id = rp.subject_grade
WHERE el.status = 'A'
  AND rp.id IS NOT NULL  -- Solo estudiantes con recuperaciones
GROUP BY g.id, g.group_name, el.level_name
ORDER BY g.id;
END;
$$ LANGUAGE plpgsql;






   ------------------------
   -----**************-----
   ------------------------






   ------------------------
   -----**************-----
   ------------------------










   ------------------------
   -----**************-----
   ------------------------









   ------------------------
   -- AGRUPACION FAMILIAS--
   ------------------------


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


