package co.edu.gimnasiolorismalaguzzi.academyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
public class AcademyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademyServiceApplication.class, args);
	}

}

@RestController
@RequestMapping("/api/academy")
class HelloController {

	@GetMapping("/hello")
	public String helloWorld() {
		return "Hola Mundo!";
	}
}