package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.RelationshipDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceRelationshipPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("api/academy/relationship-types")
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

    @GetMapping("/id")
    public ResponseEntity<RelationshipDomain> getTypeById(@PathVariable Integer id){
        RelationshipDomain relationshipDomain = persistenceRelationshipPort.findById(id);
        return ResponseEntity.ok(relationshipDomain);
    }

    @PostMapping
    public ResponseEntity<RelationshipDomain> createRelationshipType(@RequestBody RelationshipDomain relationshipDomain){
        RelationshipDomain relationshipDomain1 = persistenceRelationshipPort.save(relationshipDomain);
        return ResponseEntity.ok(relationshipDomain1);
    }

    @PutMapping
    public ResponseEntity<RelationshipDomain> updateRelationshipType(@PathVariable Integer id, @RequestBody RelationshipDomain relationshipDomain){
        RelationshipDomain updateRelationshipType = persistenceRelationshipPort.update(id, relationshipDomain);
        return ResponseEntity.ok(updateRelationshipType);
    }
}
