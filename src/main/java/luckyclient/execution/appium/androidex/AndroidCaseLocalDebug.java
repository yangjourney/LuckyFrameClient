package luckyclient.execution.appium.androidex;

import java.util.List;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import luckyclient.remote.api.GetServerApi;
import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.ProjectCase;
import luckyclient.remote.entity.ProjectCaseParams;
import luckyclient.remote.entity.ProjectCaseSteps;
import luckyclient.utils.LogUtil;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull1985
 * =================================================================
 * 
 * @author seagull
 * @date 2018��1��29��
 * 
 */
public class AndroidCaseLocalDebug {

	/**
	 * IDEA���߷�ʽ���Բ�������
	 * @param androiddriver ��ʼ��appium����
	 * @param testCaseExternalId �������
	 */
	public static void oneCasedebug(AndroidDriver<AndroidElement> androiddriver, String testCaseExternalId) {
		// ����¼��־�����ݿ�
		serverOperation.exetype = 1;
		serverOperation caselog = new serverOperation();

		try {
			ProjectCase testcase = GetServerApi.cgetCaseBysign(testCaseExternalId);
			List<ProjectCaseParams> pcplist = GetServerApi
					.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
			LogUtil.APP.info("��ʼִ����������{}��......",testCaseExternalId);
			List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
			AndroidCaseExecution.caseExcution(testcase, steps, "888888", androiddriver, caselog, pcplist);

			LogUtil.APP.info("��ǰ��������{}��ִ�����......������һ��",testcase.getCaseSign());
		} catch (Exception e) {
			LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
		}
	}

	/**
	 * �����������������е���
	 * @param androiddriver ��ʼ��appium����
	 * @param projectname ��Ŀ����
	 * @param addtestcase ������
	 * @author Seagull
	 * @date 2019��4��18��
	 */
	public static void moreCaseDebug(AndroidDriver<AndroidElement> androiddriver, String projectname,
			List<String> addtestcase) {
		System.out.println("��ǰ���������ܹ���"+addtestcase.size());
		for(String testCaseExternalId:addtestcase) {
			try {
				LogUtil.APP
						.info("��ʼ���÷�������Ŀ����{}��������ţ�{}",projectname,testCaseExternalId);
				oneCasedebug(androiddriver, testCaseExternalId);
			} catch (Exception e) {
				LogUtil.APP.error("���������Թ������׳��쳣��", e);
			}
		}
		// �ر�APP�Լ�appium�Ự
		androiddriver.closeApp();
	}

}
