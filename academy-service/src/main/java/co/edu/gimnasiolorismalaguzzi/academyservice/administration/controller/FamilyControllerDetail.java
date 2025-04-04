package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserFamilyRelationDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceFamilyPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserDetailPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/academy/families")
@Slf4j
public class FamilyControllerDetail {

    private final PersistenceFamilyPort persistenceFamilyPort;
    private final PersistenceUserDetailPort persistenceUserDetailPort;

    public FamilyControllerDetail(PersistenceFamilyPort persistenceFamilyPort, PersistenceUserDetailPort persistenceUserDetailPort) {
        this.persistenceFamilyPort = persistenceFamilyPort;
        this.persistenceUserDetailPort = persistenceUserDetailPort;
    }

    @PostMapping
    public ResponseEntity<List<FamilyDomain>> createFamilyRelations(@RequestBody UserFamilyRelationDomain userFamilyRelationDomain) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(persistenceFamilyPort.saveFamilyRelations(userFamilyRelationDomain));
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al crear relaciones familiares", e);
            throw new AppException("Error al crear relaciones familiares: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamilyDomain> updateFamilyRelation(@PathVariable Integer id, @RequestBody FamilyDomain familyDomain) {
        try {
            log.info("Actualizando relación familiar con ID: {}", id);

            if (id == null) {
                throw new AppException("El ID de la relación familiar es requerido", HttpStatus.BAD_REQUEST);
            }

            if (familyDomain == null) {
                throw new AppException("Los datos de la relación familiar son requeridos", HttpStatus.BAD_REQUEST);
            }

            // Asignar el ID al objeto de dominio
            familyDomain.setId(id);

            // Actualizar la relación
            FamilyDomain updatedRelation = persistenceFamilyPort.update(id, familyDomain);

            log.info("Relación familiar actualizada exitosamente");
            return ResponseEntity.ok(updatedRelation);

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar relación familiar", e);
            throw new AppException("Error al actualizar relación familiar: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamilyRelation(@PathVariable Integer id) {
        try {
            log.info("Eliminando relación familiar con ID: {}", id);

            if (id == null) {
                throw new AppException("El ID de la relación familiar es requerido", HttpStatus.BAD_REQUEST);
            }

            // Eliminar la relación
            HttpStatus result = persistenceFamilyPort.delete(id);

            if (result == HttpStatus.OK) {
                log.info("Relación familiar eliminada exitosamente");
                return ResponseEntity.noContent().build();
            } else {
                log.warn("La eliminación de la relación familiar devolvió estado: {}", result);
                return ResponseEntity.status(result).build();
            }

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al eliminar relación familiar", e);
            throw new AppException("Error al eliminar relación familiar: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserFamilyRelationDomain>> getAllUsersWithFamilyRelations() {
        try {
            log.info("Obteniendo todos los usuarios con sus relaciones familiares");
            List<UserFamilyRelationDomain> result = persistenceFamilyPort.findAllWithRelatives();
            log.info("Se encontraron {} usuarios con relaciones familiares", result.size());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error al obtener usuarios con relaciones familiares", e);
            throw new AppException("Error al obtener usuarios con relaciones familiares: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<List<FamilyDomain>> getFamilyRelationsByUserId(@PathVariable Integer userId) {
        try {
            log.info("Obteniendo relaciones familiares para usuario con ID: {}", userId);

            if (userId == null) {
                throw new AppException("El ID del usuario es requerido", HttpStatus.BAD_REQUEST);
            }

            List<FamilyDomain> relations = persistenceFamilyPort.findRelativesByStudent(userId);

            log.info("Se encontraron {} relaciones familiares para el usuario {}", relations.size(), userId);
            return ResponseEntity.ok(relations);

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al obtener relaciones familiares por ID de usuario", e);
            throw new AppException("Error al obtener relaciones familiares: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reports")
    public ResponseEntity<List<FamilyReportDomain>> getFamilyReports() {
        try {
            log.info("Obteniendo informes de familias");
            List<FamilyReportDomain> reports = persistenceFamilyPort.getAllFamilyReports();
            log.info("Se encontraron {} informes de familias", reports.size());
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            log.error("Error al obtener informes de familias", e);
            throw new AppException("Error al obtener informes de familias: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}