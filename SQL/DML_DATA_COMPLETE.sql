-- 1. Tipos de identificación
INSERT INTO id_type (name) VALUES
    ('CC'), ('TI'), ('CE'), ('PA'), ('NIT'),
    ('RC'), ('DNI'), ('PASAPORTE'), ('CÉDULA EXTRANJERÍA'), ('VISA'),
    ('PERMISO ESPECIAL'), ('TARJETA MILITAR'), ('CARNÉ DIPLOMÁTICO'), ('REGISTRO CIVIL'), ('GREEN CARD'),
    ('PERMISO TEMPORAL'), ('CARNÉ REFUGIADO'), ('ID NACIONAL'), ('PERMISO TRABAJO'), ('CARNÉ ESTUDIANTE');

-- 2. La tabla users ya contenía registros, no se modifica.
INSERT INTO users (username, email, password, uuid, status,first_name, last_name) VALUES
                                                                                      ('juanita.perez', 'jdramos10000@gmail.com', 'hashed_password', '47928edd-60b4-4b61-97c8-285e15d0c867', 'A','Juanita', 'Andrea'),
                                                                                      ('nicolas.rodriguez', 'jdramos100@gmail.com', 'hashed_password', '8a00719b-f1ec-4332-b187-5826278addc6', 'A','Nicolás', 'Fernando'),
                                                                                      ('pepito.perez', 'jdramos1000@gmail.com', 'hashed_password', '13644306-4de2-440c-89bd-8cec1ee2ab41', 'A','Pepito', 'José'),
                                                                                      ('rectoria', 'soporteyopal@gimnasiolorismalaguzzi.edu.co', 'hashed_password', 'cf3fb2ce-5ed0-4f10-beda-2d40999e0138', 'A','Rectoría', NULL),
                                                                                      ('admin', 'admin@gimnasiolorismalaguzzi.edu.co', 'hashed_password', 'admin-uuid-001', 'A','Admin', NULL);

-- Creación de usuarios estudiantes
INSERT INTO users (username, email, password, first_name, last_name, status) VALUES
                                                                                 ('carlos.gomez', 'carlos.gomez@mail.com', 'pass123', 'Carlos', 'Gómez', 'A'),  -- ID 4
                                                                                 ('maria.rodriguez', 'maria.rodriguez@mail.com', 'pass123', 'María', 'Rodríguez', 'A'),  -- ID 5
                                                                                 ('juan.martinez', 'juan.martinez@mail.com', 'pass123', 'Juan', 'Martínez', 'A'),  -- ID 6
                                                                                 ('ana.lopez', 'ana.lopez@mail.com', 'pass123', 'Ana', 'López', 'A'),  -- ID 7
                                                                                 ('diego.torres', 'diego.torres@mail.com', 'pass123', 'Diego', 'Torres', 'A'),  -- ID 8
                                                                                 ('laura.ruiz', 'laura.ruiz@mail.com', 'pass123', 'Laura', 'Ruiz', 'A'),  -- ID 9
                                                                                 ('santiago.sanchez', 'santiago.sanchez@mail.com', 'pass123', 'Santiago', 'Sánchez', 'A'),  -- ID 10
                                                                                 ('valentina.ramirez', 'valentina.ramirez@mail.com', 'pass123', 'Valentina', 'Ramírez', 'A'),  -- ID 11
                                                                                 ('daniel.castro', 'daniel.castro@mail.com', 'pass123', 'Daniel', 'Castro', 'A'); -- ID 12

-- 3. Tabla: user_detail
INSERT INTO user_detail (user_id, first_name, middle_name, last_name, second_last_name, address, phone_number, date_of_birth, dni, id_type_id, neighborhood, city, position_job)
VALUES
    (1, 'Juanita', 'Andrea', 'Pérez', 'Rodríguez', 'Calle 123', '3124567890', '1990-05-14', '123456789', 1, 'Centro', 'Bogotá', 'estudiante'),
    (2, 'Nicolás', 'Fernando', 'Rodríguez', 'Lopez', 'Carrera 45', '3201234567', '1985-07-20', '987654321', 2, 'Chapinero', 'Bogotá', 'teacher'),
    (3, 'Pepito', 'José', 'Pérez', 'Gómez', 'Avenida 56', '3109876543', '1992-11-05', '456123789', 3, 'Teusaquillo', 'Bogotá', 'estudiante'),
    (4, 'Rectoría', NULL, 'Gimnasio', 'Loris Malaguzzi', 'Sede Principal', '6012345678', '2000-01-01', '999999999', 4, 'Centro', 'Yopal', 'administrador'),
    (5, 'Admin', NULL, 'Gimnasio', 'Admin', 'Sede Administrativa', '6018765432', '1999-12-25', '888888888', 5, 'Suba', 'Bogotá', 'administrador');

-- Creación de detalles de usuarios para los estudiantes
INSERT INTO user_detail(user_id, first_name, middle_name, last_name, second_last_name, address, phone_number, date_of_birth, dni, id_type_id, neighborhood, city) VALUES
                                                                                                                                                                       (6, 'Carlos', 'Andrés', 'Gómez', 'Pérez', 'Calle 456', '3102345678', '2019-05-15', '234567891', 1, 'Santa Fe', 'Bogotá'),
                                                                                                                                                                       (7, 'María', 'José', 'Rodríguez', 'López', 'Calle 567', '3153456789', '2019-03-20', '345678912', 1, 'Chapinero', 'Bogotá'),
                                                                                                                                                                       (8, 'Juan', 'David', 'Martínez', 'García', 'Calle 678', '3164567890', '2018-07-10', '456789123', 1, 'Suba', 'Bogotá'),
                                                                                                                                                                       (9, 'Ana', 'Sofía', 'López', 'Torres', 'Calle 789', '3175678901', '2018-09-25', '567891234', 1, 'Usaquén', 'Bogotá'),
                                                                                                                                                                       (10, 'Diego', 'Alejandro', 'Torres', 'Ruiz', 'Calle 890', '3186789012', '2017-11-30', '678912345', 1, 'Engativá', 'Bogotá'),
                                                                                                                                                                       (11, 'Laura', 'Valentina', 'Ruiz', 'Sánchez', 'Calle 901', '3197890123', '2017-04-05', '789123456', 1, 'Fontibón', 'Bogotá'),
                                                                                                                                                                       (12, 'Santiago', NULL, 'Sánchez', 'Ramírez', 'Calle 012', '3208901234', '2016-08-20', '891234567', 1, 'Teusaquillo', 'Bogotá'),
                                                                                                                                                                       (13, 'Valentina', NULL, 'Ramírez', 'Castro', 'Calle 123', '3219012345', '2016-02-15', '912345678', 1, 'Kennedy', 'Bogotá'),
                                                                                                                                                                       (14, 'Daniel', 'Felipe', 'Castro', 'Morales', 'Calle 234', '3220123456', '2015-06-10', '123456789', 1, 'Bosa', 'Bogotá');

-- 2. Niveles educativos
INSERT INTO educational_level (level_name, status) VALUES
    ('MATERNAL', 'A'), ('CAMINADORES', 'A'), ('PÁRVULOS', 'A'), ('PREJARDÍN', 'A'), ('JARDÍN', 'A'),
    ('TRANSICIÓN', 'A'), ('PRIMERO', 'A'), ('SEGUNDO', 'A'), ('TERCERO', 'A'), ('CUARTO', 'A'),
    ('QUINTO', 'A'), ('SEXTO', 'A'), ('SÉPTIMO', 'A'), ('OCTAVO', 'A'), ('NOVENO', 'A'),
    ('DÉCIMO', 'A'), ('UNDÉCIMO', 'A'), ('PREESCOLAR GENERAL', 'A'), ('PRIMARIA GENERAL', 'A'), ('BACHILLERATO GENERAL', 'A');

-- 3. Configuración de notas por nivel
INSERT INTO grade_settings (level_id, minimum_grade, pass_grade, maximum_grade) VALUES
    (1, 0, 3, 5), (2, 0, 3, 5), (3, 0, 3, 5), (4, 0, 3, 5), (5, 0, 3, 5),
    (6, 0, 3, 5), (7, 0, 3, 5), (8, 0, 3, 5), (9, 0, 3, 5), (10, 0, 3, 5),
    (11, 0, 3, 5), (12, 0, 3, 5), (13, 0, 3, 5), (14, 0, 3, 5), (15, 0, 3, 5),
    (16, 0, 3, 5), (17, 0, 3, 5), (18, 0, 3, 5), (19, 0, 3, 5), (20, 0, 3, 5);

-- 4. Periodos académicos
INSERT INTO academic_period (setting_id, start_date, end_date, name, status) VALUES
    (1, '2025-01-15', '2025-03-15', '2025-1P1', 'A'),
    (1, '2025-03-16', '2025-05-15', '2025-1P2', 'A'),
    (1, '2025-05-16', '2025-07-15', '2025-2P1', 'A'),
    (1, '2025-07-16', '2025-09-15', '2025-2P2', 'A'),
    (2, '2025-01-15', '2025-03-15', '2025-1P1', 'A'),
    (2, '2025-03-16', '2025-05-15', '2025-1P2', 'A'),
    (2, '2025-05-16', '2025-07-15', '2025-2P1', 'A'),
    (2, '2025-07-16', '2025-09-15', '2025-2P2', 'A'),
    (3, '2025-01-15', '2025-03-15', '2025-1P1', 'A'),
    (3, '2025-03-16', '2025-05-15', '2025-1P2', 'A'),
    (3, '2025-05-16', '2025-07-15', '2025-2P1', 'A'),
    (3, '2025-07-16', '2025-09-15', '2025-2P2', 'A'),
    (4, '2025-01-15', '2025-03-15', '2025-1P1', 'A'),
    (4, '2025-03-16', '2025-05-15', '2025-1P2', 'A'),
    (4, '2025-05-16', '2025-07-15', '2025-2P1', 'A'),
    (4, '2025-07-16', '2025-09-15', '2025-2P2', 'A'),
    (5, '2025-01-15', '2025-03-15', '2025-1P1', 'A'),
    (5, '2025-03-16', '2025-05-15', '2025-1P2', 'A'),
    (5, '2025-05-16', '2025-07-15', '2025-2P1', 'A'),
    (5, '2025-07-16', '2025-09-15', '2025-2P2', 'A');

-- 5. Dimensiones
INSERT INTO dimensions (name, description) VALUES
    ('Cognitiva', 'Desarrollo del pensamiento y procesos mentales'),
    ('Comunicativa', 'Expresión y comprensión del lenguaje'),
    ('Corporal', 'Desarrollo físico y motriz'),
    ('Estética', 'Sensibilidad y expresión artística'),
    ('Ética', 'Valores y comportamiento moral'),
    ('Socioafectiva', 'Desarrollo emocional y social'),
    ('Espiritual', 'Desarrollo de la dimensión trascendental'),
    ('Digital', 'Competencias tecnológicas');

-- 6. Saberes (Knowledge)
INSERT INTO knowledge (name, percentage, status) VALUES
    ('HACER', 25, 'A'),
    ('SER', 25, 'A'),
    ('CONOCER', 25, 'A'),
    ('PENSAR', 15, 'A'),
    ('INNOVAR', 5, 'A'),
    ('CREAR', 5, 'A');


-- 7. Materias
INSERT INTO subject (subject_name, status) VALUES
    ('Matemáticas', 'A'),
    ('Lenguaje', 'A'),
    ('Ciencias Naturales', 'A'),
    ('Ciencias Sociales', 'A'),
    ('Educación Física', 'A'),
    ('Artes', 'A'),
    ('Música', 'A'),
    ('Inglés', 'A'),
    ('Tecnología', 'A'),
    ('Ética y Valores', 'A'),
    ('Religión', 'A'),
    ('Danza', 'A'),
    ('Teatro', 'A'),
    ('Informática', 'A'),
    ('Química', 'A'),
    ('Física', 'A'),
    ('Filosofía', 'A'),
    ('Educación Ambiental', 'A'),
    ('Literatura', 'A'),
    ('Geometría', 'A');

-- 8. Roles
INSERT INTO role (role_name, status) VALUES
                                         ('ADMIN', true),
                                         ('RECTOR', true),
                                         ('TEACHER', true),
                                         ('STUDENT', true),
                                         ('PARENT', true),
                                         ('COORDINATOR', true),
                                         ('SECRETARY', true),
                                         ('COUNSELOR', true),
                                         ('LIBRARIAN', true),
                                         ('NURSE', true),
                                         ('MAINTENANCE', true),
                                         ('SECURITY', true),
                                         ('CAFETERIA', true),
                                         ('TRANSPORT', true),
                                         ('ACCOUNTING', true),
                                         ('IT_SUPPORT', true),
                                         ('SUPERVISOR', true),
                                         ('ASSISTANT', true),
                                         ('SUBSTITUTE', true),
                                         ('VOLUNTEER', true);

-- 9. Permisos
INSERT INTO permission (permission, status) VALUES
                                                ('CREATE_USER', true),
                                                ('EDIT_USER', true),
                                                ('DELETE_USER', true),
                                                ('VIEW_GRADES', true),
                                                ('EDIT_GRADES', true),
                                                ('MANAGE_COURSES', true),
                                                ('TAKE_ATTENDANCE', true),
                                                ('VIEW_REPORTS', true),
                                                ('MANAGE_SCHEDULE', true),
                                                ('MANAGE_ACTIVITIES', true),
                                                ('MANAGE_ASSIGNMENTS', true),
                                                ('VIEW_ATTENDANCE', true),
                                                ('MANAGE_CALENDAR', true),
                                                ('SEND_NOTIFICATIONS', true),
                                                ('ACCESS_LIBRARY', true),
                                                ('MANAGE_INVENTORY', true),
                                                ('GENERATE_REPORTS', true),
                                                ('MANAGE_PAYMENTS', true),
                                                ('MANAGE_DOCUMENTS', true),
                                                ('SYSTEM_CONFIG', true);


-- 10. Asignación de permisos a roles
INSERT INTO role_perm (role_id, permission_id) VALUES
                                                   (1, 1), (1, 2), (1, 3), (1, 20), -- ADMIN tiene todos los permisos críticos
                                                   (2, 4), (2, 5), (2, 6), (2, 17), -- RECTOR
                                                   (3, 5), (3, 7), (3, 10), (3, 11), -- TEACHER
                                                   (4, 4), (4, 15), -- STUDENT
                                                   (5, 4), (5, 12), -- PARENT
                                                   (6, 4), (6, 5), (6, 6), (6, 9); -- COORDINATOR

-- 11. Tipos de relación familiar
INSERT INTO relationship (relationship_type) VALUES
                                                 ('FATHER'),
                                                 ('MOTHER'),
                                                 ('GUARDIAN'),
                                                 ('SIBLING'),
                                                 ('GRANDPA'),
                                                 ('GRANDMA'),
                                                 ('UNCLE'),
                                                 ('AUNT'),
                                                 ('COUSIN'),
                                                 ('STEPFATHER'),
                                                 ('STEPMOTHER'),
                                                 ('FOSTER'),
                                                 ('LEGAL_REP'),
                                                 ('CAREGIVER'),
                                                 ('TUTOR'),
                                                 ('GODFATHER'),
                                                 ('GODMOTHER'),
                                                 ('OTHER_REL'),
                                                 ('NEIGHBOR'),
                                                 ('EMERGENCY');

-- 12. Grupos escolares
INSERT INTO groups (level_id, group_code, group_name, mentor_id, status) VALUES
                                                                             (4, 'PRE-A', 'Prejardín A', 2, 'A'),
                                                                             (4, 'PRE-B', 'Prejardín B', 2, 'A'),
                                                                             (5, 'JAR-A', 'Jardín A', 2, 'A'),
                                                                             (5, 'JAR-B', 'Jardín B', 2, 'A'),
                                                                             (6, 'TRA-A', 'Transición A', 2, 'A'),
                                                                             (6, 'TRA-B', 'Transición B', 2, 'A'),
                                                                             (7, 'PRI-1A', 'Primero A', 2, 'A'),
                                                                             (7, 'PRI-1B', 'Primero B', 2, 'A'),
                                                                             (8, 'PRI-2A', 'Segundo A', 2, 'A'),
                                                                             (8, 'PRI-2B', 'Segundo B', 2, 'A'),
                                                                             (9, 'PRI-3A', 'Tercero A', 2, 'A'),
                                                                             (9, 'PRI-3B', 'Tercero B', 2, 'A'),
                                                                             (10, 'PRI-4A', 'Cuarto A', 2, 'A'),
                                                                             (10, 'PRI-4B', 'Cuarto B', 2, 'A'),
                                                                             (11, 'PRI-5A', 'Quinto A', 2, 'A'),
                                                                             (11, 'PRI-5B', 'Quinto B', 2, 'A'),
                                                                             (12, 'SEC-6A', 'Sexto A', 2, 'A'),
                                                                             (12, 'SEC-6B', 'Sexto B', 2, 'A'),
                                                                             (13, 'SEC-7A', 'Séptimo A', 2, 'A'),
                                                                             (13, 'SEC-7B', 'Séptimo B', 2, 'A');

-- Asignación de estudiantes a grupos (group_students)
INSERT INTO group_students (student_id, group_id) VALUES
                                                      -- Prejardín A y B (grupos 1 y 2)
                                                      (4, 1),  -- Carlos Gómez en Prejardín A
                                                      (5, 2),  -- María Rodríguez en Prejardín B
                                                      (3, 1),
                                                      (5, 1),

                                                      -- Jardín A y B (grupos 3 y 4)
                                                      (6, 3),  -- Juan Martínez en Jardín A
                                                      (7, 4),  -- Ana López en Jardín B

                                                      -- Transición A y B (grupos 5 y 6)
                                                      (8, 5),  -- Diego Torres en Transición A
                                                      (9, 6),  -- Laura Ruiz en Transición B

                                                      -- Primero A y B (grupos 7 y 8)
                                                      (10, 7),  -- Santiago Sánchez en Primero A
                                                      (11, 8),  -- Valentina Ramírez en Primero B

                                                      -- Segundo A y B
                                                      (12, 9);  -- Daniel Castro en Segundo A


-- 13. Asignación de materias a dimensiones (solo preescolar)
INSERT INTO subject_dimension (dimension_id, subject_id) VALUES
                                                             (1, 1), -- Matemáticas - Cognitiva
                                                             (2, 2), -- Lenguaje - Comunicativa
                                                             (3, 5), -- Educación Física - Corporal
                                                             (4, 6), -- Artes - Estética
                                                             (4, 7), -- Música - Estética
                                                             (2, 8), -- Inglés - Comunicativa
                                                             (1, 9), -- Tecnología - Cognitiva
                                                             (5, 10), -- Ética - Ética
                                                             (4, 12), -- Danza - Estética
                                                             (4, 13), -- Teatro - Estética
                                                             (1, 14), -- Informática - Cognitiva
                                                             (5, 17), -- Filosofía - Ética
                                                             (3, 18), -- Educación Ambiental - Corporal
                                                             (2, 19), -- Literatura - Comunicativa
                                                             (1, 20), -- Geometría - Cognitiva
                                                             (6, 3), -- Ciencias Naturales - Socioafectiva
                                                             (6, 4), -- Ciencias Sociales - Socioafectiva
                                                             (5, 11), -- Religión - Ética
                                                             (1, 15), -- Química - Cognitiva
                                                             (1, 16); -- Física - Cognitiva

-- 14. Asignación de saberes a materias
INSERT INTO subject_knowledge (id_subject, id_knowledge) VALUES
                                                             (1, 1), (1, 2), (1, 3), -- Matemáticas
                                                             (2, 1), (2, 2), (2, 3), -- Lenguaje
                                                             (3, 1), (3, 2), (3, 3), -- Ciencias Naturales
                                                             (4, 1), (4, 2), (4, 3), -- Ciencias Sociales
                                                             (5, 1), (5, 2), (5, 4), -- Educación Física
                                                             (6, 1), (6, 5), (6, 6), -- Artes
                                                             (7, 1), (7, 5), (7, 6), -- Música
                                                             (8, 1), (8, 2), (8, 3), -- Inglés
                                                             (9, 1), (9, 4), (9, 5), -- Tecnología
                                                             (10, 2), (10, 3), (10, 4), -- Ética
                                                             (11, 2), (11, 3), (11, 4), -- Religión
                                                             (12, 1), (12, 5), (12, 6), -- Danza
                                                             (13, 1), (13, 5), (13, 6), -- Teatro
                                                             (14, 1), (14, 4), (14, 5), -- Informática
                                                             (15, 1), (15, 3), (15, 4), -- Química
                                                             (16, 1), (16, 3), (16, 4), -- Física
                                                             (17, 2), (17, 3), (17, 4), -- Filosofía
                                                             (18, 1), (18, 2), (18, 3), -- Educación Ambiental
                                                             (19, 1), (19, 2), (19, 3), -- Literatura
                                                             (20, 1), (20, 3), (20, 4); -- Geometría

-- 15. Asignación de profesores a materias (usando el profesor existente id=2)
INSERT INTO subject_professors (subject_id, professor_id) VALUES
                                                              (1, 2), (2, 2), (3, 2), (4, 2), (5, 2),
                                                              (6, 2), (7, 2), (8, 2), (9, 2), (10, 2),
                                                              (11, 2), (12, 2), (13, 2), (14, 2), (15, 2),
                                                              (16, 2), (17, 2), (18, 2), (19, 2), (20, 2);

-- 16. Creación de grupos de materias
INSERT INTO subject_groups (subject_professor_id, group_students, academic_period_id) VALUES
                                                                                          (1, 1, 1), -- Matemáticas para Prejardín A en 2025-1P1
                                                                                          (2, 1, 1), -- Lenguaje para Prejardín A en 2025-1P1
                                                                                          (3, 1, 1), -- Ciencias Naturales para Prejardín A en 2025-1P1
                                                                                          (4, 2, 1), -- Ciencias Sociales para Prejardín B en 2025-1P1
                                                                                          (5, 2, 1), -- Educación Física para Prejardín B en 2025-1P1
                                                                                          (6, 2, 1), -- Artes para Prejardín B en 2025-1P1
                                                                                          (7, 3, 1), -- Música para Jardín A en 2025-1P1
                                                                                          (8, 3, 1), -- Inglés para Jardín A en 2025-1P1
                                                                                          (9, 3, 1), -- Tecnología para Jardín A en 2025-1P1
                                                                                          (10, 4, 1), -- Ética para Jardín B en 2025-1P1
                                                                                          (11, 4, 1), -- Religión para Jardín B en 2025-1P1
                                                                                          (12, 4, 1), -- Danza para Jardín B en 2025-1P1
                                                                                          (13, 5, 1), -- Teatro para Transición A en 2025-1P1
                                                                                          (14, 5, 1), -- Informática para Transición A en 2025-1P1
                                                                                          (15, 5, 1), -- Química para Transición A en 2025-1P1
                                                                                          (16, 6, 1), -- Física para Transición B en 2025-1P1
                                                                                          (17, 6, 1), -- Filosofía para Transición B en 2025-1P1
                                                                                          (18, 6, 1), -- Educación Ambiental para Transición B en 2025-1P1
                                                                                          (19, 7, 1), -- Literatura para Primero A en 2025-1P1
                                                                                          (20, 7, 1); -- Geometría para Primero A en 2025-1P1

-- 17. Horarios de materias
INSERT INTO subject_schedule (subject_group_id, day_of_week, start_time, end_time, status) VALUES
                                                                                               (1, 'Monday', '07:00', '08:00', 'A'),
                                                                                               (1, 'Wednesday', '07:00', '08:00', 'A'),
                                                                                               (2, 'Monday', '08:00', '09:00', 'A'),
                                                                                               (2, 'Wednesday', '08:00', '09:00', 'A'),
                                                                                               (3, 'Monday', '09:00', '10:00', 'A'),
                                                                                               (3, 'Wednesday', '09:00', '10:00', 'A'),
                                                                                               (4, 'Tuesday', '07:00', '08:00', 'A'),
                                                                                               (4, 'Thursday', '07:00', '08:00', 'A'),
                                                                                               (5, 'Tuesday', '08:00', '09:00', 'A'),
                                                                                               (5, 'Thursday', '08:00', '09:00', 'A'),
                                                                                               (6, 'Tuesday', '09:00', '10:00', 'A'),
                                                                                               (6, 'Thursday', '09:00', '10:00', 'A'),
                                                                                               (7, 'Friday', '07:00', '08:00', 'A'),
                                                                                               (8, 'Friday', '08:00', '09:00', 'A'),
                                                                                               (9, 'Friday', '09:00', '10:00', 'A'),
                                                                                               (10, 'Monday', '10:00', '11:00', 'A'),
                                                                                               (11, 'Tuesday', '10:00', '11:00', 'A'),
                                                                                               (12, 'Wednesday', '10:00', '11:00', 'A'),
                                                                                               (13, 'Thursday', '10:00', '11:00', 'A'),
                                                                                               (14, 'Friday', '10:00', '11:00', 'A');

-- 18. Logros por grupo (achievement_groups)
INSERT INTO achievement_groups (subject_knowledge_id, achievement, group_id, period_id) VALUES
                                                                                            (1, 'Realiza operaciones matemáticas básicas de suma y resta', 1, 1),
                                                                                            (2, 'Muestra interés y participación en actividades matemáticas', 1, 1),
                                                                                            (3, 'Comprende conceptos básicos de cantidad y número', 1, 1),
                                                                                            (4, 'Desarrolla habilidades de comunicación oral básica', 2, 1),
                                                                                            (5, 'Participa activamente en actividades de lectura grupal', 2, 1),
                                                                                            (6, 'Reconoce y escribe vocales y algunas consonantes', 2, 1),
                                                                                            (7, 'Identifica elementos básicos del entorno natural', 3, 1),
                                                                                            (8, 'Muestra curiosidad por fenómenos naturales simples', 3, 1),
                                                                                            (9, 'Participa en experimentos científicos básicos', 3, 1),
                                                                                            (10, 'Reconoce la importancia de las normas de convivencia', 4, 1),
                                                                                            (11, 'Desarrolla habilidades motrices básicas', 5, 1),
                                                                                            (12, 'Expresa creatividad a través del arte', 6, 1),
                                                                                            (13, 'Participa en actividades musicales grupales', 7, 1),
                                                                                            (14, 'Comprende instrucciones básicas en inglés', 8, 1),
                                                                                            (15, 'Utiliza herramientas tecnológicas básicas', 9, 1),
                                                                                            (16, 'Practica valores en la convivencia diaria', 10, 1),
                                                                                            (17, 'Participa en actividades de expresión corporal', 12, 1),
                                                                                            (18, 'Desarrolla habilidades de actuación básica', 13, 1),
                                                                                            (19, 'Comprende textos literarios simples', 19, 1),
                                                                                            (20, 'Identifica figuras geométricas básicas', 20, 1);

-- 19. Actividades
-- Continuación de actividades (40 registros adicionales)
INSERT INTO activity (activity_name, description, achievement_groups_id, status) VALUES
                                                                                     -- Actividades de Matemáticas
                                                                                     ('Conteo rítmico', 'Aprendizaje de números mediante canciones y ritmos', 1, 'A'),
                                                                                     ('Patrones con objetos', 'Creación de secuencias con objetos cotidianos', 1, 'A'),
                                                                                     ('Formas en mi entorno', 'Identificación de formas geométricas en el ambiente', 20, 'A'),
                                                                                     ('Medidas con el cuerpo', 'Medición usando partes del cuerpo', 1, 'A'),

                                                                                     -- Actividades de Lenguaje
                                                                                     ('Creación de historias', 'Inventar cuentos cortos con imágenes', 4, 'A'),
                                                                                     ('Rincón de letras', 'Exploración de letras con diferentes materiales', 4, 'A'),
                                                                                     ('Teatro de títeres', 'Narración de historias con títeres', 4, 'A'),
                                                                                     ('Poesía infantil', 'Memorización y recitación de poemas cortos', 19, 'A'),

                                                                                     -- Actividades de Ciencias Naturales
                                                                                     ('Huerta escolar', 'Siembra y cuidado de plantas', 7, 'A'),
                                                                                     ('Estados del agua', 'Experimentos con agua en diferentes estados', 7, 'A'),
                                                                                     ('Animales domésticos', 'Identificación y características de animales', 7, 'A'),
                                                                                     ('Clima diario', 'Observación y registro del clima', 7, 'A'),

                                                                                     -- Actividades de Ciencias Sociales
                                                                                     ('Mi familia', 'Presentación sobre miembros de la familia', 10, 'A'),
                                                                                     ('Oficios y profesiones', 'Exploración de diferentes trabajos', 10, 'A'),
                                                                                     ('Mi barrio', 'Reconocimiento del entorno cercano', 10, 'A'),
                                                                                     ('Símbolos patrios', 'Identificación de símbolos nacionales', 10, 'A'),

                                                                                     -- Actividades de Educación Física
                                                                                     ('Juegos cooperativos', 'Actividades grupales de cooperación', 11, 'A'),
                                                                                     ('Yoga infantil', 'Ejercicios de respiración y posturas básicas', 11, 'A'),
                                                                                     ('Baile creativo', 'Expresión corporal con diferentes ritmos', 11, 'A'),
                                                                                     ('Circuito de obstáculos', 'Desarrollo de habilidades motrices', 11, 'A'),

                                                                                     -- Actividades de Artes
                                                                                     ('Collage natural', 'Creación artística con elementos naturales', 12, 'A'),
                                                                                     ('Pintura con dedos', 'Expresión artística usando las manos', 12, 'A'),
                                                                                     ('Escultura con masa', 'Modelado de figuras con masa casera', 12, 'A'),
                                                                                     ('Estampado creativo', 'Creación de patrones con sellos', 12, 'A'),

                                                                                     -- Actividades de Música
                                                                                     ('Instrumentos caseros', 'Creación de instrumentos con material reciclado', 13, 'A'),
                                                                                     ('Coro infantil', 'Práctica de canciones grupales', 13, 'A'),
                                                                                     ('Ritmos corporales', 'Exploración de sonidos con el cuerpo', 13, 'A'),
                                                                                     ('Orquesta reciclada', 'Interpretación musical con instrumentos reciclados', 13, 'A'),

                                                                                     -- Actividades de Inglés
                                                                                     ('My family members', 'Vocabulario sobre la familia en inglés', 14, 'A'),
                                                                                     ('Colors and numbers', 'Práctica de colores y números en inglés', 14, 'A'),
                                                                                     ('Animal sounds', 'Sonidos de animales en inglés', 14, 'A'),
                                                                                     ('Action songs', 'Canciones con movimientos en inglés', 14, 'A'),

                                                                                     -- Actividades de Tecnología
                                                                                     ('Mi primera tablet', 'Introducción al uso de dispositivos táctiles', 15, 'A'),
                                                                                     ('Juegos educativos', 'Uso de aplicaciones educativas básicas', 15, 'A'),
                                                                                     ('Dibujo digital', 'Creación artística en dispositivos', 15, 'A'),
                                                                                     ('Robots simples', 'Introducción a la robótica educativa', 15, 'A'),

                                                                                     -- Actividades de Ética y Valores
                                                                                     ('Buenos modales', 'Práctica de cortesía y respeto', 16, 'A'),
                                                                                     ('Cuidado del entorno', 'Actividades de conservación ambiental', 16, 'A'),
                                                                                     ('Resolución de conflictos', 'Manejo de situaciones cotidianas', 16, 'A'),
                                                                                     ('Trabajo en equipo', 'Actividades cooperativas y solidarias', 16, 'A'),

                                                                                     -- Actividades de Danza
                                                                                     ('Bailes tradicionales', 'Aprendizaje de danzas folclóricas', 17, 'A'),
                                                                                     ('Expresión corporal', 'Movimientos libres con música', 17, 'A'),
                                                                                     ('Coreografías grupales', 'Creación de secuencias de baile', 17, 'A'),
                                                                                     ('Ritmos del mundo', 'Exploración de diferentes géneros musicales', 17, 'A');
-- 20. Asignación de actividades a grupos (activity_group)
-- Usando las fechas del primer periodo académico (2025-01-15 al 2025-03-15)
INSERT INTO activity_group (activity_id, group_id, start_date, end_date) VALUES
                                                                             -- Matemáticas (Prejardín A)
                                                                             (1, 1, '2025-01-20', '2025-01-24'),  -- Suma con bloques
                                                                             (14, 1, '2025-02-03', '2025-02-07'), -- Números mágicos
                                                                             (21, 1, '2025-02-17', '2025-02-21'), -- Conteo rítmico
                                                                             (22, 1, '2025-03-02', '2025-03-06'), -- Patrones con objetos

                                                                             -- Lenguaje (Prejardín A)
                                                                             (2, 1, '2025-01-27', '2025-01-31'),  -- Lectura grupal
                                                                             (15, 1, '2025-02-10', '2025-02-14'), -- Vocales divertidas
                                                                             (25, 1, '2025-02-24', '2025-02-28'), -- Creación de historias
                                                                             (26, 1, '2025-03-09', '2025-03-13'), -- Rincón de letras

                                                                             -- Ciencias Naturales (Prejardín A)
                                                                             (3, 1, '2025-01-20', '2025-01-24'),  -- Mini experimento
                                                                             (29, 1, '2025-02-03', '2025-02-07'), -- Huerta escolar
                                                                             (30, 1, '2025-02-17', '2025-02-21'), -- Estados del agua
                                                                             (31, 1, '2025-03-02', '2025-03-06'), -- Animales domésticos

                                                                             -- Educación Física (Prejardín B)
                                                                             (5, 2, '2025-01-27', '2025-01-31'),  -- Circuito motriz
                                                                             (17, 2, '2025-02-10', '2025-02-14'), -- Mi cuerpo en movimiento
                                                                             (37, 2, '2025-02-24', '2025-02-28'), -- Juegos cooperativos
                                                                             (38, 2, '2025-03-09', '2025-03-13'), -- Yoga infantil

                                                                             -- Artes (Jardín A)
                                                                             (6, 3, '2025-01-20', '2025-01-24'),  -- Pintura creativa
                                                                             (16, 3, '2025-02-03', '2025-02-07'), -- Colores y formas
                                                                             (41, 3, '2025-02-17', '2025-02-21'), -- Collage natural
                                                                             (42, 3, '2025-03-02', '2025-03-06'); -- Pintura con dedos

-- 21. Registro de asistencia
-- Insertando registros de asistencia para las primeras dos semanas de clase
INSERT INTO attendance (student_id, schedule_id, attendance_date, status) VALUES
                                                                              -- Semana 1: Lunes 20 de enero 2025
                                                                              (3, 1, '2025-01-20', 'P'),  -- Presente
                                                                              (4, 1, '2025-01-20', 'P'),
                                                                              (5, 1, '2025-01-20', 'T'),  -- Tardanza
                                                                              (3, 2, '2025-01-20', 'P'),
                                                                              (4, 2, '2025-01-20', 'P'),
                                                                              (5, 2, '2025-01-20', 'P'),

                                                                              -- Semana 1: Miércoles 22 de enero 2025
                                                                              (3, 1, '2025-01-22', 'P'),
                                                                              (4, 1, '2025-01-22', 'A'),  -- Ausente
                                                                              (5, 1, '2025-01-22', 'P'),
                                                                              (3, 2, '2025-01-22', 'P'),
                                                                              (4, 2, '2025-01-22', 'A'),
                                                                              (5, 2, '2025-01-22', 'P'),

                                                                              -- Semana 2: Lunes 27 de enero 2025
                                                                              (3, 1, '2025-01-27', 'P'),
                                                                              (4, 1, '2025-01-27', 'P'),
                                                                              (5, 1, '2025-01-27', 'P'),
                                                                              (3, 2, '2025-01-27', 'T'),
                                                                              (4, 2, '2025-01-27', 'P'),
                                                                              (5, 2, '2025-01-27', 'P'),

                                                                              -- Semana 2: Miércoles 29 de enero 2025
                                                                              (3, 1, '2025-01-29', 'P'),
                                                                              (4, 1, '2025-01-29', 'P'),
                                                                              (5, 1, '2025-01-29', 'A');

-- 22. Calificaciones de actividades
INSERT INTO activity_grade (student_id, activity_id, score, comment) VALUES
                                                                         -- Matemáticas: Suma con bloques
                                                                         (3, 1, 4.5, 'Excelente manejo de los bloques y comprensión de sumas'),
                                                                         (4, 1, 4.0, 'Buen trabajo en equipo y entendimiento del concepto'),
                                                                         (5, 1, 3.8, 'Necesita más práctica con números mayores'),

                                                                         -- Lenguaje: Lectura grupal
                                                                         (3, 2, 4.2, 'Participación activa y buena pronunciación'),
                                                                         (4, 2, 3.9, 'Muestra interés y sigue la lectura adecuadamente'),
                                                                         (5, 2, 4.1, 'Excelente comprensión de la historia'),

                                                                         -- Ciencias Naturales: Mini experimento
                                                                         (3, 3, 4.3, 'Gran curiosidad y participación en el experimento'),
                                                                         (4, 3, 4.4, 'Excelente seguimiento de instrucciones'),
                                                                         (5, 3, 4.0, 'Buenas observaciones durante la actividad'),

                                                                         -- Educación Física: Circuito motriz
                                                                         (3, 5, 4.6, 'Excelente coordinación y equilibrio'),
                                                                         (4, 5, 4.2, 'Buen esfuerzo y persistencia'),
                                                                         (5, 5, 4.5, 'Destacada participación y habilidad motriz'),

                                                                         -- Artes: Pintura creativa
                                                                         (3, 6, 4.7, 'Creatividad excepcional y uso de colores'),
                                                                         (4, 6, 4.3, 'Buenos detalles y expresión artística'),
                                                                         (5, 6, 4.4, 'Excelente manejo de materiales'),

                                                                         -- Números mágicos
                                                                         (3, 14, 4.4, 'Reconoce y escribe números correctamente'),
                                                                         (4, 14, 4.1, 'Buen progreso en el conteo secuencial'),
                                                                         (5, 14, 3.9, 'Necesita refuerzo en escritura de números'),

                                                                         -- Vocales divertidas
                                                                         (3, 15, 4.3, 'Excelente reconocimiento de vocales'),
                                                                         (4, 15, 4.2, 'Buena participación en actividades fonéticas'),
                                                                         (5, 15, 4.0, 'Progresa en la escritura de vocales');

-- 23. Calificaciones finales por materia
INSERT INTO subject_grade (subject_id, student_id, period_id, comment, total_score, recovered) VALUES
                                                                                                   -- Matemáticas
                                                                                                   (1, 3, 1, 'Excelente desempeño en operaciones básicas', 4.5, 'N'),
                                                                                                   (1, 4, 1, 'Buen progreso en conceptos numéricos', 4.2, 'N'),
                                                                                                   (1, 5, 1, 'Necesita refuerzo en resolución de problemas', 3.9, 'N'),

                                                                                                   -- Lenguaje
                                                                                                   (2, 3, 1, 'Destacada participación en actividades de lectura', 4.3, 'N'),
                                                                                                   (2, 4, 1, 'Buena comprensión lectora', 4.1, 'N'),
                                                                                                   (2, 5, 1, 'Progresa adecuadamente en escritura', 4.0, 'N'),

                                                                                                   -- Ciencias Naturales
                                                                                                   (3, 3, 1, 'Gran interés por la experimentación', 4.4, 'N'),
                                                                                                   (3, 4, 1, 'Excelente participación en actividades prácticas', 4.3, 'N'),
                                                                                                   (3, 5, 1, 'Buena comprensión de conceptos básicos', 4.2, 'N'),

                                                                                                   -- Educación Física
                                                                                                   (5, 3, 1, 'Excelente desarrollo motriz', 4.6, 'N'),
                                                                                                   (5, 4, 1, 'Muy buen desempeño en actividades físicas', 4.4, 'N'),
                                                                                                   (5, 5, 1, 'Destacada participación en juegos grupales', 4.5, 'N'),

                                                                                                   -- Artes
                                                                                                   (6, 3, 1, 'Excepcional creatividad y expresión artística', 4.7, 'N'),
                                                                                                   (6, 4, 1, 'Muy buen manejo de técnicas artísticas', 4.3, 'N'),
                                                                                                   (6, 5, 1, 'Excelente participación en actividades creativas', 4.4, 'N');

-- Continuación de registros adicionales

-- 1. Asignación de actividades a grupos adicionales (activity_group)
INSERT INTO activity_group (activity_id, group_id, start_date, end_date) VALUES
                                                                             -- Música (Jardín B)
                                                                             (27, 4, '2025-01-20', '2025-01-24'), -- Instrumentos caseros
                                                                             (28, 4, '2025-02-03', '2025-02-07'), -- Coro infantil
                                                                             (29, 4, '2025-02-17', '2025-02-21'), -- Ritmos corporales
                                                                             (30, 4, '2025-03-02', '2025-03-06'), -- Orquesta reciclada

                                                                             -- Inglés (Transición A)
                                                                             (31, 5, '2025-01-27', '2025-01-31'), -- My family members
                                                                             (32, 5, '2025-02-10', '2025-02-14'), -- Colors and numbers
                                                                             (33, 5, '2025-02-24', '2025-02-28'), -- Animal sounds
                                                                             (34, 5, '2025-03-09', '2025-03-13'), -- Action songs

                                                                             -- Tecnología (Transición B)
                                                                             (35, 6, '2025-01-20', '2025-01-24'), -- Mi primera tablet
                                                                             (36, 6, '2025-02-03', '2025-02-07'), -- Juegos educativos
                                                                             (37, 6, '2025-02-17', '2025-02-21'), -- Dibujo digital
                                                                             (38, 6, '2025-03-02', '2025-03-06'); -- Robots simples

-- 2. Registros adicionales de asistencia
INSERT INTO attendance (student_id, schedule_id, attendance_date, status) VALUES
                                                                              -- Semana 3: Lunes 3 de febrero 2025
                                                                              (3, 3, '2025-02-03', 'P'),
                                                                              (4, 3, '2025-02-03', 'P'),
                                                                              (5, 3, '2025-02-03', 'P'),
                                                                              (6, 3, '2025-02-03', 'T'),
                                                                              (7, 3, '2025-02-03', 'A'),

                                                                              -- Semana 3: Miércoles 5 de febrero 2025
                                                                              (3, 4, '2025-02-05', 'P'),
                                                                              (4, 4, '2025-02-05', 'A'),
                                                                              (5, 4, '2025-02-05', 'P'),
                                                                              (6, 4, '2025-02-05', 'P'),
                                                                              (7, 4, '2025-02-05', 'P'),

                                                                              -- Semana 4: Lunes 10 de febrero 2025
                                                                              (3, 5, '2025-02-10', 'T'),
                                                                              (4, 5, '2025-02-10', 'P'),
                                                                              (5, 5, '2025-02-10', 'P'),
                                                                              (6, 5, '2025-02-10', 'A'),
                                                                              (7, 5, '2025-02-10', 'P');

-- 3. Calificaciones adicionales de actividades
INSERT INTO activity_grade (student_id, activity_id, score, comment) VALUES
                                                                         -- Música: Instrumentos caseros
                                                                         (6, 27, 4.8, 'Excelente creatividad en la creación de instrumentos'),
                                                                         (7, 27, 4.5, 'Muy buen trabajo en equipo y originalidad'),
                                                                         (8, 27, 4.3, 'Buena participación y entusiasmo'),

                                                                         -- Inglés: My family members
                                                                         (6, 31, 4.6, 'Excelente pronunciación y vocabulario'),
                                                                         (7, 31, 4.2, 'Buena participación en diálogos'),
                                                                         (8, 31, 4.0, 'Progresa en el uso del vocabulario'),

                                                                         -- Tecnología: Mi primera tablet
                                                                         (6, 35, 4.7, 'Excelente manejo de la herramienta digital'),
                                                                         (7, 35, 4.4, 'Muy buena adaptación a la tecnología'),
                                                                         (8, 35, 4.1, 'Buen progreso en habilidades digitales'),

                                                                         -- Coro infantil
                                                                         (6, 28, 4.5, 'Excelente participación y memorización'),
                                                                         (7, 28, 4.3, 'Muy buen ritmo y entonación'),
                                                                         (8, 28, 4.0, 'Buena actitud y participación'),

                                                                         -- Colors and numbers
                                                                         (6, 32, 4.4, 'Excelente reconocimiento de colores y números'),
                                                                         (7, 32, 4.2, 'Muy buena pronunciación'),
                                                                         (8, 32, 3.9, 'Progresa en el vocabulario');

-- 4. Calificaciones finales adicionales por materia
INSERT INTO subject_grade (subject_id, student_id, period_id, comment, total_score, recovered) VALUES
                                                                                                   -- Música
                                                                                                   (7, 6, 1, 'Excelente desempeño en actividades musicales', 4.8, 'N'),
                                                                                                   (7, 7, 1, 'Muy buena participación y ritmo', 4.5, 'N'),
                                                                                                   (7, 8, 1, 'Buen progreso en expresión musical', 4.2, 'N'),

                                                                                                   -- Inglés
                                                                                                   (8, 6, 1, 'Excelente manejo del vocabulario básico', 4.6, 'N'),
                                                                                                   (8, 7, 1, 'Muy buena comprensión y participación', 4.3, 'N'),
                                                                                                   (8, 8, 1, 'Buen avance en el idioma', 4.0, 'N'),

                                                                                                   -- Tecnología
                                                                                                   (9, 6, 1, 'Excelente adaptación a herramientas digitales', 4.7, 'N'),
                                                                                                   (9, 7, 1, 'Muy buen manejo de dispositivos', 4.4, 'N'),
                                                                                                   (9, 8, 1, 'Buen desarrollo de habilidades tecnológicas', 4.1, 'N'),

                                                                                                   -- Ética y Valores
                                                                                                   (10, 6, 1, 'Excelente comportamiento y valores', 4.8, 'N'),
                                                                                                   (10, 7, 1, 'Muy buena convivencia y respeto', 4.6, 'N'),
                                                                                                   (10, 8, 1, 'Buen desarrollo de valores', 4.3, 'N'),

                                                                                                   -- Danza
                                                                                                   (12, 6, 1, 'Excelente expresión corporal y ritmo', 4.7, 'N'),
                                                                                                   (12, 7, 1, 'Muy buena coordinación y participación', 4.5, 'N'),
                                                                                                   (12, 8, 1, 'Buen desempeño en actividades de danza', 4.2, 'N');

-- 24. Tipos de seguimiento
INSERT INTO tracking_type (type) VALUES
                                     ('A'), -- Académico
                                     ('C'), -- Comportamental
                                     ('S'), -- Social
                                     ('F'), -- Familiar
                                     ('M'); -- Médico

-- 25. Seguimiento estudiantil (40 registros)
INSERT INTO student_tracking (student, professor, period_id, tracking_type, situation, compromise, follow_up, status) VALUES
                                                                                                                          -- Seguimientos Académicos
                                                                                                                          (3, 2, 1, 1, 'Dificultad en operaciones matemáticas básicas', 'Asistir a refuerzo los martes', 'Muestra mejora gradual en sumas y restas', 'A'),
                                                                                                                          (4, 2, 1, 1, 'Bajo rendimiento en lectura', 'Práctica diaria de lectura en casa', 'Avance significativo en comprensión lectora', 'A'),
                                                                                                                          (5, 2, 1, 1, 'Problemas con escritura de números', 'Ejercicios de caligrafía numérica', 'Mejora en la formación de números', 'A'),
                                                                                                                          (6, 2, 1, 1, 'Dificultad en reconocimiento de letras', 'Uso de material didáctico en casa', 'Progreso en identificación de vocales', 'A'),
                                                                                                                          (7, 2, 1, 1, 'Bajo desempeño en inglés', 'Práctica con aplicaciones educativas', 'Mejora en vocabulario básico', 'A'),
                                                                                                                          (8, 2, 1, 1, 'Necesita refuerzo en motricidad fina', 'Actividades de recorte y dibujo', 'Avance en control de lápiz', 'A'),
                                                                                                                          (3, 2, 1, 1, 'Dificultad en seguimiento de instrucciones', 'Ejercicios de atención dirigida', 'Mejora en comprensión de consignas', 'A'),
                                                                                                                          (4, 2, 1, 1, 'Problemas en secuencias numéricas', 'Juegos de patrones y series', 'Progreso en reconocimiento de patrones', 'A'),

                                                                                                                          -- Seguimientos Comportamentales
                                                                                                                          (5, 2, 1, 2, 'Dificultad para seguir normas en clase', 'Establecer rutinas claras', 'Mejora en comportamiento grupal', 'A'),
                                                                                                                          (6, 2, 1, 2, 'Conducta dispersa durante actividades', 'Implementar pausas activas', 'Mayor tiempo de concentración', 'A'),
                                                                                                                          (7, 2, 1, 2, 'Interrumpe frecuentemente la clase', 'Asignar roles de responsabilidad', 'Disminución de interrupciones', 'A'),
                                                                                                                          (8, 2, 1, 2, 'Dificultad en trabajo en equipo', 'Participar en actividades grupales', 'Mejor integración con compañeros', 'A'),
                                                                                                                          (3, 2, 1, 2, 'Comportamiento impulsivo', 'Técnicas de autocontrol', 'Progreso en manejo de emociones', 'A'),
                                                                                                                          (4, 2, 1, 2, 'Falta de participación en clase', 'Estimular participación activa', 'Mayor intervención en actividades', 'A'),
                                                                                                                          (5, 2, 1, 2, 'Dificultad para compartir materiales', 'Actividades cooperativas', 'Mejora en interacción social', 'A'),
                                                                                                                          (6, 2, 1, 2, 'Conducta agresiva ocasional', 'Implementar tiempo fuera', 'Disminución de incidentes', 'A'),

                                                                                                                          -- Seguimientos Sociales
                                                                                                                          (7, 2, 1, 3, 'Aislamiento durante el recreo', 'Promover juegos grupales', 'Mayor integración con pares', 'A'),
                                                                                                                          (8, 2, 1, 3, 'Dificultad para hacer amigos', 'Actividades de integración', 'Formación de vínculos positivos', 'A'),
                                                                                                                          (3, 2, 1, 3, 'Timidez excesiva', 'Participación gradual en actividades', 'Aumento de confianza social', 'A'),
                                                                                                                          (4, 2, 1, 3, 'Problemas de adaptación', 'Acompañamiento personalizado', 'Mejor adaptación al grupo', 'A'),
                                                                                                                          (5, 2, 1, 3, 'Dificultad en resolución de conflictos', 'Mediación entre pares', 'Mejora en habilidades sociales', 'A'),
                                                                                                                          (6, 2, 1, 3, 'Liderazgo negativo', 'Reorientar habilidades de liderazgo', 'Liderazgo más positivo', 'A'),
                                                                                                                          (7, 2, 1, 3, 'Exclusión del grupo', 'Actividades de inclusión', 'Mayor aceptación grupal', 'A'),
                                                                                                                          (8, 2, 1, 3, 'Dificultad en expresión emocional', 'Talleres de expresión', 'Mejor comunicación emocional', 'A'),

                                                                                                                          -- Seguimientos Familiares
                                                                                                                          (3, 2, 1, 4, 'Falta de acompañamiento en tareas', 'Establecer rutina de estudio', 'Mayor compromiso familiar', 'A'),
                                                                                                                          (4, 2, 1, 4, 'Inasistencias frecuentes', 'Compromiso de puntualidad', 'Mejora en asistencia', 'A'),
                                                                                                                          (5, 2, 1, 4, 'Situación familiar compleja', 'Apoyo psicológico familiar', 'Avance en estabilidad emocional', 'A'),
                                                                                                                          (6, 2, 1, 4, 'Falta de materiales escolares', 'Gestión de apoyo escolar', 'Provisión regular de materiales', 'A'),
                                                                                                                          (7, 2, 1, 4, 'Cambios en dinámica familiar', 'Acompañamiento en transición', 'Adaptación progresiva', 'A'),
                                                                                                                          (8, 2, 1, 4, 'Comunicación familia-escuela limitada', 'Reuniones periódicas', 'Mejor comunicación institucional', 'A'),
                                                                                                                          (3, 2, 1, 4, 'Ausencia en reuniones escolares', 'Programar horarios flexibles', 'Mayor participación familiar', 'A'),
                                                                                                                          (4, 2, 1, 4, 'Falta de rutinas en casa', 'Establecer horarios estructurados', 'Mejora en hábitos diarios', 'A'),

                                                                                                                          -- Seguimientos Médicos
                                                                                                                          (5, 2, 1, 5, 'Dificultades visuales', 'Evaluación oftalmológica', 'Uso de lentes correctivos', 'A'),
                                                                                                                          (6, 2, 1, 5, 'Problemas de atención', 'Valoración neuropsicológica', 'Seguimiento especializado', 'A'),
                                                                                                                          (7, 2, 1, 5, 'Alergias frecuentes', 'Control alergológico', 'Manejo preventivo de alergias', 'A'),
                                                                                                                          (8, 2, 1, 5, 'Dificultades de lenguaje', 'Terapia de lenguaje', 'Progreso en pronunciación', 'A'),
                                                                                                                          (3, 2, 1, 5, 'Problemas de alimentación', 'Orientación nutricional', 'Mejora en hábitos alimenticios', 'A'),
                                                                                                                          (4, 2, 1, 5, 'Dolor frecuente de cabeza', 'Evaluación médica general', 'Control de síntomas', 'A'),
                                                                                                                          (5, 2, 1, 5, 'Problemas de motricidad', 'Terapia ocupacional', 'Avance en desarrollo motor', 'A'),
                                                                                                                          (6, 2, 1, 5, 'Fatiga inusual', 'Exámenes médicos generales', 'Seguimiento de condición física', 'A');

-- 26. Datos institucionales
INSERT INTO institution (name, nit, address, phone_number, email, city, department, country, postal_code, legal_representative, incorporation_date) VALUES
    ('Colegio Nuevo Horizonte',
     '901234567-8',
     'Calle 123 #45-67',
     '601-234-5678',
     'info@nuevohorizonte.edu.co',
     'Bogotá',
     'Cundinamarca',
     'Colombia',
     '110111',
     'Ana María Rodríguez',
     '2024-01-01');

-- 27. Historial de respaldos
INSERT INTO backup_history (backup_date, backup_name, description, file_path, created_by) VALUES
                                                                                              ('2025-01-15 08:00:00', 'BK_20250115_FULL', 'Respaldo completo inicial periodo 2025-1', '/backups/2025/01/BK_20250115_FULL.bak', 1),
                                                                                              ('2025-01-31 20:00:00', 'BK_20250131_DIFF', 'Respaldo diferencial enero', '/backups/2025/01/BK_20250131_DIFF.bak', 1),
                                                                                              ('2025-02-15 08:00:00', 'BK_20250215_FULL', 'Respaldo completo mensual', '/backups/2025/02/BK_20250215_FULL.bak', 1),
                                                                                              ('2025-02-28 20:00:00', 'BK_20250228_DIFF', 'Respaldo diferencial febrero', '/backups/2025/02/BK_20250228_DIFF.bak', 1),
                                                                                              ('2025-03-15 08:00:00', 'BK_20250315_FULL', 'Respaldo completo fin de periodo', '/backups/2025/03/BK_20250315_FULL.bak', 1);


-- Continuará en la parte 5 con seguimiento estudiantil, datos institucionales y respaldos...