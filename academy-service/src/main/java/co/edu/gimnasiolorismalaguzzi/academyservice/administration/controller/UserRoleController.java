package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.RoleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserRoleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceRolePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserRolePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/academy/roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleCrudRepo userRoleCrudRepo;
    private final RoleCrudRepo roleCrudRepo;

    private final PersistenceUserRolePort persistenceUserRolePort;
    private final PersistenceRolePort persistenceRolePort;


    @GetMapping("/assigned")
    public ResponseEntity<?> getRolesAssigned() {
        return ResponseEntity.ok(persistenceUserRolePort.findAll());
    }

    @GetMapping("")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(persistenceRolePort.findAll());
    }


    /*@GetMapping("/users/{userId}")
    public ResponseEntity<List<UserDomain>> get(@PathVariable Long userId) {
        return ResponseEntity.ok(userRoleCrudRepo.);
    }*/

    @GetMapping("/users/administrative")
    public ResponseEntity<List<UserDomain>> getAdministrativeUsers() {
        return ResponseEntity.ok(persistenceUserRolePort.getAdministrativeUsers());
    }

    @GetMapping("/users/students")
    public ResponseEntity<List<UserDomain>> getStudents() {
        return ResponseEntity.ok(persistenceUserRolePort.getStudents());
    }
}