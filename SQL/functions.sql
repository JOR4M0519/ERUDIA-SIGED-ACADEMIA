CREATE OR REPLACE FUNCTION associate_knowledge_func()
RETURNS TRIGGER AS $$
BEGIN
insert into subject_knowledge (id_subject, id_knowledge)
SELECT NEW.id, id FROM knowledge;

RETURN NEW;
end;
$$ LANGUAGE plpgsql

CREATE OR REPLACE FUNCTION get_academic_periods(p_year TEXT)
RETURNS TABLE (
                  id INT,
                  setting_id INT,
                  start_date DATE,
                  end_date DATE,
                  name VARCHAR(8),
                  status VARCHAR(1),
                  percentage INT
              ) AS $$
BEGIN
RETURN QUERY
SELECT ap.id, ap.setting_id, ap.start_date, ap.end_date, ap.name, ap.status, ap.percentage
FROM academic_period ap
WHERE ap.start_date <= TO_DATE(p_year || '-12-31', 'YYYY-MM-DD')  -- 🔹 Inicia antes o durante el año
  AND ap.end_date >= TO_DATE(p_year || '-01-01', 'YYYY-MM-DD')  -- 🔹 Termina después o durante el año
  AND ap.status IN ('A', 'F');
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_all_academic_periods(p_year TEXT)
RETURNS TABLE (
                  id INT,
                  setting_id INT,
                  start_date DATE,
                  end_date DATE,
                  name VARCHAR(8),
                  status VARCHAR(1),
                  percentage INT
              ) AS $$
BEGIN
RETURN QUERY
SELECT ap.id, ap.setting_id, ap.start_date, ap.end_date, ap.name, ap.status, ap.percentage
FROM academic_period ap
WHERE ap.start_date <= TO_DATE(p_year || '-12-31', 'YYYY-MM-DD')  --  Inicia antes o durante el año
  AND ap.end_date >= TO_DATE(p_year || '-01-01', 'YYYY-MM-DD');  --  Termina después o durante el año
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_subject_schedules(
    p_group_id INT,
    p_period_id INT,
    p_subject_id INT,
    p_professor_id INT
)
RETURNS SETOF subject_schedule AS $$
BEGIN
RETURN QUERY
SELECT
    ss.*
FROM
    subject_schedule ss
        JOIN
    subject_groups sg ON ss.subject_group_id = sg.id
        JOIN
    groups g ON sg.group_students = g.id
WHERE
    g.id = p_group_id
  AND sg.academic_period_id = p_period_id
  AND sg.subject_professor_id = (
    SELECT sp.id
    FROM subject_professors sp
    WHERE sp.subject_id = p_subject_id
      AND sp.professor_id = p_professor_id
);
END;
$$ LANGUAGE plpgsql;

create function get_academic_level_report()
    returns TABLE(group_id integer, level_name text, group_name text, status_name text, students_total bigint)
    language plpgsql
as
$$
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
$$;

alter function get_academic_level_report() owner to postgres;

create function get_attendance_report()
    returns TABLE(group_id integer, level_name text, group_name text, section_name text, total_active bigint, present_count bigint, absent_count bigint, late_count bigint, last_record timestamp without time zone)
    language plpgsql
as
$$
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
$$;

alter function get_attendance_report() owner to postgres;


create function get_groups_by_period(p_subject_id integer, p_period_id integer, p_level_id integer)
    returns TABLE(group_id integer, group_code character varying, group_name character varying, mentor_id integer)
    language plpgsql
as
$$
BEGIN
RETURN QUERY
SELECT g.id, g.group_code, g.group_name, g.mentor_id
FROM groups g
         JOIN subject_groups sg ON g.id = sg.group_students
         JOIN subject_professors sp ON sg.subject_professor_id = sp.id
WHERE sp.subject_id = p_subject_id
  AND sg.academic_period_id = p_period_id
  AND g.level_id = p_level_id
  AND g.status = 'A';
END;
$$;

alter function get_groups_by_period(integer, integer, integer) owner to postgres;

create function get_repeating_students_report_by_group()
    returns TABLE(group_id integer, group_name character varying, level_name character varying, repeating_count bigint)
    language plpgsql
as
$$
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
$$;

alter function get_repeating_students_report_by_group() owner to postgres;

create function get_subject_schedules(p_group_id integer, p_period_id integer, p_subject_id integer, p_professor_id integer) returns SETOF subject_schedule
    language plpgsql
as
$$
BEGIN
RETURN QUERY
SELECT
    ss.*
FROM
    subject_schedule ss
        JOIN
    subject_groups sg ON ss.subject_group_id = sg.id
        JOIN
    groups g ON sg.group_students = g.id
WHERE
    g.id = p_group_id
  AND sg.academic_period_id = p_period_id
  AND sg.subject_professor_id = (
    SELECT sp.id
    FROM subject_professors sp
    WHERE sp.subject_id = p_subject_id
      AND sp.professor_id = p_professor_id
);
END;
$$;

alter function get_subject_schedules(integer, integer, integer, integer) owner to postgres;

create function obtener_familias()
    returns TABLE(codigo text, familia text, miembros bigint, estudiantes_activos bigint, ids_estudiantes integer[])
    language plpgsql
as
$$
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
$$;

alter function obtener_familias() owner to postgres;

------------

create function update_subject_grade_after_activity_grade() returns trigger
    language plpgsql
as
$$
DECLARE
student_id_val INT;
    rec RECORD;
BEGIN
    student_id_val := NEW.student_id;

    -- Para todas las materias afectadas por la calificación
FOR rec IN (
        WITH activity_scores AS (
            -- Paso 1: Obtener todas las calificaciones con sus relaciones completas
            SELECT
                ag.student_id,
                ag.score,
                sk.id_subject,
                achg.period_id,
                sk.id_knowledge,
                k.percentage  -- Ahora confirmado que existe en la tabla knowledge
            FROM
                activity_grade ag
                JOIN activity_group agr ON ag.activity_id = agr.id
                JOIN activity a ON agr.activity_id = a.id
                JOIN achievement_groups achg ON a.achievement_groups_id = achg.id
                JOIN subject_knowledge sk ON achg.subject_knowledge_id = sk.id
                JOIN knowledge k ON sk.id_knowledge = k.id
            WHERE
                ag.student_id = student_id_val
        ),

        -- Paso 2: Calcular el promedio por cada conocimiento
        knowledge_averages AS (
            SELECT
                id_subject,
                period_id,
                id_knowledge,
                percentage,
                AVG(score) AS avg_knowledge_score
            FROM
                activity_scores
            GROUP BY
                id_subject, period_id, id_knowledge, percentage
        ),

        -- Paso 3: Calcular la suma ponderada por materia
        subject_scores AS (
            SELECT
                id_subject,
                period_id,
                SUM(avg_knowledge_score * percentage / 100) AS weighted_sum,
                SUM(percentage) AS total_percentage
            FROM
                knowledge_averages
            GROUP BY
                id_subject, period_id
        )

        -- Paso 4: Normalizar la calificación final (escala 0-5)
        SELECT
            id_subject AS subject_id,
            period_id,
            CASE
                WHEN total_percentage > 0 THEN ROUND((weighted_sum * 5 / total_percentage)::numeric, 2)
                ELSE 0
            END AS final_score
        FROM
            subject_scores
    ) LOOP
        -- Actualizar o insertar en subject_grade
UPDATE subject_grade
SET total_score = rec.final_score
WHERE student_id = student_id_val
  AND subject_id = rec.subject_id
  AND period_id = rec.period_id;

IF NOT FOUND THEN
            INSERT INTO subject_grade (subject_id, student_id, period_id, comment, total_score, recovered)
            VALUES (rec.subject_id, student_id_val, rec.period_id, 'Calificación generada automáticamente', rec.final_score, 'N');
END IF;
END LOOP;

RETURN NEW;
END;
$$;

alter function update_subject_grade_after_activity_grade() owner to postgres;

------
---- v2
create function update_subject_grade_after_activity_grade() returns trigger
    language plpgsql
as
$$
DECLARE
student_id_val INT;
    rec RECORD;
BEGIN
    student_id_val := NEW.student_id;

    -- Para todas las materias afectadas por la calificación
FOR rec IN (
        WITH activity_scores AS (
            -- Paso 1: Obtener todas las calificaciones con sus relaciones completas
            SELECT
                ag.student_id,
                ag.score,
                sk.id_subject,
                achg.period_id,
                sk.id_knowledge,
                k.percentage  -- Ahora confirmado que existe en la tabla knowledge
            FROM
                activity_grade ag
                    JOIN activity_group agr ON ag.activity_id = agr.id
                    JOIN activity a ON agr.activity_id = a.id
                    JOIN achievement_groups achg ON a.achievement_groups_id = achg.id
                    JOIN subject_knowledge sk ON achg.subject_knowledge_id = sk.id
                    JOIN knowledge k ON sk.id_knowledge = k.id
            WHERE
                ag.student_id = student_id_val
        ),

             -- Paso 2: Calcular el promedio por cada conocimiento
             knowledge_averages AS (
                 SELECT
                     id_subject,
                     period_id,
                     id_knowledge,
                     percentage,
                     AVG(score) AS avg_knowledge_score
                 FROM
                     activity_scores
                 GROUP BY
                     id_subject, period_id, id_knowledge, percentage
             ),

             -- Paso 3: Calcular la suma ponderada por materia
             subject_scores AS (
                 SELECT
                     id_subject,
                     period_id,
                     SUM(avg_knowledge_score * percentage / 100) AS weighted_sum,
                     SUM(percentage) AS total_percentage
                 FROM
                     knowledge_averages
                 GROUP BY
                     id_subject, period_id
             )

        -- Paso 4: Normalizar la calificación final (escala 0-5)
        SELECT
            id_subject AS subject_id,
            period_id,
            CASE
                WHEN total_percentage > 0 THEN ROUND((weighted_sum * 5 / total_percentage)::numeric, 2)
                ELSE 0
                END AS final_score
        FROM
            subject_scores
    ) LOOP
            -- Actualizar o insertar en subject_grade
UPDATE subject_grade
SET total_score = rec.final_score
WHERE student_id = student_id_val
  AND subject_id = rec.subject_id
  AND period_id = rec.period_id;

IF NOT FOUND THEN
                INSERT INTO subject_grade (subject_id, student_id, period_id, comment, total_score, recovered)
                    VALUES (rec.subject_id, student_id_val, rec.period_id, 'Calificación generada automática', COALESCE(rec.final_score, 0), 'N');
END IF;
END LOOP;

RETURN NEW;
END;
$$;

alter function update_subject_grade_after_activity_grade() owner to postgres;

------