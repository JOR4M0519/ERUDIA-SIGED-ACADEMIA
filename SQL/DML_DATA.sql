-- 1. Tabla: id_type
INSERT INTO id_type (name) VALUES
                               ('Ciudadanía'),
                               ('Extranjería'),
                               ('Identidad'),
                               ('Pasaporte'),
                               ('Registro Civil');

-- 2. La tabla users ya contenía registros, no se modifica.
INSERT INTO users (username, email, password, uuid, status) VALUES
                                                                ('juanita.perez', 'jdramos10000@gmail.com', 'hashed_password', '47928edd-60b4-4b61-97c8-285e15d0c867', 'A'),
                                                                ('nicolas.rodriguez', 'jdramos100@gmail.com', 'hashed_password', '8a00719b-f1ec-4332-b187-5826278addc6', 'A'),
                                                                ('pepito.perez', 'jdramos1000@gmail.com', 'hashed_password', '13644306-4de2-440c-89bd-8cec1ee2ab41', 'A'),
                                                                ('rectoria', 'soporteyopal@gimnasiolorismalaguzzi.edu.co', 'hashed_password', 'cf3fb2ce-5ed0-4f10-beda-2d40999e0138', 'A'),
                                                                ('admin', 'admin@gimnasiolorismalaguzzi.edu.co', 'hashed_password', 'admin-uuid-001', 'A');

-- 3. Tabla: user_detail
INSERT INTO user_detail (user_id, first_name, middle_name, last_name, second_last_name, address, phone_number, date_of_birth, dni, id_type_id, neighborhood, city, position_job)
VALUES
    (1, 'Juanita', 'Andrea', 'Pérez', 'Rodríguez', 'Calle 123', '3124567890', '1990-05-14', '123456789', 1, 'Centro', 'Bogotá', 'Docente'),
    (2, 'Nicolás', 'Fernando', 'Rodríguez', 'Lopez', 'Carrera 45', '3201234567', '1985-07-20', '987654321', 2, 'Chapinero', 'Bogotá', 'Administrador'),
    (3, 'Pepito', 'José', 'Pérez', 'Gómez', 'Avenida 56', '3109876543', '1992-11-05', '456123789', 3, 'Teusaquillo', 'Bogotá', 'Coordinador'),
    (4, 'Rectoría', NULL, 'Gimnasio', 'Loris Malaguzzi', 'Sede Principal', '6012345678', '2000-01-01', '999999999', 4, 'Centro', 'Yopal', 'Rector'),
    (5, 'Admin', NULL, 'Gimnasio', 'Admin', 'Sede Administrativa', '6018765432', '1999-12-25', '888888888', 5, 'Suba', 'Bogotá', 'Administrador');

-- 4. Tabla: role
INSERT INTO role (role_name, status) VALUES
                                         ('Admin', TRUE),
                                         ('Docente', TRUE),
                                         ('Estudiante', TRUE),
                                         ('Coordinador', TRUE),
                                         ('Secretaria', TRUE);

-- 5. Tabla: user_role
INSERT INTO user_role (user_id, role_id) VALUES
                                             (1, 2),
                                             (2, 1),
                                             (3, 3),
                                             (4, 4),
                                             (5, 5);

-- 6. Tabla: permission
INSERT INTO permission (permission, status) VALUES
                                                ('Crear usuario', TRUE),
                                                ('Editar usuario', TRUE),
                                                ('Eliminar usuario', FALSE),
                                                ('Asignar roles', TRUE),
                                                ('Ver reportes', TRUE);

-- 7. Tabla: role_perm
INSERT INTO role_perm (role_id, permission_id) VALUES
                                                   (1, 1),
                                                   (2, 2),
                                                   (3, 3),
                                                   (4, 4),
                                                   (5, 5);

-- 8. Tabla: audit_log
INSERT INTO audit_log (table_name, operation, record_id, changed_by, ip) VALUES
                                                                             ('users', 'INSERT', 1, 1, '192.168.1.1'),
                                                                             ('role', 'INSERT', 2, 2, '192.168.1.2'),
                                                                             ('user_role', 'INSERT', 3, 3, '192.168.1.3'),
                                                                             ('permission', 'INSERT', 4, 4, '192.168.1.4'),
                                                                             ('role_perm', 'INSERT', 5, 5, '192.168.1.5');

-- 9. Tabla: relationship
INSERT INTO relationship (relationship_type) VALUES
                                                 ('Padre'),
                                                 ('Madre'),
                                                 ('Hermano'),
                                                 ('Tutor'),
                                                 ('Abuelo');

-- 10. Tabla: family
INSERT INTO family (student_id, user_id, relationship_id) VALUES
                                                              (3, 1, 1),
                                                              (3, 2, 2),
                                                              (3, 4, 4),
                                                              (1, 5, 5),
                                                              (2, 3, 3);

-- 11. Tabla: educational_level
INSERT INTO educational_level (level_name, status) VALUES
                                                       ('Preescolar', 'A'),
                                                       ('Primaria', 'A'),
                                                       ('Secundaria', 'A'),
                                                       ('Media', 'A'),
                                                       ('Universitaria', 'A');

-- 12. Tabla: grade_settings
INSERT INTO grade_settings (level_id, minimum_grade, pass_grade, maximum_grade) VALUES
                                                                                    (1, 1, 3, 5),
                                                                                    (2, 2, 3, 5),
                                                                                    (3, 3, 3, 5),
                                                                                    (4, 4, 3, 5),
                                                                                    (5, 5, 3, 5);

-- 13. Tabla: academic_period
INSERT INTO academic_period (setting_id, start_date, end_date, name, status) VALUES
                                                                                 (1, '2024-01-01', '2024-06-30', 'Q1', 'A'),
                                                                                 (2, '2024-07-01', '2024-12-31', 'Q2', 'A'),
                                                                                 (3, '2023-01-01', '2023-06-30', 'Q1', 'I'),
                                                                                 (4, '2023-07-01', '2023-12-31', 'Q2', 'I'),
                                                                                 (5, '2022-01-01', '2022-12-31', 'Annual', 'I');

-- 14. Tabla: groups
INSERT INTO groups (level_id, group_code, group_name, mentor_id, status) VALUES
                                                                             (1, 'G1A', 'Preescolar A', 1, 'A'),
                                                                             (2, 'G2B', 'Primaria B', 2, 'A'),
                                                                             (3, 'G3C', 'Secundaria C', 3, 'A'),
                                                                             (4, 'G4D', 'Media D', 4, 'A'),
                                                                             (5, 'G5E', 'Universitaria E', 5, 'A');

-- 15. Tabla: group_students
INSERT INTO group_students (student_id, group_id) VALUES
                                                      (3, 1),
                                                      (1, 2),
                                                      (2, 3),
                                                      (4, 4),
                                                      (5, 5);

-- 16. Tabla: dimensions
INSERT INTO dimensions (name, description) VALUES
                                               ('Cognitiva', 'Aspectos relacionados con el aprendizaje'),
                                               ('Socioemocional', 'Habilidades de socialización'),
                                               ('Motora', 'Desarrollo físico y habilidades motoras'),
                                               ('Creatividad', 'Expresión artística y creatividad'),
                                               ('Ética', 'Valores y formación ética');

-- 17. Tabla: subject
INSERT INTO subject (subject_name, status) VALUES
                                               ('Matemáticas', 'A'),
                                               ('Lenguaje', 'A'),
                                               ('Ciencias', 'A'),
                                               ('Historia', 'A'),
                                               ('Inglés', 'A');

-- 18. Tabla: subject_groups
INSERT INTO subject_groups (subject_id, group_students) VALUES
                                                            (1, 1),
                                                            (2, 2),
                                                            (3, 3),
                                                            (4, 4),
                                                            (5, 5);

-- 19. Tabla: subject_dimension
INSERT INTO subject_dimension (dimension_id, subject_id) VALUES
                                                             (1, 1),
                                                             (2, 2),
                                                             (3, 3),
                                                             (4, 4),
                                                             (5, 5);

-- 20. Tabla: subject_schedule
INSERT INTO subject_schedule (subject_id, day_of_week, start_time, end_time, status) VALUES
                                                                                         (1, 'Monday', '08:00', '10:00', 'A'),
                                                                                         (2, 'Tuesday', '10:00', '12:00', 'A'),
                                                                                         (3, 'Wednesday', '14:00', '16:00', 'A'),
                                                                                         (4, 'Thursday', '08:00', '10:00', 'A'),
                                                                                         (5, 'Friday', '12:00', '14:00', 'A');

-- 21. Tabla: attendance
INSERT INTO attendance (student_id, schedule_id, attendance_date, status) VALUES
                                                                              (1, 1, '2024-02-01', 'P'),
                                                                              (2, 2, '2024-02-01', 'A'),
                                                                              (3, 3, '2024-02-01', 'T'),
                                                                              (4, 4, '2024-02-01', 'P'),
                                                                              (5, 5, '2024-02-01', 'A');

-- 22. Tabla: subject_professors
INSERT INTO subject_professors (subject_id, professor_id) VALUES
                                                              (1, 1),
                                                              (2, 2),
                                                              (3, 3),
                                                              (4, 4),
                                                              (5, 5);

-- 23. Tabla: knowledge
INSERT INTO knowledge (name, percentage, status) VALUES
                                                     ('Ser', 20, 'A'),
                                                     ('Hacer', 30, 'A'),
                                                     ('Nose', 40, 'A'),
                                                     ('test', 50, 'A'),
                                                     ('a', 60, 'A');

-- 24. Tabla: achievement_groups
INSERT INTO achievement_groups (knowledge_id, achievement, group_id, period_id) VALUES
                                                                                    (1, 'Logro en teoría', 1, 1),
                                                                                    (2, 'Logro en práctica', 2, 2),
                                                                                    (3, 'Logro en evaluación', 3, 3),
                                                                                    (4, 'Logro en proyecto', 4, 4),
                                                                                    (5, 'Logro en investigación', 5, 5);

-- 25. Tabla: activity
INSERT INTO activity (activity_name, description, subject, period_id, knowledge, status) VALUES
                                                                                             ('Quiz Matemáticas', 'Prueba de álgebra', 1, 1, 1, 'A'),
                                                                                             ('Ensayo Historia', 'Ensayo sobre la independencia', 2, 2, 2, 'A'),
                                                                                             ('Laboratorio Ciencias', 'Experimento químico', 3, 3, 3, 'A'),
                                                                                             ('Debate Lenguaje', 'Debate sobre literatura', 4, 4, 4, 'A'),
                                                                                             ('Listening Inglés', 'Comprensión auditiva', 5, 5, 5, 'A');

-- 26. Tabla: activity_group
INSERT INTO activity_group (activity_id, group_id, start_date, end_date) VALUES
                                                                             (1, 1, '2024-02-05', '2024-02-06'),
                                                                             (2, 2, '2024-02-07', '2024-02-08'),
                                                                             (3, 3, '2024-02-09', '2024-02-10'),
                                                                             (4, 4, '2024-02-11', '2024-02-12'),
                                                                             (5, 5, '2024-02-13', '2024-02-14');

-- 27. Tabla: subject_knowledge
INSERT INTO subject_knowledge (id_subject, id_knowledge) VALUES
                                                             (1, 1),
                                                             (2, 2),
                                                             (3, 3),
                                                             (4, 4),
                                                             (5, 5);

-- 28. Tabla: activity_grade
INSERT INTO activity_grade (student_id, activity_id, score, comment) VALUES
                                                                         (1, 1, 4.5, 'Buen desempeño'),
                                                                         (2, 2, 3.8, 'Necesita mejorar'),
                                                                         (3, 3, 4.0, 'Correcto'),
                                                                         (4, 4, 3.5, 'Regular'),
                                                                         (5, 5, 5.0, 'Excelente');

-- 29. Tabla: subject_grade
INSERT INTO subject_grade (subject_id, student_id, period_id, total_score, recovered) VALUES
                                                                                          (1, 1, 1, 4.2, 'N'),
                                                                                          (2, 2, 2, 3.6, 'N'),
                                                                                          (3, 3, 3, 4.0, 'N'),
                                                                                          (4, 4, 4, 3.2, 'S'),
                                                                                          (5, 5, 5, 4.8, 'N');

-- 30. Tabla: recovery_period
INSERT INTO recovery_period (subject_grade, previous_score) VALUES
                                                                (4, 2.8),
                                                                (5, 3.4),
                                                                (3, 3.0),
                                                                (2, 2.5),
                                                                (1, 3.0);

-- 31. Tabla: institution
INSERT INTO institution (name, nit, address, phone_number, email, city, department, country, postal_code, legal_representative, incorporation_date) VALUES
                                                                                                                                                        ('Gimnasio Loris Malaguzzi', '900123456-7', 'Carrera 7 #123-45', '6012345678', 'info@gimnasiolorismalaguzzi.edu.co', 'Bogotá', 'Cundinamarca', 'Colombia', '110111', 'Carlos Pérez', '2010-01-15'),
                                                                                                                                                        ('Colegio Los Andes', '900765432-1', 'Calle 10 #20-30', '6023456789', 'info@colegiolosandes.edu.co', 'Medellín', 'Antioquia', 'Colombia', '050011', 'Ana Martínez', '2005-03-22'),
                                                                                                                                                        ('Instituto Bilingüe', '901234567-8', 'Avenida 40 #50-60', '6034567890', 'info@institutobilingue.edu.co', 'Cali', 'Valle del Cauca', 'Colombia', '760033', 'Luis Gómez', '2015-08-10'),
                                                                                                                                                        ('Escuela Moderna', '902345678-9', 'Carrera 9 #99-99', '6045678901', 'info@escuelamoderna.edu.co', 'Barranquilla', 'Atlántico', 'Colombia', '080011', 'Patricia Sánchez', '2012-05-30'),
                                                                                                                                                        ('Academia Científica', '903456789-0', 'Calle 5 #15-25', '6056789012', 'info@academiacientifica.edu.co', 'Cartagena', 'Bolívar', 'Colombia', '130001', 'Juan Ramírez', '2008-11-17');

-- 32. Tabla: backup_history
INSERT INTO backup_history (backup_name, description, file_path, created_by) VALUES
                                                                                 ('Backup 01', 'Copia de seguridad enero', '/backups/backup01.sql', 1),
                                                                                 ('Backup 02', 'Copia de seguridad febrero', '/backups/backup02.sql', 2),
                                                                                 ('Backup 03', 'Copia de seguridad marzo', '/backups/backup03.sql', 3),
                                                                                 ('Backup 04', 'Copia de seguridad abril', '/backups/backup04.sql', 4),
                                                                                 ('Backup 05', 'Copia de seguridad mayo', '/backups/backup05.sql', 5);

-- 33. Tabla: student_tracking
INSERT INTO student_tracking (student, professor, situation, compromise, follow_up, status) VALUES
                                                                                                (1, 2, 'Falta a clases', 'Asistir puntualmente', 'Revisión semanal', 'A'),
                                                                                                (2, 3, 'Bajo rendimiento', 'Estudio diario', 'Informe mensual', 'A'),
                                                                                                (3, 4, 'Comportamiento inadecuado', 'Respeto en clase', 'Seguimiento disciplinario', 'A'),
                                                                                                (4, 5, 'Dificultades en inglés', 'Tutorías adicionales', 'Evaluación trimestral', 'A'),
                                                                                                (5, 1, 'Problemas de concentración', 'Técnicas de estudio', 'Seguimiento psicopedagógico', 'A');

