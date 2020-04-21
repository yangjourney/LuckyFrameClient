package luckyclient.execution.appium;

import java.util.List;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import luckyclient.execution.appium.androidex.AndroidCaseExecution;
import luckyclient.remote.api.GetServerApi;
import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.ProjectCase;
import luckyclient.remote.entity.ProjectCaseParams;
import luckyclient.remote.entity.ProjectCaseSteps;
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
public class CaseLocalDebug{

	/**
	 * �����ƶ�����������
	 * @param appium appium��ʼ������
	 * @param testCaseExternalId �������
	 * @author Seagull
	 * @date 2020��1��20��
	 */
	public static void oneCasedebug(AndroidDriver<AndroidElement> appium,String testCaseExternalId){
		 //����¼��־�����ݿ�
		serverOperation.exetype = 1;  
		serverOperation caselog = new serverOperation();
		try {
			ProjectCase testcase = GetServerApi.cgetCaseBysign(testCaseExternalId);
			List<ProjectCaseParams> pcplist=GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
			LogUtil.APP.info("��ʼִ������:��{}��......",testCaseExternalId);
			List<ProjectCaseSteps> steps=GetServerApi.getStepsbycaseid(testcase.getCaseId());
			AndroidCaseExecution.caseExcution(testcase, steps, "888888", appium, caselog, pcplist);
			LogUtil.APP.info("��ǰ��������{}��ִ�����......������һ��",testcase.getCaseSign());
		} catch (Exception e) {
			LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
		}
        //�˳�
		appium.quit();
	}
	
	/**
	 * ����ƶ�����������
	 * @param appium appium����
	 * @param projectname ��Ŀ��
	 * @param addtestcase ��������
	 * @author Seagull
	 * @date 2020��1��20��
	 */
	public static void moreCaseDebug(AndroidDriver<AndroidElement> appium,String projectname,List<String> addtestcase){
		System.out.println("��ǰ���������ܹ���"+addtestcase.size());
		for(String testCaseExternalId:addtestcase) {
		    try{
		    LogUtil.APP.info("��ʼ���÷�������Ŀ��:{}���������:{}",projectname,testCaseExternalId); 
		    oneCasedebug(appium,testCaseExternalId);
		    }catch(Exception e){
				LogUtil.APP.info("�������� �����쳣���������:{}",testCaseExternalId);
			}
		}
	}

}
