package co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyReportDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Component
public class FamilyReportMapper implements RowMapper<FamilyReportDomain> {

    @Override
    public FamilyReportDomain mapRow(ResultSet rs, int rowNum) throws SQLException {
        FamilyReportDomain report = new FamilyReportDomain();

        try {
        report.setCode(rs.getString("codigo"));
        report.setFamilyName(rs.getString("familia"));

            // Convertir los valores numéricos a Long
            report.setTotalMembers(rs.getLong("miembros"));
            report.setActiveStudents(rs.getLong("estudiantes_activos"));

            // Manejar el array de estudiantes
        Array sqlArray = rs.getArray("ids_estudiantes");
        if (sqlArray != null) {
                try {
                    Object[] array = (Object[]) sqlArray.getArray();
                    // Convertir cada elemento a Integer y agregarlo a la lista
                    report.setStudentIds(
                        Arrays.stream(array)
                            .map(obj -> {
                                if (obj instanceof Integer) {
                                    return (Integer) obj;
                                } else if (obj instanceof Number) {
                                    return ((Number) obj).intValue();
        }
                                return null;
                            })
                            .filter(id -> id != null)
                            .toList()
                    );
                } catch (Exception e) {
                    log.error("Error al convertir array de IDs de estudiantes para código {}: {}",
                             report.getCode(), e.getMessage());
                    report.setStudentIds(new ArrayList<>());
    }
            } else {
                report.setStudentIds(new ArrayList<>());
}

        } catch (SQLException e) {
            log.error("Error al mapear el reporte de familia en la fila {}: {}",
                     rowNum, e.getMessage());
            throw e;
        }

        return report;
    }

    // Método auxiliar para mapear desde Object[]
    public FamilyReportDomain mapFromObjectArray(Object[] result) {
        FamilyReportDomain report = new FamilyReportDomain();

        try {
            report.setCode((String) result[0]);
            report.setFamilyName((String) result[1]);

            // Convertir números a Long
            report.setTotalMembers(convertToLong(result[2]));
            report.setActiveStudents(convertToLong(result[3]));

            // Manejar el array de estudiantes
            if (result[4] instanceof Array) {
                try {
                    Array sqlArray = (Array) result[4];
                    Object[] array = (Object[]) sqlArray.getArray();
                    report.setStudentIds(
                        Arrays.stream(array)
                            .map(obj -> {
                                if (obj instanceof Integer) {
                                    return (Integer) obj;
                                } else if (obj instanceof Number) {
                                    return ((Number) obj).intValue();
                                }
                                return null;
                            })
                            .filter(id -> id != null)
                            .toList()
                    );
                } catch (Exception e) {
                    log.error("Error al convertir array de IDs de estudiantes para código {}: {}",
                             report.getCode(), e.getMessage());
                    report.setStudentIds(new ArrayList<>());
                }
            } else {
                report.setStudentIds(new ArrayList<>());
            }

        } catch (Exception e) {
            log.error("Error al mapear el reporte de familia desde array: {}",
                     e.getMessage());
            throw new RuntimeException("Error al mapear el reporte de familia", e);
        }

        return report;
    }

    private Long convertToLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return 0L;
    }
}
