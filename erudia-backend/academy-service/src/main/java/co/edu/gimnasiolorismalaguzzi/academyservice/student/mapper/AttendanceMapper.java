package co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Attendance;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "student", target = "student"),
            @Mapping(source = "schedule", target = "schedule"),
            @Mapping(source = "attendanceDate", target = "attendanceDate"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "recordedAt", target = "recordedAt"),
    })
    AttendanceDomain toDomain(Attendance attendance);

    @InheritInverseConfiguration
    Attendance toEntity(AttendanceDomain attendanceDomain);

    List<AttendanceDomain> toDomains(List<Attendance> ateAttendanceList);
    List<Attendance> toEntities(List<AttendanceDomain> attendanceDomainList);
}
