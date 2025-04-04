package co.edu.gimnasiolorismalaguzzi.gatewayservice.controller;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserRegistrationDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.services.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

    @PostMapping("/users/students/register")
    public Mono<ResponseEntity<UserDetailDomain>> registerStudent(@RequestBody UserRegistrationDomain registrationDomain) {
        return keycloakService.registerByGroupinStudentUser(registrationDomain)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    if (e instanceof AppException) {
                        return Mono.just(ResponseEntity
                                .status(((AppException) e).getCode())
                                .body(null));
                    }
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null));
                });
    }

    @PostMapping("/users/register")
    public Mono<ResponseEntity<UserDetailDomain>> registerUser(@RequestBody UserRegistrationDomain registrationDomain) {
        return keycloakService.registerUser(registrationDomain)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    if (e instanceof AppException) {
                        return Mono.just(ResponseEntity
                                .status(((AppException) e).getCode())
                                .body(null));
                    }
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null));
                });
    }


    @GetMapping("/users/{username}/roles")
    public ResponseEntity<?> getRolesUserByUuid(@PathVariable String username) {
        return ResponseEntity.ok(keycloakService.getUserGroupsByUsername(username));
    }

}
