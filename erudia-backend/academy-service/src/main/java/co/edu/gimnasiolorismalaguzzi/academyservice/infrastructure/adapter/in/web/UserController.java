package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.UserDetailServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/users/detail")
public class UserController {

    private final UserDetailServicePort userDetailServicePort;

    public UserController(UserDetailServicePort userDetailServicePort) {
        this.userDetailServicePort = userDetailServicePort;
    }

    @GetMapping()
    public ResponseEntity<List<UserDetailDomain>> getAllUsers() {
        List<UserDetailDomain> users = userDetailServicePort.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserDetailDomain> getUserById(@PathVariable String uuid) {
        UserDetailDomain user = userDetailServicePort.getUserById(uuid);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{uuid}")
    public ResponseEntity<UserDetailDomain> createUser(@PathVariable String uuid,@RequestBody UserDetailDomain userDetailDomain) {
        UserDetailDomain createdUser = userDetailServicePort.createUser(uuid,userDetailDomain);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateUser(@PathVariable String uuid, @RequestBody UserDetailDomain userDetailDomain) {
        userDetailServicePort.updateUser(uuid, userDetailDomain);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uuid) {
        userDetailServicePort.deleteUser(uuid);
        return ResponseEntity.noContent().build();
    }
}
