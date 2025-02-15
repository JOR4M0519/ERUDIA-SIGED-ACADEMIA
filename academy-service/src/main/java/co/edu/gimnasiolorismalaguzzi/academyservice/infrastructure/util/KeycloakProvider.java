package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.util;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.http.HttpStatus;

public class KeycloakProvider {

    private static final String SERVER_URL = "http://localhost:8181";
    private static final String REALM_NAME = "academy-glm-realm";
    private static final String REALM_MASTER = "master";
    private static final String ADMIN_CLI = "admin-cli";
    private static final String USER_CONSOLE = "admin";
    private static final String PASSWORD_CONSOLE = "admin";
    private static final String CLIENT_SECRET = "admin";

    public static RealmResource getRealmResource() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .clientId(ADMIN_CLI)
                .username(USER_CONSOLE)
                .password(PASSWORD_CONSOLE)
                .clientSecret(CLIENT_SECRET)
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)               //Number of conections of client to Keycloak
                        .build())
                .build();

        return keycloak.realm(REALM_NAME);
    }

    public String getToken(String username, String password) {
        try {
            // Construye un cliente Keycloak con el flujo de contraseña
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(KeycloakProvider.SERVER_URL)
                    .realm(KeycloakProvider.REALM_NAME)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(KeycloakProvider.ADMIN_CLI)
                    .username(username)
                    .password(password)
                    .resteasyClient(new ResteasyClientBuilderImpl().build())
                    .build();

            // Obtén el token de acceso
            String accessToken = keycloak.tokenManager().getAccessToken().getToken();

            return accessToken;
        } catch (Exception e) {
            throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
        }
    }

    public static UsersResource getUserResource() {
        RealmResource realmResource = getRealmResource();
        return realmResource.users();
    }

}
