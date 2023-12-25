package com.lego.core.data.hibernate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lego.core.dto.LegoPage;
import com.lego.core.vo.GenericConditionVO;

public interface IGenericDao<T extends BaseEntity> extends JpaRepository<T, Long> {

    List<T> findByCodes(List<String> codes);

    T findByCode(String code);

    T findByUnsureCode(String code);

    List<T> findAll();

    LegoPage<T> findPageBy(GenericConditionVO vo);

    List<T> findBy(GenericConditionVO vo);

    long findCountBy(GenericConditionVO vo);
}
