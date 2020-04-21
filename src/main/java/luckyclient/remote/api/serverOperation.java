package luckyclient.remote.api;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.StrUtil;
import luckyclient.remote.entity.TaskExecute;
import luckyclient.remote.entity.TaskScheduling;
import luckyclient.utils.LogUtil;

/**
 * 
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��4��23��
 */
public class serverOperation {
	/**
	 * ����ִ�����ͣ� 0   �������ģʽ    1   ����̨ģʽ
	 */
	public static int exetype;

	/**
	 * ��������ִ��״̬ 0ͨ�� 1ʧ�� 2���� 3ִ���� 4δִ��
	 */
	public void insertTaskCaseExecute(String taskIdStr, Integer projectId,Integer caseId,  String caseSign,String caseName, Integer caseStatus) {
		if (0 == exetype) {
			Integer taskId=Integer.valueOf(taskIdStr);
			PostServerApi.clientPostInsertTaskCaseExecute(taskId, projectId, caseId, caseSign, caseName, caseStatus);
		}
	}

	/**
	 * ��������ִ��״̬ 0ͨ�� 1ʧ�� 2���� 3ִ���� 4δִ��
	 */
	public void updateTaskCaseExecuteStatus(String taskIdStr, Integer caseId, Integer caseStatus) {
		if (0 == exetype) {
			Integer taskId=Integer.valueOf(taskIdStr);
			PostServerApi.clientUpdateTaskCaseExecuteStatus(taskId, caseId, caseStatus);
		}
	}

	/**
	 * ��������ִ����־
	 */
	public void insertTaskCaseLog(String taskIdStr, Integer caseId, String logDetail, String logGrade, String logStep,
			String imgname) {
		if (0 == exetype) {
			if (logDetail.length()>5000) {
				 LogUtil.APP.info("��{}������־����{}����־��ϸ��{}��...��־��ϸ����5000�ַ����޷��������ݿ�洢��������־��ϸ��ӡ...",logStep,logGrade,logDetail);
				 logDetail="��־��ϸ����5000�ַ��޷��������ݿ⣬����LOG4J��־�д�ӡ����ǰ���鿴...";
			}
			
			Integer taskId=Integer.valueOf(taskIdStr);
			PostServerApi.clientPostInsertTaskCaseLog(taskId, caseId, logDetail, logGrade, logStep, imgname);
		}
	}

	/**
	 * ���±��������ִ��ͳ�����
	 * ״̬ 0δִ�� 1ִ���� 2ִ����� 3ִ��ʧ�� 4����ͻ���ʧ��
	 */
	public static int[] updateTaskExecuteData(String taskIdStr, int caseCount, int taskStatus) {
		int[] taskcount = null;
		if (0 == exetype) {
			Integer taskId = Integer.parseInt(taskIdStr);
			String str = PostServerApi.clientUpdateTaskExecuteData(taskId, caseCount,taskStatus);
			JSONObject jsonObject = JSONObject.parseObject(str);

			// ���ر�������ִ�����
			taskcount = new int[5];
			taskcount[0] = jsonObject.getInteger("caseCount");
			taskcount[1] = jsonObject.getInteger("caseSuc");
			taskcount[2] = jsonObject.getInteger("caseFail");
			taskcount[3] = jsonObject.getInteger("caseLock");
			taskcount[4] = jsonObject.getInteger("caseNoExec");

		}
		return taskcount;
	}

	/**
	 * ���±��������ִ��״̬
	 * ״̬ 0δִ�� 1ִ���� 2ִ����� 3ִ��ʧ�� 4����ͻ���ʧ��
	 */
	public static void updateTaskExecuteStatusIng(String taskIdStr, int caseCount) {
		if (0 == exetype) {
			Integer taskId = Integer.parseInt(taskIdStr);
			PostServerApi.clientUpdateTaskExecuteData(taskId, caseCount,1);
		}
	}

	/**
	 * ɾ����������ָ����������־��ϸ
	 */
	public static void deleteTaskCaseLog(Integer caseId, String taskIdStr) {
		Integer taskId = Integer.parseInt(taskIdStr);
		PostServerApi.clientDeleteTaskCaseLog(taskId, caseId);
	}

	/**
	 * ȡ��ָ������ID�еĲ����ڳɹ�״̬������ID
	 */
	public List<Integer> getCaseListForUnSucByTaskId(String taskIdStr) {
		int taskId = Integer.parseInt(taskIdStr);
		return GetServerApi.clientGetCaseListForUnSucByTaskId(taskId);
	}

	/**
	 * ȡ��ָ������ID�������ĵ����Ƿ�Ҫ�����ʼ�״̬���ռ��˵�ַ �����ʼ�֪ͨʱ�ľ����߼�, -1-��֪ͨ 0-ȫ����1-�ɹ���2-ʧ��
	 * ���� eMailer varchar(100) ; --�ռ���
	 */

	public static String[] getEmailAddress(String taskIdStr) {
		int taskId = Integer.parseInt(taskIdStr);
		String[] address = null;
		try {
			TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(taskId);
			if (!taskScheduling.getEmailSendCondition().equals(-1)) {
				String temp = taskScheduling.getEmailAddress();
				// ������һ��;
				if (temp.contains(";") && temp.substring(temp.length() - 1).contains(";")) {
					temp = temp.substring(0, temp.length() - 1);
				}
				// �����ַ
				if (!temp.contains("null") && temp.contains(";")) {
					address = temp.split(";", -1);
					// һ����ַ
				} else if (!temp.contains("null") && !temp.contains(";")) {
					address = new String[1];
					address[0] = temp;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("��ȡ�ʼ��ռ��˵�ַ�����쳣�����飡",e);
			return null;
		}
		return address;
	}

	/**
	 * ȡ��ָ������ID�������ĵ����Ƿ�Ҫ�Զ������Լ���������Ŀ���� Ϊ��ʱ������
	 */
	public static String[] getBuildName(String taskIdStr) {
		int taskId = Integer.parseInt(taskIdStr);
		String[] buildname = null;
		try {
			TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(taskId);
			if (StrUtil.isEmpty(taskScheduling.getBuildingLink())) {
				return null;
			}else{
				String jobName = taskScheduling.getBuildingLink();
				// ������һ��;
				if (jobName.contains(";") && jobName.substring(jobName.length() - 1).contains(";")) {
					jobName = jobName.substring(0, jobName.length() - 1);
				}
				// �������
				if (!jobName.contains("null") && jobName.contains(";")) {
					buildname = jobName.split(";", -1);
					// һ������
				} else if (!jobName.contains("null") && !jobName.contains(";")) {
					buildname = new String[1];
					buildname[0] = jobName;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("��ȡ������ַ�����쳣�����飡",e);
			return null;
		}
		return buildname;
	}

	/**
	 * ȡ��ָ������ID�������ĵ����Ƿ�Ҫ�Զ�����TOMCAT
	 * �Զ����� restartcomm varchar(200) ; -- ��ʽ��������IP;�������û���;����������;ssh�˿�;Shell����;
	 * ����192.168.222.22;pospsettle;pospsettle;22;cd
	 * /home/pospsettle/tomcat-7.0-7080/bin&&./restart.sh;
	 */

	public static String[] getRestartComm(String taskIdStr) {
		int taskId = Integer.parseInt(taskIdStr);
		String[] command = null;
		try {
			TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(taskId);
			if (null == taskScheduling.getRemoteShell() || "".equals(taskScheduling.getRemoteShell())) {
				return null;
			}else{
				String temp = taskScheduling.getRemoteShell();
				// ������һ��;
				if (temp.contains(";") && temp.substring(temp.length() - 1).contains(";")) {
					temp = temp.substring(0, temp.length() - 1);
				}
				// �������
				if (!temp.contains("null") && temp.contains(";")) {
					command = temp.split(";", -1);
					// һ������
				} else if (!temp.contains("null") && !temp.contains(";")) {
					command = new String[1];
					command[0] = temp;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("��ȡԶ��shell��ַ�����쳣�����飡",e);
			return null;
		}
		return command;

	}

	/**
	 * ��ȡ�������ʱ��
	 */
	public static String getTestTime(String taskIdStr) {
		int taskId = Integer.parseInt(taskIdStr);
		String desTime = "�������ʱ������";
		try {
			TaskExecute taskExecute = GetServerApi.cgetTaskbyid(taskId);
			Date start = taskExecute.getCreateTime();
            if (null!= taskExecute.getFinishTime()) {
                Date finish = taskExecute.getFinishTime();
                long l = finish.getTime() - start.getTime();
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                desTime = "<font color='#2828FF'>" + hour + "</font>Сʱ<font color='#2828FF'>" + min
                        + "</font>��<font color='#2828FF'>" + s + "</font>��";
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("��ȡ�������ʱ�������쳣�����飡",e);
			return desTime;
		}
		return desTime;
	}

	/**
	 * ��ѯwebִ�У����������  UI�Զ������������ 0 IE 1 ��� 2 �ȸ� 3 Edge
	 */
	public static int querydrivertype(String taskIdStr) {
		int taskId = Integer.parseInt(taskIdStr);
		int driverType = 0;
		try {
			TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(taskId);
			driverType = taskScheduling.getBrowserType();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("��ȡ��������ͳ����쳣�����飡",e);
			return driverType;
		}
		return driverType;
	}

}
