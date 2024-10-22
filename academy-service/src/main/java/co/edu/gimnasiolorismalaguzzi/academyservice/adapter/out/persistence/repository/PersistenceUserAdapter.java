package co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PersistenceUserAdapter extends PersistenceAdapter<UserDomain, Integer> {

    // Aquí puedes agregar métodos específicos para UserDomain si es necesario
}
