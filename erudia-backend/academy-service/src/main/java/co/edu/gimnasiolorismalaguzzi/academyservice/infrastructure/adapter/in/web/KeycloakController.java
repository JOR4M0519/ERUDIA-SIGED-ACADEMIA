package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.UserServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;

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
    private UserServicePort userServicePort;


    @GetMapping()
    @PermitAll
    public ResponseEntity<?> findAllUsers(){
        return ResponseEntity.ok(userServicePort.getAllUsers());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> searchUserByUuid(@PathVariable String uuid){
        return ResponseEntity.ok(userServicePort.getUserByUuid(uuid));
    }

    @GetMapping("/keycloak")
    //@PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> findAllUsersKeycloak(){
        return ResponseEntity.ok(userServicePort.getAllUsersKeycloak());
    }

    @GetMapping("/{username}/keycloak")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<?> searchUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userServicePort.getUsersByUsername(username));
    }


    @PostMapping("")
    @Transactional
    public ResponseEntity<?> createUser(@RequestBody UserDomain userDomain) throws URISyntaxException {

        //Se crea en la BD Keycloak
        String uuid = userServicePort.createUsersKeycloak(userDomain);

        //Se crea en la BD APP
        userDomain.setUuid(uuid);
        userServicePort.createUser(userDomain);

        return ResponseEntity.ok("The user was Created succesfully");

        //return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);
    }


    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateUser(@PathVariable String uuid, @RequestBody UserDomain userDomain){
        userServicePort.updateUsersKeycloak(uuid, userDomain);
        userServicePort.updateUser(uuid, userDomain);
        return ResponseEntity.ok("User updated successfully");
    }


    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(@PathVariable String uuid){
        userServicePort.deleteUsersKeycloak(uuid);
        //userServicePort.deleteUser(uuid);
        return ResponseEntity.noContent().build();
    }
}
