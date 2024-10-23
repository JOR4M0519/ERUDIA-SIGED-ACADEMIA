package co.edu.gimnasiolorismalaguzzi.authservice.service;

import co.edu.gimnasiolorismalaguzzi.authservice.entity.Credential;
import co.edu.gimnasiolorismalaguzzi.authservice.entity.Role;
import co.edu.gimnasiolorismalaguzzi.authservice.model.request.LoginRequest;
import co.edu.gimnasiolorismalaguzzi.authservice.model.request.RegisterRequest;
import co.edu.gimnasiolorismalaguzzi.authservice.model.response.AuthResponse;
import co.edu.gimnasiolorismalaguzzi.authservice.repository.CredentialRepository;

import co.edu.gimnasiolorismalaguzzi.authservice.repository.RoleRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CredentialRepository credentialRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final GoogleTokenVerifier googleTokenVerifier;

    public AuthResponse loginDB(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user= credentialRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();

    }

    public AuthResponse googleLogin(String token) throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(token);
        String email = payload.getEmail();

        Credential user = credentialRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        String jwtToken = jwtService.getToken(user);

        return AuthResponse.builder().token(jwtToken).build();
    }

    public Role createRole(Role role){
        return roleRepository.save(role);
    }

    public AuthResponse register(RegisterRequest request) {

        Role role = roleRepository.findById(request.getIdRole())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + request.getIdRole()));

        Credential user = Credential.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode( request.getPassword()))
                .role(role)
                .build();

        credentialRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();

    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    };

}
