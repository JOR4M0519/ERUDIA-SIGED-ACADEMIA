package co.edu.gimnasiolorismalaguzzi.academyservice.student.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.RepeatingStudentsGroupReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.ReportGroupsStatusDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.GroupsMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.GroupsCrudRepo;
//import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupsPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistenceAdapter
@Slf4j
public class GroupsAdapter implements PersistenceGroupsPort {

    private final GroupsCrudRepo groupsCrudRepo;
    private final JdbcTemplate jdbcTemplate;
    private final GroupsMapper groupsMapper;

    @Autowired
    public GroupsAdapter(GroupsCrudRepo groupsCrudRepo,
                         JdbcTemplate jdbcTemplate,
                         GroupsMapper groupsMapper) {
        this.groupsCrudRepo = groupsCrudRepo;
        this.jdbcTemplate = jdbcTemplate;
        this.groupsMapper = groupsMapper;
    }

    @Override
    public List<GroupsDomain> findAll() {
        return this.groupsMapper.toDomains(this.groupsCrudRepo.findAll());
    }

    @Override
    public List<GroupsDomain> findByStatus(String status) {
        return this.groupsMapper.toDomains(groupsCrudRepo.findByStatus("A"));
    }

    @Override
    public GroupsDomain findById(Integer id) {
        Optional<Groups> groupStudentOptional = this.groupsCrudRepo.findById(id);
        return groupStudentOptional.map(groupsMapper::toDomain).orElse(null);
    }
    @Override
    public List<GroupsDomain> findByLevelId(Integer levelId) {
        return this.groupsMapper.toDomains(groupsCrudRepo.findByLevel_Id(levelId));
    }

    @Override
    public GroupsDomain save(GroupsDomain groupsDomain) {
        Groups groups = groupsMapper.toEntity(groupsDomain);
        Groups savedGroup = this.groupsCrudRepo.save(groups);
        return groupsMapper.toDomain(savedGroup);
    }

    @Override
    public GroupsDomain update(Integer id, GroupsDomain domain) {
        try{
            Optional<Groups> existingGroup = groupsCrudRepo.findById(id);
            if(existingGroup.isPresent()){
                existingGroup.get().setLevel(groupsMapper.toEntity(domain).getLevel());
                existingGroup.get().setGroupCode(domain.getGroupCode());
                existingGroup.get().setGroupName(domain.getGroupName());
                existingGroup.get().setMentor(groupsMapper.toEntity(domain).getMentor());
                existingGroup.get().setStatus(domain.getStatus());
            }
            return groupsMapper.toDomain(groupsCrudRepo.save(existingGroup.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Group with id: " + id + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer id) {
        try {
            if(this.groupsCrudRepo.existsById(id)){
                groupsCrudRepo.deleteById(id);
                return HttpStatus.OK;
            } else {
                throw new AppException("Group ID doesnt exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            groupsCrudRepo.updateStatusById("I",id); //I de inactivo
            throw new AppException("Internal Error", HttpStatus.CONFLICT);
        }
    }



    @Override
    public List<ReportGroupsStatusDomain> getAcademicLevelReport() {
        List<Object[]> results = groupsCrudRepo.getAcademicLevelReport();
        List<ReportGroupsStatusDomain> reportList = new ArrayList<>();

        for (Object[] result : results) {
            ReportGroupsStatusDomain report = new ReportGroupsStatusDomain();
            report.setGroupId((Integer) result[0]);
            report.setLevelName((String) result[1]);
            report.setGroupName((String) result[2]);
            report.setStatusName((String) result[3]);
            report.setStudentsTotal((Long) result[4]);
            reportList.add(report);
        }

        return reportList;
    }

    @Override
    public List<AttendanceReportDomain> getAttendanceReport() {
        return jdbcTemplate.query(
                "SELECT * FROM get_attendance_report()",
                (rs, rowNum) -> new AttendanceReportDomain(
                        rs.getInt("group_id"),
                        rs.getString("level_name"),
                        rs.getString("group_name"),
                        rs.getString("section_name"),
                        rs.getLong("total_active"),
                        rs.getLong("present_count"),
                        rs.getLong("absent_count"),
                        rs.getLong("late_count"),
                        rs.getTimestamp("last_record") != null ?
                                rs.getTimestamp("last_record").toLocalDateTime() : null
                )
        );
    }

    @Override
    public List<RepeatingStudentsGroupReportDomain> getRepeatingStudentsByGroupReport() {
        return jdbcTemplate.query(
                "SELECT * FROM get_repeating_students_report_by_group()",
                (rs, rowNum) -> new RepeatingStudentsGroupReportDomain(
                        rs.getInt("group_id"),
                        rs.getString("group_name"),
                        rs.getString("level_name"),
                        rs.getLong("repeating_count")
                )
        );
    }

    // Añadir este método a GroupsAdapter.java

    public List<GroupsDomain> getGroupsByLevelAndStatus(String levelId, String status) {
        try {
            // Convertir levelId de String a Integer antes de pasarlo al repositorio
            Integer levelIdInt = Integer.parseInt(levelId);
            List<Groups> groups = groupsCrudRepo.findByLevelIdAndStatus(levelIdInt, status);
            return groupsMapper.toDomains(groups);
        } catch (NumberFormatException e) {
            throw new AppException("El ID del nivel debe ser un número válido", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new AppException("Error al obtener grupos por nivel y estado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
