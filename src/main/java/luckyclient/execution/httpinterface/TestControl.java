package luckyclient.execution.httpinterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.offbytwo.jenkins.model.BuildResult;

import luckyclient.remote.api.GetServerApi;
import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.ProjectCase;
import luckyclient.remote.entity.ProjectCaseParams;
import luckyclient.remote.entity.ProjectCaseSteps;
import luckyclient.remote.entity.TaskExecute;
import luckyclient.remote.entity.TaskScheduling;
import luckyclient.tool.jenkins.BuildingInitialization;
import luckyclient.tool.mail.HtmlMail;
import luckyclient.tool.mail.MailSendInitialization;
import luckyclient.tool.shell.RestartServerInitialization;
import luckyclient.utils.LogUtil;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull1985
 * =================================================================
 * 
 * @ClassName: TestControl
 * @Description: ����ɨ��ָ����Ŀ�������ű��������ýű��еķ��� @author�� seagull
 * @date 2014��8��24�� ����9:29:40
 * 
 */
public class TestControl {
	public static String TASKID = "NULL";
	public static int THREAD_COUNT = 0;

	/**
	 * ����̨ģʽ���ȼƻ�ִ������
	 * @param planname �ƻ�����
	 */
	public static void manualExecutionPlan(String planname) throws Exception {
		serverOperation.exetype = 1;
		int threadcount = 10;
		// �����̳߳أ����߳�ִ������
		ThreadPoolExecutor threadExecute = new ThreadPoolExecutor(threadcount, 20, 3, TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

		List<ProjectCase> testCases = GetServerApi.getCasesbyplanname(planname);
		List<ProjectCaseParams> pcplist = new ArrayList<>();
		if (testCases.size() != 0) {
			pcplist = GetServerApi.cgetParamsByProjectid(String.valueOf(testCases.get(0).getProjectId()));
		}

		String taskid = "888888";
		// ��ʼ��д��������Լ���־ģ��
		serverOperation caselog = new serverOperation();
		for (ProjectCase testcase : testCases) {
			List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
			if (steps.size() == 0) {
				LogUtil.APP.warn("������{}��û���ҵ����裬ֱ�����������飡",testcase.getCaseSign());
				caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "��������û���ҵ����裬����", "error", "1", "");
				continue;
			}
			THREAD_COUNT++; // ���̼߳���++�����ڼ���߳��Ƿ�ȫ��ִ����
			threadExecute.execute(new ThreadForExecuteCase(testcase, steps, taskid, pcplist, caselog));
		}
		// ���̼߳��������ڼ���߳��Ƿ�ȫ��ִ����
		int i = 0;
		while (THREAD_COUNT != 0) {
			i++;
			if (i > 600) {
				break;
			}
			Thread.sleep(6000);
		}
		LogUtil.APP.info("�ף�û����һ�������ҷ�����������Ѿ�ȫ��ִ����ϣ���ȥ������û��ʧ�ܵ������ɣ�");
		threadExecute.shutdown();
	}

	/**
	 * �ƻ�����ģʽ���ȼƻ�ִ������
	 * @param task �������
	 */
	public static void taskExecutionPlan(TaskExecute task) throws Exception {
		serverOperation.exetype = 0;
		String taskid = task.getTaskId().toString();
		TestControl.TASKID = taskid;
		String restartstatus = RestartServerInitialization.restartServerRun(taskid);
		BuildResult buildResult = BuildingInitialization.buildingRun(taskid);
		TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(task.getTaskId());
		String jobname = taskScheduling.getSchedulingName();
		int timeout = taskScheduling.getTaskTimeout();
		int[] tastcount;
		List<ProjectCaseParams> pcplist = GetServerApi.cgetParamsByProjectid(taskScheduling.getProjectId().toString());
		// ��ʼ��д��������Լ���־ģ��
		serverOperation caselog = new serverOperation();
		// �ж��Ƿ�Ҫ�Զ�����TOMCAT
		if (restartstatus.contains("Status:true")) {
			// �ж��Ƿ񹹽��Ƿ�ɹ�
			if (BuildResult.SUCCESS.equals(buildResult)) {
				int threadcount = taskScheduling.getExThreadCount();
				// �����̳߳أ����߳�ִ������
				ThreadPoolExecutor threadExecute = new ThreadPoolExecutor(threadcount, 20, 3, TimeUnit.SECONDS,
						new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

				List<ProjectCase> cases = GetServerApi.getCasesbyplanId(taskScheduling.getPlanId());
				LogUtil.APP.info("��ǰ�������� {} �й��С�{}��������������...",task.getTaskName(),cases.size());
				serverOperation.updateTaskExecuteStatusIng(taskid, cases.size());
				int casepriority = 0;
				for (int j = 0; j < cases.size(); j++) {
					ProjectCase projectcase = cases.get(j);
					List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(projectcase.getCaseId());
					if (steps.size() == 0) {
						caselog.insertTaskCaseExecute(taskid, taskScheduling.getProjectId(),projectcase.getCaseId(),projectcase.getCaseSign(), projectcase.getCaseName(), 2);
						LogUtil.APP.warn("������{}��û���ҵ����裬ֱ�����������飡",projectcase.getCaseSign());
						caselog.insertTaskCaseLog(taskid, projectcase.getCaseId(), "��������û���ҵ����裬����", "error", "1", "");
						continue;
					}
					// ���̼߳���,����������������ȼ�����������ȼ��ߵ�����ִ����ɣ��ż������������
					if (casepriority < projectcase.getPriority()) {
						LogUtil.APP.info("�������:{} �����������ȼ�:{} ��ǰ�������ȼ�:{}",projectcase.getCaseSign(),casepriority,projectcase.getPriority());
						int i = 0;
						while (THREAD_COUNT != 0) {
							i++;
							if (i > timeout * 60 * 5 / cases.size()) {
								LogUtil.APP.warn("�������:{} �����������ȼ�:{} ��ǰ�������ȼ�:{} �ȴ�ʱ���Ѿ��������õ�����ƽ����ʱ��{}��(���㹫ʽ������ʱʱ��*5/��������)�����ڼ�������ִ��...",projectcase.getCaseSign(),casepriority,projectcase.getPriority(),i);
								break;
							}
							Thread.sleep(1000);
						}
					}
					casepriority = projectcase.getPriority();
					THREAD_COUNT++; // ���̼߳���++�����ڼ���߳��Ƿ�ȫ��ִ����
					LogUtil.APP.info("��ʼִ�е�ǰ�������� {} �ĵڡ�{}������������...",task.getTaskName(),j+1);
					threadExecute.execute(new ThreadForExecuteCase(projectcase, steps, taskid, pcplist, caselog));
				}
				// ���̼߳��������ڼ���߳��Ƿ�ȫ��ִ����
				int i = 0;
				int taskStatus=2;
				while (THREAD_COUNT != 0) {
					i++;
					if (i > timeout * 10) {
						taskStatus=3;
						LogUtil.APP.warn("��ǰ�������� {} ִ���Ѿ��������õ��������ʱʱ�䡾{}�����ӣ����ڼ���ֹͣ����ִ��...",task.getTaskName(),timeout);
						break;
					}
					Thread.sleep(6000);
				}
				tastcount = serverOperation.updateTaskExecuteData(taskid, cases.size(),taskStatus);

				String testtime = serverOperation.getTestTime(taskid);
				MailSendInitialization.sendMailInitialization(HtmlMail.htmlSubjectFormat(jobname),
						HtmlMail.htmlContentFormat(tastcount, taskid, buildResult.toString(), restartstatus, testtime, jobname),
						taskid, taskScheduling, tastcount,testtime,buildResult.toString(),restartstatus);
				threadExecute.shutdown();
				LogUtil.APP.info("�ף�û����һ�������ҷ�����������Ѿ�ȫ��ִ����ϣ���ȥ������û��ʧ�ܵ������ɣ�");
			} else {
				LogUtil.APP.warn("��Ŀ����ʧ�ܣ��Զ��������Զ��˳�����鿴������־�����Ŀ�������...");
				MailSendInitialization.sendMailInitialization(jobname, "������Ŀ������ʧ�ܣ��Զ��������Զ��˳�����鿴������־�����Ŀ�������...", taskid,
						taskScheduling, null,"0Сʱ0��0��",buildResult.toString(),restartstatus);
			}
		} else {
			LogUtil.APP.warn("��ĿTOMCAT����ʧ�ܣ��Զ��������Զ��˳���������ĿTOMCAT���������");
			MailSendInitialization.sendMailInitialization(jobname, "��ĿTOMCAT����ʧ�ܣ��Զ��������Զ��˳���������ĿTOMCAT���������", taskid,
					taskScheduling, null,"0Сʱ0��0��",buildResult.toString(),restartstatus);
		}
	}

}
