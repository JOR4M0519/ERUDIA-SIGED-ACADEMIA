package co.edu.gimnasiolorismalaguzzi.gatewayservice.controller;


import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.Login;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.services.KeycloakService;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.services.UserService;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.http.ResponseCookie;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/gtw/public")
public class PublicController {

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private UserService userService;

    /*@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login userDomain) {
        String token = keycloakService.getToken(userDomain.getUsername(), userDomain.getPassword());
        UserDetailDomain userDetail = userService.getDetailUser(userDomain.getUsername());
        return ResponseEntity.ok(token);
    }*/

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody Login request) {
        return keycloakService.getToken(request.getUsername(), request.getPassword())  // 🔥 Llamada reactiva para el token
                .flatMap(token -> userService.getDetailUser(request.getUsername())  // 🔥 Llamada reactiva para los detalles del usuario
                        .map(userDetail -> {
                            Map<String, Object> response = new HashMap<>();
                            response.put("token", token);
                            response.put("user", userDetail);
                            return ResponseEntity.ok(response);
                        })
                );
    }



/*    @GetMapping("/groups/{username}")
    public ResponseEntity<?> getGroupByUser(@PathVariable String username) {
        List<String> groups = keycloakService.getGroupByUser(username);
        return ResponseEntity.ok(groups);
    }*/

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login loginRequest, ServerWebExchange exchange) {
        // Aquí validas el usuario y generas el token (esto depende de tu lógica)
        String token = generateJwtToken(loginRequest.getUsername(),loginRequest.getPassword());

        // Crear cookie con el token JWT
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true) // Evita acceso desde JavaScript (protección contra XSS)
                .secure(false)  // En producción usa true si estás en HTTPS
                .sameSite("Strict") // Protección contra CSRF
                .path("/") // Disponible en toda la aplicación
                .build();

        // Agregar la cookie en la respuesta
        exchange.getResponse().addCookie(cookie);

        return Mono.just(ResponseEntity.ok().build()).block(); // Retornar respuesta 200 OK
    }


    private String generateJwtToken(String username, String password) {
        // Genera el token JWT (debes implementar esto según tu configuración actual)
        return keycloakService.getToken(username, password);
    }*/


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
