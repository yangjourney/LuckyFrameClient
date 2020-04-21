package luckyclient.execution.webdriver.ex;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import luckyclient.execution.dispose.ActionManageForSteps;
import luckyclient.execution.dispose.ParamsManageForSteps;
import luckyclient.execution.httpinterface.TestCaseExecution;
import luckyclient.execution.httpinterface.analyticsteps.InterfaceAnalyticCase;
import luckyclient.execution.webdriver.BaseWebDrive;
import luckyclient.execution.webdriver.EncapsulateOperation;
import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.ProjectCase;
import luckyclient.remote.entity.ProjectCaseParams;
import luckyclient.remote.entity.ProjectCaseSteps;
import luckyclient.utils.Constants;
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
public class WebCaseExecution{
    private static Map<String, String> variable = new HashMap<>();
    private static String casenote = "��ע��ʼ��";

    public static void caseExcution(ProjectCase testcase, List<ProjectCaseSteps> steps, String taskid, WebDriver wd, serverOperation caselog, List<ProjectCaseParams> pcplist) {
    	caselog.updateTaskCaseExecuteStatus(taskid, testcase.getCaseId(), 3);
    	// �ѹ����������뵽MAP��
        for (ProjectCaseParams pcp : pcplist) {
            variable.put(pcp.getParamsName(), pcp.getParamsValue());
        }
        // ����ȫ�ֱ���
        variable.putAll(ParamsManageForSteps.GLOBAL_VARIABLE);
        // 0:�ɹ� 1:ʧ�� 2:���� ����������
        int setcaseresult = 0;
        for (ProjectCaseSteps step : steps) {
            Map<String, String> params;
            String result;

            // ���ݲ��������������������
            if (1 == step.getStepType()){
            	params = WebDriverAnalyticCase.analyticCaseStep(testcase, step, taskid, caselog, variable);
            }else{
            	params = InterfaceAnalyticCase.analyticCaseStep(testcase, step, taskid, caselog, variable);
            }

            // �жϷ�����������Ƿ����쳣
            if (null != params.get("exception") && params.get("exception").contains("�����쳣")) {
            	setcaseresult = 2;
                break;
            }

            // ���ݲ���������ִ�в���
            if (1 == step.getStepType()){
            	result = runWebStep(params, wd, taskid, testcase.getCaseId(), step.getStepSerialNumber(), caselog);
            }else{
            	TestCaseExecution testCaseExecution=new TestCaseExecution();
            	result = testCaseExecution.runStep(params, taskid, testcase.getCaseSign(), step, caselog);
            }

            String expectedResults = params.get("ExpectedResults");

            // �жϽ��
			int stepresult = judgeResult(testcase, step, params, wd, taskid, expectedResults, result, caselog);
			// ʧ�ܣ����Ҳ��ڼ���,ֱ����ֹ
            if (0 != stepresult) {
            	setcaseresult = stepresult;
                if (testcase.getFailcontinue() == 0) {
                    LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ��жϱ���������������ִ�У����뵽��һ������ִ����......",testcase.getCaseSign(),step.getStepSerialNumber());
                    break;
                } else {
                    LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ���������������������ִ�У������¸�����ִ����......",testcase.getCaseSign(),step.getStepSerialNumber());
                }
            }
        }

        variable.clear();
        caselog.updateTaskCaseExecuteStatus(taskid, testcase.getCaseId(), setcaseresult);
        if (setcaseresult == 0) {
            LogUtil.APP.info("������{}��ȫ������ִ�н���ɹ�...",testcase.getCaseSign());
            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����ȫ������ִ�н���ɹ�", "info", "ending", "");
        } else {
            LogUtil.APP.warn("������{}������ִ�й�����ʧ�ܻ�������...��鿴����ԭ��:{}",testcase.getCaseSign(),casenote);
            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����ִ�й�����ʧ�ܻ�������" + casenote, "error", "ending", "");
        }
    }

    public static String runWebStep(Map<String, String> params, WebDriver wd, String taskid, Integer caseId, int stepno, serverOperation caselog) {
        String result;
        String property;
        String propertyValue;
        String operation;
        String operationValue;

        try {
            property = params.get("property");
            propertyValue = params.get("property_value");
            operation = params.get("operation");
            operationValue = params.get("operation_value");

            LogUtil.APP.info("���ν�������������ɣ��ȴ����ж������......");
            caselog.insertTaskCaseLog(taskid, caseId, "�������:" + operation + "; ����ֵ:" + operationValue, "info", String.valueOf(stepno), "");
        } catch (Exception e) {
            LogUtil.APP.error("���ν������������׳��쳣��",e);
            return "����ִ��ʧ�ܣ���������ʧ��!";
        }

        try {
            //������һ��������֧�ֽӿڣ�web��������
            if (null != operationValue && "runcase".equals(operation)) {
                String[] temp = operationValue.split(",", -1);
                TestCaseExecution testCaseExecution=new TestCaseExecution();
                String ex = testCaseExecution.oneCaseExecuteForUICase(temp[0], taskid, caselog, wd);
                if (!ex.contains("CallCase���ó���") && !ex.contains("������������") && !ex.contains("ʧ��")) {
                    return ex;
                } else {
                    return "����ִ��ʧ�ܣ�"+ex;
                }
            }

            // ҳ��Ԫ�ز�
            if (null != property && null != propertyValue && null != operation) {
                WebElement we = isElementExist(wd, property, propertyValue);
                
                //�ж�Ԫ���Ƿ���ڹؼ���
            	if(operation.equals("iselementexist")){
                    // �жϴ�Ԫ���Ƿ����
                    if (null == we) {
                        LogUtil.APP.warn("��ȡ����ֵ�ǡ�false��");
                        return "��ȡ����ֵ�ǡ�false��";
                    }else{
                        LogUtil.APP.info("��ȡ����ֵ�ǡ�true��");
                        return "��ȡ����ֵ�ǡ�true��";
                    }
            	}
            	
                // �жϴ�Ԫ���Ƿ����
                if (null == we) {
                    LogUtil.APP.warn("��λ����ʧ�ܣ�isElementExistΪnull!");
                    return "����ִ��ʧ�ܣ���λ��Ԫ�ز����ڣ�";
                }

                //��������������Ԫ��
                BaseWebDrive.highLightElement(wd, we);
                
                if (operation.contains("select")) {
                    result = EncapsulateOperation.selectOperation(we, operation, operationValue);
                } else if (operation.contains("get")) {
                    result = EncapsulateOperation.getOperation(wd, we, operation, operationValue);
                } else if (operation.contains("mouse")) {
                    result = EncapsulateOperation.actionWeOperation(wd, we, operation, operationValue, property, propertyValue);
                } else {
                    result = EncapsulateOperation.objectOperation(wd, we, operation, operationValue, property, propertyValue);
                }
                // Driver�����
            } else if (null == property && null != operation) {
                // ���������¼�
                if (operation.contains("alert")) {
                    result = EncapsulateOperation.alertOperation(wd, operation);
                } else if (operation.contains("mouse")) {
                    result = EncapsulateOperation.actionOperation(wd, operation, operationValue);
                } else {
                    result = EncapsulateOperation.driverOperation(wd, operation, operationValue);
                }
            } else {
                LogUtil.APP.warn("Ԫ�ز�������ʧ�ܣ�");
                result = "����ִ��ʧ�ܣ�Ԫ�ز�������ʧ�ܣ�";
            }
        } catch (Exception e) {
            LogUtil.APP.error("Ԫ�ض�λ���̻��ǲ�������ʧ�ܻ��쳣��",e);
            return "����ִ��ʧ�ܣ�Ԫ�ض�λ���̻��ǲ�������ʧ�ܻ��쳣��" + e.getMessage();
        }

        if (result.contains("����ִ��ʧ�ܣ�")){
        	caselog.insertTaskCaseLog(taskid, caseId, result, "error", String.valueOf(stepno), "");
        } else{
        	caselog.insertTaskCaseLog(taskid, caseId, result, "info", String.valueOf(stepno), "");
        } 

        if (result.contains("��ȡ����ֵ�ǡ�") && result.contains("��")) {
            result = result.substring(result.indexOf("��ȡ����ֵ�ǡ�") + "��ȡ����ֵ�ǡ�".length(), result.length() - 1);
        }
        return result;

    }

    private static WebElement isElementExist(WebDriver wd, String property, String propertyValue) {
        try {
            WebElement we = null;
            property = property.toLowerCase();
            // ����WebElement����λ
            switch (property) {
                case "id":
                    we = wd.findElement(By.id(propertyValue));
                    break;
                case "name":
                    we = wd.findElement(By.name(propertyValue));
                    break;
                case "xpath":
                    we = wd.findElement(By.xpath(propertyValue));
                    break;
                case "linktext":
                    we = wd.findElement(By.linkText(propertyValue));
                    break;
                case "tagname":
                    we = wd.findElement(By.tagName(propertyValue));
                    break;
                case "cssselector":
                    we = wd.findElement(By.cssSelector(propertyValue));
                    break;
                default:
                    break;
            }

            return we;

        } catch (Exception e) {
            LogUtil.APP.error("��ǰ����λʧ�ܣ�",e);
            return null;
        }

    }

    public static int judgeResult(ProjectCase testcase, ProjectCaseSteps step, Map<String, String> params, WebDriver driver, String taskid, String expect, String result, serverOperation caselog) {
        int setresult = 0;
        java.text.DateFormat timeformat = new java.text.SimpleDateFormat("MMdd-hhmmss");
        String imagname = timeformat.format(new Date());
        
        result = ActionManageForSteps.actionManage(step.getAction(), result);
        if (null != result && !result.contains("����ִ��ʧ�ܣ�")) {
            // ��Ԥ�ڽ��
            if (null != expect && !expect.isEmpty()) {
                LogUtil.APP.info("�������Ϊ��{}��",expect);
                // ��ֵ����ģʽ
                if (expect.length() > Constants.ASSIGNMENT_SIGN.length() && expect.startsWith(Constants.ASSIGNMENT_SIGN)) {
                    variable.put(expect.substring(Constants.ASSIGNMENT_SIGN.length()), result);
                    LogUtil.APP.info("����:{} ��{}���������Խ����{}����ֵ��������{}��",testcase.getCaseSign(),step.getStepSerialNumber(),result,expect.substring(Constants.ASSIGNMENT_SIGN.length()));
                    caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�����Խ����" + result + "����ֵ��������" + expect.substring(Constants.ASSIGNMENT_SIGN.length()) + "��", "info", String.valueOf(step.getStepSerialNumber()), "");
                }
                // ��ֵȫ�ֱ���
                else if (expect.length() > Constants.ASSIGNMENT_GLOBALSIGN.length() && expect.startsWith(Constants.ASSIGNMENT_GLOBALSIGN)) {
                	variable.put(expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), result);
                	ParamsManageForSteps.GLOBAL_VARIABLE.put(expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), result);
                    LogUtil.APP.info("����:{} ��{}���������Խ����{}����ֵ��ȫ�ֱ�����{}��",testcase.getCaseSign(),step.getStepSerialNumber(),result,expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()));
                    caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�����Խ����" + result + "����ֵ��ȫ�ֱ�����" + expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()) + "��", "info", String.valueOf(step.getStepSerialNumber()), "");
                }
                // WebUI���ģʽ
                else if (1 == step.getStepType() && params.get("checkproperty") != null && params.get("checkproperty_value") != null) {
                    String checkproperty = params.get("checkproperty");
                    String checkPropertyValue = params.get("checkproperty_value");

                    WebElement we = isElementExist(driver, checkproperty, checkPropertyValue);
                    if (null != we) {
                        LogUtil.APP.info("����:{} ��{}�����ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ�гɹ���",testcase.getCaseSign(),step.getStepSerialNumber());
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ�гɹ���", "info", String.valueOf(step.getStepSerialNumber()), "");
                    } else {
                        casenote = "��" + step.getStepSerialNumber() + "����û���ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж���ִ��ʧ�ܣ�";
                        setresult = 1;
                        BaseWebDrive.webScreenShot(driver, imagname);
                        LogUtil.APP.warn("����:{} ��{}����û���ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ��ʧ�ܣ�",testcase.getCaseSign(),step.getStepSerialNumber());
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�ڵ�ǰҳ����û���ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ��ʧ�ܣ�" + "checkproperty��" + checkproperty + "��  checkproperty_value��" + checkPropertyValue + "��", "error", String.valueOf(step.getStepSerialNumber()), imagname);
                    }
                }
                // ����ƥ��ģʽ
                else {
                    // ģ��ƥ��Ԥ�ڽ��ģʽ
                    if (expect.length() > Constants.FUZZY_MATCHING_SIGN.length() && expect.startsWith(Constants.FUZZY_MATCHING_SIGN)) {
                        if (result.contains(expect.substring(Constants.FUZZY_MATCHING_SIGN.length()))) {
                            LogUtil.APP.info("����:{} ��{}����ģ��ƥ��Ԥ�ڽ���ɹ���ִ�н����{}",testcase.getCaseSign(),step.getStepSerialNumber(),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "ģ��ƥ��Ԥ�ڽ���ɹ���ִ�н����" + result, "info", String.valueOf(step.getStepSerialNumber()), "");
                        } else {
                            casenote = "��" + step.getStepSerialNumber() + "����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�";
                            setresult = 1;
                            BaseWebDrive.webScreenShot(driver, imagname);
                            LogUtil.APP.warn("����:{} ��{}����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ��:{}�����Խ��:{}",testcase.getCaseSign(),step.getStepSerialNumber(),expect.substring(Constants.FUZZY_MATCHING_SIGN.length()),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expect.substring(Constants.FUZZY_MATCHING_SIGN.length()) + "�����Խ����" + result, "error", String.valueOf(step.getStepSerialNumber()), imagname);
                        }
                    }
                    // ����ƥ��Ԥ�ڽ��ģʽ
                    else if (expect.length() > Constants.REGULAR_MATCHING_SIGN.length() && expect.startsWith(Constants.REGULAR_MATCHING_SIGN)) {
                        Pattern pattern = Pattern.compile(expect.substring(Constants.REGULAR_MATCHING_SIGN.length()));
                        Matcher matcher = pattern.matcher(result);
                        if (matcher.find()) {
                            LogUtil.APP.info("����:{} ��{}��������ƥ��Ԥ�ڽ���ɹ���ִ�н��:{}",testcase.getCaseSign(),step.getStepSerialNumber(),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����ƥ��Ԥ�ڽ���ɹ���", "info", String.valueOf(step.getStepSerialNumber()), "");
                        } else {
                            casenote = "��" + step.getStepSerialNumber() + "��������ƥ��Ԥ�ڽ��ʧ�ܣ�";
                            setresult = 1;
                            BaseWebDrive.webScreenShot(driver, imagname);
                            LogUtil.APP.warn("����:{} ��{}��������ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ��:{}�����Խ��:{}",testcase.getCaseSign(),step.getStepSerialNumber(),expect.substring(Constants.REGULAR_MATCHING_SIGN.length()),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expect.substring(Constants.REGULAR_MATCHING_SIGN.length()) + "�����Խ����" + result, "error", String.valueOf(step.getStepSerialNumber()), imagname);
                        }
                    }
                    // ��ȷƥ��Ԥ�ڽ��ģʽ
                    else {
                        if (expect.equals(result)) {
                            LogUtil.APP.info("����:{} ��{}������ȷƥ��Ԥ�ڽ���ɹ���ִ�н��:{}",testcase.getCaseSign(),step.getStepSerialNumber(),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "��ȷƥ��Ԥ�ڽ���ɹ���", "info", String.valueOf(step.getStepSerialNumber()), "");
                        } else {
                            casenote = "��" + step.getStepSerialNumber() + "������ȷƥ��Ԥ�ڽ��ʧ�ܣ�";
                            setresult = 1;
                            BaseWebDrive.webScreenShot(driver, imagname);
                            LogUtil.APP.warn("����:{} ��{}������ȷƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����:��{}��  ִ�н��:��{}��",testcase.getCaseSign(),step.getStepSerialNumber(),expect,result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "��ȷƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ���ǣ���"+expect+"��  ִ�н������"+ result+"��", "error", String.valueOf(step.getStepSerialNumber()), imagname);
                        }
                    }
                }
            }
        } else {
            casenote = (null != result) ? result : "";
            setresult = 2;
            BaseWebDrive.webScreenShot(driver, imagname);
            LogUtil.APP.warn("����:{} ��{}����ִ�н��:{}",testcase.getCaseSign(),step.getStepSerialNumber(),casenote);
            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "��ǰ������ִ�й����н���|��λԪ��|��������ʧ�ܣ�" + casenote, "error", String.valueOf(step.getStepSerialNumber()), imagname);
        }
        
        return setresult;
    }

}