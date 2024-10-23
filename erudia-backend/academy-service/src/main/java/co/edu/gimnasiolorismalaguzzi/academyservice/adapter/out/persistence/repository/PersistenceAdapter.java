package co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;


public interface PersistenceAdapter<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T save(ID id, T entity);
    T update(ID id, T entity);
    HttpStatus delete(ID id);
}
