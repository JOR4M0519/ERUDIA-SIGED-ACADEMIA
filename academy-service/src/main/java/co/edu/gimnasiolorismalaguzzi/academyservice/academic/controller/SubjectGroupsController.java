package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subjects-groups")
public class SubjectGroupsController {
    private final PersistenceSubjectGroupPort subjectGroupPort;

    public SubjectGroupsController(PersistenceSubjectGroupPort subjectGroupPort) {
        this.subjectGroupPort = subjectGroupPort;
    }

    @GetMapping
    public ResponseEntity<List<SubjectGroupDomain>> getAllSubjectGroups(){
        List<SubjectGroupDomain> subjectGroupDomains = subjectGroupPort.findAll();
        return ResponseEntity.ok(subjectGroupDomains);
    }

    /**
     * Obtiene las materias de los estudiante con base en un id del estudiante
     * @param studentId
     * @param year
     * @return
     */

    @GetMapping("/students-groups/students/{studentId}")
    public ResponseEntity<List<?>> getAllSubjectGroupsByStudentsGroupsId(@PathVariable Integer studentId,@RequestParam String year){
        List<SubjectGroupDomain> subjectGroupDomains = subjectGroupPort.getAllSubjectGroupsByStudentId(studentId,year);
        return ResponseEntity.ok(subjectGroupDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectGroupDomain> getSubjectGroupById(@PathVariable Integer id){
        SubjectGroupDomain subjectGroupDomain = subjectGroupPort.findById(id);
        return ResponseEntity.ok(subjectGroupDomain);
    }

    /**
     * Obtiene las materias de que tienen los profesores asignadas con base un id del profesor
     * @param id
     * @return Materias de los Profesores
     */
    @GetMapping("/teacher-groups/teachers/{id}/subjects")
    public ResponseEntity<?> getAllSubjectByTeacherOfYear(
            @PathVariable Integer id,
            @RequestParam("year") Integer year) {

        List<SubjectGroupDomain> subjectGroupDomains = subjectGroupPort.getAllSubjectByTeacher(id, year);
        return ResponseEntity.ok(subjectGroupDomains);
    }

    /**
     * Obtiene las materias de cada grupo de estudiantes de un periodo
     * @param periodId
     * @param levelId
     * @return Materias de los Profesores
     */
    @GetMapping("/periods/{periodId}/edu-level/{levelId}")
    public ResponseEntity<?> getAllSubjectGRoupsByPeriodAndLevel(
            @PathVariable Integer periodId,
            @PathVariable Integer levelId) {

        List<SubjectGroupDomain> subjectGroupDomains = subjectGroupPort.
                getAllSubjectGRoupsByPeriodAndLevel(periodId,levelId);
        return ResponseEntity.ok(subjectGroupDomains);
    }


    /**
     * Obtiene la lista de estudiantes de una materia !!!ELIMINAR!!!
     * @param groupId
     * @param teacherId
     * @param periodId
     * @return
     */
    @GetMapping("/groups/{groupId}/subjects/{subjectId}/teachers/{teacherId}/periods/{periodId}/students")
    public ResponseEntity<?> getStudentListBySubjectId(@PathVariable Integer groupId,
                                                       @PathVariable Integer subjectId,
                                                       @PathVariable Integer teacherId,
                                                       @PathVariable Integer periodId){
        List<?> userList = subjectGroupPort.getStudentListByGroupTeacherPeriod(groupId,subjectId,teacherId,periodId);
        return ResponseEntity.ok(userList);
    }

    /**
     * Obtiene la lista de registros por grupo de estudiante y periodo
     * @param groupId
     * @param periodId
     * @return
     */
    @GetMapping("/groups/{groupId}/periods/{periodId}")
    public ResponseEntity<?> getSubjectsByGroupIdAndPeriodId(
            @PathVariable Integer groupId,
            @PathVariable Integer periodId){
        List<SubjectGroupDomain> subjectGroupDomainList = subjectGroupPort.getSubjectsByGroupIdAndPeriodId(
                groupId,periodId
        );
        return ResponseEntity.ok(subjectGroupDomainList);
    }

    /**
     * Obtiene la lista de de estudiantes de una materia para el grupo
     * @param periodId  a
     * @param subjectId a
     * @return
     */
    @GetMapping("/periods/{periodId}/subjects/{subjectId}")
    public ResponseEntity<?> getGroupsStudentsByPeriodIdAndSubjectId(
            @PathVariable Integer periodId,
            @PathVariable Integer subjectId){
        List<GroupStudentsDomain> subjectGroupDomainList= subjectGroupPort.getGroupsStudentsByPeriodIdAndSubjectId(
                periodId,subjectId
        );
        return ResponseEntity.ok(subjectGroupDomainList);
    }

    @PostMapping
    public ResponseEntity<SubjectGroupDomain> createSubjectGroup(@RequestBody SubjectGroupDomain subjectGroupDomain){
        SubjectGroupDomain created = subjectGroupPort.save(subjectGroupDomain);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectGroupDomain> updateSubjectGroup(@PathVariable Integer id, @RequestBody SubjectGroupDomain subjectGroupDomain){
        SubjectGroupDomain updated = subjectGroupPort.update(id,subjectGroupDomain);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubjectGroup(@PathVariable Integer id){
        subjectGroupPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
