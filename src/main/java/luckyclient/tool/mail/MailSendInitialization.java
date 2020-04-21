package luckyclient.tool.mail;

import java.util.Properties;

import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.TaskScheduling;
import luckyclient.utils.LogUtil;
import luckyclient.utils.config.SysConfig;

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

    public static void sendMailInitialization(String subject, String content, String taskid, TaskScheduling taskScheduling, int[] taskCount) {
        boolean isSend = false;
        if (null == taskCount) {
            isSend = true;
        } else {
            if (taskCount.length == 5 && null != taskScheduling) {
                Integer sendCondition = taskScheduling.getEmailSendCondition();
                // ����ȫ���ɹ��˷���, casecount != casesuc
                if (null!=sendCondition&&1 == sendCondition) {
                    if (taskCount[0] == taskCount[1]) {
                        isSend = true;
                    }
                }
                // ��������ʧ���˷���
                if (null!=sendCondition&&2 == sendCondition) {
                    if (taskCount[2] > 0) {
                        isSend = true;
                    }
                }
                // ȫ��
                if (null!=sendCondition&&0 == sendCondition) {
                    isSend = true;
                }
            }
        }
        if (!isSend) {
            LogUtil.APP.info("��ǰ������Ҫ�����ʼ�֪ͨ!");
            return;
        }
        String[] addresses = serverOperation.getEmailAddress(taskid);
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
                LogUtil.APP.info("��{}�Ĳ��Խ��֪ͨ�ʼ�������ɣ�",addressesmail);
            } else {
                LogUtil.APP.warn("��{}�Ĳ��Խ��֪ͨ�ʼ�����ʧ�ܣ�",addressesmail);
            }
        } else {
            LogUtil.APP.info("��ǰ������Ҫ�����ʼ�֪ͨ��");
        }
    }

}
