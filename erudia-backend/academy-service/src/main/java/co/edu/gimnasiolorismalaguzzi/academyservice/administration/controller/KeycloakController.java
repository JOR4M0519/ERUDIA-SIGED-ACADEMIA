package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserKeycloakPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;

import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/academy/users")
@PreAuthorize("hasRole('admin_client_role')")
public class KeycloakController {

    @Autowired
    private PersistenceUserKeycloakPort persistenceUserKeycloakPort;
    @Autowired
    private PersistenceUserPort persistenceUserPort;


    @GetMapping()
    @PermitAll
    public ResponseEntity<?> findAllUsers(){
        return ResponseEntity.ok(persistenceUserKeycloakPort.getAllUsersKeycloak());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> searchUserByUuid(@PathVariable String uuid){
        return ResponseEntity.ok(persistenceUserPort.searchUserByUuid(uuid));
    }

    @GetMapping("/keycloak")
    //@PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> findAllUsersKeycloak(){
        return ResponseEntity.ok(persistenceUserKeycloakPort.getAllUsersKeycloak());
    }

    @GetMapping("/{username}/keycloak")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<?> searchUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(persistenceUserKeycloakPort.getUsersByUsername(username));
    }


    @PostMapping("")
    @Transactional
    public ResponseEntity<?> createUser(@RequestBody UserDomain userDomain) throws URISyntaxException {

        //Se crea en la BD Keycloak
        String uuid = persistenceUserKeycloakPort.createUsersKeycloak(userDomain);

        //Se crea en la BD APP
        userDomain.setUuid(uuid);
        persistenceUserPort.save(userDomain);

        return ResponseEntity.ok("The user was Created succesfully");

        //return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);
    }


    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateUser(@PathVariable String uuid, @RequestBody UserDomain userDomain){
        persistenceUserKeycloakPort.updateUsersKeycloak(uuid, userDomain);
        persistenceUserPort.update(uuid, userDomain);
        return ResponseEntity.ok("User updated successfully");
    }


    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(@PathVariable String uuid){
        persistenceUserKeycloakPort.deleteUsersKeycloak(uuid);
        //userServicePort.deleteUser(uuid);
        return ResponseEntity.noContent().build();
    }
}
