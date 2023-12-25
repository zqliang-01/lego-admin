package com.lego.system.assembler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lego.core.assembler.TreeAssembler;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.system.dto.SysAppInfo;
import com.lego.system.dto.SysPermissionInfo;
import com.lego.system.entity.SysPermission;

@Component
public class SysPermissionAssembler extends TreeAssembler<SysPermissionInfo, SysPermission> {

	@Override
	protected SysPermissionInfo doCreate(SysPermission entity) {
		SysPermissionInfo info = new SysPermissionInfo();
		info.setCode(entity.getCode());
		info.setName(entity.getName());
		info.setIcon(entity.getIcon());
		info.setType(createTypeInfo(entity.getType()));
		info.setCreateTime(entity.getCreateTime());
		info.setParentCode(EntityUtil.getCode(entity.getParent()));
		return info;
	}

	public List<SysAppInfo> createApp(List<SysPermission> permissions) {
		List<SysAppInfo> infos = new ArrayList<SysAppInfo>();
		for (SysPermission permission : permissions) {
			SysAppInfo info = new SysAppInfo();
			info.setCode(permission.getCode());
			info.setPath("/" + permission.getCode());
			info.setName(permission.getName());
			info.setIcon(permission.getIcon());
			infos.add(info);
		}
		return infos;
	}

	public JSONObject createAuth(List<SysPermissionInfo> permissions, List<String> validApps) {
		JSONObject auth = new JSONObject();
		for (SysPermissionInfo permission : permissions) {
			if (permission.isApp() && !validApps.contains(permission.getCode())) {
				continue;
			}
			List<SysPermissionInfo> childrens = permission.getChildrens();
			if (!childrens.isEmpty()) {
				JSONObject value = createAuth(childrens, validApps);
				if (StringUtil.isNotBlank(permission.getIcon())) {
					value.put("icon", permission.getIcon());
				}
				auth.put(permission.getRealm(), value);
				continue;
			}
			auth.put(permission.getRealm(), true);
		}
		return auth;
	}
}
