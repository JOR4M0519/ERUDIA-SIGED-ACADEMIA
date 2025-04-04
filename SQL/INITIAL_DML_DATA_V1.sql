--1 Table: id_type
CREATE TABLE id_type (
                         id int primary key generated always as identity,
                         name VARCHAR(20) NOT NULL
);

-- Table: users
CREATE TABLE users (
                       id int primary key generated always as identity,
                       username VARCHAR(30) NOT NULL UNIQUE,
                       email VARCHAR(256) NOT NULL UNIQUE,
                       password VARCHAR(256) NOT NULL,
                       first_name VARCHAR(40),
                       last_name VARCHAR(40),
                       uuid VARCHAR(256),
                       status varchar(1) NOT NULL
);

-- Table: user_detail
CREATE TABLE user_detail (
                             id int primary key generated always as identity,
                             user_id INT NOT NULL REFERENCES users(id),
                             first_name VARCHAR(40),
                             middle_name VARCHAR(40),
                             last_name VARCHAR(40),
                             second_last_name VARCHAR(40),
                             address VARCHAR(30) NOT NULL,
                             phone_number VARCHAR(20) NOT NULL,
                             date_of_birth DATE NOT NULL,
                             dni VARCHAR(20) NOT NULL,
                             id_type_id INT NOT NULL REFERENCES id_type(id),
                             neighborhood VARCHAR(20) NOT NULL,
                             city VARCHAR(20) NOT NULL,
                             promotionStatus VARCHAR(2) DEFAULT 'A',
                             position_job VARCHAR(40)
);

-- Table: role
CREATE TABLE role (
                      id int primary key generated always as identity,
                      role_name VARCHAR(20) NOT NULL,
                      status BOOLEAN NOT NULL
);

-- Table: user_role
CREATE TABLE user_role (
                           id int primary key generated always as identity,
                           user_id INT NOT NULL REFERENCES users(id),
                           role_id INT NOT NULL REFERENCES role(id)
);

-- Table: permission
CREATE TABLE permission (
                            id int primary key generated always as identity,
                            permission TEXT NOT NULL,
                            status BOOLEAN NOT NULL
);

-- Table: role_perm
CREATE TABLE role_perm (
                           id int primary key generated always as identity,
                           role_id INT NOT NULL REFERENCES role(id) ,
                           permission_id INT NOT NULL REFERENCES permission(id)
);

-- Table: audit_log
CREATE TABLE audit_log (
                           id int primary key generated always as identity,
                           table_name VARCHAR(20) NOT NULL,
                           operation VARCHAR(20) NOT NULL,
                           record_id INT NOT NULL,
                           changed_at TIMESTAMPTZ DEFAULT now(),
                           changed_by INT NOT NULL,
                           ip VARCHAR(20) NOT NULL
);

CREATE TABLE relationship(
                             id int not null primary key generated always as identity,
                             relationship_type varchar(10)
);

CREATE TABLE family(
                       id int not null primary key generated always as identity,
                       student_id int not null references users(id),
                       user_id int not null references users(id),
                       relationship_id int not null references relationship(id)
);
-- Table: educational_level
CREATE TABLE educational_level (
                                   id int primary key generated always as identity,
                                   level_name VARCHAR(30) NOT NULL,
                                   status VARCHAR(1) NOT NULL DEFAULT 'A'
);


CREATE TABLE grade_settings(
                               id int not null primary key generated always as identity,
                               level_id int not null references educational_level (id),
                               minimum_grade int,
                               pass_grade int,
                               maximum_grade int,
                               name VARCHAR(40),
                               description VARCHAR(400)

);

-- Table: academic_period
CREATE TABLE academic_period (
                                 id int primary key generated always as identity,
                                 setting_id int not null references grade_settings(id),
                                 start_date DATE NOT NULL,
                                 end_date DATE NOT NULL,
                                 name VARCHAR(30) NOT NULL,
                                 percentage int,
                                 status varchar(1) NOT NULL
);


-- Table: group_students (Primero, segundo tercero)
CREATE TABLE groups (
                        id int primary key generated always as identity,
                        level_id INT NOT NULL REFERENCES educational_level(id),
                        group_code varchar(15),
                        group_name VARCHAR(50) NOT NULL,
                        mentor_id INT NOT NULL REFERENCES users(id),
                        status VARCHAR (1) NOT NULL DEFAULT 'A'
);

-- La relación / agrupación
CREATE TABLE group_students (
                                id int not null primary key generated always as identity,
                                student_id int not null references users(id),
                                group_id int not null references groups(id),
                                status VARCHAR (1) DEFAULT 'A'
);



-- Table: dimensions
CREATE TABLE dimensions (
                            id int not null primary key generated always as identity,
                            name varchar(60),
                            description text
);

-- Table: subject
CREATE TABLE subject (
                         id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                         subject_name VARCHAR(50) NOT NULL,
                         status VARCHAR(1) NOT NULL DEFAULT 'A'
);

-- Table: subject_professors
CREATE TABLE subject_professors (
                                    id int primary key generated always as identity,
                                    subject_id INT NOT NULL REFERENCES subject(id),
                                    professor_id INT NOT NULL REFERENCES users(id)
);

CREATE TABLE subject_groups (
                                id int not null primary key generated always as identity,
                                subject_professor_id int not null references subject_professors(id),
                                group_students int not  null references groups(id),
                                academic_period_id int not  null references academic_period(id)
);

-- All subjects in this table are from preschool, because they have dimensions.
CREATE TABLE subject_dimension (
                                   id int not null primary key generated always as identity,
                                   dimension_id int not null references dimensions(id),
                                   subject_id int not null references subject(id)
);

-- Needs a trigger
CREATE TABLE subject_schedule (
                                  id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                  subject_group_id INT NOT NULL REFERENCES subject_groups(id),
                                  day_of_week VARCHAR(10) NOT NULL, -- E.g., 'Monday', 'Tuesday', etc.
                                  start_time TIME NOT NULL,         -- E.g., '09:00'
                                  end_time TIME NOT NULL,           -- E.g., '11:00'
                                  status VARCHAR(1) NOT NULL           -- To indicate if the schedule is active or not
);

-- Table: attendance
CREATE TABLE attendance (
                            id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                            student_id INT NOT NULL REFERENCES users(id),        -- The student who attends
                            schedule_id INT REFERENCES subject_schedule(id), -- Specific schedule
                            attendance_date DATE NOT NULL,                       -- Date of attendance
                            status VARCHAR(2) NOT NULL,                          -- Attendance status ('Present', 'Absent', 'Late', etc.)
                            recorded_at TIMESTAMPTZ DEFAULT now()                -- Date and time of record
);

-- Table: knowledge
CREATE TABLE knowledge (
                           id int primary key generated always as identity,
                           name varchar(10) default null,
                           percentage int,
                           status varchar(1) DEFAULT 'A'
);

-- Table: subject_knowledge
CREATE TABLE subject_knowledge (
                                   id int not null primary key generated always as identity,
                                   id_subject int not null references subject(id),
                                   id_knowledge int not null references knowledge(id)
);


CREATE TABLE achievement_groups(
                                   id int not null primary key generated always as identity,
                                   subject_knowledge_id int not null references subject_knowledge(id),
                                   achievement text not null ,
                                   group_id int not null references groups(id),
                                   period_id int not null references academic_period(id)

);

-- Table: activity
CREATE TABLE activity (
                          id int primary key generated always as identity,
                          activity_name VARCHAR(50) NOT NULL,
                          description TEXT NOT NULL,
                          achievement_groups_id int not null references achievement_groups(id),
                          status varchar(1) NOT NULL
);

-- Table: activity_group
CREATE TABLE activity_group(
                               id int primary key generated always as identity,
                               activity_id INT NOT NULL REFERENCES activity(id),
                               group_id INT NOT NULL REFERENCES group_students(id),
                               start_date DATE NOT NULL,
                               end_date DATE
);

-- Table: grades
CREATE TABLE activity_grade (
                                id int primary key generated always as identity,
                                student_id INT NOT NULL REFERENCES users(id),
                                activity_id INT NOT NULL REFERENCES activity_group(id),
                                score NUMERIC(5, 2),
                                comment TEXT
);

CREATE TABLE subject_grade (
                               id int not null primary key generated always as identity,
                               subject_id INT NOT NULL REFERENCES subject(id),
                               student_id INT NOT NULL REFERENCES users(id),
                               period_id INT NOT NULL REFERENCES academic_period(id),
                               comment varchar,
                               total_score NUMERIC (5,2) NOT NULL,
                               recovered VARCHAR(1)
);

-- Table: recovery_period
CREATE TABLE recovery_period(
                                id int not null primary key generated always as identity,
                                subject_grade INT NOT NULL REFERENCES subject_grade(id),
                                previous_score NUMERIC(5, 2) NOT NULL
);

-- Table: institution
CREATE TABLE institution (
                             id int primary key generated always as identity,
                             name VARCHAR(30) NOT NULL,
                             nit VARCHAR(15) NOT NULL UNIQUE,
                             address TEXT NOT NULL,
                             phone_number VARCHAR(20) NOT NULL,
                             email VARCHAR(256) NOT NULL,
                             city TEXT NOT NULL,
                             department TEXT NOT NULL,
                             country TEXT NOT NULL,
                             postal_code VARCHAR(10),
                             legal_representative VARCHAR(30) NOT NULL,
                             incorporation_date DATE NOT NULL DEFAULT now()
);

-- Table: backup_history
CREATE TABLE backup_history (
                                id int primary key generated always as identity,
                                backup_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                backup_name VARCHAR(255) NOT NULL,
                                description TEXT,
                                file_path TEXT NOT NULL,
                                created_by INT NOT NULL REFERENCES users(id)
);

CREATE TABLE tracking_type(
                              id int primary key generated always as identity,
                              type varchar(1)
);

-- Table: student_tracking
CREATE TABLE student_tracking (
                                  id int primary key generated always as identity,
                                  student INT NOT NULL REFERENCES users(id),
                                  professor INT NOT NULL REFERENCES users(id),
                                  period_id INT NOT NULL REFERENCES academic_period(id),
                                  tracking_type INT NOT NULL REFERENCES tracking_type(id),
                                  date DATE NOT NULL DEFAULT now(),
                                  situation TEXT NOT NULL, -- Behavior description
                                  compromise TEXT NOT NULL, -- Commitment
                                  follow_up TEXT NOT NULL, -- Follow-up
                                  status VARCHAR(1) NOT NULL
);

-----------------

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

---- v3
create or replace function update_subject_grade_after_activity_grade() returns trigger
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
                k.percentage
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
SET total_score = COALESCE(rec.final_score, 0)  -- Aquí está el cambio principal
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


------
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
update subject_grade sg set recovered = 'Y', total_score = newScore where sg.subject_id = idSubject and student_id = idStudent and sg.period_id = idPeriod returning id into subjectGradeId;
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

-- auto-generated definition
create trigger after_activity_grade_change
    after insert or update
                        on activity_grade
                        for each row
                        execute procedure update_subject_grade_after_activity_grade();

-- Primero, creamos la función que ejecutará el trigger
CREATE OR REPLACE FUNCTION create_default_subject_schedule()
RETURNS TRIGGER AS $$
BEGIN
    -- Insertamos un registro en subject_schedule con valores predeterminados
INSERT INTO subject_schedule (
    subject_group_id,
    day_of_week,
    start_time,
    end_time,
    status
) VALUES (
             NEW.id,                -- El ID del subject_group recién insertado
             'Lunes',               -- Día predeterminado
             '08:00:00'::TIME,      -- Hora de inicio predeterminada
             '09:00:00'::TIME,      -- Hora de fin predeterminada
             'A'                    -- Estado activo por defecto (A = Activo)
         );

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Luego, creamos el trigger que llama a esta función
CREATE TRIGGER after_insert_subject_group
    AFTER INSERT ON subject_groups
    FOR EACH ROW
    EXECUTE FUNCTION create_default_subject_schedule();



------------------------------------
-----------DATA---------------------
------------------------------------
-- 1. Tabla: id_type
INSERT INTO id_type (name) VALUES
                               ('Ciudadanía'),
                               ('Extranjería'),
                               ('Identidad'),
                               ('Pasaporte'),
                               ('Registro Civil');

-- 2. La tabla users ya contenía registros, no se modifica.
INSERT INTO users (username, email, password, uuid, status,first_name, last_name) VALUES
                                                                                      ('rectoria', 'soporteyopal@gimnasiolorismalaguzzi.edu.co', 'hashed_password', 'cf3fb2ce-5ed0-4f10-beda-2d40999e0138', 'A','Rectoría', 'Gimnasio' ),
                                                                                      ('profesor','profesor@gmail.com','hashed_password','6e022f93-d0e9-45df-8616-3448bac4dd31','A','profesor','Gimnasio');
--('juanita.perez', 'jdramos10000@gmail.com', 'hashed_password', '47928edd-60b4-4b61-97c8-285e15d0c867', 'A','Juanita', 'Andrea'),
--('nicolas.rodriguez', 'jdramos100@gmail.com', 'hashed_password', '8a00719b-f1ec-4332-b187-5826278addc6', 'A','Nicolás', 'Fernando'),
--('pepito.perez', 'jdramos1000@gmail.com', 'hashed_password', '13644306-4de2-440c-89bd-8cec1ee2ab41', 'A','Pepito', 'José'),


-- 3. Tabla: user_detail
INSERT INTO user_detail (user_id, first_name, middle_name, last_name, second_last_name, address, phone_number, date_of_birth, dni, id_type_id, neighborhood, city, position_job, promotionStatus)
VALUES
    (1, 'Rectoría', NULL, 'Gimnasio', 'Admin', 'Sede Principal', '6012345678', '2000-01-01', '999999999', 4, 'Centro', 'Yopal', 'administrador',NULL),
    (2, 'Profesor', NULL, 'Gimnasio', 'Loris', 'Sede Administrativa', '6018765432', '1999-12-25', '888888888', 5, 'Suba', 'Bogotá', 'administrador',NULL);
--(3, 'Juanita', 'Andrea', 'Pérez', 'Rodríguez', 'Calle 123', '3124567890', '1990-05-14', '123456789', 1, 'Centro', 'Bogotá', 'estudiante','A'),
--(4, 'Nicolás', 'Fernando', 'Rodríguez', 'Lopez', 'Carrera 45', '3201234567', '1985-07-20', '987654321', 2, 'Chapinero', 'Bogotá', 'estudiante','A'),
--(5, 'Pepito', 'José', 'Pérez', 'Gómez', 'Avenida 56', '3109876543', '1992-11-05', '456123789', 3, 'Teusaquillo', 'Bogotá', 'estudiante','A'),

-- 4. Tabla: role
INSERT INTO role (role_name, status) VALUES
                                         ('administrador', TRUE),
                                         ('profesor', TRUE),
                                         ('estudiante', TRUE),
                                         ('usuario', TRUE);

-- 5. Tabla: user_role
INSERT INTO user_role (user_id, role_id) VALUES
                                             (1, 1),
                                             (2, 2);

INSERT INTO institution (name, nit, address, phone_number, email, city, department, country, postal_code, legal_representative, incorporation_date) VALUES
    ('Gimnasio Loris Malaguzzi', '900123456-7', 'Carrera 7 #123-45', '6012345678', 'info@gimnasiolorismalaguzzi.edu.co', 'Bogotá', 'Cundinamarca', 'Colombia', '110111', 'Carlos Pérez', '2010-01-15');

-- 33. Tabla: tracking_type
INSERT INTO tracking_type (type) VALUES
                                     ('A'), -- Académico
                                     ('P'); -- Comportamental