package com.lego.core.data.hibernate;

import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.ActionVO;

/**
 * 跨服务接口，微服务部署时需切换为RPC
 */
public interface ICommonService {

	void save(ActionVO vo);

	TypeInfo findEmployeeBy(String code);

	TypeInfo findDeptBy(String code);
}