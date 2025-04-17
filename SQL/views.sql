CREATE OR REPLACE VIEW v_academic_report AS
SELECT
    sg.id AS grade_id,
    sg.student_id,
    u.first_name || ' ' || u.last_name AS student_name,
    sg.subject_id,
    s.subject_name,
    sg.period_id,
    ap.name AS period_name,
    sg.total_score,
    sg.recovered,
    sg.comment,
    gs.group_id,
    g.group_name,
    g.group_code,
    sk.id AS subject_knowledge_id,
    k.id AS knowledge_id,
    k.name AS knowledge_name,
    k.percentage AS knowledge_percentage,
    ag.id AS achievement_group_id,
    ag.achievement
FROM
    subject_grade sg
        JOIN users u ON sg.student_id = u.id
        JOIN subject s ON sg.subject_id = s.id
        JOIN academic_period ap ON sg.period_id = ap.id
        JOIN group_students gs ON sg.student_id = gs.student_id
        JOIN groups g ON gs.group_id = g.id
        JOIN subject_groups sgr ON sgr.group_students = g.id AND sgr.academic_period_id = sg.period_id
        JOIN subject_knowledge sk ON sk.id_subject = sg.subject_id
        JOIN knowledge k ON sk.id_knowledge = k.id
        JOIN achievement_groups ag ON ag.subject_knowledge_id = sk.id
        AND ag.group_id = g.id
        AND ag.period_id = sg.period_id
WHERE
    gs.status = 'A';

-- Consulta para un grupo completo en un periodo específico
SELECT * FROM v_academic_report
WHERE group_id = 2 AND period_id = 1
ORDER BY student_name, subject_name, knowledge_name;

-- Consulta para un estudiante específico en un grupo y periodo
SELECT * FROM v_academic_report
WHERE group_id = 2 AND student_id = 5 AND period_id = 1
ORDER BY subject_name, knowledge_name;


