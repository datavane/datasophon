package com.datasophon.worker.utils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.datasophon.common.Constants;
import com.datasophon.common.model.AlertItem;
import com.datasophon.common.model.Generators;
import com.datasophon.common.model.ServiceConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class FreemakerUtils {
    private static final String XML = "xml";
    private static final String PROPERTIES = "properties";
    private static final String PROPERTIES2 = "properties2";
    private static final String PROPERTIES3 = "properties3";
    private static final String PROMETHEUS = "prometheus";
    private static final String CUSTOM = "custom";


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
        if (XML.equals(configFormat)) {
            template = config.getTemplate("xml.ftl");
        }
        if (PROPERTIES.equals(configFormat)) {
            template = config.getTemplate("properties.ftl");
        }
        if (PROPERTIES2.equals(configFormat)) {
            template = config.getTemplate("properties2.ftl");
        }
        if (PROPERTIES3.equals(configFormat)) {
            template = config.getTemplate("properties3.ftl");
        }
        if (PROMETHEUS.equals(configFormat)) {
            template = config.getTemplate("alert.yml");
        }
        if (CUSTOM.equals(configFormat)) {
            template = config.getTemplate(generators.getTemplateName());
            data = configs.stream().filter(e -> "map".equals(e.getConfigType())).collect(Collectors.toMap(key -> key.getName(), value -> value.getValue()));
            configs = configs.stream().filter(e -> !"map".equals(e.getConfigType())).collect(Collectors.toList());
        }

        data.put("itemList", configs);
        // 3.产生输出
        processOut(generators, template, data, decompressPackageName);
    }

    public static void testGenerateConfigFile(Generators generators, List<ServiceConfig> configs, String decompressPackageName) throws IOException, TemplateException {
        // 1.加载模板
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        config.setClassForTemplateLoading(FreemakerUtils.class, "/templates"); // ""代表当前包

        Map<String, Object> data = new HashMap<>();
        // 得到模板对象
        String configFormat = generators.getConfigFormat();
        Template template = null;
        if (XML.equals(configFormat)) {
            template = config.getTemplate("xml.ftl");
        }
        if (PROPERTIES.equals(configFormat)) {
            template = config.getTemplate("properties.ftl");
        }
        if (PROPERTIES2.equals(configFormat)) {
            template = config.getTemplate("properties2.ftl");
        }
        if (PROMETHEUS.equals(configFormat)) {
            template = config.getTemplate("alert.yml");
        }
        if (CUSTOM.equals(configFormat)) {
            template = config.getTemplate(generators.getTemplateName());
            data = configs.stream().filter(e -> "map".equals(e.getConfigType())).collect(Collectors.toMap(key -> key.getName(), value -> value.getValue()));
            configs = configs.stream().filter(e -> !"map".equals(e.getConfigType())).collect(Collectors.toList());
        }

        data.put("itemList", configs);
        // 3.产生输出
        testProcessOut(generators, template, data, decompressPackageName);
    }

    public static void generatePromAlertFile(Generators generators, List<AlertItem> configs, String serviceName) throws IOException, TemplateException {
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        config.setClassForTemplateLoading(FreemakerUtils.class, "/templates"); // ""代表当前包
        // 得到模板对象
        String configFormat = generators.getConfigFormat();
        Template template = null;

        if (PROMETHEUS.equals(configFormat)) {
            template = config.getTemplate("alert.yml");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("itemList", configs);
        data.put("serviceName", serviceName);
        // 3.产生输出
        processOut(generators, template, data, "prometheus-2.17.2");
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
        String packagePath = Constants.INSTALL_PATH + Constants.SLASH + decompressPackageName + Constants.SLASH;
        if(generators.getOutputDirectory().contains(Constants.COMMA)){
            for (String outPutDir : generators.getOutputDirectory().split(StrUtil.COMMA)) {
                FileWriter out = new FileWriter(new File(packagePath + outPutDir + Constants.SLASH + generators.getFilename()));
                template.process(data, out);
                out.close();
            }
        }else{
            FileWriter out = new FileWriter(new File(packagePath + generators.getOutputDirectory() + Constants.SLASH + generators.getFilename()));
            template.process(data, out);
            out.close();
        }
    }

    private static void testProcessOut(Generators generators, Template template, Map<String, Object> data, String decompressPackageName) throws IOException, TemplateException {
        FileWriter out = new FileWriter(new File("D:\\360downloads\\"+generators.getOutputDirectory() + Constants.SLASH + generators.getFilename()));
        template.process(data, out);
        out.close();
    }
}
