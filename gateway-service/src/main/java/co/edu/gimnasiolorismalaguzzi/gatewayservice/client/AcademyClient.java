package co.edu.gimnasiolorismalaguzzi.gatewayservice.client;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.config.FeignConfig;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserRegistrationDomain;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Si usas Eureka (el nombre debe coincidir con el registrado en Eureka)
@FeignClient(name = "academy", configuration = FeignConfig.class)
// Si NO usas Eureka, coloca la URL fija del microservicio
// @FeignClient(name = "academy-service", url = "http://localhost:8081")

public interface AcademyClient {

    @GetMapping("/api/academy/public/users/{username}")
    UserDetailDomain getDetailUser(@PathVariable("username") String username);

    @PostMapping("/api/academy/users/detail/students/register")
    UserDetailDomain createStudentUser(@RequestBody UserRegistrationDomain registrationDomain);

    @PostMapping("/api/academy/users/detail")
    UserDetailDomain createGeneralUser(@RequestBody UserRegistrationDomain registrationDomain);
}
