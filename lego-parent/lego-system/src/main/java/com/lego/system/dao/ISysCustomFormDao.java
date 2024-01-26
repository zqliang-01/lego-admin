package com.lego.system.dao;

import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.dto.LegoPage;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysGenTable;
import com.lego.system.vo.SysCustomFormSearchVO;

public interface ISysCustomFormDao extends IGenericDao<SysCustomForm> {

    LegoPage<SysCustomForm> findBy(SysCustomFormSearchVO vo);

    SysCustomForm findByTable(SysGenTable table);

    SysCustomForm findByTableCode(String tableCode);

}
