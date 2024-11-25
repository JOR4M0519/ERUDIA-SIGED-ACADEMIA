package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.AcademicPeriodServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.AcademicPeriodDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/periods")
public class AcademicPeriodController {
    private final AcademicPeriodServicePort academicPeriodServicePort;

    public AcademicPeriodController(AcademicPeriodServicePort academicPeriodServicePort) {
        this.academicPeriodServicePort = academicPeriodServicePort;
    }

    @GetMapping
    public ResponseEntity<List<AcademicPeriodDomain>> getAllStudentGroups(){
        List<AcademicPeriodDomain> AcademicPeriodDomains = academicPeriodServicePort.getAllPeriods();
        return ResponseEntity.ok(AcademicPeriodDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicPeriodDomain> getAcademicPeriodById(@PathVariable Integer id) {
        AcademicPeriodDomain AcademicPeriod = academicPeriodServicePort.getPeriodById(id);
        return ResponseEntity.ok(AcademicPeriod);
    }

    @PostMapping()
    public ResponseEntity<AcademicPeriodDomain> createAcademicPeriod(@RequestBody AcademicPeriodDomain AcademicPeriodDomain) {
        AcademicPeriodDomain createdAcademicPeriod = academicPeriodServicePort.createPeriod(AcademicPeriodDomain);
        return ResponseEntity.ok(createdAcademicPeriod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicPeriodDomain> updateAcademicPeriod(@PathVariable Integer id, @RequestBody AcademicPeriodDomain AcademicPeriodDomain) {
        AcademicPeriodDomain updatedAcademicPeriod = academicPeriodServicePort.updatePeriod(id, AcademicPeriodDomain);
        return ResponseEntity.ok(updatedAcademicPeriod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcademicPeriod(@PathVariable Integer id) {
        academicPeriodServicePort.deletePeriod(id);
        return ResponseEntity.noContent().build();
    }

}
