package co.edu.gimnasiolorismalaguzzi.gatewayservice.util;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.exception.AppException;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;


import org.keycloak.representations.AccessTokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.PublicKey;
import java.security.KeyFactory;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.reactive.function.client.WebClient;




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
                    //.scope(OAuth2Constants.OFFLINE_ACCESS)
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

    /**
     * Obtiene tanto el access token como el refresh token de Keycloak
     *
     * @param username Usuario
     * @param password Contraseña
     * @return Map con accessToken y refreshToken
     */
    public Map<String, String> getTokens(String username, String password) {
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

            // Obtén el token de acceso y el refresh token
            AccessTokenResponse tokenResponse = keycloak.tokenManager().grantToken();
            String accessToken = tokenResponse.getToken();
            String refreshToken = tokenResponse.getRefreshToken();

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return tokens;
        } catch (Exception e) {
            throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Refresca un token JWT utilizando el refresh token proporcionado
     *
     * @param refreshToken Token de actualización proporcionado durante el login
     * @return Nuevo token JWT con tiempo de expiración renovado
     */
    public String refreshToken(String refreshToken) {
        try {
            // Usar un enfoque directo con WebClient para el refresh token
            WebClient webClient = WebClient.create();

            // Preparar los datos del formulario para la solicitud
            Map<String, String> formData = new HashMap<>();
            formData.put("grant_type", "refresh_token");
            formData.put("client_id", ADMIN_CLI);
            formData.put("client_secret", CLIENT_SECRET);
            formData.put("refresh_token", refreshToken);

            // Realizar la solicitud al endpoint de token
            Map<String, Object> response = webClient.post()
                    .uri(SERVER_URL + "/realms/" + REALM_NAME + "/protocol/openid-connect/token")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .bodyValue(formData.entrySet().stream()
                            .map(entry -> entry.getKey() + "=" + entry.getValue())
                            .collect(Collectors.joining("&")))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            // Extraer el nuevo access token
            return (String) response.get("access_token");
        } catch (Exception e) {
            throw new AppException("Unable to refresh token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    public String getTokenOAuth(String username, String password) {
        try {
            // Construye un cliente Keycloak con el flujo de contraseña
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(KeycloakProvider.SERVER_URL)
                    .realm(KeycloakProvider.REALM_NAME)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId("spring-cloud-client")
                    .username(username)
                    .password(password)
                    .scope("openid offline_access")
                    .resteasyClient(new ResteasyClientBuilderImpl().build())
                    .build();

            // Obtén el token de acceso
            String accessToken = keycloak.tokenManager().getAccessToken().getToken();

            return accessToken;
        } catch (Exception e) {
            throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
        }
    }

    public UsersResource getUserResource() {
        RealmResource realmResource = getRealmResource();
        return realmResource.users();
    }

}
