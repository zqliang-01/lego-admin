package com.lego.system.util;

import com.lego.core.common.Constants;
import com.lego.core.data.hibernate.BusEntity;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.system.dto.SysCodePreviewInfo;
import com.lego.system.dto.SysGenTableColumnInfo;
import com.lego.system.dto.SysGenTableInfo;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class VelocityUtil {

    public static void init() {
        Properties p = new Properties();
        p.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.setProperty(Velocity.INPUT_ENCODING, Constants.DEFAULT_CHARSET_NAME);
        Velocity.init(p);
    }

    public static VelocityContext prepareContext(int startSn, SysGenTableInfo table, List<SysGenTableColumnInfo> columns) {
        BusinessException.check(!columns.isEmpty(), "当前数据表[{0}]无数据列，请先初始化后再操作！", table.getCode());
        init();
        VelocityContext velocityContext = new VelocityContext();
        // java类定义
        velocityContext.put("startSn", startSn);
        velocityContext.put("TableName", table.getCode());
        velocityContext.put("UrlName", table.getUrlName());
        velocityContext.put("AppCode", table.getAppCode());
        velocityContext.put("FieldName", table.getFieldName());
        velocityContext.put("ClassName", table.getClassName());
        velocityContext.put("PackageName", table.getPackageName());
        velocityContext.put("PermissionCode", table.getPermissionCode());
        velocityContext.put("Property", getProperty(columns));
        velocityContext.put("BaseImportList", getBaseImportList(columns));
        velocityContext.put("UtilImportList", getUtilImportList(columns));
        velocityContext.put("EntityImportList", getEntityImportList(columns));

        // vue定义
        velocityContext.put("FunctionName", table.getName());
        velocityContext.put("UnionKey", getUnionKey(columns));

        columns.sort(new Comparator<SysGenTableColumnInfo>() {
            @Override
            public int compare(SysGenTableColumnInfo o1, SysGenTableColumnInfo o2) {
                CustomFieldTypeEnum fieldType = CustomFieldTypeEnum.get(o1.getFormType());
                if (fieldType != null && fieldType.isEntity()) {
                    return 1;
                }
                fieldType = CustomFieldTypeEnum.get(o2.getFormType());
                if (fieldType != null && fieldType.isEntity()) {
                    return -1;
                }
                return o1.getSn() > o2.getSn() ? 1 : -1;
            }
        });
        velocityContext.put("columns", columns);
        return velocityContext;
    }

    private static Map<String, Boolean> getProperty(List<SysGenTableColumnInfo> columns) {
        Map<String, Boolean> map = new HashedMap<String, Boolean>();
        for (SysGenTableColumnInfo column : columns) {
            String formType = column.getFormType();
            CustomFieldTypeEnum fieldType = CustomFieldTypeEnum.get(formType);
            BusinessException.check(fieldType != null, "不存在的表单类型{{0}}，字段[{1}]生成失败！", formType, column.getComment());
            if (fieldType == CustomFieldTypeEnum.BOOLEAN) {
                map.put("hasBoolean", true);
            }
            if (fieldType == CustomFieldTypeEnum.ENTITY || fieldType == CustomFieldTypeEnum.SELECT) {
                map.put("hasEntity", true);
            }
            if (fieldType.getType().isAssignableFrom(Date.class)) {
                map.put("hasDate", true);
            }
            if (fieldType.isCommon()) {
                map.put("hasCommon", true);
            }
        }
        return map;
    }

    private static String getUnionKey(List<SysGenTableColumnInfo> columns) {
        for (SysGenTableColumnInfo column : columns) {
            if (column.isUnique()) {
                return column.getJavaField();
            }
        }
        return columns.get(0).getJavaField();
    }

    private static Set<String> getBaseImportList(List<SysGenTableColumnInfo> columns) {
        Set<String> list = new HashSet<String>();
        for (SysGenTableColumnInfo column : columns) {
            String formType = column.getFormType();
            CustomFieldTypeEnum fieldType = CustomFieldTypeEnum.get(formType);
            BusinessException.check(fieldType != null, "不存在的表单类型{{0}}，字段[{1}]生成失败！", formType, column.getComment());

            if (fieldType.getType().isAssignableFrom(BusEntity.class)) {
                continue;
            }
            Class<?> fieldClass = fieldType.getType();
            if (fieldClass.isPrimitive() || fieldClass.isAssignableFrom(String.class)) {
                continue;
            }
            list.add(fieldClass.getName());
        }
        return list;
    }

    private static Set<String> getUtilImportList(List<SysGenTableColumnInfo> columns) {
        Set<String> list = new HashSet<String>();
        for (SysGenTableColumnInfo column : columns) {
            String formType = column.getFormType();
            CustomFieldTypeEnum fieldType = CustomFieldTypeEnum.get(formType);
            BusinessException.check(fieldType != null, "不存在的表单类型{{0}}，字段[{1}]生成失败！", formType, column.getComment());

            if (fieldType.getType().isAssignableFrom(Date.class)) {
                list.add(DateUtil.class.getName());
            }
            if (column.isEntityType()) {
                list.add(EntityUtil.class.getName());
            }
        }
        return list;
    }

    private static Set<String> getEntityImportList(List<SysGenTableColumnInfo> columns) {
        Set<String> list = new HashSet<String>();
        for (SysGenTableColumnInfo column : columns) {
            String formType = column.getFormType();
            CustomFieldTypeEnum fieldType = CustomFieldTypeEnum.get(formType);
            BusinessException.check(fieldType != null, "不存在的表单类型{{0}}，字段[{1}]生成失败！", formType, column.getComment());
            if (fieldType.isEntity()) {
                list.add(column.getJavaFieldType());
            }
        }
        return list;
    }

    public static List<TypeInfo> getTemplateList() {
        List<TypeInfo> templates = new ArrayList<TypeInfo>();
        templates.add(new TypeInfo("{0}.java", "vm/java/entity/Entity.java.vm"));
        templates.add(new TypeInfo("{0}Controller.java", "vm/java/controller/Controller.java.vm"));
        templates.add(new TypeInfo("I{0}Service.java", "vm/java/service/IService.java.vm"));
        templates.add(new TypeInfo("{0}Service.java", "vm/java/service/impl/ServiceImpl.java.vm"));
        templates.add(new TypeInfo("I{0}Dao.java", "vm/java/dao/IDao.java.vm"));
        templates.add(new TypeInfo("{0}Dao.java", "vm/java/dao/impl/DaoImpl.java.vm"));
        templates.add(new TypeInfo("{0}Assembler.java", "vm/java/assembler/Assembler.java.vm"));
        templates.add(new TypeInfo("Add{0}Action.java", "vm/java/action/AddAction.java.vm"));
        templates.add(new TypeInfo("Delete{0}Action.java", "vm/java/action/DeleteAction.java.vm"));
        templates.add(new TypeInfo("Modify{0}Action.java", "vm/java/action/ModifyAction.java.vm"));
        templates.add(new TypeInfo("{0}CreateVO.java", "vm/java/vo/CreateVO.java.vm"));
        templates.add(new TypeInfo("{0}ModifyVO.java", "vm/java/vo/ModifyVO.java.vm"));
        templates.add(new TypeInfo("{0}Info.java", "vm/java/dto/Dto.java.vm"));
//        templates.add(new TypeInfo("index.vue", "vm/vue/index.vue.vm"));
//        templates.add(new TypeInfo("Detail.vue", "vm/vue/Detail.vue.vm"));
//        templates.add(new TypeInfo("Create.vue", "vm/vue/Create.vue.vm"));
//        templates.add(new TypeInfo("{0}.js", "vm/js/api.js.vm"));
//        templates.add(new TypeInfo("router.js", "vm/js/router.js.vm"));
        templates.add(new TypeInfo("{0}.sql", "vm/sql/permission.sql.vm"));
        return templates;
    }

    public static String getFileName(TypeInfo template, SysGenTableInfo table) {
        String javaPath = "";
        String templatePath = template.getName();
        String fileName = MessageFormat.format(template.getCode(), table.getClassName());
        if (templatePath.equals("vm/js/api.js.vm")) {
            fileName = MessageFormat.format(template.getCode(), table.getFieldName());
        }
        if (templatePath.startsWith("vm/java")) {
            javaPath = "java/" + table.getPackageName().replace(".", "/");
            templatePath = StringUtil.substringAfter(templatePath, "vm/java");
        } else {
            templatePath = StringUtil.substringAfter(templatePath, "vm/");
        }
        templatePath = templatePath.substring(0, templatePath.lastIndexOf("/") + 1);
        return javaPath + templatePath + fileName;
    }

    public static void addTemplateCode(SysCodePreviewInfo info, String fileName, String value) {
        String path = StringUtil.substringBefore(fileName, "/");
        if (StringUtil.isBlank(path)) {
            path = fileName;
        }
        if (StringUtil.equals(info.getCode(), path)) {
            if (StringUtil.equals(path, fileName)) {
                info.setValue(value);
                return;
            }
        }
        SysCodePreviewInfo children = info.getChildren(path);
        if (children == null) {
            children = new SysCodePreviewInfo(path);
            info.addChildren(children);
        }
        String subfix = StringUtil.substringAfter(fileName, "/");
        if (StringUtil.isBlank(subfix)) {
            subfix = fileName;
        }
        addTemplateCode(children, subfix, value);
    }
}
