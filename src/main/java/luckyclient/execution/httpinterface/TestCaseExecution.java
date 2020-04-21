package luckyclient.execution.httpinterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import luckyclient.driven.SubString;
import luckyclient.execution.appium.AppDriverAnalyticCase;
import luckyclient.execution.appium.androidex.AndroidCaseExecution;
import luckyclient.execution.appium.iosex.IosCaseExecution;
import luckyclient.execution.dispose.ActionManageForSteps;
import luckyclient.execution.dispose.ParamsManageForSteps;
import luckyclient.execution.httpinterface.analyticsteps.InterfaceAnalyticCase;
import luckyclient.execution.webdriver.ex.WebCaseExecution;
import luckyclient.execution.webdriver.ex.WebDriverAnalyticCase;
import luckyclient.remote.api.GetServerApi;
import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.ProjectCase;
import luckyclient.remote.entity.ProjectCaseParams;
import luckyclient.remote.entity.ProjectCaseSteps;
import luckyclient.utils.Constants;
import luckyclient.utils.InvokeMethod;
import luckyclient.utils.LogUtil;

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
public class TestCaseExecution {
    private Map<String, String> VARIABLE = new HashMap<>(0);

    /**
     * ���ڵ����������ԣ���ͨ����־���д��־��UTP�ϣ�����UTP�ϵ�����������
     */
    public void oneCaseExecuteForTask(Integer caseId, String taskid) {
        TestControl.TASKID = taskid;
        serverOperation.exetype = 0;
        // ��ʼ��д��������Լ���־ģ��
        serverOperation caselog = new serverOperation();
        String packagename;
        String functionname;
        String expectedresults;
        int setcaseresult = 0;
        Object[] getParameterValues;
        String testnote = "��ʼ�����Խ��";
        int k = 0;
        ProjectCase testcase = GetServerApi.cGetCaseByCaseId(caseId);
        //��������״̬
        caselog.updateTaskCaseExecuteStatus(taskid, testcase.getCaseId(), 3);
        // ɾ���ɵ���־
        serverOperation.deleteTaskCaseLog(testcase.getCaseId(), taskid);

        List<ProjectCaseParams> pcplist = GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
        // �ѹ����������뵽MAP��
        for (ProjectCaseParams pcp : pcplist) {
        	VARIABLE.put(pcp.getParamsName(), pcp.getParamsValue());
        }
        // ����ȫ�ֱ���
        VARIABLE.putAll(ParamsManageForSteps.GLOBAL_VARIABLE);
        List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
        if (steps.size() == 0) {
            setcaseresult = 2;
            LogUtil.APP.warn("������δ�ҵ����裬���飡");
            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "������δ�ҵ����裬���飡", "error", "1", "");
            testnote = "������δ�ҵ����裬���飡";
        }
        // ����ѭ���������������в���
        for (int i = 0; i < steps.size(); i++) {
            Map<String, String> casescript = InterfaceAnalyticCase.analyticCaseStep(testcase, steps.get(i), taskid, caselog,VARIABLE);
            try {
                packagename = casescript.get("PackageName");
                functionname = casescript.get("FunctionName");
            } catch (Exception e) {
                LogUtil.APP.error("����:{} �����������Ƿ�����ʧ�ܣ����飡",testcase.getCaseSign(),e);
                caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�����������Ƿ�����ʧ�ܣ����飡", "error", String.valueOf(i + 1), "");
                break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
            }
            // �������ƽ��������쳣���ǵ���������������쳣
            if ((null != functionname && functionname.contains("�����쳣")) || k == 1) {
                testnote = "������" + (i + 1) + "��������������";
                break;
            }
            expectedresults = casescript.get("ExpectedResults");
            // �жϷ����Ƿ������
            if (casescript.size() > 4) {
                // ��ȡ�����������������У���ʼ�������������
                getParameterValues = new Object[casescript.size() - 4];
                for (int j = 0; j < casescript.size() - 4; j++) {
                    if (casescript.get("FunctionParams" + (j + 1)) == null) {
                        k = 1;
                        break;
                    }

                    String parameterValues = casescript.get("FunctionParams" + (j + 1));
                    LogUtil.APP.info("����:{} ��������:{} ������:{} ��{}������:{}",testcase.getCaseSign(),packagename,functionname,(j+1),parameterValues);
                    caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����������" + packagename + " ��������" + functionname + " ��" + (j + 1) + "��������" + parameterValues, "info", String.valueOf(i + 1), "");
                    getParameterValues[j] = parameterValues;
                }
            } else {
                getParameterValues = null;
            }
            // ���ö�̬������ִ�в�������
            try {
                LogUtil.APP.info("��ʼ���÷���:{} .....",functionname);
                caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "��ʼ���÷�����" + functionname + " .....", "info", String.valueOf(i + 1), "");
                testnote = InvokeMethod.callCase(packagename, functionname, getParameterValues, steps.get(i).getStepType(), steps.get(i).getExtend());
                testnote = ActionManageForSteps.actionManage(casescript.get("Action"), testnote);
                // �жϽ��
                int stepresult = interfaceJudgeResult(testcase, steps.get(i), taskid, expectedresults, testnote, caselog);
    			// ʧ�ܣ����Ҳ��ڼ���,ֱ����ֹ
                if (0 != stepresult) {
                	setcaseresult = stepresult;
                    if (testcase.getFailcontinue() == 0) {
                        LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ��жϱ���������������ִ�У����뵽��һ������ִ����......",testcase.getCaseSign(),steps.get(i).getStepSerialNumber());
                        break;
                    } else {
                        LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ���������������������ִ�У������¸�����ִ����......",testcase.getCaseSign(),steps.get(i).getStepSerialNumber());
                    }
                }

            } catch (Exception e) {
                caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "���÷������̳�����������" + functionname + " �����¼��ű����������Լ�������", "error", String.valueOf(i + 1), "");
                LogUtil.APP.error("���÷������̳���������:{} �����¼��ű����������Լ�������",functionname, e);
                testnote = "CallCase���ó���";
                setcaseresult = 1;
                e.printStackTrace();
                if (testcase.getFailcontinue() == 0) {
                    LogUtil.APP.error("������{}���ڡ�{}������ִ��ʧ�ܣ��жϱ���������������ִ�У����뵽��һ������ִ����......",testcase.getCaseSign(),(i+1));
                    break;
                } else {
                    LogUtil.APP.error("������{}���ڡ�{}������ִ��ʧ�ܣ���������������������ִ�У������¸�����ִ����......",testcase.getCaseSign(),(i+1));
                }
            }
        }

        VARIABLE.clear(); // ��մ���MAP
        // ������÷���������δ�����������ò��Խ������
        if (!testnote.contains("CallCase���ó���") && !testnote.contains("������������")) {
            LogUtil.APP.info("����{}�����ɹ������ɹ����������з�����������鿴ִ�н����",testcase.getCaseSign());
            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�����ɹ������ɹ����������з�����������鿴ִ�н����", "info", "SETCASERESULT...", "");
            caselog.updateTaskCaseExecuteStatus(taskid, testcase.getCaseId(), setcaseresult);
        } else {
            setcaseresult = 1;
            LogUtil.APP.warn("����{}�������ǵ��ò����еķ�������",testcase.getCaseSign());
            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�������ǵ��ò����еķ�������", "error", "SETCASERESULT...", "");
            caselog.updateTaskCaseExecuteStatus(taskid, testcase.getCaseId(), 2);
        }
        if (0 == setcaseresult) {
            LogUtil.APP.info("����{}����ȫ��ִ�гɹ���",testcase.getCaseSign());
            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����ȫ��ִ�гɹ���", "info", "EXECUTECASESUC...", "");
        } else {
            LogUtil.APP.warn("����{}��ִ�й�����ʧ�ܣ�������־��",testcase.getCaseSign());
            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "��ִ�й�����ʧ�ܣ�������־��", "error", "EXECUTECASESUC...", "");
        }
        serverOperation.updateTaskExecuteData(taskid, 0, 2);
    }

    /**
     * �ṩ��Web�����У�runcase��ʱ��ʹ��
     * @param testCaseExternalId �������
     * @param taskid ����ID
     * @param caselog ������־����
     * @param driver UI����
     * @return ����ִ�н��
     */
    @SuppressWarnings("unchecked")
	public String oneCaseExecuteForUICase(String testCaseExternalId, String taskid, serverOperation caselog, Object driver) {
        String expectedresults;
        int setresult = 1;
        String testnote = "��ʼ�����Խ��";
        ProjectCase testcase = GetServerApi.cgetCaseBysign(testCaseExternalId);
        List<ProjectCaseParams> pcplist = GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
        // �ѹ����������뵽MAP��
        for (ProjectCaseParams pcp : pcplist) {
        	VARIABLE.put(pcp.getParamsName(), pcp.getParamsValue());
        }
        // ����ȫ�ֱ���
        VARIABLE.putAll(ParamsManageForSteps.GLOBAL_VARIABLE);
        List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
        if (steps.size() == 0) {
            setresult = 2;
            LogUtil.APP.warn("������δ�ҵ����裬���飡");
            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "������δ�ҵ����裬���飡", "error", "1", "");
            testnote = "������δ�ҵ����裬���飡";
        }

        // ����ѭ���������������в���
        for (ProjectCaseSteps step : steps) {
            Map<String, String> params;

            // ���ݲ��������������������
            if (1 == step.getStepType()){
            	params = WebDriverAnalyticCase.analyticCaseStep(testcase, step, taskid, caselog,VARIABLE);
            }else if (3 == step.getStepType()){
            	params = AppDriverAnalyticCase.analyticCaseStep(testcase, step, taskid,caselog,VARIABLE);
            } else{
            	params = InterfaceAnalyticCase.analyticCaseStep(testcase, step, taskid, caselog,VARIABLE);
            }

            // �жϷ�����������Ƿ����쳣
            if (params.get("exception") != null && params.get("exception").contains("�����쳣")) {
                setresult = 2;
                break;
            }

            expectedresults = params.get("ExpectedResults");

            // ���ݲ���������ִ�в���
            if (1 == step.getStepType()){
            	WebDriver wd=(WebDriver)driver;
            	testnote = WebCaseExecution.runWebStep(params, wd, taskid, testcase.getCaseId(), step.getStepSerialNumber(), caselog);
                // �жϽ��
                setresult = WebCaseExecution.judgeResult(testcase, step, params, wd, taskid, expectedresults, testnote, caselog);
            }else if (3 == step.getStepType()){
            	if (driver instanceof AndroidDriver){
            		AndroidDriver<AndroidElement> ad=(AndroidDriver<AndroidElement>)driver;
            		testnote = AndroidCaseExecution.androidRunStep(params, ad, taskid, testcase.getCaseId(), step.getStepSerialNumber(), caselog);
            		// �жϽ��
                    setresult = AndroidCaseExecution.judgeResult(testcase, step, params, ad, taskid, expectedresults, testnote, caselog);
            	}else{
            		IOSDriver<IOSElement> ios=(IOSDriver<IOSElement>)driver;
            		testnote = IosCaseExecution.iosRunStep(params, VARIABLE, ios, taskid, testcase.getCaseId(), step.getStepSerialNumber(), caselog);
            		// �жϽ��
                    setresult = IosCaseExecution.judgeResult(testcase, step, params, ios, taskid, expectedresults, testnote, caselog);
            	}

            } else{
            	testnote = runStep(params, taskid, testcase.getCaseSign(), step, caselog);
            	// �жϽ��
            	setresult = interfaceJudgeResult(testcase, step, taskid, expectedresults, testnote, caselog);
            }

            if (0 != setresult){
            	testnote = "����������:"+testcase.getCaseSign()+" ��"+step.getStepSerialNumber()+"����ִ�й�����ʧ�ܡ�";
            	LogUtil.APP.warn("��������:{} ��{}����ִ�й�����ʧ�ܣ�������־��{}",testcase.getCaseSign(),step.getStepSerialNumber(),testnote);
            	break;
            }
        }

        VARIABLE.clear(); // ��մ���MAP
        if (0 == setresult) {
            LogUtil.APP.info("��������:{}����ȫ��ִ�гɹ���",testcase.getCaseSign());
        }
        
        return testnote;
    }

    /**
     * �������Ͳ��������е��ýӿڲ��Բ���
     * @param params ����
     * @param taskid ����ID
     * @param casenum �������
     * @param step �������
     * @param caselog ��־����
     * @return ����ִ�н��
     */
    public String runStep(Map<String, String> params, String taskid, String casenum, ProjectCaseSteps step, serverOperation caselog) {
        String result;
        String packagename;
        String functionname = "";
        Object[] getParameterValues;
        ProjectCase projectCase = GetServerApi.cgetCaseBysign(casenum);
        try {
            packagename = params.get("PackageName");
            functionname = params.get("FunctionName");

            if (null != functionname && functionname.contains("�����쳣")) {
                LogUtil.APP.warn("����:{}, �������������{}��ʧ�ܣ�",casenum,functionname);
                caselog.insertTaskCaseLog(taskid, projectCase.getCaseId(), "����: " + casenum + ", �������������" + functionname + "��ʧ�ܣ�", "error", String.valueOf(step.getStepSerialNumber()), "");
                result = "����ִ��ʧ�ܣ���������ʧ��!";
            } else {
                // �жϷ����Ƿ������
                if (params.size() > 4) {
                    // ��ȡ������������������
                    getParameterValues = new Object[params.size() - 4];
                    for (int j = 0; j < params.size() - 4; j++) {
                        if (params.get("FunctionParams" + (j + 1)) == null) {
                            break;
                        }
                        String parameterValues = params.get("FunctionParams" + (j + 1));
                        LogUtil.APP.info("����:{}, ������·��:{}; ������:{} ��{}������:{}",casenum,packagename,functionname,(j+1),parameterValues);
                        caselog.insertTaskCaseLog(taskid, projectCase.getCaseId(), "����: " + casenum + ", ����������" + packagename + " ��������" + functionname + " ��" + (j + 1) + "��������" + parameterValues, "info", String.valueOf(step.getStepSerialNumber()), "");
                        getParameterValues[j] = parameterValues;
                    }
                } else {
                    getParameterValues = null;
                }

                LogUtil.APP.info("���ν�������������ɣ��ȴ����нӿڲ���......");
                caselog.insertTaskCaseLog(taskid, projectCase.getCaseId(), "��·��: " + packagename + "; ������: " + functionname, "info", String.valueOf(step.getStepSerialNumber()), "");

                result = InvokeMethod.callCase(packagename, functionname, getParameterValues, step.getStepType(), step.getExtend());
            }
        } catch (Exception e) {
            LogUtil.APP.error("���÷������̳���������:{}�������¼��ű����������Լ�������",functionname,e);
            result = "����ִ��ʧ�ܣ��ӿڵ��ó���";
        }
        if (result.contains("����ִ��ʧ�ܣ�")){
        	caselog.insertTaskCaseLog(taskid, projectCase.getCaseId(), result, "error", String.valueOf(step.getStepSerialNumber()), "");
        } else{
        	caselog.insertTaskCaseLog(taskid, projectCase.getCaseId(), result, "info", String.valueOf(step.getStepSerialNumber()), "");
        }
        return result;
    }

    private int interfaceJudgeResult(ProjectCase testcase, ProjectCaseSteps step, String taskid, String expectedresults, String testnote, serverOperation caselog){
        int setresult = 0;
        try{
        	if (null != expectedresults && !expectedresults.isEmpty()) {
                LogUtil.APP.info("expectedResults=��{}��",expectedresults);
                // ��ֵ����
                if (expectedresults.length() > Constants.ASSIGNMENT_SIGN.length() && expectedresults.startsWith(Constants.ASSIGNMENT_SIGN)) {
                	VARIABLE.put(expectedresults.substring(Constants.ASSIGNMENT_SIGN.length()), testnote);
                    LogUtil.APP.info("����:{} ��{}���������Խ����{}����ֵ��������{}��",testcase.getCaseSign(),step.getStepSerialNumber(),testnote,expectedresults.substring(Constants.ASSIGNMENT_SIGN.length()));
                    caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�����Խ����" + testnote + "����ֵ��������" + expectedresults.substring(Constants.ASSIGNMENT_SIGN.length()) + "��", "info", String.valueOf(step.getStepSerialNumber()), "");
                }
                // ��ֵȫ�ֱ���
                else if (expectedresults.length() > Constants.ASSIGNMENT_GLOBALSIGN.length() && expectedresults.startsWith(Constants.ASSIGNMENT_GLOBALSIGN)) {
                	VARIABLE.put(expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), testnote);
                    ParamsManageForSteps.GLOBAL_VARIABLE.put(expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), testnote);
                    LogUtil.APP.info("����:{} ��{}���������Խ����{}����ֵ��ȫ�ֱ�����{}��",testcase.getCaseSign(),step.getStepSerialNumber(),testnote,expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()));
                    caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�����Խ����" + testnote + "����ֵ��ȫ�ֱ�����" + expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()) + "��", "info", String.valueOf(step.getStepSerialNumber()), "");
                }
                // ģ��ƥ��
                else if (expectedresults.length() > Constants.FUZZY_MATCHING_SIGN.length() && expectedresults.startsWith(Constants.FUZZY_MATCHING_SIGN)) {
                    if (testnote.contains(expectedresults.substring(Constants.FUZZY_MATCHING_SIGN.length()))) {
                        LogUtil.APP.info("����:{} ��{}����ģ��ƥ��Ԥ�ڽ���ɹ���ִ�н��:{}",testcase.getCaseSign(),step.getStepSerialNumber(),testnote);
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "ģ��ƥ��Ԥ�ڽ���ɹ���ִ�н����" + testnote, "info", String.valueOf(step.getStepSerialNumber()), "");
                    } else {
                    	setresult = 1;
                        LogUtil.APP.warn("����:{} ��{}����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ��:{}�����Խ��:{}",testcase.getCaseSign(),step.getStepSerialNumber(),expectedresults.substring(Constants.FUZZY_MATCHING_SIGN.length()),testnote);
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults.substring(Constants.FUZZY_MATCHING_SIGN.length()) + "�����Խ����" + testnote, "error", String.valueOf(step.getStepSerialNumber()), "");
                    }
                }
                // ����ƥ��
                else if (expectedresults.length() > Constants.REGULAR_MATCHING_SIGN.length() && expectedresults.startsWith(Constants.REGULAR_MATCHING_SIGN)) {
                    Pattern pattern = Pattern.compile(expectedresults.substring(Constants.REGULAR_MATCHING_SIGN.length()));
                    Matcher matcher = pattern.matcher(testnote);
                    if (matcher.find()) {
                        LogUtil.APP.info("����:{} ��{}��������ƥ��Ԥ�ڽ���ɹ���ִ�н��:{}",testcase.getCaseSign(),step.getStepSerialNumber(),testnote);
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����ƥ��Ԥ�ڽ���ɹ���ִ�н����" + testnote, "info", String.valueOf(step.getStepSerialNumber()), "");
                    } else {
                        setresult = 1;
                        LogUtil.APP.warn("����:{} ��{}��������ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ��:{}�����Խ��:{}",testcase.getCaseSign(),step.getStepSerialNumber(),expectedresults.substring(Constants.REGULAR_MATCHING_SIGN.length()),testnote);
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults.substring(Constants.REGULAR_MATCHING_SIGN.length()) + "�����Խ����" + testnote, "error", String.valueOf(step.getStepSerialNumber()), "");
                    }
                }
                //jsonpath����
                else if (expectedresults.length() > Constants.JSONPATH_SIGN.length() && expectedresults.startsWith(Constants.JSONPATH_SIGN)) {
                    expectedresults = expectedresults.substring(Constants.JSONPATH_SIGN.length());
                    String expression = expectedresults.split("(?<!\\\\)=")[0].replace("\\=","=");
                    String exceptResult = expectedresults.split("(?<!\\\\)=")[1].replace("\\=","=");
                    //�Բ��Խ������jsonPathȡֵ
                    String result = SubString.jsonPathGetParams(expression, testnote);
                    
                    if (exceptResult.equals(result)) {
                        setresult = 0;
                        LogUtil.APP.info("����:{} ��{}����jsonpath����Ԥ�ڽ���ɹ���Ԥ�ڽ��:{} ���Խ��: {} ִ�н��:true",testcase.getCaseSign(),step.getStepSerialNumber(),exceptResult,result);
                    } else {
                        setresult = 1;
                        LogUtil.APP.warn("����:{} ��{}����jsonpath����Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ��:{}�����Խ��:{}" + expectedresults + "�����Խ����" + result, "error", step.getStepSerialNumber(), "");
                        // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
                    }

                }
                // ��ȫ���
                else {
                    if (expectedresults.equals(testnote)) {
                        LogUtil.APP.info("����:{} ��{}������ȷƥ��Ԥ�ڽ���ɹ���ִ�н��:{}",testcase.getCaseSign(),step.getStepSerialNumber(),testnote);
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "��ȷƥ��Ԥ�ڽ���ɹ���ִ�н����" + testnote, "info", String.valueOf(step.getStepSerialNumber()), "");
                    } else {
                        setresult = 1;
                        LogUtil.APP.warn("����:{} ��{}������ȷƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ��:{}�����Խ��:{}",testcase.getCaseSign(),step.getStepSerialNumber(),expectedresults,testnote);
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "��ȷƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults + "�����Խ����" + testnote, "error", String.valueOf(step.getStepSerialNumber()), "");
                    }
                }
            }
        }catch(Exception e){
        	LogUtil.APP.error("ƥ��ӿ�Ԥ�ڽ�������쳣��",e);
        	setresult = 2;
        	return setresult;
        }
        return setresult;
    }


}
