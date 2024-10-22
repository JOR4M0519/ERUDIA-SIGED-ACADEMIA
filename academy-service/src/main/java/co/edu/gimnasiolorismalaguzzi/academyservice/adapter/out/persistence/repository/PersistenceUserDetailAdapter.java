package co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PersistenceUserDetailAdapter extends PersistenceAdapter<UserDetailDomain, Integer> {
    UserDetailDomain saveDetailUser(Integer id,UserDetailDomain user); // Aquí puedes agregar métodos específicos para UserDetailDomain si es necesario
}
