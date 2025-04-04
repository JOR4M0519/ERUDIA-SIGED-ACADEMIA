/*
package co.edu.gimnasiolorismalaguzzi.gatewayservice;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.util.KeycloakProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeycloakProviderTest {

    private KeycloakProvider keycloakProvider;
    private final String USERNAME = "testuser";
    private final String PASSWORD = "testpassword";
    private final String TOKEN = "test-token-value";

    @BeforeEach
    void setUp() {
        keycloakProvider = new KeycloakProvider();
    }

    @Test
    void getToken_ShouldReturnToken_WhenCredentialsAreValid() {
        // Arrange
        Keycloak keycloakMock = mock(Keycloak.class);
        TokenManager tokenManagerMock = mock(TokenManager.class);
        AccessTokenResponse accessTokenResponseMock = mock(AccessTokenResponse.class);

        try (MockedStatic<KeycloakBuilder> keycloakBuilderMockedStatic = Mockito.mockStatic(KeycloakBuilder.class)) {
            KeycloakBuilder keycloakBuilderMock = mock(KeycloakBuilder.class);

            keycloakBuilderMockedStatic.when(KeycloakBuilder::builder).thenReturn(keycloakBuilderMock);

            when(keycloakBuilderMock.serverUrl(anyString())).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.realm(anyString())).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.grantType(anyString())).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.clientId(anyString())).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.username(USERNAME)).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.password(PASSWORD)).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.resteasyClient(any())).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.build()).thenReturn(keycloakMock);

            when(keycloakMock.tokenManager()).thenReturn(tokenManagerMock);
            when(tokenManagerMock.getAccessToken()).thenReturn(accessTokenResponseMock);
            when(accessTokenResponseMock.getToken()).thenReturn(TOKEN);

            // Act
            String result = keycloakProvider.getToken(USERNAME, PASSWORD);

            // Assert
            assertEquals(TOKEN, result);
        }
    }

    @Test
    void getToken_ShouldThrowAppException_WhenAuthenticationFails() {
        // Arrange
        try (MockedStatic<KeycloakBuilder> keycloakBuilderMockedStatic = Mockito.mockStatic(KeycloakBuilder.class)) {
            KeycloakBuilder keycloakBuilderMock = mock(KeycloakBuilder.class);

            keycloakBuilderMockedStatic.when(KeycloakBuilder::builder).thenReturn(keycloakBuilderMock);

            when(keycloakBuilderMock.serverUrl(anyString())).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.realm(anyString())).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.grantType(anyString())).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.clientId(anyString())).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.username(USERNAME)).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.password(PASSWORD)).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.resteasyClient(any())).thenReturn(keycloakBuilderMock);
            when(keycloakBuilderMock.build()).thenThrow(new RuntimeException("Authentication failed"));

            // Act & Assert
            AppException exception = assertThrows(AppException.class, () -> {
                keycloakProvider.getToken(USERNAME, PASSWORD);
            });

            assertEquals(HttpStatus.UNAUTHORIZED, exception.getCode());
            assertTrue(exception.getMessage().contains("Unable to authenticate user"));
        }
    }

}
*/
