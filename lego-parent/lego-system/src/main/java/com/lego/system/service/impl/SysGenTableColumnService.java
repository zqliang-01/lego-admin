package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BaseService;
import com.lego.core.data.mybatis.mapper.MetaTableMapper;
import com.lego.core.dto.MetaTableColumnInfo;
import com.lego.core.dto.TypeInfo;
import com.lego.core.module.gen.GenConstants;
import com.lego.sharding.config.ShardingHintConfig;
import com.lego.system.action.AddSysGenTableColumnAction;
import com.lego.system.action.DeleteSysGenTableColumnAction;
import com.lego.system.action.ModifySysGenTableColumnAction;
import com.lego.system.assembler.SysGenTableColumnAssembler;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.dto.SysGenTableColumnInfo;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.service.ISysGenTableColumnService;
import com.lego.system.vo.SysGenTableColumnCreateVO;
import com.lego.system.vo.SysGenTableColumnModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysGenTableColumnService extends BaseService<ISysGenTableColumnDao, SysGenTableColumnAssembler> implements ISysGenTableColumnService {

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
    public void modify(String operatorCode, SysGenTableColumnModifyVO vo) {
        new ModifySysGenTableColumnAction(operatorCode, vo).run();
    }

    @Override
    public List<MetaTableColumnInfo> findMetaColumnBy(String dataSource, String tableCode) {
        ShardingHintConfig.setDataSource(dataSource);
        List<MetaTableColumnInfo> metaTableColumnInfos = tableMapper.selectColumns(tableCode);
        ShardingHintConfig.clear();
        return metaTableColumnInfos.stream().filter(colunm -> {
            return !GenConstants.COLUMNNAME_IGNORE_GEN.contains(colunm.getColumnName());
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(String operatorCode, String code) {
        new DeleteSysGenTableColumnAction(operatorCode, code).run();
    }

    @Override
    public void add(String operatorCode, SysGenTableColumnCreateVO vo) {
        new AddSysGenTableColumnAction(operatorCode, vo).run();
    }

}
