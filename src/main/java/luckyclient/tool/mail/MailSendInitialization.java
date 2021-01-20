package luckyclient.tool.mail;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.ProjectProtocolTemplate;
import luckyclient.remote.entity.TaskScheduling;
import luckyclient.utils.LogUtil;
import luckyclient.utils.config.SysConfig;
import luckyclient.utils.httputils.HttpClientTools;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 *
 * @author�� seagull
 * @date 2018��3��1��
 */
public class MailSendInitialization {

    public static void sendMailInitialization(String subject, String content, String taskid, TaskScheduling taskScheduling, int[] taskCount, String time, String buildStatus, String restartStatus) {
        boolean isSend = false;
        if (null == taskCount) {
            isSend = true;
        } else {
            if (taskCount.length == 5 && null != taskScheduling) {
                Integer sendCondition = taskScheduling.getEmailSendCondition();
                // ����ȫ���ɹ��˷���, casecount != casesuc
                if (null != sendCondition && 1 == sendCondition) {
                    if (taskCount[0] == taskCount[1]) {
                        isSend = true;
                    }
                }
                // ��������ʧ���˷���
                if (null != sendCondition && 2 == sendCondition) {
                    if (taskCount[2] > 0) {
                        isSend = true;
                    }
                }
                // ȫ��
                if (null != sendCondition && 0 == sendCondition) {
                    isSend = true;
                }
            }
        }
        if (!isSend) {
            LogUtil.APP.info("��ǰ������Ҫ�����ʼ���������֪ͨ!");
            return;
        }
        //�������������Ϣ
        MailSendInitialization msi= new MailSendInitialization();
        msi.pushMessage(taskScheduling, content, taskCount, time, buildStatus, restartStatus);

        String[] addresses = serverOperation.getEmailAddress(taskScheduling,taskid);
        Properties properties = SysConfig.getConfiguration();
        if (addresses != null) {
            LogUtil.APP.info("׼�������Խ�������ʼ�֪ͨ�����Ե�...");
            //�������Ҫ�������ʼ�
            MailSenderInfo mailInfo = new MailSenderInfo();
            //�������Ҫ�������ʼ�
            SimpleMailSender sms = new SimpleMailSender();
            mailInfo.setMailServerHost(properties.getProperty("mail.smtp.ip"));
            mailInfo.setMailServerPort(properties.getProperty("mail.smtp.port"));
            mailInfo.setSslenable(properties.getProperty("mail.smtp.ssl.enable").equals("true"));
            mailInfo.setValidate(true);
            mailInfo.setUserName(properties.getProperty("mail.smtp.username"));
            //������������
            mailInfo.setPassword(properties.getProperty("mail.smtp.password"));
            mailInfo.setFromAddress(properties.getProperty("mail.smtp.username"));
            //����
            mailInfo.setSubject(subject);
            //����
            mailInfo.setContent(content);
            mailInfo.setToAddresses(addresses);
            //sms.sendHtmlMail(mailInfo);

            StringBuilder stringBuilder = new StringBuilder();
            for (String address : addresses) {
                stringBuilder.append(address).append(";");
            }
            String addressesmail = stringBuilder.toString();
            if (sms.sendHtmlMail(mailInfo)) {
                LogUtil.APP.info("��{}�Ĳ��Խ��֪ͨ�ʼ�������ɣ�", addressesmail);
            } else {
                LogUtil.APP.warn("��{}�Ĳ��Խ��֪ͨ�ʼ�����ʧ�ܣ�", addressesmail);
            }
        } else {
            LogUtil.APP.info("��ǰ������Ҫ�����ʼ�֪ͨ��");
        }
    }

    private void pushMessage(TaskScheduling taskScheduling, String content, int[] taskCount, String time, String buildStatus, String restartStatus) {
        try {
            Map<String, String> headmsg = new HashMap<>(0);
            Properties properties = SysConfig.getConfiguration();
            LogUtil.APP.info("׼����ʼ����������Ϣ���͵�����...");

            String pushUrl = taskScheduling.getPushUrl();

            if(StrUtil.isNotBlank(pushUrl)){
                String ip = properties.getProperty("server.web.ip");
                String port = properties.getProperty("server.web.port");
                String path = properties.getProperty("server.web.path");

                ProjectProtocolTemplate ppt = new ProjectProtocolTemplate();
                ppt.setEncoding("utf-8");
                ppt.setTimeout(60);
                ppt.setIsResponseHead(1);
                ppt.setIsResponseCode(1);
                HttpClientTools hct = new HttpClientTools();

                Map<String, Object> parameters = new HashMap<>(0);
                if(null != taskCount){
                    content = "LuckyFrame�Զ�����������" + taskScheduling.getSchedulingName() + "��ִ�н��\n" +
                            "�Զ�����״̬����" + buildStatus + "��\n" +
                            "�Զ�����TOMCAT״̬����" + restartStatus + "��\n" +
                            "��������Ԥ��ִ����������" + taskCount[0] + "����,�ĕr��" + time + "��\n" +
                            "����ִ�гɹ�����" + taskCount[1] + "��\n" +
                            "����ִ��ʧ�ܣ���" + taskCount[2] + "��\n" +
                            "�����п������ڽű�ԭ��δ�ɹ���������������" + taskCount[3] + "��\n" +
                            "�������ڳ�ʱ��δ�յ��ӿ�Responseδִ����ɣ���" + taskCount[4] + "��\n" +
                            "������ǰ���Զ�������ƽ̨�鿴��http://" + ip + ":" + port + path;
                }
                JSONObject contentJson = JSON.parseObject("{\"content\": \"" + content + "\"}");

                JSONObject atJson = JSON.parseObject("{\"atMobiles\": [],\"isAtAll\":true}");

                parameters.put("msgtype", "text");
                parameters.put("text", contentJson);
                parameters.put("at", atJson);
                LogUtil.APP.info("��ʼ�������ƽ̨��������ִ�����...");
                String result=hct.httpClientPostJson(pushUrl, parameters, headmsg, ppt);
                if(result.startsWith("ʹ��HttpClient��JSON��ʽ����post��������쳣")){
                    LogUtil.APP.error("�������ƽ̨��������ִ������ʧ��...����ԭ��");
                    LogUtil.APP.error("����֣�javax.net.ssl.SSLKeyException: RSA premaster secret error  �쳣��" +
                            "���ҵ����jre������lib/ext/sunjce_provider.jar���Ѵ˰��ŵ��ͻ��˱����libĿ¼�¡�");
                } else {
                    LogUtil.APP.info("�������ƽ̨��������ִ�����ݳɹ�...");
                }
            }else{
                LogUtil.APP.warn("���͵�ַ����Ϊ�գ�ȡ������������...");
            }

        } catch (Exception e) {
            LogUtil.APP.error("�������ƽ̨��������ִ����������쳣�����飡", e);
        }
    }

}
