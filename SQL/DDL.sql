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
                               maximum_grade int
);

-- Table: academic_period
CREATE TABLE academic_period (
                                 id int primary key generated always as identity,
                                 setting_id int not null references grade_settings(id),
                                 start_date DATE NOT NULL,
                                 end_date DATE NOT NULL,
                                 name VARCHAR(8) NOT NULL,
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
                                group_id int not null references groups(id)
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
                                group_students int not  null references groups(id)
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
                            schedule_id INT NOT NULL REFERENCES subject_schedule(id), -- Specific schedule
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
                                score NUMERIC(5, 2) NOT NULL,
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
                                  situation TEXT NOT NULL, -- Behavior description
                                  compromise TEXT NOT NULL, -- Commitment
                                  follow_up TEXT NOT NULL, -- Follow-up
                                  status VARCHAR(1) NOT NULL
);


-------------------------------------------------------------------------------------------------------------------------
------------------------------------------ [ ADMINISTRACIÓN ] -----------------------------------------------------------

-- ( PARA SETEAR EL SISTEMA CORRECTAMENTE, HAY QUE SEGUIR LOS SIGUIENTES PASOS ADMINISTRATIVOS )

-- 1. Creación de periodos: Nombre del periodo -> Decidir fecha inicio y fecha fin = Crear
-- 2. Creación de nivel educativo: Nombre del nivel = Crear
-- 3. Configuración del sistema de notas: Seleccionar periodo -> Seleccionar nivel educativo -> Seleccionar nota maxima, minima y pasable = Crear
-- 4. Creación de dimensiones: Nombre de la dimensión y descripción = Crear
-- 5. Creación de saberes: Nombre del saber -> Descripción -> Porcentaje = Crear
-- 6. Creación de materias: Nombre de la materia -> Asignar Profesor -> Asignar dimension = Crear (Saberes se asocian automáticamente)
-- 7. Creación de logro (descripción del saber) : Seleccionar materia -> Administrar -> Saberes -> Editar -> Rellenar logro = Crear
-- 8. Creación de horarios: Seleccionar materia -> Seleccionar curso -> Seleccionar franja horaria = Crear
-- 9. Creación de grupos (cursos de estudiantes | 1a, 1,b etc..): Crear curso -> Nombre del curso -> Asignar estudiantes (Opcional) = Crear

-- (). Editar definitivas (Si se necesita) -> Entrar a materia -> Calificaciones -> En la columna de definitiva -> Editar = Crear

-- (). Abrir periodo (Para profesores): -> Configuración -> Periodo -> Abrir periodo = Crear
-------------------------------------------------------------------------------------------------------------------------
------------------------------------------ [ ACTIVIDADES O TAREAS ] -----------------------------------------------------------

-- 1. Creación de tareas: Entrar al apartado de tareas -> Crear tarea -> Rellenar datos -> Asignar Cursos = Crear
-- 2. Toma de asistencia: Entrar a materia -> Estudiantes -> Asistencia -> Seleccionar clase -> Rellenar asistencia = Crear
-- 3. Registro de notas (actividades): Entrar a materia -> Actividades -> Seleccionar actividad -> Rellenar notas = Crear
