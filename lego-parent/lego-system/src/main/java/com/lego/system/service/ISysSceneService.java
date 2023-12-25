package com.lego.system.service;

import java.util.List;

import com.lego.system.dto.SysSceneInfo;
import com.lego.system.vo.SysSceneCreateVO;
import com.lego.system.vo.SysSceneModifyVO;
import com.lego.system.vo.SysSceneVisibleVO;

public interface ISysSceneService {

	List<SysSceneInfo> findByForm(String formCode);

	List<SysSceneInfo> findBy(String formCode, boolean visible);

	void add(String operatorCode, SysSceneCreateVO vo);

	void modify(String operatorCode, SysSceneModifyVO vo);

	void modifyVisible(String operatorCode, SysSceneVisibleVO vo);

}
