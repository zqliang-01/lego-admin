package com.lego.system.service.impl;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.data.mybatis.mapper.MetaTableMapper;
import com.lego.core.dto.MetaTableColumnInfo;
import com.lego.core.dto.TypeInfo;
import com.lego.system.action.ModifySysGenTableColumnAction;
import com.lego.system.assembler.SysGenTableColumnAssembler;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.dto.SysGenTableColumnInfo;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.service.ISysGenTableColumnService;
import com.lego.system.vo.SysGenTableColumnModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysGenTableColumnService extends BusService<ISysGenTableColumnDao, SysGenTableColumnAssembler> implements ISysGenTableColumnService {

    @Autowired
    private MetaTableMapper tableMapper;

    @Override
    public List<SysGenTableColumnInfo> findByTable(String tableCode) {
        List<SysGenTableColumn> columns = dao.findBy(tableCode);
        return assembler.create(columns);
    }

    @Override
    public List<TypeInfo> findSimpleTypeBy(String tableCode) {
        List<SysGenTableColumn> columns = dao.findBy(tableCode);
        List<TypeInfo> columnInfos = new ArrayList<TypeInfo>();
        for (SysGenTableColumn column : columns) {
            String code = column.getJavaField();
            String name = column.getComment();
            columnInfos.add(new TypeInfo(code, name));
        }
        return columnInfos;
    }

    @Override
    public void modify(String operatorCode, List<SysGenTableColumnModifyVO> vos) {
        for (SysGenTableColumnModifyVO vo : vos) {
            new ModifySysGenTableColumnAction(operatorCode, vo).run();
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<MetaTableColumnInfo> findMetaColumnBy(String dataSource, String tableCode) {
        DynamicDataSourceContextHolder.push(dataSource);
        List<MetaTableColumnInfo> tableColumns = tableMapper.selectColumns(tableCode);
        DynamicDataSourceContextHolder.clear();
        return tableColumns;
    }

}
