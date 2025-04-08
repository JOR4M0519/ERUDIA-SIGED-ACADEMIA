package co.edu.gimnasiolorismalaguzzi.academyservice.student.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.ErrorDto;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.StudentPromotionDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.ws.rs.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/student-groups")
public class GroupStudentsController {
    private final PersistenceGroupStudentPort groupStudentPort;

    public GroupStudentsController(PersistenceGroupStudentPort groupStudentPort) {
        this.groupStudentPort = groupStudentPort;
    }

    @GetMapping
    public ResponseEntity<List<GroupStudentsDomain>> getAllStudentsInGroups(){
        List<GroupStudentsDomain> groupStudentsDomains = groupStudentPort.findAll();
        return ResponseEntity.ok(groupStudentsDomains);
    }

    @GetMapping("/active")
    public ResponseEntity<List<GroupStudentsDomain>> getAllStudentsInGroupsByStatus(){
        String status = "A";
        List<GroupStudentsDomain> groupStudentsDomains = groupStudentPort.getGroupListByStatus(status);
        return ResponseEntity.ok(groupStudentsDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupStudentsDomain> getGroupStudentById(@PathVariable Integer id){
        GroupStudentsDomain groupStudentsDomain = groupStudentPort.findById(id);
        return ResponseEntity.ok(groupStudentsDomain);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getGroupsStudentByUsername(@PathVariable Integer id){
        List<GroupStudentsDomain> groupStudentsList = groupStudentPort.getGroupsStudentById(id,"A");
        return ResponseEntity.ok(groupStudentsList);
    }
    @GetMapping("/groups/{groupId}/users")
    public ResponseEntity<?> getGroupsStudentByGroupId(@PathVariable Integer groupId){
        List<GroupStudentsDomain> groupStudentsList = groupStudentPort.getGroupsStudentByGroupId(groupId,"I");
        return ResponseEntity.ok(groupStudentsList);
    }

    /**
     * Trae la lista de estudiantes del grupo de un profesor
     * @param mentorId
     * @return
     */
    @GetMapping("/mentors/{mentorId}/students") //?year={year}
    public ResponseEntity<?> getListByMentorIdByYear(@PathVariable Integer mentorId
                                              // ,@RequestParam("year") Integer year
    ){
        List<GroupStudentsDomain> groupStudentsList = groupStudentPort.getListByMentorIdByYear(mentorId,2025);
        return ResponseEntity.ok(groupStudentsList);
    }

    @PostMapping()
    public ResponseEntity<GroupStudentsDomain> createGroupStudent(@RequestBody GroupStudentsDomain groupStudentsDomain){
        GroupStudentsDomain groupStudentsDomain1 = groupStudentPort.save(groupStudentsDomain);
        return ResponseEntity.ok(groupStudentsDomain1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupStudentsDomain> updateGroupStudent(@PathVariable Integer id, @RequestBody GroupStudentsDomain groupStudentsDomain){
        GroupStudentsDomain updated = groupStudentPort.update(id,groupStudentsDomain);
        return ResponseEntity.ok(updated);
    }


    @PostMapping("/promote")
    public ResponseEntity<?> promoteStudents(@RequestBody StudentPromotionDTO promotionDTO) {
        try {
            // Validaciones básicas
            if (promotionDTO.getStudentIds() == null || promotionDTO.getStudentIds().isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorDto("La lista de estudiantes no puede estar vacía"));
            }

            if (promotionDTO.getTargetGroupId() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorDto("El ID del grupo destino es obligatorio"));
            }

            List<GroupStudentsDomain> promotedStudents = groupStudentPort.promoteStudents(promotionDTO);
            return ResponseEntity.ok(promotedStudents);

        } catch (AppException e) {
            // Manejo específico según el código de estado HTTP
            HttpStatus status = e.getCode();
            String message = e.getMessage();

            // Personalizar mensajes según el tipo de error detectado en el adapter
            if (status == HttpStatus.CONFLICT) {
                // Error cuando el estudiante ya está en el grupo destino
                return ResponseEntity
                        .status(status)
                        .body(new ErrorDto(message));

            } else if (status == HttpStatus.UPGRADE_REQUIRED) {
                // Error cuando el estudiante no está activo para promoción
                return ResponseEntity
                        .status(status)
                        .body(new ErrorDto(message));

            } else if (status == HttpStatus.BAD_REQUEST) {
                // Error cuando no se pudo promover a ningún estudiante
                return ResponseEntity
                        .status(status)
                        .body(new ErrorDto(message));

            } else {
                // Otros errores específicos de la aplicación
                return ResponseEntity
                        .status(status)
                        .body(new ErrorDto(message));
            }

        } catch (EntityNotFoundException e) {
            // Error cuando no se encuentra alguna entidad (estudiante o grupo)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorDto("No se encontró una entidad necesaria: " + e.getMessage()));

        } catch (Exception e) {
            // Error general no controlado
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDto("Error inesperado al promover estudiantes: " + e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupStudents(@PathVariable Integer id){
        groupStudentPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
