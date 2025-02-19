package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.enums.ActionType;
import com.lego.core.util.EntityUtil;
import com.lego.system.dao.ISysSceneDao;
import com.lego.system.entity.SysScene;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysSceneVisibleVO;

import java.text.MessageFormat;
import java.util.List;

public class ModifySysSceneVisibleAction extends MaintainAction {

    private SysSceneVisibleVO vo;
    private ISysSceneDao sceneDao = getDao(ISysSceneDao.class);

    public ModifySysSceneVisibleAction(String operatorCode, SysSceneVisibleVO vo) {
        super(SysPermissionCode.manage, operatorCode);
        this.vo = vo;
    }

    @Override
    protected void doRun() {
        List<SysScene> scenes = visibleScene();
        List<SysScene> hiddenScenes = hiddenScene();
        this.description = MessageFormat.format("显示的场景{0}\r\n隐藏的场景{1}", EntityUtil.toString(scenes), EntityUtil.toString(hiddenScenes));
    }

    private List<SysScene> visibleScene() {
        List<SysScene> scenes = sceneDao.findByCodes(vo.getCodes());
        if (scenes.isEmpty()) {
            return scenes;
        }
        scenes.stream().forEach(s -> s.setVisible(true));
        sceneDao.saveAll(scenes);
        return scenes;
    }

    private List<SysScene> hiddenScene() {
        List<SysScene> hiddenScenes = sceneDao.findByCodes(vo.getHiddenCodes());
        if (hiddenScenes.isEmpty()) {
            return hiddenScenes;
        }
        hiddenScenes.stream().forEach(s -> s.setVisible(false));
        sceneDao.saveAll(hiddenScenes);
        return hiddenScenes;
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }

    @Override
    protected String getEntityName() {
        return "场景";
    }
}
