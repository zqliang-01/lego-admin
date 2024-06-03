package com.lego.core.data.mybatis.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public interface CommonMapper {

    @Select("SELECT NEXT_ID('${type}') FROM DUAL")
    long getId(String type);

    @Select("SELECT SYSDATE FROM DUAL")
    Date getCurrentDate();
}