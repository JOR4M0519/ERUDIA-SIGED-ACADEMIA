package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.GradeSettingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceGradeSettingPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.List;

@PersistenceAdapter
@Slf4j
public class GradeSettingAdapter implements PersistenceGradeSettingPort {

    public GradeSettingAdapter() {
    }

    @Override
    public List<GradeSettingDomain> findAll() {

        return null;
    }

    @Override
    public List<GradeSettingDomain> findByLevelId(Integer levelId) {
        return null;
    }

    @Override
    public GradeSettingDomain findById(Integer integer) {
        return null;
    }

    @Override
    public GradeSettingDomain save(GradeSettingDomain entity) {
        return null;
    }

    @Override
    public GradeSettingDomain update(Integer integer, GradeSettingDomain entity) {
     return null;
    }

    @Override
    public HttpStatus delete(Integer integer) {
        return HttpStatus.I_AM_A_TEAPOT;
    }

}
