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
                  status VARCHAR(1)
              ) AS $$
BEGIN
RETURN QUERY
SELECT ap.id, ap.setting_id, ap.start_date, ap.end_date, ap.name, ap.status
FROM academic_period ap
WHERE ap.start_date <= TO_DATE(p_year || '-12-31', 'YYYY-MM-DD')  -- 🔹 Inicia antes o durante el año
  AND ap.end_date >= TO_DATE(p_year || '-01-01', 'YYYY-MM-DD')  -- 🔹 Termina después o durante el año
  AND ap.status IN ('A', 'F');
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
