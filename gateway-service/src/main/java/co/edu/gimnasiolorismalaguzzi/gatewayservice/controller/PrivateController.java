package co.edu.gimnasiolorismalaguzzi.gatewayservice.controller;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.Login;
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
import java.util.Map;


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

    @PutMapping("/users/password")
    public ResponseEntity<?> updatePasswordCredentials(
            @RequestBody Login login) {

        String username = login.getUsername();
        try {
            // Validar que la nueva contraseña no esté vacía
            if (login.getPassword() == null || login.getPassword().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "La nueva contraseña no puede estar vacía"));
            }

            HttpStatus status = keycloakService.updatePassword(username, login);
            return ResponseEntity
                    .status(status)
                    .body(Map.of("message", "Contraseña actualizada exitosamente para el usuario: " + username));
        } catch (AppException e) {
            String errorMsg = e.getMessage();
            // Personalizar mensajes para casos específicos
            if (e.getCode() == HttpStatus.UNAUTHORIZED) {
                errorMsg = "La contraseña anterior es incorrecta";
            } else if (e.getCode() == HttpStatus.BAD_REQUEST) {
                errorMsg = "Se requiere la contraseña anterior para actualizar la contraseña";
            }

            return ResponseEntity
                    .status(e.getCode())
                    .body(Map.of("error", errorMsg));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error inesperado al actualizar la contraseña: " + e.getMessage()));
        }
    }


    @GetMapping("/users/{username}/roles")
    public ResponseEntity<?> getRolesUserByUuid(@PathVariable String username) {
        return ResponseEntity.ok(keycloakService.getUserGroupsByUsername(username));
    }

}
