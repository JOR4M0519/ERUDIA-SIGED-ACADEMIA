package co.edu.gimnasiolorismalaguzzi.gatewayservice.services;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.client.AcademyClient;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserRegistrationDomain;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class UserService {

    private final AcademyClient academyClient;

    public UserService(AcademyClient academyClient) {
        this.academyClient = academyClient;
    }

    public Mono<UserDetailDomain> getDetailUser(String username) {
        return Mono.fromCallable(() -> academyClient.getDetailUser(username))
                .subscribeOn(Schedulers.boundedElastic());
    }

}
