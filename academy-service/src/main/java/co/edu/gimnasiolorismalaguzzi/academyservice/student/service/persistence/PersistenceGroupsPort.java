package co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.RepeatingStudentsGroupReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.ReportGroupsStatusDomain;

import java.util.List;

public interface PersistenceGroupsPort extends PersistencePort<GroupsDomain, Integer> {

    List<GroupsDomain> findByLevelId(Integer levelId);
    List<ReportGroupsStatusDomain> getAcademicLevelReport();

    List<AttendanceReportDomain> getAttendanceReport();

    List<RepeatingStudentsGroupReportDomain> getRepeatingStudentsByGroupReport();

    List<GroupsDomain> findByStatus(String status);
}
