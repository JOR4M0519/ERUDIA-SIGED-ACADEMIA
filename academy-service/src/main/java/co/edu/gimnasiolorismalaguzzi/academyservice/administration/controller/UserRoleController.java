package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.RoleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserRoleCrudRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/academy/users/roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleCrudRepo userRoleCrudRepo;
    private final RoleCrudRepo roleCrudRepo;

    @GetMapping("")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleCrudRepo.findAll());
    }

    @GetMapping("/administrative")
    public ResponseEntity<List<User>> getAdministrativeUsers() {
        return ResponseEntity.ok(userRoleCrudRepo.findAllAdministrativeUsers());
    }

    @GetMapping("/students")
    public ResponseEntity<List<User>> getStudents() {
        return ResponseEntity.ok(userRoleCrudRepo.findAllStudents());
    }
}