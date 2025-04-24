CREATE OR REPLACE VIEW v_academic_report
    (grade_id, student_id, student_name, document_number, document_type, subject_id, subject_name, period_id,
     period_name, academic_year, total_score, recovered, comment, group_id, group_name, group_code,
     subject_knowledge_id, knowledge_id, knowledge_name, knowledge_percentage, achievement_group_id,
     achievement, score, definitiva_score, period_number, teacher_name, inasistencias, period_scores)
AS
WITH knowledge_scores AS (
    SELECT
        sg_1.id                                                   AS grade_id,
        gs.student_id,
        g.id                                                      AS group_id,
        g.group_name,
        g.group_code,
        sg_1.subject_id,
        s.subject_name,
        sg_1.period_id,
        ap.name                                                   AS period_name,
        EXTRACT(year FROM ap.start_date)                          AS academic_year,
        sg_1.total_score,
        sg_1.recovered,
        sg_1.comment,
        k.id                                                      AS knowledge_id,
        k.name                                                    AS knowledge_name,
        k.percentage                                              AS knowledge_percentage,
        ag.id                                                     AS achievement_group_id,
        ag.achievement,
        COALESCE((
            SELECT kgs.score
              FROM calculateknowledgesgradesbysubjectandgroupandperiod(
                       g.id, sg_1.subject_id, sg_1.period_id
                   ) AS kgs(student_id, knowledge_id, knowledge_name, knowledge_percentage, score, definitiva_score)
             WHERE kgs.student_id   = gs.student_id
               AND kgs.knowledge_id = k.id
        ), 0::numeric)                                            AS knowledge_score,
        COALESCE((
            SELECT kgs.definitiva_score
              FROM calculateknowledgesgradesbysubjectandgroupandperiod(
                       g.id, sg_1.subject_id, sg_1.period_id
                   ) AS kgs(student_id, knowledge_id, knowledge_name, knowledge_percentage, score, definitiva_score)
             WHERE kgs.student_id   = gs.student_id
               AND kgs.knowledge_id = k.id
        ), 0::numeric)                                            AS knowledge_definitiva_score
    FROM subject_grade sg_1
    JOIN users u_1            ON sg_1.student_id            = u_1.id
    JOIN user_detail ud_1     ON u_1.id                     = ud_1.user_id
    JOIN id_type it_1         ON ud_1.id_type_id           = it_1.id
    JOIN subject s            ON sg_1.subject_id           = s.id
    JOIN academic_period ap   ON sg_1.period_id            = ap.id
    JOIN group_students gs    ON sg_1.student_id           = gs.student_id
    JOIN groups g             ON gs.group_id               = g.id
    JOIN subject_groups sgr   ON sgr.group_students        = g.id
                             AND sgr.academic_period_id   = sg_1.period_id
    JOIN subject_knowledge sk_1
                             ON sk_1.id_subject           = sg_1.subject_id
    JOIN knowledge k          ON sk_1.id_knowledge         = k.id
    JOIN achievement_groups ag
                             ON ag.subject_knowledge_id   = sk_1.id
                            AND ag.group_id               = g.id
                            AND ag.period_id              = sg_1.period_id
    WHERE gs.status::text = 'A'
),
period_info AS (
    SELECT
        ap.id,
        ap.name,
        EXTRACT(year FROM ap.start_date)                                   AS year,
        row_number() OVER (PARTITION BY EXTRACT(year FROM ap.start_date)
                           ORDER BY ap.start_date)                         AS period_number
    FROM academic_period ap
    WHERE ap.status::text = 'A'
),
period_scores_data AS (
    SELECT
        sg_hist.student_id,
        sg_hist.subject_id,
        sg_hist.period_id,
        ap_hist.name                                                      AS period_name,
        EXTRACT(year FROM ap_hist.start_date)                              AS academic_year,
        'P' || row_number() OVER (
            PARTITION BY sg_hist.student_id, sg_hist.subject_id,
                         EXTRACT(year FROM ap_hist.start_date)
            ORDER BY ap_hist.start_date
        )                                                                  AS period_number,
        sg_hist.total_score
    FROM subject_grade sg_hist
    JOIN academic_period ap_hist ON sg_hist.period_id = ap_hist.id
)
SELECT DISTINCT ON (ks.student_id, ks.subject_id, ks.period_id, ks.knowledge_id)
    sg.id                                                   AS grade_id,
    ks.student_id,
    (
    ((ud.first_name::text || ' ') || ud.middle_name::text)
    || ud.last_name::text || ' ' || ud.second_last_name::text
    )                                                        AS student_name,
    ud.dni                                                  AS document_number,
    it.name                                                 AS document_type,
    ks.subject_id,
    ks.subject_name,
    ks.period_id,
    ks.period_name,
    ks.academic_year,
    ks.total_score,
    ks.recovered,
    ks.comment,
    ks.group_id,
    ks.group_name,
    ks.group_code,
    sk.id                                                   AS subject_knowledge_id,
    ks.knowledge_id,
    ks.knowledge_name,
    ks.knowledge_percentage,
    ks.achievement_group_id,
    ks.achievement,
    ks.knowledge_score                                      AS score,
    ks.knowledge_definitiva_score                           AS definitiva_score,
    pi.period_number,
    (
    SELECT
    ((((u_d2.first_name::text || ' '::text)
    || u_d2.middle_name::text)
    || u_d2.last_name::text) || ' '::text)
    || u_d2.second_last_name::text
    FROM subject_groups   sg2
    JOIN subject_professors sp2 ON sg2.subject_professor_id = sp2.id
    JOIN users            u2  ON sp2.professor_id         = u2.id
    JOIN user_detail      u_d2 ON u_d2.user_id            = u2.id
    WHERE sg2.group_students      = ks.group_id
    AND sg2.academic_period_id   = ks.period_id
    AND sp2.subject_id           = ks.subject_id
    LIMIT 1
    )                                                        AS teacher_name,
    COALESCE((
    SELECT COUNT(*) AS count
    FROM attendance a
    JOIN subject_schedule ss ON a.schedule_id = ss.id
    WHERE a.student_id          = ks.student_id
    AND ss.subject_group_id   = sg.id
    AND a.status::text        = 'A'
    ), 0::bigint)                                             AS inasistencias,
    (
    SELECT jsonb_agg(
    jsonb_build_object(
    'period_id',     psd.period_id,
    'period_name',   psd.period_name,
    'period_number', psd.period_number,
    'score',         psd.total_score
    )
    )
    FROM period_scores_data psd
    WHERE psd.student_id     = ks.student_id
    AND psd.subject_id     = ks.subject_id
    AND psd.academic_year  = ks.academic_year
    )                                                        AS period_scores
FROM knowledge_scores ks
    JOIN users u            ON ks.student_id  = u.id
    JOIN user_detail ud     ON u.id           = ud.user_id
    JOIN id_type it         ON ud.id_type_id  = it.id
    JOIN subject_knowledge sk
    ON sk.id_subject  = ks.subject_id
    AND sk.id_knowledge = ks.knowledge_id
    JOIN subject_groups sg  ON sg.group_students   = ks.group_id
    AND sg.academic_period_id = ks.period_id
    JOIN period_info pi     ON pi.id              = ks.period_id
ORDER BY ks.student_id, ks.subject_id, ks.period_id, ks.knowledge_id, ks.grade_id;



-- Consulta para un grupo completo en un periodo específico
SELECT * FROM v_academic_report
WHERE group_id = 2 AND period_id = 1
ORDER BY student_name, subject_name, knowledge_name;

-- Consulta para un estudiante específico en un grupo y periodo
SELECT * FROM v_academic_report
WHERE group_id = 2 AND student_id = 5 AND period_id = 1
ORDER BY subject_name, knowledge_name;


