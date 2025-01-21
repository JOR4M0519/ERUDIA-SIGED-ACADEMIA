package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.SubjectScheduleServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectScheduleDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subject-schedules")
public class SubjectScheduleController {
    private final SubjectScheduleServicePort subjectScheduleServicePort;

    public SubjectScheduleController(SubjectScheduleServicePort subjectScheduleServicePort) {
        this.subjectScheduleServicePort = subjectScheduleServicePort;
    }

    @GetMapping()
    public ResponseEntity<List<SubjectScheduleDomain>> getAllEducationalLevels() {
        List<SubjectScheduleDomain> EducationalLevels = subjectScheduleServicePort.getAllSubjectSchedules();
        return ResponseEntity.ok(EducationalLevels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectScheduleDomain> getEducationalLevelById(@PathVariable Integer id) {
        SubjectScheduleDomain EducationalLevel = subjectScheduleServicePort.getScheduleById(id);
        return ResponseEntity.ok(EducationalLevel);
    }

    @PostMapping()
    public ResponseEntity<SubjectScheduleDomain> createEducationalLevel(@RequestBody SubjectScheduleDomain SubjectScheduleDomain) {
        SubjectScheduleDomain createdEducationalLevel = subjectScheduleServicePort.createSubjectSchedule(SubjectScheduleDomain);
        return ResponseEntity.ok(createdEducationalLevel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectScheduleDomain> updateEducationalLevel(@PathVariable Integer id, @RequestBody SubjectScheduleDomain SubjectScheduleDomain) {
        SubjectScheduleDomain updatedEducationalLevel = subjectScheduleServicePort.updateSubjectSchedule(id, SubjectScheduleDomain);
        return ResponseEntity.ok(updatedEducationalLevel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationalLevel(@PathVariable Integer id) {
        subjectScheduleServicePort.deleteSubjectSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
