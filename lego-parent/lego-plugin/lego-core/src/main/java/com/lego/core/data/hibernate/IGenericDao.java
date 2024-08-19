package com.lego.core.data.hibernate;

import com.lego.core.data.DataPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericConditionVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IGenericDao<T extends BaseEntity> extends JpaRepository<T, Long> {

    List<T> findByCodes(List<String> codes);

    T findByCode(String code);

    T findByUnsureCode(String code);

    List<T> findAll();

    @DataPermission
    LegoPage<T> findPageBy(GenericConditionVO vo);

    List<T> findBy(GenericConditionVO vo);

    long findCountBy(GenericConditionVO vo);

    boolean exists(GenericConditionVO vo);

    boolean exists(String code);
}
