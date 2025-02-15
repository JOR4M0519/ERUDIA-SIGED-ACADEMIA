-- Inserts for id_type
INSERT INTO id_type (name) VALUES ('ID Card');
INSERT INTO id_type (name) VALUES ('Passport');
INSERT INTO id_type (name) VALUES ('Driver License');

-- Inserts for users
INSERT INTO users (username, email, password, status)
VALUES ('jdoe', 'jdoe@example.com', 'hashed_password1', 'A'),
       ('asmith', 'asmith@example.com', 'hashed_password2', 'A'),
       ('mbrown', 'mbrown@example.com', 'hashed_password3', 'A');

-- Inserts for user_detail
INSERT INTO user_detail (user_id, first_name, middle_name, last_name, second_last_name, address, phone_number, date_of_birth, dni, id_type_id, neighborhood, city, position_job)
VALUES ((SELECT id FROM users WHERE username = 'jdoe'), 'John', 'A.', 'Doe', 'Smith', '123 Main St', '123-456-7890', '1990-01-01', '123456789', (SELECT id FROM id_type WHERE name = 'ID Card'), 'Downtown', 'Metropolis', 'Teacher'),
       ((SELECT id FROM users WHERE username = 'asmith'), 'Alice', NULL, 'Smith', 'Johnson', '456 Oak St', '987-654-3210', '1985-05-15', '987654321', (SELECT id FROM id_type WHERE name = 'Passport'), 'Westside', 'Metropolis', 'Admin'),
       ((SELECT id FROM users WHERE username = 'mbrown'), 'Michael', 'B.', 'Brown', NULL, '789 Pine St', '555-123-4567', '1992-09-21', '543216789', (SELECT id FROM id_type WHERE name = 'Driver License'), 'Eastside', 'Gotham', 'Professor');

-- Inserts for role
INSERT INTO role (role_name, status)
VALUES ('Admin', TRUE),
       ('Professor', TRUE),
       ('Student', TRUE);

-- Inserts for user_role
INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM users WHERE username = 'jdoe'), (SELECT id FROM role WHERE role_name = 'Admin')),
       ((SELECT id FROM users WHERE username = 'asmith'), (SELECT id FROM role WHERE role_name = 'Professor')),
       ((SELECT id FROM users WHERE username = 'mbrown'), (SELECT id FROM role WHERE role_name = 'Student'));

-- Inserts for permission
INSERT INTO permission (permission, status)
VALUES ('View Grades', TRUE),
       ('Edit Grades', TRUE),
       ('Manage Users', TRUE);

-- Inserts for role_perm
INSERT INTO role_perm (role_id, permission_id)
VALUES ((SELECT id FROM role WHERE role_name = 'Admin'), (SELECT id FROM permission WHERE permission = 'Manage Users')),
       ((SELECT id FROM role WHERE role_name = 'Professor'), (SELECT id FROM permission WHERE permission = 'Edit Grades')),
       ((SELECT id FROM role WHERE role_name = 'Student'), (SELECT id FROM permission WHERE permission = 'View Grades'));

-- Inserts for academic_period
INSERT INTO academic_period (start_date, end_date, name, status)
VALUES ('2024-01-01', '2024-06-30', '2024-1', 'A'),
       ('2024-07-01', '2024-12-31', '2024-2', 'A');

-- Inserts for educational_level
INSERT INTO educational_level (level_name, status)
VALUES ('Preschool', 'A'),
       ('Primary', 'A'),
       ('High School', 'A');

-- Inserts for group_students
INSERT INTO group_students (level_id, group_code, group_name, professor_id, status)
VALUES ((SELECT id FROM educational_level WHERE level_name = 'Preschool'), 'P001', 'Preschool A', (SELECT id FROM users WHERE username = 'mbrown'), 'A'),
       ((SELECT id FROM educational_level WHERE level_name = 'Primary'), 'P002', 'Primary B', (SELECT id FROM users WHERE username = 'mbrown'), 'I');

-- Inserts for dimensions
INSERT INTO dimensions (name, description)
VALUES ('Cognitive', 'Focuses on knowledge and understanding'),
       ('Motor', 'Development of physical skills');

-- Inserts for subject
INSERT INTO subject (subject_name, status)
VALUES ('Mathematics', 'A'),
       ('Science', 'A');

-- Inserts for subject_dimension
INSERT INTO subject_dimension (dimension_id, subject_id)
VALUES ((SELECT id FROM dimensions WHERE name = 'Cognitive'), (SELECT id FROM subject WHERE subject_name = 'Mathematics')),
       ((SELECT id FROM dimensions WHERE name = 'Motor'), (SELECT id FROM subject WHERE subject_name = 'Science'));

-- Inserts for subject_schedule
INSERT INTO subject_schedule (subject_id, day_of_week, start_time, end_time, status)
VALUES ((SELECT id FROM subject WHERE subject_name = 'Mathematics'), 'Monday', '09:00', '11:00', 'A'),
       ((SELECT id FROM subject WHERE subject_name = 'Science'), 'Wednesday', '10:00', '12:00', 'A');

-- Inserts for attendance
INSERT INTO attendance (student_id, schedule_id, attendance_date, status)
VALUES ((SELECT id FROM users WHERE username = 'mbrown'), (SELECT id FROM subject_schedule WHERE subject_id = (SELECT id FROM subject WHERE subject_name = 'Mathematics')), '2024-01-08', 'PR'),
       ((SELECT id FROM users WHERE username = 'mbrown'), (SELECT id FROM subject_schedule WHERE subject_id = (SELECT id FROM subject WHERE subject_name = 'Science')), '2024-01-10', 'AU');

-- Inserts for subject_professors
INSERT INTO subject_professors (subject_id, professor_id)
VALUES ((SELECT id FROM subject WHERE subject_name = 'Mathematics'), (SELECT id FROM users WHERE username = 'asmith')),
       ((SELECT id FROM subject WHERE subject_name = 'Science'), (SELECT id FROM users WHERE username = 'asmith'));

-- Inserts for knowledge
INSERT INTO knowledge (name, achievement, status)
VALUES ('Math', 'Achieved basic skills', 'A'),
       ('Science', 'Completed lab experiments', 'A');

-- Inserts for activity
INSERT INTO activity (activity_name, description, subject, period_id, knowledge, status)
VALUES ('Math Exam', 'Final exam covering algebra and geometry', (SELECT id FROM subject WHERE subject_name = 'Mathematics'), (SELECT id FROM academic_period WHERE name = '2024-1'), (SELECT id FROM knowledge WHERE name = 'Math'), 'A'),
       ('Science Project', 'Complete a model of the solar system', (SELECT id FROM subject WHERE subject_name = 'Science'), (SELECT id FROM academic_period WHERE name = '2024-2'), (SELECT id FROM knowledge WHERE name = 'Science'), 'A');

-- Inserts for activity_group
INSERT INTO activity_group (activity_id, group_id, due)
VALUES ((SELECT id FROM activity WHERE activity_name = 'Math Exam'), (SELECT id FROM group_students WHERE group_code = 'P001'), '2024-02-15'),
       ((SELECT id FROM activity WHERE activity_name = 'Science Project'), (SELECT id FROM group_students WHERE group_code = 'P002'), '2024-03-01');

-- Inserts for grades
INSERT INTO activity_grade (student_id, activity_id, score, comment)
VALUES ((SELECT id FROM users WHERE username = 'mbrown'), (SELECT id FROM activity_group WHERE activity_id = (SELECT id FROM activity WHERE activity_name = 'Math Exam')), 85.50, 'Good effort'),
       ((SELECT id FROM users WHERE username = 'mbrown'), (SELECT id FROM activity_group WHERE activity_id = (SELECT id FROM activity WHERE activity_name = 'Science Project')), 90.00, 'Excellent project');

-- Inserts for institution
INSERT INTO institution (name, nit, address, phone_number, email, city, department, country, postal_code, legal_representative, incorporation_date)
VALUES ('ABC School', '123456789', '123 School Rd', '555-987-6543', 'contact@abcschool.com', 'Metropolis', 'Central', 'USA', '12345', 'Jane Doe', '2000-01-01');

-- Inserts for student_tracking
INSERT INTO student_tracking (student, professor, situation, compromise, follow_up, status)
VALUES ((SELECT id FROM users WHERE username = 'mbrown'), (SELECT id FROM users WHERE username = 'asmith'), 'Disruptive behavior in class', 'Will attend extra tutoring', 'Improved behavior noticed', 'A');

-- Inserts for backup_history
INSERT INTO backup_history (backup_name, description, file_path, created_by)
VALUES ('backup_2024_01', 'Monthly backup', '/backups/backup_2024_01.sql', (SELECT id FROM users WHERE username = 'jdoe'));

INSERT INTO student_tracking (student, professor, situation, compromise, follow_up, status)
VALUES
    ((SELECT id FROM users WHERE username = 'mbrown'),
     (SELECT id FROM users WHERE username = 'asmith'),
     'Bajo rendimiento en matemáticas',
     'Asistir a tutorías adicionales dos veces por semana',
     'Mejoría observada en comprensión de conceptos básicos',
     'A'),

    ((SELECT id FROM users WHERE username = 'mbrown'),
     (SELECT id FROM users WHERE username = 'jdoe'),
     'Falta de concentración en clase de ciencias',
     'Completar ejercicios de concentración antes de la clase',
     'Progreso parcial, necesita seguimiento adicional',
     'A'),

    ((SELECT id FROM users WHERE username = 'jdoe'),
     (SELECT id FROM users WHERE username = 'asmith'),
     'Dificultades en la participación en actividades grupales',
     'Participar en una sesión de coaching grupal',
     'Incremento en participación activa',
     'A');