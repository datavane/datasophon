package com.datasophon.common.utils;

import com.datasophon.common.Constants;
import com.datasophon.common.model.AlertItem;
import com.datasophon.common.model.Generators;
import com.datasophon.common.model.ServiceConfig;
import com.sun.org.apache.bcel.internal.Const;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class FreemakerUtils {

    public static void generateConfigFile(Generators generators, List<ServiceConfig> configs, String decompressPackageName) throws IOException, TemplateException {
        // 1.加载模板
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        config.setClassForTemplateLoading(FreemakerUtils.class, "/templates"); // ""代表当前包

        Map<String, Object> data = new HashMap<>();
        // 得到模板对象
        String configFormat = generators.getConfigFormat();
        Template template = null;
        if ("xml".equals(configFormat)) {
            template = config.getTemplate("xml.ftl");
        }
        if ("properties".equals(configFormat)) {
            template = config.getTemplate("properties.ftl");
        }
        if ("prometheus".equals(configFormat)) {
            template = config.getTemplate("alert.yml");
        }
        if ("custom".equals(configFormat)) {
            template = config.getTemplate(generators.getTemplateName());
            data = configs.stream().collect(Collectors.toMap(key -> key.getName(), value -> value.getValue()));
        }

        data.put("itemList", configs);
        // 3.产生输出
        processOut(generators, template, data, decompressPackageName);
    }

    public static void generatePromAlertFile(Generators generators, List<AlertItem> configs, String serviceName) throws IOException, TemplateException {
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        config.setClassForTemplateLoading(FreemakerUtils.class, "/templates"); // ""代表当前包
        // 得到模板对象
        String configFormat = generators.getConfigFormat();
        Template template = null;

        if ("prometheus".equals(configFormat)) {
            template = config.getTemplate("alert.yml");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("itemList", configs);
        data.put("serviceName", serviceName);
        // 3.产生输出
        processOut(generators, template, data, serviceName);
    }


    public static void generatePromScrapeConfig(Generators generators, List<ServiceConfig> configs, String serviceName) throws IOException, TemplateException {
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        config.setClassForTemplateLoading(FreemakerUtils.class, "/templates"); // ""代表当前包
        // 得到模板对象
        Template template = config.getTemplate("scrape.ftl");

        Map<String, Object> data = new HashMap<>();
        data.put("itemList", configs);
        // 3.产生输出
        processOut(generators, template, data, serviceName);
    }

    private static void processOut(Generators generators, Template template, Map<String, Object> data, String decompressPackageName) throws IOException, TemplateException {
//        String packagePath = Constants.INSTALL_PATH + Constants.SLASH + decompressPackageName + Constants.SLASH;
        if(generators.getOutputDirectory().contains(Constants.COMMA)){
            for (String outPutDir : generators.getOutputDirectory().split(",")) {
                FileWriter out = new FileWriter(new File( outPutDir + generators.getFilename()));
                template.process(data, out);
                out.close();
            }
        }else{
            FileWriter out = new FileWriter(new File( generators.getOutputDirectory() + generators.getFilename()));
            template.process(data, out);
            out.close();
        }


    }
}
