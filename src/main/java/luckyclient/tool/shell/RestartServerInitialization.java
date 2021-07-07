package luckyclient.tool.shell;

import luckyclient.remote.api.serverOperation;
import luckyclient.utils.LogUtil;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 * 
 * @author�� seagull
 * @date 2017��12��1�� ����9:29:40
 * 
 */
public class RestartServerInitialization {

	public static String restartServerRun(String tastid){
		String result;
		try{
			String[] command = serverOperation.getRestartComm(tastid);
			if(command!=null){
				LogUtil.APP.info("׼������ָ����TOMCAT�����Եȡ�������������:{}",command.length);
				if(command.length==5){
					LogUtil.APP.info("��ʼ��������TOMCAT��������������0:{} ����1:{} ����2:{} ����3:{} ����4:{}",command[0],command[1],command[2],command[3],command[4]);
					result = RmtShellExecutor.sshShell(command[0], command[1], command[2], Integer.parseInt(command[3]), command[4]);
				}else{
					LogUtil.APP.warn("����TOMCAT�����в��������쳣������������Ϣ��");
					result = "����TOMCAT�����в��������쳣������������Ϣ��";
				}				
			}else{
				result = "Status:true"+" ��ǰ����û���ҵ���Ҫ������TOMCAT���";
				LogUtil.APP.info("��ǰ����û��ָ����Ҫ����TOMCAT��");
			}
		}catch(Throwable e){
			LogUtil.APP.error("����TOMCAT�����г����쳣",e);
			result = "����TOMCAT�����г����쳣";
			return result;
		}
		return result;

	}

}
