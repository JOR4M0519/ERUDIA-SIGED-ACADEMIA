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

    // ----------------------------
    // createStudentUser tests
    // ----------------------------

    @Test
    void createStudentUser_ShouldReturnUserDetails_WhenCreationSucceeds() {
        // Arrange
        when(academyClient.createStudentUser(registrationDomain)).thenReturn(userDetailDomain);

        // Act
        Mono<UserDetailDomain> result = userService.createStudentUser(registrationDomain);

        // Assert
        StepVerifier.create(result)
                .expectNext(userDetailDomain)
                .verifyComplete();

        verify(academyClient).createStudentUser(registrationDomain);
    }

    @Test
    void createStudentUser_ShouldPropagateError_WhenClientThrowsError() {
        // Arrange
        RuntimeException exception = new RuntimeException("Creation Error");
        when(academyClient.createStudentUser(registrationDomain)).thenThrow(exception);

        // Act
        Mono<UserDetailDomain> result = userService.createStudentUser(registrationDomain);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof RuntimeException
                        && err.getMessage().equals("Creation Error"))
                .verify();

        verify(academyClient).createStudentUser(registrationDomain);
    }

    @Test
    void createStudentUser_ShouldHandleMissingFields() {
        // Arrange - registration with missing fields
        UserRegistrationDomain incompleteRegistration = UserRegistrationDomain.builder()
                .userDomain(UserDomain.builder().username(USERNAME).build()) // Missing other fields
                .userDetailDomain(UserDetailDomain.builder().build()) // Empty detail
                .groupId(42)
                .build();

        // Mock a specific exception for missing fields
        IllegalArgumentException validationException = new IllegalArgumentException("Missing required fields");
        when(academyClient.createStudentUser(incompleteRegistration)).thenThrow(validationException);

        // Act
        Mono<UserDetailDomain> result = userService.createStudentUser(incompleteRegistration);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof IllegalArgumentException
                        && err.getMessage().equals("Missing required fields"))
                .verify();

        verify(academyClient).createStudentUser(incompleteRegistration);
    }

    @Test
    void createStudentUser_ShouldHandleInvalidFormatData() {
        // Arrange - registration with invalid format (e.g., invalid email)
        UserDomain invalidUserDomain = UserDomain.builder()
                .username(USERNAME)
                .email("invalid-email") // Invalid email format
                .firstName("Test")
                .lastName("User")
                .password("pass")
                .build();

        UserRegistrationDomain invalidRegistration = UserRegistrationDomain.builder()
                .userDomain(invalidUserDomain)
                .userDetailDomain(userDetailDomain)
                .groupId(42)
                .build();

        // Mock a specific exception for validation errors
        AppException validationException = new AppException("Invalid email format", HttpStatus.BAD_REQUEST);
        when(academyClient.createStudentUser(invalidRegistration)).thenThrow(validationException);

        // Act
        Mono<UserDetailDomain> result = userService.createStudentUser(invalidRegistration);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof AppException
                        && ((AppException) err).getCode() == HttpStatus.BAD_REQUEST
                        && err.getMessage().equals("Invalid email format"))
                .verify();

        verify(academyClient).createStudentUser(invalidRegistration);
    }

    @Test
    void createStudentUser_ShouldHandleDuplicateUser() {
        // Arrange
        AppException duplicateException = new AppException("User already exists", HttpStatus.CONFLICT);
        when(academyClient.createStudentUser(registrationDomain)).thenThrow(duplicateException);

        // Act
        Mono<UserDetailDomain> result = userService.createStudentUser(registrationDomain);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof AppException
                        && ((AppException) err).getCode() == HttpStatus.CONFLICT)
                .verify();

        verify(academyClient).createStudentUser(registrationDomain);
    }

    // ----------------------------
    // createGeneralUser tests
    // ----------------------------

    @Test
    void createGeneralUser_ShouldReturnUserDetails_WhenCreationSucceeds() {
        // Arrange
        UserRegistrationDomain generalRegistration = UserRegistrationDomain.builder()
                .userDomain(userDomain)
                .userDetailDomain(userDetailDomain)
                .groupId(0)  // Represents "general" user
                .build();

        when(academyClient.createGeneralUser(generalRegistration)).thenReturn(userDetailDomain);

        // Act
        Mono<UserDetailDomain> result = userService.createGeneralUser(generalRegistration);

        // Assert
        StepVerifier.create(result)
                .expectNext(userDetailDomain)
                .verifyComplete();

        verify(academyClient).createGeneralUser(generalRegistration);
    }

    @Test
    void createGeneralUser_ShouldPropagateError_WhenClientThrowsError() {
        // Arrange
        RuntimeException exception = new RuntimeException("General Creation Error");
        when(academyClient.createGeneralUser(registrationDomain)).thenThrow(exception);

        // Act
        Mono<UserDetailDomain> result = userService.createGeneralUser(registrationDomain);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof RuntimeException
                        && err.getMessage().equals("General Creation Error"))
                .verify();

        verify(academyClient).createGeneralUser(registrationDomain);
    }

    @Test
    void createGeneralUser_ShouldHandleNullUserDomain() {
        // Arrange - registration with null userDomain
        UserRegistrationDomain nullUserDomainRegistration = UserRegistrationDomain.builder()
                .userDomain(null) // Null userDomain
                .userDetailDomain(userDetailDomain)
                .groupId(0)
                .build();

        // Mock a NullPointerException
        NullPointerException nullException = new NullPointerException("userDomain cannot be null");
        when(academyClient.createGeneralUser(nullUserDomainRegistration)).thenThrow(nullException);

        // Act
        Mono<UserDetailDomain> result = userService.createGeneralUser(nullUserDomainRegistration);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof NullPointerException
                        && err.getMessage().equals("userDomain cannot be null"))
                .verify();

        verify(academyClient).createGeneralUser(nullUserDomainRegistration);
    }

    @Test
    void createGeneralUser_ShouldHandleEmptyUserDetailDomain() {
        // Arrange - registration with empty userDetailDomain
        UserRegistrationDomain emptyDetailRegistration = UserRegistrationDomain.builder()
                .userDomain(userDomain)
                .userDetailDomain(UserDetailDomain.builder().build()) // Empty detail
                .groupId(0)
                .build();

        // Mock a specific exception for validation errors
        AppException validationException = new AppException("User details cannot be empty", HttpStatus.BAD_REQUEST);
        when(academyClient.createGeneralUser(emptyDetailRegistration)).thenThrow(validationException);

        // Act
        Mono<UserDetailDomain> result = userService.createGeneralUser(emptyDetailRegistration);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof AppException
                        && ((AppException) err).getCode() == HttpStatus.BAD_REQUEST)
                .verify();

        verify(academyClient).createGeneralUser(emptyDetailRegistration);
    }

    @Test
    void createGeneralUser_ShouldHandleServerError() {
        // Arrange
        AppException serverException = new AppException("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        when(academyClient.createGeneralUser(any())).thenThrow(serverException);

        // Act
        Mono<UserDetailDomain> result = userService.createGeneralUser(registrationDomain);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof AppException
                        && ((AppException) err).getCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verify();

        verify(academyClient).createGeneralUser(registrationDomain);
    }
}