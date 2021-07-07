package luckyclient.tool.mail;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import luckyclient.utils.LogUtil;
import luckyclient.utils.config.SysConfig;

/**
 * ����Freemarkerģ�弼�����ʼ�ģ�����
 *
 * @author Administrator
 */
public class FreemarkerEmailTemplate {
    final Properties properties = SysConfig.getConfiguration();
    /**
     * �ʼ�ģ��Ĵ��λ��
     */
    private final String TEMPLATE_PATH = properties.getProperty("mail.freemarker.template");
    /**
     * �ʼ�ģ���WEBϵͳ��ip�Լ��˿�
     */
    private final String WEB_IP = properties.getProperty("server.web.ip");
    private final String WEB_PORT = properties.getProperty("server.web.port");
    private final String WEB_PATH = properties.getProperty("server.web.path");
    /**
     * ����ģ�建��
     */
    private static final Map<String, Template> TEMPLATE_CACHE = new HashMap<>();
    /**
     * ģ���ļ���׺
     */
    private static final String SUFFIX = ".ftl";

    /**
     * ģ����������
     */
    public String getText(String templateId, Map<String, Object> parameters) {
        @SuppressWarnings("deprecation")
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(new ClassTemplateLoader(FreemarkerEmailTemplate.class, TEMPLATE_PATH));
        configuration.setDefaultEncoding("gbk");
        //configuration.setEncoding(Locale.getDefault(), "UTF-8");
        configuration.setDateFormat("yyyy-MM-dd HH:mm:ss");
        String templateFile = templateId + SUFFIX;
        try {
            Template template = TEMPLATE_CACHE.get(templateFile);
            if (template == null) {
                template = configuration.getTemplate(templateFile);
                TEMPLATE_CACHE.put(templateFile, template);
            }
            StringWriter stringWriter = new StringWriter();
            parameters.put("webip", WEB_IP);
            parameters.put("webport", WEB_PORT);
            parameters.put("webpath", WEB_PATH);
            template.process(parameters, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
        	LogUtil.APP.error("��ȡ�ʼ�ģ���������ó����쳣",e);
            throw new RuntimeException(e);
        }
    }

}