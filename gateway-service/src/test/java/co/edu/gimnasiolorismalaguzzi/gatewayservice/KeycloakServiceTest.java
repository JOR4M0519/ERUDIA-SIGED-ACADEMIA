/*
package co.edu.gimnasiolorismalaguzzi.gatewayservice;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.services.KeycloakService;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.util.KeycloakProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeycloakServiceTest {

    @Mock
    private KeycloakProvider keycloakProvider;

    @InjectMocks
    @Spy
    private KeycloakService keycloakService;

    private final String USERNAME = "testuser";
    private final String PASSWORD = "testpassword";
    private final String TOKEN = "test-token-value";
    private final String USER_ID = "user-id-123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(keycloakService, "keycloakProvider", keycloakProvider);
    }

    @Test
    void getToken_ShouldReturnToken_WhenCredentialsAreValid() {
        // Arrange
        when(keycloakProvider.getToken(USERNAME, PASSWORD)).thenReturn(TOKEN);

        // Act
        Mono<String> result = keycloakService.getToken(USERNAME, PASSWORD);

        // Assert
        StepVerifier.create(result)
                .expectNext(TOKEN)
                .verifyComplete();

        verify(keycloakProvider).getToken(USERNAME, PASSWORD);
    }

    @Test
    void getToken_ShouldThrowAppException_WhenCredentialsAreInvalid() {
        // Arrange
        when(keycloakProvider.getToken(USERNAME, PASSWORD))
                .thenThrow(new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED));

        // Act & Assert
        StepVerifier.create(keycloakService.getToken(USERNAME, PASSWORD))
                .expectErrorMatches(throwable ->
                        throwable instanceof AppException &&
                                ((AppException) throwable).getCode() == HttpStatus.UNAUTHORIZED)
                .verify();

        verify(keycloakProvider).getToken(USERNAME, PASSWORD);
    }

    @Test
    void getUserGroupsByUsername_ShouldReturnGroups_WhenUserExists() {
        // Arrange
        UsersResource usersResource = mock(UsersResource.class);
        UserResource userResource = mock(UserResource.class);
        List<UserRepresentation> users = new ArrayList<>();
        UserRepresentation user = new UserRepresentation();
        user.setId(USER_ID);
        users.add(user);

        List<GroupRepresentation> groups = new ArrayList<>();
        GroupRepresentation group1 = new GroupRepresentation();
        group1.setName("admin");
        GroupRepresentation group2 = new GroupRepresentation();
        group2.setName("user");
        groups.add(group1);
        groups.add(group2);

        when(keycloakProvider.getUserResource()).thenReturn(usersResource);
        when(usersResource.searchByUsername(USERNAME, true)).thenReturn(users);
        when(usersResource.get(USER_ID)).thenReturn(userResource);
        when(userResource.groups()).thenReturn(groups);

        // Act
        List<String> result = keycloakService.getUserGroupsByUsername(USERNAME);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains("admin"));
        assertTrue(result.contains("user"));
        verify(usersResource).searchByUsername(USERNAME, true);
        verify(userResource).groups();
    }

    @Test
    void getUserGroupsByUsername_ShouldThrowAppException_WhenUserNotFound() {
        // Arrange
        UsersResource usersResource = mock(UsersResource.class);
        when(keycloakProvider.getUserResource()).thenReturn(usersResource);
        when(usersResource.searchByUsername(USERNAME, true)).thenReturn(new ArrayList<>());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            keycloakService.getUserGroupsByUsername(USERNAME);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getCode());
        assertTrue(exception.getMessage().contains("User not found"));
        verify(usersResource).searchByUsername(USERNAME, true);
    }

    @Test
    void getGroups_ShouldReturnGroups() {
        // Arrange
        RealmResource realmResource = mock(RealmResource.class);
        GroupsResource groupsResource = mock(GroupsResource.class);
        List<GroupRepresentation> groups = new ArrayList<>();
        GroupRepresentation group = new GroupRepresentation();
        group.setName("admin");
        groups.add(group);

        when(keycloakProvider.getRealmResource()).thenReturn(realmResource);
        when(realmResource.groups()).thenReturn(groupsResource);
        when(groupsResource.groups()).thenReturn(groups);

        // Act
        List<GroupRepresentation> result = keycloakService.getGroups();

        // Assert
        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).getName());
        verify(realmResource).groups();
        verify(groupsResource).groups();
    }

    @Test
    void getUsersByGroup_ShouldReturnUsers_WhenGroupExists() {
        // Arrange
        String groupId = "group-id-123";
        UsersResource usersResource = mock(UsersResource.class);
        UserResource userResource = mock(UserResource.class);

        List<UserRepresentation> allUsers = new ArrayList<>();
        UserRepresentation user = new UserRepresentation();
        user.setId(USER_ID);
        user.setFirstName("test");
        user.setLastName("user");
        allUsers.add(user);

        List<GroupRepresentation> userGroups = new ArrayList<>();
        GroupRepresentation group = new GroupRepresentation();
        group.setId(groupId);
        group.setName("admin");
        userGroups.add(group);

        when(keycloakProvider.getUserResource()).thenReturn(usersResource);
        when(usersResource.list()).thenReturn(allUsers);
        when(usersResource.get(USER_ID)).thenReturn(userResource);
        when(userResource.groups()).thenReturn(userGroups);

        // Act
        List<Map<String, String>> result = keycloakService.getUsersByGroup(groupId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(USER_ID, result.get(0).get("id"));
        assertEquals("Test User", result.get(0).get("name"));
        assertEquals("Admin", result.get(0).get("role"));

        verify(usersResource).list();
        verify(userResource, atLeastOnce()).groups();
    }
}
*/
