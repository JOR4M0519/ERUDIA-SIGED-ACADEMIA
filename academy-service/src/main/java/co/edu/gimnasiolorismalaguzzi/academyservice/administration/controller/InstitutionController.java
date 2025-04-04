package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceInstitutionPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.InstitutionDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/institutions")
public class InstitutionController {

    private final PersistenceInstitutionPort persistenceInstitutionPort;

    public InstitutionController(PersistenceInstitutionPort persistenceInstitutionPort) {
        this.persistenceInstitutionPort = persistenceInstitutionPort;
    }

    @GetMapping()
    public ResponseEntity<List<InstitutionDomain>> getAllInstitutions() {
        List<InstitutionDomain> Institutions = persistenceInstitutionPort.findAll();
        return ResponseEntity.ok(Institutions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDomain> getInstitutionById(@PathVariable Integer id) {
        InstitutionDomain Institution = persistenceInstitutionPort.findById(id);
        return ResponseEntity.ok(Institution);
    }

    @GetMapping("/nit/{nit}")
    public ResponseEntity<InstitutionDomain> getInstitutionByNit(@PathVariable String nit) {
        InstitutionDomain Institution = persistenceInstitutionPort.findByNit(nit);
        return ResponseEntity.ok(Institution);
    }

    @PostMapping()
    public ResponseEntity<InstitutionDomain> createInstitution(@RequestBody InstitutionDomain InstitutionDomain) {
        InstitutionDomain createdInstitution = persistenceInstitutionPort.save(InstitutionDomain);
        return ResponseEntity.ok(createdInstitution);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitutionDomain> updateInstitution(@PathVariable Integer id, @RequestBody InstitutionDomain InstitutionDomain) {
        InstitutionDomain updatedInstitution = persistenceInstitutionPort.update(id, InstitutionDomain);
        return ResponseEntity.ok(updatedInstitution);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstitution(@PathVariable Integer id) {
        persistenceInstitutionPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
