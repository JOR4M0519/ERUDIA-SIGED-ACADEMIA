package co.edu.gimnasiolorismalaguzzi.gatewayservice;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.client.AcademyClient;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserRegistrationDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private AcademyClient academyClient;

    @InjectMocks
    private UserService userService;

    private static final String USERNAME = "testuser";
    private UserDetailDomain userDetailDomain;
    private UserDomain userDomain;
    private UserRegistrationDomain registrationDomain;

    @BeforeEach
    void setUp() {
        // Setup UserDetailDomain
        userDetailDomain = UserDetailDomain.builder()
                .firstName("Test")
                .lastName("User")
                .dni("123456789")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .phoneNumber("1234567890")
                .address("Test Address")
                .build();

        // Setup UserDomain
        userDomain = UserDomain.builder()
                .username(USERNAME)
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .password("password123")
                .build();

        // Setup UserRegistrationDomain
        registrationDomain = UserRegistrationDomain.builder()
                .userDomain(userDomain)
                .userDetailDomain(userDetailDomain)
                .groupId(42)
                .build();
    }

    // ----------------------------
    // getDetailUser tests
    // ----------------------------

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

    @Test
    void getDetailUser_ShouldReturnEmptyMono_WhenClientReturnsNull() {
        // Arrange: si el cliente devuelve null, el Mono debe completar sin emitir
        when(academyClient.getDetailUser(USERNAME)).thenReturn(null);

        // Act
        Mono<UserDetailDomain> result = userService.getDetailUser(USERNAME);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(academyClient).getDetailUser(USERNAME);
    }

    @Test
    void getDetailUser_ShouldPropagateError_WhenClientThrowsError() {
        // Arrange
        RuntimeException exception = new RuntimeException("API Error");
        when(academyClient.getDetailUser(USERNAME)).thenThrow(exception);

        // Act
        Mono<UserDetailDomain> result = userService.getDetailUser(USERNAME);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof RuntimeException
                        && err.getMessage().equals("API Error"))
                .verify();

        verify(academyClient).getDetailUser(USERNAME);
    }

    @Test
    void getDetailUser_ShouldHandleEmptyUsername() {
        // Arrange
        String emptyUsername = "";
        when(academyClient.getDetailUser(emptyUsername)).thenReturn(null);

        // Act
        Mono<UserDetailDomain> result = userService.getDetailUser(emptyUsername);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(academyClient).getDetailUser(emptyUsername);
    }

    @Test
    void getDetailUser_ShouldHandleSpecialCharactersInUsername() {
        // Arrange
        String specialUsername = "user@#$%";
        UserDetailDomain specialUserDetail = UserDetailDomain.builder()
                .firstName("Special")
                .lastName("User")
                .build();

        when(academyClient.getDetailUser(specialUsername)).thenReturn(specialUserDetail);

        // Act
        Mono<UserDetailDomain> result = userService.getDetailUser(specialUsername);

        // Assert
        StepVerifier.create(result)
                .expectNext(specialUserDetail)
                .verifyComplete();

        verify(academyClient).getDetailUser(specialUsername);
    }

    @Test
    void getDetailUser_ShouldHandleAppException() {
        // Arrange
        AppException appException = new AppException("User not found", HttpStatus.NOT_FOUND);
        when(academyClient.getDetailUser(USERNAME)).thenThrow(appException);

        // Act
        Mono<UserDetailDomain> result = userService.getDetailUser(USERNAME);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof AppException
                        && ((AppException) err).getCode() == HttpStatus.NOT_FOUND)
                .verify();

        verify(academyClient).getDetailUser(USERNAME);
    }

}