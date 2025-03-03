package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
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
     * @param id
     * @return Materias de los estudiantes
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
