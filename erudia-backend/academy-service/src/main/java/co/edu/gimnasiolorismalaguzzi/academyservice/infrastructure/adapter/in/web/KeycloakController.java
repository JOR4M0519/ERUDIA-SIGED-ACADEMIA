package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.UserServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.UserAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/keycloak/user")
@PreAuthorize("hasRole('client_admin')")
public class KeycloakController {

    @Autowired
    private UserServicePort userServicePort;


    @GetMapping("/search")
    public ResponseEntity<?> findAllUsers(){
        return ResponseEntity.ok(userServicePort.getAllUsers());
    }


    @GetMapping("/search/{username}")
    public ResponseEntity<?> searchUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userServicePort.getUsersByUsername(username));
    }


    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDomain userDomain) throws URISyntaxException {
        String response = userServicePort.createUser(userDomain);
        return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);
    }


    @PutMapping("/update/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody UserDomain userDomain){
        userServicePort.updateUser(username, userDomain);
        return ResponseEntity.ok("User updated successfully");
    }


    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        userServicePort.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
