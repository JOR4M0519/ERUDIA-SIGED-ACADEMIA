package co.edu.gimnasiolorismalaguzzi.gatewayservice.services;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.util.KeycloakProvider;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class KeycloakService {


    public String getToken(String username, String password) {
        try {
            KeycloakProvider keycloakProvider = new KeycloakProvider();
            // Obtén el token de acceso
            String accessToken = keycloakProvider.getToken(username,password);

            return accessToken;
        } catch (Exception e) {
            log.error("Error obtaining token from Keycloak: ", e);
            throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
        }
    }

    public List<UserRepresentation> getAllUsersKeycloak() {
        return KeycloakProvider.getRealmResource()
                .users()
                .list();
    }

    public List<UserRepresentation> getUsersByUsername(String username) {
        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }

    public void deleteUsersKeycloak(String userId) {
        KeycloakProvider.getUserResource()
                .get(userId)
                .remove();
    }
}
