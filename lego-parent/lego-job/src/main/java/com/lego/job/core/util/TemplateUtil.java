package com.lego.job.core.util;

import com.lego.core.common.Constants;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Properties;

public class TemplateUtil {

    public static void init() {
        Properties p = new Properties();
        p.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.setProperty(Velocity.INPUT_ENCODING, Constants.DEFAULT_CHARSET_NAME);
        Velocity.init(p);
    }

    public static String getValue(String name) {
        init();
        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate(name, Constants.DEFAULT_CHARSET_NAME);
        tpl.merge(new VelocityContext(), sw);
        return sw.toString();
    }
}
