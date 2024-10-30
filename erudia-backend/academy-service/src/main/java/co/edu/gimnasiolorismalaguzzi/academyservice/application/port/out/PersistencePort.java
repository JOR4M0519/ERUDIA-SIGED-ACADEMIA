package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out;

import org.springframework.http.HttpStatus;

import java.util.List;


public interface PersistencePort<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T save(ID id, T entity);
    T update(ID id, T entity);
    HttpStatus delete(ID id);
}
