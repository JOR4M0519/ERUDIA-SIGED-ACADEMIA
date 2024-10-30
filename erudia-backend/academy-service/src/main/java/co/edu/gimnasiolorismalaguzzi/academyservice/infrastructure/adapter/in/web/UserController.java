package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.UserDetailServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/users")
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

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDomain> getUserById(@PathVariable Integer id) {
        UserDetailDomain user = userDetailServicePort.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<UserDetailDomain> createUser(@PathVariable Integer id,@RequestBody UserDetailDomain userDetailDomain) {
        UserDetailDomain createdUser = userDetailServicePort.createUser(id,userDetailDomain);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailDomain> updateUser(@PathVariable Integer id, @RequestBody UserDetailDomain userDetailDomain) {
        UserDetailDomain updatedUser = userDetailServicePort.updateUser(id, userDetailDomain);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userDetailServicePort.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
