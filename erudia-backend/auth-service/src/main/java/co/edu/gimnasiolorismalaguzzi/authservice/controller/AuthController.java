package co.edu.gimnasiolorismalaguzzi.authservice.controller;

import co.edu.gimnasiolorismalaguzzi.authservice.entity.Role;
import co.edu.gimnasiolorismalaguzzi.authservice.model.request.LoginRequest;
import co.edu.gimnasiolorismalaguzzi.authservice.model.request.RegisterRequest;
import co.edu.gimnasiolorismalaguzzi.authservice.model.response.AuthResponse;
import co.edu.gimnasiolorismalaguzzi.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping(value ="/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.loginDB(request));
    }

    @PostMapping(value = "/google/login")
    public ResponseEntity<AuthResponse> googleLogin(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            return ResponseEntity.ok(authService.googleLogin(token));
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid token"));
        }
    }

    @PostMapping(value ="/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping(value ="/role")
    public ResponseEntity<Role> role(@RequestBody Role role){
        return ResponseEntity.ok(authService.createRole(role));
    }

    @GetMapping("role")
    public ResponseEntity<?> getRoles() {

        try {
            List<Role> roleList = authService.getRoles();
            return ResponseEntity.status(HttpStatus.OK).body(roleList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting roles: " + e.getMessage());
        }
    }


}
