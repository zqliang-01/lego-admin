package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.system.action.AddSysSceneAction;
import com.lego.system.action.ModifySysSceneAction;
import com.lego.system.action.ModifySysSceneVisibleAction;
import com.lego.system.assembler.SysSceneAssembler;
import com.lego.system.dao.ISysSceneDao;
import com.lego.system.dto.SysSceneInfo;
import com.lego.system.entity.SysScene;
import com.lego.system.service.ISysSceneService;
import com.lego.system.vo.SysSceneCreateVO;
import com.lego.system.vo.SysSceneModifyVO;
import com.lego.system.vo.SysSceneVisibleVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysSceneService extends BusService<ISysSceneDao, SysSceneAssembler> implements ISysSceneService {

    @Override
    public List<SysSceneInfo> findByForm(String formCode) {
        List<SysScene> scene = dao.findByForm(formCode);
        return assembler.create(scene);
    }

    @Override
    public List<SysSceneInfo> findBy(String formCode, boolean visible) {
        List<SysScene> scene = dao.findBy(formCode, visible);
        return assembler.create(scene);
    }

    @Override
    public void add(String operatorCode, SysSceneCreateVO vo) {
        new AddSysSceneAction(operatorCode, vo).run();
    }

    @Override
    public void modify(String operatorCode, SysSceneModifyVO vo) {
        new ModifySysSceneAction(operatorCode, vo).run();
    }

    @Override
    public void modifyVisible(String operatorCode, SysSceneVisibleVO vo) {
        new ModifySysSceneVisibleAction(operatorCode, vo).run();
    }

}
