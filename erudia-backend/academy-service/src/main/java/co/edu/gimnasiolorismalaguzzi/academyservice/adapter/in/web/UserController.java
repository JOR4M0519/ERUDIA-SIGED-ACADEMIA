package co.edu.gimnasiolorismalaguzzi.academyservice.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.service.UserDetailService;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/users")
public class UserController {

    private final UserDetailService userDetailService;

    public UserController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @GetMapping()
    public ResponseEntity<List<UserDetailDomain>> getAllUsers() {
        List<UserDetailDomain> users = userDetailService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDomain> getUserById(@PathVariable Integer id) {
        UserDetailDomain user = userDetailService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<UserDetailDomain> createUser(@PathVariable Integer id,@RequestBody UserDetailDomain userDetailDomain) {
        UserDetailDomain createdUser = userDetailService.createUser(id,userDetailDomain);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailDomain> updateUser(@PathVariable Integer id, @RequestBody UserDetailDomain userDetailDomain) {
        UserDetailDomain updatedUser = userDetailService.updateUser(id, userDetailDomain);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userDetailService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
