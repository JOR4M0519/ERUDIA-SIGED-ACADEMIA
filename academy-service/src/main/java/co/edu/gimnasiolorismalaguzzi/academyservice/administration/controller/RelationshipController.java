package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.RelationshipDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceRelationshipPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/relationship-types")
public class RelationshipController {

    private final PersistenceRelationshipPort persistenceRelationshipPort;


    public RelationshipController(PersistenceRelationshipPort persistenceRelationshipPort) {
        this.persistenceRelationshipPort = persistenceRelationshipPort;
    }

    @GetMapping
    public ResponseEntity<List<RelationshipDomain>> getAllRelationshipTypes(){
        List<RelationshipDomain> relationshipDomains = persistenceRelationshipPort.findAll();
        return ResponseEntity.ok(relationshipDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RelationshipDomain> getTypeById(@PathVariable Integer id){
        RelationshipDomain relationshipDomain = persistenceRelationshipPort.findById(id);
        return ResponseEntity.ok(relationshipDomain);
    }

    @PostMapping
    public ResponseEntity<RelationshipDomain> createRelationshipType(@RequestBody RelationshipDomain relationshipDomain){
        RelationshipDomain relationshipDomain1 = persistenceRelationshipPort.save(relationshipDomain);
        return ResponseEntity.ok(relationshipDomain1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RelationshipDomain> updateRelationshipType(@PathVariable Integer id, @RequestBody RelationshipDomain relationshipDomain){
        RelationshipDomain updateRelationshipType = persistenceRelationshipPort.update(id, relationshipDomain);
        return ResponseEntity.ok(updateRelationshipType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKnowledge(@PathVariable Integer id) {
        try {
            persistenceRelationshipPort.delete(id);
            return ResponseEntity.noContent().build();
        } catch (AppException e) {
            if (e.getCode() == HttpStatus.CONFLICT) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(e.getMessage());
            } else if (e.getCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud");
        }
    }
}
