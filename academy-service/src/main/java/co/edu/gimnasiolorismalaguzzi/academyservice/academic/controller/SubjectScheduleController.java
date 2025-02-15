package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectSchedulePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectScheduleDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subject-schedules")
public class SubjectScheduleController {
    private final PersistenceSubjectSchedulePort subjectSchedulePort;

    public SubjectScheduleController(PersistenceSubjectSchedulePort subjectSchedulePort) {
        this.subjectSchedulePort = subjectSchedulePort;
    }

    @GetMapping()
    public ResponseEntity<List<SubjectScheduleDomain>> getAllEducationalLevels() {
        List<SubjectScheduleDomain> EducationalLevels = subjectSchedulePort.findAll();
        return ResponseEntity.ok(EducationalLevels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectScheduleDomain> getEducationalLevelById(@PathVariable Integer id) {
        SubjectScheduleDomain EducationalLevel = subjectSchedulePort.findById(id);
        return ResponseEntity.ok(EducationalLevel);
    }

    @PostMapping()
    public ResponseEntity<SubjectScheduleDomain> createEducationalLevel(@RequestBody SubjectScheduleDomain SubjectScheduleDomain) {
        SubjectScheduleDomain createdEducationalLevel = subjectSchedulePort.save(SubjectScheduleDomain);
        return ResponseEntity.ok(createdEducationalLevel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectScheduleDomain> updateEducationalLevel(@PathVariable Integer id, @RequestBody SubjectScheduleDomain SubjectScheduleDomain) {
        SubjectScheduleDomain updatedEducationalLevel = subjectSchedulePort.update(id, SubjectScheduleDomain);
        return ResponseEntity.ok(updatedEducationalLevel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationalLevel(@PathVariable Integer id) {
        subjectSchedulePort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
