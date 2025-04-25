package co.edu.gimnasiolorismalaguzzi.gatewayservice;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.Login;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserRegistrationDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.services.KeycloakService;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.services.UserService;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.util.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeycloakServiceTest {

    @Mock
    private KeycloakProvider keycloakProvider;

    @Mock
    private UserService userService;

    @InjectMocks
    @Spy
    private KeycloakService service;

    private static final String USERNAME = "user";
    private static final String PASSWORD = "pass";
    private static final String TOKEN = "tok";
    private static final String REFRESH = "reftok";
    private static final String USER_ID = "uid";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "keycloakProvider", keycloakProvider);
    }

    @Test
    void testGetTokenSuccess() {
        when(keycloakProvider.getToken(USERNAME, PASSWORD)).thenReturn(TOKEN);
        Mono<String> mono = service.getToken(USERNAME, PASSWORD);
        StepVerifier.create(mono)
                .expectNext(TOKEN)
                .verifyComplete();
        verify(keycloakProvider).getToken(USERNAME, PASSWORD);
    }

    @Test
    void testGetTokenError() {
        when(keycloakProvider.getToken(USERNAME, PASSWORD))
                .thenThrow(new AppException("err", HttpStatus.UNAUTHORIZED));
        StepVerifier.create(service.getToken(USERNAME, PASSWORD))
                .expectErrorSatisfies(err -> {
                    assertTrue(err instanceof AppException);
                    assertEquals(HttpStatus.UNAUTHORIZED, ((AppException)err).getCode());
                }).verify();
    }

    @Test
    void testGetTokensSuccess() {
        Map<String,String> map = Map.of("accessToken", TOKEN, "refreshToken", REFRESH);
        when(keycloakProvider.getTokens(USERNAME, PASSWORD)).thenReturn(map);
        Mono<Map<String,String>> mono = service.getTokens(USERNAME, PASSWORD);
        StepVerifier.create(mono)
                .expectNext(map)
                .verifyComplete();
    }

    @Test
    void testGetTokensError() {
        when(keycloakProvider.getTokens(USERNAME, PASSWORD))
                .thenThrow(new AppException("no", HttpStatus.UNAUTHORIZED));
        StepVerifier.create(service.getTokens(USERNAME, PASSWORD))
                .expectErrorSatisfies(err -> {
                    assertInstanceOf(AppException.class, err);
                    assertEquals(HttpStatus.UNAUTHORIZED, ((AppException)err).getCode());
                }).verify();
    }

    @Test
    void testRefreshToken() {
        when(keycloakProvider.refreshToken(REFRESH)).thenReturn("new");
        assertEquals("new", service.refreshToken(REFRESH));
    }

    @Test
    void testGetUserGroupsByUsernameSuccess() {
        UsersResource ur = mock(UsersResource.class);
        UserResource ures = mock(UserResource.class);
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId(USER_ID);
        GroupRepresentation g1 = new GroupRepresentation(); g1.setName("A");

        when(keycloakProvider.getUserResource()).thenReturn(ur);
        when(ur.searchByUsername(USERNAME, true)).thenReturn(List.of(userRep));
        when(ur.get(USER_ID)).thenReturn(ures);
        when(ures.groups()).thenReturn(List.of(g1));

        List<String> roles = service.getUserGroupsByUsername(USERNAME);
        assertEquals(List.of("A"), roles);
    }

    @Test
    void testGetUserGroupsByUsernameNotFound() {
        UsersResource ur = mock(UsersResource.class);
        when(keycloakProvider.getUserResource()).thenReturn(ur);
        when(ur.searchByUsername(USERNAME,true)).thenReturn(Collections.emptyList());
        AppException ex = assertThrows(AppException.class,
                () -> service.getUserGroupsByUsername(USERNAME));
        assertEquals(HttpStatus.NOT_FOUND, ex.getCode());
    }

    @Test
    void testGetUsersByGroupSuccess() {
        String gid="g1";
        UsersResource ur = mock(UsersResource.class);
        UserResource ures = mock(UserResource.class);
        UserRepresentation urep = new UserRepresentation();
        urep.setId(USER_ID);
        urep.setFirstName("fn"); urep.setLastName("ln");
        when(keycloakProvider.getUserResource()).thenReturn(ur);
        when(ur.list()).thenReturn(List.of(urep));
        when(ur.get(USER_ID)).thenReturn(ures);
        GroupRepresentation gr = new GroupRepresentation(); gr.setId(gid); gr.setName("role");
        when(ures.groups()).thenReturn(List.of(gr));
        List<Map<String,String>> out = service.getUsersByGroup(gid);
        assertEquals(1, out.size());
        var m=out.get(0);
        assertEquals(USER_ID, m.get("id"));
        assertEquals("Fn Ln", m.get("name"));
        assertEquals("Role", m.get("role"));
    }

    @Test
    void testGetUsersByGroupError() {
        UsersResource ur = mock(UsersResource.class);
        when(keycloakProvider.getUserResource()).thenReturn(ur);
        when(ur.list()).thenThrow(new RuntimeException());
        AppException ex = assertThrows(AppException.class,
                () -> service.getUsersByGroup("g"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

   /* @Test
    void testGetGroupsSuccess() {
        RealmResource rr = mock(RealmResource.class);
        GroupsResource grr = mock(GroupsResource.class);
        GroupRepresentation gr = new GroupRepresentation(); gr.setName("grp");
        when(keycloakProvider.getRealmResource()).thenReturn(rr);
        when(rr.groups()).thenReturn(grr);
        when(grr.groups()).thenReturn(List.of(gr));
        var list = service.getGroups();
        assertEquals(1, list.size());
    }

    @Test
    void testGetGroupsError() {
        RealmResource rr = mock(RealmResource.class);
        when(keycloakProvider.getRealmResource()).thenReturn(rr);
        when(rr.groups()).thenThrow(new RuntimeException());
        AppException ex = assertThrows(AppException.class,
                () -> service.getGroups());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getCode());
    }

    @Test
    void testGetUsersByUsernameSuccess() {
        RealmResource rr = mock(RealmResource.class);
        UsersResource ur = mock(UsersResource.class);
        UserRepresentation u = new UserRepresentation(); u.setId(USER_ID);
        when(keycloakProvider.getRealmResource()).thenReturn(rr);
        when(rr.users()).thenReturn(ur);
        when(ur.searchByUsername(USERNAME,true)).thenReturn(List.of(u));
        var rep = service.getUsersByUsername(USERNAME);
        assertEquals(USER_ID, rep.getId());
    }

    @Test
    void testGetUsersByUsernameNotFound() {
        RealmResource rr = mock(RealmResource.class);
        UsersResource ur = mock(UsersResource.class);
        when(keycloakProvider.getRealmResource()).thenReturn(rr);
        when(rr.users()).thenReturn(ur);
        when(ur.searchByUsername(USERNAME,true)).thenReturn(Collections.emptyList());
        AppException ex = assertThrows(AppException.class,
                () -> service.getUsersByUsername(USERNAME));
        assertEquals(HttpStatus.NOT_FOUND, ex.getCode());
    }*/

    @Test
    void testCreateUserInKeycloakConflict() {
        UsersResource ur = mock(UsersResource.class);
        Response res = mock(Response.class);
        when(keycloakProvider.getUserResource()).thenReturn(ur);
        when(ur.create(any())).thenReturn(res);
        when(res.getStatus()).thenReturn(409);
        AppException ex = assertThrows(AppException.class,
                () -> service.createUserInKeycloak(UserRegistrationDomain.builder()
                        .userDomain(UserDomain.builder().build())
                        .build()));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());
    }

    @Test
    void testCreateUserInKeycloakError() {
        UsersResource ur = mock(UsersResource.class);
        Response res = mock(Response.class);
        when(keycloakProvider.getUserResource()).thenReturn(ur);
        when(ur.create(any())).thenReturn(res);
        when(res.getStatus()).thenReturn(500);
        AppException ex = assertThrows(AppException.class,
                () -> service.createUserInKeycloak(UserRegistrationDomain.builder()
                        .userDomain(UserDomain.builder().build())
                        .build()));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

   /* @Test
    void testUpdateUsersKeycloakSuccess() {
        UserResource ur = mock(UserResource.class);
        UsersResource urs = mock(UsersResource.class);
        when(keycloakProvider.getUserResource().get(USER_ID)).thenReturn(ur);
        assertDoesNotThrow(() -> service.updateUsersKeycloak(USER_ID, UserDomain.builder().password("p").build()));
    }*/

    @Test
    void testUpdateUsersKeycloakError() {
        UserResource ur = mock(UserResource.class);
        when(keycloakProvider.getUserResource()).thenReturn(mock(UsersResource.class));
        when(keycloakProvider.getUserResource().get(USER_ID)).thenReturn(ur);
        doThrow(new RuntimeException()).when(ur).update(any());
        AppException ex = assertThrows(AppException.class,
                () -> service.updateUsersKeycloak(USER_ID, UserDomain.builder().password("p").build()));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

    @Test
    void testDeleteUsersKeycloak() {
        UserResource ur = mock(UserResource.class);
        when(keycloakProvider.getUserResource()).thenReturn(mock(UsersResource.class));
        when(keycloakProvider.getUserResource().get(USER_ID)).thenReturn(ur);
        assertDoesNotThrow(() -> service.deleteUsersKeycloak(USER_ID));
        verify(ur).remove();
    }

   /* @Test
    void testUpdatePasswordSuccess() {
        UserRepresentation u = new UserRepresentation(); u.setId(USER_ID);
        when(service.getUsersByUsername(USERNAME)).thenReturn(u);
        when(keycloakProvider.getToken(USERNAME, "old")).thenReturn(TOKEN);
        UserResource ur = mock(UserResource.class);
        when(keycloakProvider.getUserResource().get(USER_ID)).thenReturn(ur);
        HttpStatus s = service.updatePassword(USERNAME, Login.builder().lastPassword("old").password("new").build());
        assertEquals(HttpStatus.OK, s);
    }

    @Test
    void testUpdatePasswordMissingLast() {
        Login l = Login.builder().lastPassword(null).password("n").build();
        AppException ex = assertThrows(AppException.class,
                () -> service.updatePassword(USERNAME, l));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getCode());
    }

    @Test
    void testUpdatePasswordInvalidLast() {
        UserRepresentation u = new UserRepresentation(); u.setId(USER_ID);
        when(service.getUsersByUsername(USERNAME)).thenReturn(u);
        when(keycloakProvider.getToken(USERNAME, "wrong")).thenThrow(new AppException("no", HttpStatus.UNAUTHORIZED));
        AppException ex = assertThrows(AppException.class,
                () -> service.updatePassword(USERNAME, Login.builder().lastPassword("wrong").password("n").build()));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getCode());
    }*/
}
