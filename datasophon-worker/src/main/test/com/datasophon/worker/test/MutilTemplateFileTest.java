package com.datasophon.worker.test;

import com.datasophon.common.model.Generators;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.worker.utils.FreemakerUtils;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <pre>
 *
 * Created by zhenqin.
 * User: zhenqin
 * Date: 2023/4/19
 * Time: 下午3:54
 * Vendor: yiidata.com
 *
 * </pre>
 *
 * @author zhenqin
 */
public class MutilTemplateFileTest {

    @Test
    public void generateCustomTemplate() throws IOException, TemplateException {
        Generators generators = new Generators();
        generators.setConfigFormat("custom");
        generators.setFilename("application.yml");
        generators.setTemplateName("dataops-application.ftl");
        generators.setOutputDirectory("templa");

        // config data
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setConfigType("map");
        serviceConfig.setName("flowschedulerUrl");
        serviceConfig.setValue("http://192.168.1.10:12345/dolphinscheduler");

        ServiceConfig serviceConfig2 = new ServiceConfig();
        serviceConfig2.setConfigType("map");
        serviceConfig2.setName("flowschedulerToken");
        serviceConfig2.setValue("337d43d7aaa062a2a6032a14aa836240");

        ArrayList<ServiceConfig> serviceConfigs = new ArrayList<>();
        serviceConfigs.add(serviceConfig);
        serviceConfigs.add(serviceConfig2);

        FreemakerUtils.generateConfigFile(generators, serviceConfigs,"", "manual/docs");
    }
}
