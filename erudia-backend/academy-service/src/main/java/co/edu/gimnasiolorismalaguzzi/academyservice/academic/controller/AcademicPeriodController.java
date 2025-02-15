package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.PersistenceAcademicPeriodPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/periods")
public class AcademicPeriodController {
    private final PersistenceAcademicPeriodPort academicPeriodServicePort;

    public AcademicPeriodController(PersistenceAcademicPeriodPort academicPeriodServicePort) {
        this.academicPeriodServicePort = academicPeriodServicePort;
    }

    @GetMapping
    public ResponseEntity<List<AcademicPeriodDomain>> getAllPeriods(){
        List<AcademicPeriodDomain> AcademicPeriodDomains = academicPeriodServicePort.findAll();
        return ResponseEntity.ok(AcademicPeriodDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicPeriodDomain> getAcademicPeriodById(@PathVariable Integer id) {
        AcademicPeriodDomain AcademicPeriod = academicPeriodServicePort.findById(id);
        return ResponseEntity.ok(AcademicPeriod);
    }

    @PostMapping()
    public ResponseEntity<AcademicPeriodDomain> createAcademicPeriod(@RequestBody AcademicPeriodDomain AcademicPeriodDomain) {
        AcademicPeriodDomain createdAcademicPeriod = academicPeriodServicePort.save(AcademicPeriodDomain);
        return ResponseEntity.ok(createdAcademicPeriod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicPeriodDomain> updateAcademicPeriod(@PathVariable Integer id, @RequestBody AcademicPeriodDomain AcademicPeriodDomain) {
        AcademicPeriodDomain updatedAcademicPeriod = academicPeriodServicePort.update(id, AcademicPeriodDomain);
        return ResponseEntity.ok(updatedAcademicPeriod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcademicPeriod(@PathVariable Integer id) {
        academicPeriodServicePort.delete(id);
        return ResponseEntity.noContent().build();
    }

}
