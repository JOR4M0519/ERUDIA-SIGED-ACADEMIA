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
    update subject_grade sg set recovered = 'R', total_score = newScore where sg.subject_id = idSubject and student_id = idStudent and sg.period_id = idPeriod returning id into subjectGradeId;
    insert into recovery_period (subject_grade, previous_score) values (subjectGradeId, previousScore);
end;
$$