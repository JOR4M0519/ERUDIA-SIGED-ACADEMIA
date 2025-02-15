package co.edu.gimnasiolorismalaguzzi.academyservice.common.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserDetailPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/academy/public")
public class PublicController {

    private final PersistenceUserDetailPort persistenceUserDetailPort;
    /*@Autowired
    private UserServicePort userServicePort;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDomain userDomain) {
        String token = userServicePort. getToken(userDomain.getUsername(), userDomain.getPassword());
        return ResponseEntity.ok(token);
    }
*/
    public PublicController(PersistenceUserDetailPort persistenceUserDetailPort) {
        this.persistenceUserDetailPort = persistenceUserDetailPort;
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDetailDomain> getUserByUsername(@PathVariable String username) {
        UserDetailDomain user = persistenceUserDetailPort.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('admin_client_role')")
    @GetMapping("/roless")
    public ResponseEntity<String> getExample(@RequestHeader("X-Roles") String roles,
                                             @RequestHeader("X-User") String user) {
        return ResponseEntity.ok("User: " + user + ", Roles: " + roles);
    }


    /*
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
    */

}
