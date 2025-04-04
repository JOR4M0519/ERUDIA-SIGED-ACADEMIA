package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceActivityGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/activity-group")
public class ActivityGroupController {
    private final PersistenceActivityGroupPort persistenceActivityGroupPort;

    public ActivityGroupController(PersistenceActivityGroupPort persistenceActivityGroupPort) {
        this.persistenceActivityGroupPort = persistenceActivityGroupPort;
    }
    @GetMapping
    public ResponseEntity<List<ActivityGroupDomain>> getAllActivity_Group(){
        List<ActivityGroupDomain> ActivityGroupDomainList = persistenceActivityGroupPort.findAll();
        return ResponseEntity.ok(ActivityGroupDomainList);
    }

    /**
     * Obtiene todas las actividades de un estudiante de un periodo especifico (Activas y finalizadas)
     * @param periodId
     * @param userId
     * @return Lista de actividades.
     */
    //Se necesita agregar el pageable - Buscar como se usa

    @GetMapping("/periods/{periodId}/users/{userId}")
    public ResponseEntity<List<?>> getAllActivity_ByPeriodUser(@PathVariable Integer periodId,
                                                               @PathVariable Integer userId){
        List<ActivityGroupDomain> activityGroupDomains = persistenceActivityGroupPort.getAllActivity_ByPeriodUser(periodId, userId, "I");
        return ResponseEntity.ok(activityGroupDomains);
    }

    /**
     * Las actividades de una materia, por el periodo de un grupo de estudiantes (Activas y finalizadas)
     * @param subjectId
     * @param periodId
     * @param
     * @return Lista de actividades
     */

    @GetMapping("/periods/{periodId}/subjects/{subjectId}/groups/{groupId}")
    public ResponseEntity<List<?>> getAllActivity_ByPeriodSubjectGroup(@PathVariable Integer subjectId,
                                                                             @PathVariable Integer periodId,
                                                                             @PathVariable Integer groupId ){
        List<ActivityGroupDomain> activityGroupDomains = persistenceActivityGroupPort.getAllActivity_ByPeriodSubjectGroup(subjectId,periodId,groupId,"I");
        return ResponseEntity.ok(activityGroupDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityGroupDomain> getActivityGroupById(@PathVariable Integer id){
        ActivityGroupDomain ActivityGroupDomain = persistenceActivityGroupPort.findById(id);
        return ResponseEntity.ok(ActivityGroupDomain);
    }

    @GetMapping("/activities/{activityId}")
    public ResponseEntity<ActivityGroupDomain> getRangeDateActivityByActivityId(@PathVariable Integer activityId){
        ActivityGroupDomain ActivityGroupDomain = persistenceActivityGroupPort.getRangeDateActivityByActivityId(activityId);
        return ResponseEntity.ok(ActivityGroupDomain);
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<List<?>> getActivitiesByGroupId(@PathVariable Integer id){
        List<ActivityGroupDomain> activityGroupDomains = persistenceActivityGroupPort.findActivitiesByGroupId(id, "I");
        return ResponseEntity.ok(activityGroupDomains);
    }

    @PostMapping
    public ResponseEntity<ActivityGroupDomain> createActivityGroup(@RequestBody ActivityGroupDomain activityGroupDomain){
        ActivityGroupDomain createdDimension = persistenceActivityGroupPort.save(activityGroupDomain);
        return ResponseEntity.ok(createdDimension);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityGroupDomain> updateActivityGroup(@PathVariable Integer id, @RequestBody ActivityGroupDomain ActivityGroupDomain){
        ActivityGroupDomain updatedDimension = persistenceActivityGroupPort.update(id,ActivityGroupDomain);
        return ResponseEntity.ok(updatedDimension);
    }
}
