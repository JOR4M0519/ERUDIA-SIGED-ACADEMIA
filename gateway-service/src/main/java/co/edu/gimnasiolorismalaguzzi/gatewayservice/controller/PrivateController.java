package co.edu.gimnasiolorismalaguzzi.gatewayservice.controller;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.services.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/gtw/private")
public class PrivateController {
    @Autowired
    private KeycloakService keycloakService;

    @GetMapping("/groups")
    public ResponseEntity<?> getGroups() {
        return ResponseEntity.ok(keycloakService.getGroups());
    }

    @GetMapping("/groups/users/{id}")
    public ResponseEntity<?> getUsersByGroup(@PathVariable String id) {
        return ResponseEntity.ok(keycloakService.getUsersByGroup(id));
    }
}
