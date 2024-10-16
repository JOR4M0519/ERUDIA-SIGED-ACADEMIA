package co.edu.gimnasiolorismalaguzzi.academyservice.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.service.UserService;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academy/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserDetailDomain>> getAllUsers() {
        List<UserDetailDomain> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDomain> getUserById(@PathVariable Integer id) {
        UserDetailDomain user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDetailDomain> createUser(@RequestBody UserDetailDomain userDetailDomain) {
        UserDetailDomain createdUser = userService.createUser(userDetailDomain);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailDomain> updateUser(@PathVariable Integer id, @RequestBody UserDetailDomain userDetailDomain) {
        UserDetailDomain updatedUser = userService.updateUser(id, userDetailDomain);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
