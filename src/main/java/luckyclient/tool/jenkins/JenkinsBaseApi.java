package luckyclient.tool.jenkins;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.Plugin;
import com.offbytwo.jenkins.model.PluginManager;

import luckyclient.utils.LogUtil;

/**
 * ��ȡ Jenkins ������Ϣ�Լ���������
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��10��29��
 */
public class JenkinsBaseApi {
 
    // Jenkins ����
    private JenkinsServer jenkinsServer;
 
    /**
     * ���췽���е������� Jenkins ����
     * @param jenkinsConnect
     * 2019��10��29��
     */
    JenkinsBaseApi(JenkinsConnect jenkinsConnect){
        this.jenkinsServer = jenkinsConnect.connection();
    }
    
    /**
     * ��ȡ������Ϣ
     * @author Seagull
     * @date 2019��10��29��
     */
    public void getComputerInfo() {
        try {
            Map<String, Computer> map = jenkinsServer.getComputers();
            for (Computer computer : map.values()) {
                // ��ȡ��ǰ�ڵ�-�ڵ�����
            	LogUtil.APP.info("��ǰ�ڵ�-�ڵ�����:{}",computer.details().getDisplayName());
                // ��ȡ��ǰ�ڵ�-ִ��������
                LogUtil.APP.info("��ǰ�ڵ�-ִ��������:{}",computer.details().getNumExecutors().toString());
                // ��ȡ��ǰ�ڵ�-ִ������ϸ��Ϣ
                //List<Executor> executorList = computer.details().getExecutors();
                // �鿴��ǰ�ڵ�-�Ƿ��ѻ�
                LogUtil.APP.info("��ǰ�ڵ�-�Ƿ��ѻ�:{}",computer.details().getOffline().toString());
                // ��ýڵ��ȫ��ͳ����Ϣ
                //LoadStatistics loadStatistics = computer.details().getLoadStatistics();
                // ��ȡ�ڵ��-�������
                //Map<String, Map> monitorData = computer.details().getMonitorData();
                //......
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * ���� Jenkins
     * @author Seagull
     * @date 2019��10��29��
     */
    public void restart() {
        try {
        	LogUtil.APP.info("׼������Jenkins...");
            jenkinsServer.restart(true);
            LogUtil.APP.info("����Jenkins�ɹ���");
        } catch (IOException e) {
        	LogUtil.APP.error("����Jenkins�����쳣",e);
        }
    }
 
    /**
     * ��ȫ���� Jenkins
     * @author Seagull
     * @date 2019��10��29��
     */
    public void safeRestart() {
        try {
        	LogUtil.APP.info("׼����ȫ���� Jenkins...");
            jenkinsServer.safeRestart(true);
            LogUtil.APP.info("��ȫ����Jenkins�ɹ���");
        } catch (IOException e) {
        	LogUtil.APP.error("��ȫ����Jenkins�����쳣",e);
        }
    }
 
    /**
     * ��ȫ���� Jenkins
     * 
     * @author Seagull
     * @date 2019��10��29��
     */
    public void safeExit() {
        try {
        	LogUtil.APP.info("׼����ȫ���� Jenkins...");
            jenkinsServer.safeExit(true);
            LogUtil.APP.info("��ȫ���� Jenkins�ɹ���");
        } catch (IOException e) {
        	LogUtil.APP.error("��ȫ����Jenkins�����쳣",e);
        }
    }
 
    /**
     * �ر� Jenkins ����
     * 
     * @author Seagull
     * @date 2019��10��29��
     */
    public void close() {
    	LogUtil.APP.info("׼���ر�Jenkins����");
        jenkinsServer.close();
        LogUtil.APP.info("�ر�Jenkins���ӳɹ�");
    }
 
    /**
     * �ж� Jenkins �Ƿ�����
     * 
     * @author Seagull
     * @date 2019��10��29��
     */
    public void isRunning() {
        boolean isRunning = jenkinsServer.isRunning();
        LogUtil.APP.info("Jenkins����״̬:{}",isRunning);
    }
 
    /**
     * ��ȡ Jenkins �����Ϣ
     * 
     * @author Seagull
     * @date 2019��10��29��
     */
    public void getPluginInfo(){
        try {
            PluginManager pluginManager =jenkinsServer.getPluginManager();
            // ��ȡ����б�
            List<Plugin> plugins = pluginManager.getPlugins();
            for (Plugin plugin:plugins){
                // ��� wiki URL ��ַ
                LogUtil.APP.info(plugin.getUrl());
                // �汾��
                LogUtil.APP.info(plugin.getVersion());
                // ���
                LogUtil.APP.info(plugin.getShortName());
                // ��������
                LogUtil.APP.info(plugin.getLongName());
                // �Ƿ�֧�ֶ�̬����
                LogUtil.APP.info(plugin.getSupportsDynamicLoad());
                // ������������
                //LogUtil.APP.info(plugin.getDependencies());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
}
