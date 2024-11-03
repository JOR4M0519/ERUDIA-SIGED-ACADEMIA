
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
VALUES (1, 'John', 'A.', 'Doe', 'Smith', '123 Main St', '123-456-7890', '1990-01-01', '123456789', 1, 'Downtown', 'Metropolis', 'Teacher'),
       (2, 'Alice', NULL, 'Smith', 'Johnson', '456 Oak St', '987-654-3210', '1985-05-15', '987654321', 2, 'Westside', 'Metropolis', 'Admin'),
       (3, 'Michael', 'B.', 'Brown', NULL, '789 Pine St', '555-123-4567', '1992-09-21', '543216789', 3, 'Eastside', 'Gotham', 'Professor');

-- Inserts for role
INSERT INTO role (role_name, status)
VALUES ('Admin', TRUE),
       ('Professor', TRUE),
       ('Student', TRUE);

-- Inserts for user_role
INSERT INTO user_role (user_id, role_id)
VALUES (1, 1), -- John Doe is Admin
       (2, 2), -- Alice Smith is Professor
       (3, 3); -- Michael Brown is Student

-- Inserts for permission
INSERT INTO permission (permission, status)
VALUES ('View Grades', TRUE),
       ('Edit Grades', TRUE),
       ('Manage Users', TRUE);

-- Inserts for role_perm
INSERT INTO role_perm (role_id, permission_id)
VALUES (1, 3), -- Admin can Manage Users
       (2, 2), -- Professor can Edit Grades
       (3, 1); -- Student can View Grades

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
VALUES (1, 'P001', 'Preschool A', 3, TRUE),
       (2, 'P002', 'Primary B', 3, TRUE);

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
VALUES (1, 1), -- Cognitive dimension for Mathematics
       (2, 2); -- Motor dimension for Science

-- Inserts for subject_schedule
INSERT INTO subject_schedule (subject_id, day_of_week, start_time, end_time, status)
VALUES (1, 'Monday', '09:00', '11:00', TRUE),
       (2, 'Wednesday', '10:00', '12:00', TRUE);

-- Inserts for attendance
INSERT INTO attendance (student_id, schedule_id, attendance_date, status)
VALUES (3, 1, '2024-01-08', 'PR'),
       (3, 2, '2024-01-10', 'AU');

-- Inserts for subject_professors
INSERT INTO subject_professors (subject_id, professor_id)
VALUES (1, 2), -- Alice Smith teaches Mathematics
       (2, 2); -- Alice Smith teaches Science

-- Inserts for knowledge
INSERT INTO knowledge (name, achievement, status)
VALUES ('Math', 'Achieved basic skills', TRUE),
       ('Science', 'Completed lab experiments', TRUE);

-- Inserts for activity
INSERT INTO activity (activity_name, description, subject, period_id, knowledge, status)
VALUES ('Math Exam', 'Final exam covering algebra and geometry', 1, 1, 1, 'A'),
       ('Science Project', 'Complete a model of the solar system', 2, 2, 2, 'A');

-- Inserts for activity_group
INSERT INTO activity_group (activity_id, group_id, due)
VALUES (1, 1, '2024-02-15'),
       (2, 2, '2024-03-01');

-- Inserts for grades
INSERT INTO activity_grade (student_id, activity_id, score, comment)
VALUES (3, 1, 85.50, 'Good effort'),
       (3, 2, 90.00, 'Excellent project');

-- Inserts for recovery_period
INSERT INTO recovery_period (student_id, subject_id, previous_score, new_score, period)
VALUES (3, 1, 65.00, 80.00, 1);

-- Inserts for institution
INSERT INTO institution (name, nit, address, phone_number, email, city, department, country, postal_code, legal_representative, incorporation_date)
VALUES ('ABC School', '123456789', '123 School Rd', '555-987-6543', 'contact@abcschool.com', 'Metropolis', 'Central', 'USA', '12345', 'Jane Doe', '2000-01-01');

-- Inserts for student_tracking
INSERT INTO student_tracking (student, professor, situation, compromise, follow_up, status)
VALUES (3, 2, 'Disruptive behavior in class', 'Will attend extra tutoring', 'Improved behavior noticed', TRUE);

-- Inserts for backup_history
INSERT INTO backup_history (backup_name, description, file_path, created_by)
VALUES ('backup_2024_01', 'Monthly backup', '/backups/backup_2024_01.sql', 1);