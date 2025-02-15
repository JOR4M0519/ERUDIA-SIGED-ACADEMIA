package co.edu.gimnasiolorismalaguzzi.gatewayservice.client;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.config.FeignConfig;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserDetailDomain;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Si usas Eureka (el nombre debe coincidir con el registrado en Eureka)
@FeignClient(name = "academy", configuration = FeignConfig.class)
// Si NO usas Eureka, coloca la URL fija del microservicio
// @FeignClient(name = "academy-service", url = "http://localhost:8081")

public interface AcademyClient {

    @GetMapping("/api/academy/public/users/{username}")
    UserDetailDomain getDetailUser(@PathVariable("username") String username);
}
