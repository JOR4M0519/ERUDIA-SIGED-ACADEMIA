package co.edu.gimnasiolorismalaguzzi.gatewayservice;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.client.AcademyClient;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private AcademyClient academyClient;

    @InjectMocks
    private UserService userService;

    private final String USERNAME = "testuser";
    private UserDetailDomain userDetailDomain;

    @BeforeEach
    void setUp() {
        userDetailDomain = new UserDetailDomain();
        userDetailDomain.setFirstName("Test");
        userDetailDomain.setLastName("User");
    }

    @Test
    void getDetailUser_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange
        when(academyClient.getDetailUser(USERNAME)).thenReturn(userDetailDomain);

        // Act
        Mono<UserDetailDomain> result = userService.getDetailUser(USERNAME);

        // Assert
        StepVerifier.create(result)
                .expectNext(userDetailDomain)
                .verifyComplete();

        verify(academyClient).getDetailUser(USERNAME);
    }

/*
    @Test
    void getDetailUser_ShouldReturnEmptyMono_WhenUserDoesNotExist() {
        // Arrange
        when(academyClient.getUserDetail(USERNAME)).thenReturn(Mono.empty());

        // Act
        Mono<UserDetailDomain> result = userService.getDetailUser(USERNAME);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(academyClient).getUserDetail(USERNAME);
    }

    @Test
    void getDetailUser_ShouldPropagateError_WhenClientThrowsError() {
        // Arrange
        RuntimeException exception = new RuntimeException("API Error");
        when(academyClient.getUserDetail(USERNAME)).thenReturn(Mono.error(exception));

        // Act
        Mono<UserDetailDomain> result = userService.getDetailUser(USERNAME);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("API Error"))
                .verify();

        verify(academyClient).getUserDetail(USERNAME);
    }
 */
}
