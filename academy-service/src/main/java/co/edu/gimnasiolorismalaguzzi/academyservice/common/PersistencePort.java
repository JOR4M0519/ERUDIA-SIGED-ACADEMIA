package co.edu.gimnasiolorismalaguzzi.academyservice.common;

import org.springframework.http.HttpStatus;

import java.util.List;


public interface PersistencePort<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T save(T entity);
    T update(ID id, T entity);
    HttpStatus delete(ID id);
}
