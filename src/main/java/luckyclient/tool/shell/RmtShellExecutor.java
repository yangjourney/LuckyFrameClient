package luckyclient.tool.shell;

import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import luckyclient.utils.LogUtil;

/**
 * Զ��ִ��shell�ű���
 *
 * @author l
 */
public class RmtShellExecutor {
    /**
     * ����JSch��ʵ��Զ������SHELL����ִ��
     *
     * @param ip      ����IP
     * @param user    ������½�û���
     * @param psw     ������½����
     * @param port    ����ssh2��½�˿ڣ����ȡĬ��ֵ����-1
     * @param command Shell����   cd /home/pospsettle/tomcat-7.0-7080/bin&&./restart.sh
     */
    public static String sshShell(String ip, String user, String psw
            , int port, String command) {

        Session session = null;
        Channel channel = null;
        String result = "Status:true" + " ��������ִ�гɹ���";
        try {
            JSch jsch = new JSch();
            LogUtil.APP.info("���뵽����TOMCAT����...");

            if (port <= 0) {
                //���ӷ�����������Ĭ�϶˿�
                LogUtil.APP.info("��������TOMCAT������IP��Ĭ�϶˿�...");
                session = jsch.getSession(user, ip);
            } else {
                //����ָ���Ķ˿����ӷ�����
                LogUtil.APP.info("��������TOMCAT������IP���˿�...");
                session = jsch.getSession(user, ip, port);
                LogUtil.APP.info("��������TOMCAT������IP���˿����!");
            }

            //������������Ӳ��ϣ����׳��쳣
            if (session == null) {
                LogUtil.APP.warn("����TOMCAT�����У����ӷ�����session is null");
                throw new Exception("session is null");
            }
            //���õ�½����������
            session.setPassword(psw);
            //���õ�һ�ε�½��ʱ����ʾ����ѡֵ��(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //���õ�½��ʱʱ��
            session.connect(30000);

            //����sftpͨ��ͨ��
            channel = session.openChannel("shell");
            channel.connect(1000);

            //��ȡ�������������
            InputStream instream = channel.getInputStream();
            OutputStream outstream = channel.getOutputStream();

            //������Ҫִ�е�SHELL�����Ҫ��\n��β����ʾ�س�
            LogUtil.APP.info("׼��������TOMCAT��������������!");
            String shellCommand = command + "  \n";
            outstream.write(shellCommand.getBytes());
            outstream.flush();

            Thread.sleep(10000);
            //��ȡ����ִ�еĽ��
            if (instream.available() > 0) {
                byte[] data = new byte[instream.available()];
                int nLen = instream.read(data);
                if (nLen < 0) {
                    LogUtil.APP.warn("����TOMCAT�����У���ȡ����ִ�н�������쳣��");
                }

                //ת������������ӡ����
                String temp = new String(data, 0, nLen, "iso8859-1");
                LogUtil.APP.info("��ʼ��ӡ����TOMCAT����ִ�н��...{}", temp);
            }
            outstream.close();
            instream.close();
        } catch (Exception e) {
            result = "����TOMCAT�����У������쳣��";
            LogUtil.APP.error("����TOMCAT�����У������쳣��", e);
            return result;
        } finally {
            if (null != session) {
                session.disconnect();
            }
            if (null != channel) {
                channel.disconnect();
            }

        }
        return result;
    }

}