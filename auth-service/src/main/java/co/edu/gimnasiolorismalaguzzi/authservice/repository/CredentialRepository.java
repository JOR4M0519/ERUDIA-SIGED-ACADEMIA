package co.edu.gimnasiolorismalaguzzi.authservice.repository;

import co.edu.gimnasiolorismalaguzzi.authservice.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CredentialRepository extends JpaRepository<Credential, Integer> {

    Optional<Credential> findByUsername(String username);
    Optional<Credential> findByEmail(String email);
}
