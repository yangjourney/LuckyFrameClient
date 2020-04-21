package luckyclient.execution.webdriver;

import java.util.List;

import org.openqa.selenium.WebDriver;

import luckyclient.execution.webdriver.ex.WebCaseExecution;
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
	 * ��������Web UI����
	 * @param wd ����
	 * @param testCaseExternalId �������
	 * @author Seagull
	 * @date 2020��1��20��
	 */
	public static void oneCasedebug(WebDriver wd,String testCaseExternalId){
		 //����¼��־�����ݿ�
		serverOperation.exetype = 1;  
		serverOperation caselog = new serverOperation();
		try {
			ProjectCase testcase = GetServerApi.cgetCaseBysign(testCaseExternalId);
			List<ProjectCaseParams> pcplist=GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
			LogUtil.APP.info("��ʼִ������:��{}��......",testCaseExternalId);
			List<ProjectCaseSteps> steps=GetServerApi.getStepsbycaseid(testcase.getCaseId());
			WebCaseExecution.caseExcution(testcase,steps, "888888",wd,caselog,pcplist);
			LogUtil.APP.info("��ǰ��������{}��ִ�����......������һ��",testcase.getCaseSign());
		} catch (Exception e) {
			LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
		}
        //�ر������
        wd.quit();
	}
	
	/**
	 * �����������Web UI����
	 * @param wd ����
	 * @param projectname ��Ŀ����
	 * @param addtestcase ������
	 * @author Seagull
	 * @date 2020��1��20��
	 */
	public static void moreCaseDebug(WebDriver wd,String projectname,List<String> addtestcase){
		System.out.println("��ǰ���������ܹ���"+addtestcase.size());
		for(String testCaseExternalId:addtestcase) {
		    try{
		    LogUtil.APP.info("��ʼ���÷�������Ŀ��:{}���������:{}",projectname,testCaseExternalId); 
		    oneCasedebug(wd,testCaseExternalId);
		    }catch(Exception e){
				LogUtil.APP.error("�������г����쳣���������:{}",testCaseExternalId);
			}
		}
	}

}
