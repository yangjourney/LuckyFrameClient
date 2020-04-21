package luckyclient.execution.appium.androidex;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import luckyclient.execution.appium.AppDriverAnalyticCase;
import luckyclient.execution.dispose.ActionManageForSteps;
import luckyclient.execution.dispose.ChangString;
import luckyclient.execution.dispose.ParamsManageForSteps;
import luckyclient.execution.httpinterface.TestCaseExecution;
import luckyclient.execution.httpinterface.analyticsteps.InterfaceAnalyticCase;
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
 * @author seagull
 * @date 2018��1��21�� ����15:12:48
 */
public class AndroidCaseExecution{
	static Map<String, String> variable = new HashMap<>();
    private static String casenote = "��ע��ʼ��";

	public static void caseExcution(ProjectCase testcase, List<ProjectCaseSteps> steps,String taskid, AndroidDriver<AndroidElement> appium,serverOperation caselog,List<ProjectCaseParams> pcplist) {
		caselog.updateTaskCaseExecuteStatus(taskid, testcase.getCaseId(), 3);
		// �ѹ����������뵽MAP��
		for (ProjectCaseParams pcp : pcplist) {
			variable.put(pcp.getParamsName(), pcp.getParamsValue());
		}
		 // ����ȫ�ֱ���
        variable.putAll(ParamsManageForSteps.GLOBAL_VARIABLE);
	    // 0ͨ�� 1ʧ�� 2���� 3ִ���� 4δִ��
	    int setcaseresult = 0;
		for (ProjectCaseSteps step : steps) {
            Map<String, String> params;
            String result;

            // ���ݲ��������������������
            if (3 == step.getStepType()){
            	params = AppDriverAnalyticCase.analyticCaseStep(testcase, step, taskid,caselog,variable);
            }else{
            	params = InterfaceAnalyticCase.analyticCaseStep(testcase, step, taskid, caselog,variable);
            }
			
			if(null != params.get("exception") && params.get("exception").contains("�����쳣")){
				setcaseresult = 2;
				break;
			}
			
            // ���ݲ���������ִ�в���
            if (3 == step.getStepType()){
            	result = androidRunStep(params, appium, taskid, testcase.getCaseId(), step.getStepSerialNumber(), caselog);
            }else{
            	TestCaseExecution testCaseExecution=new TestCaseExecution();
            	result = testCaseExecution.runStep(params, taskid, testcase.getCaseSign(), step, caselog);
            }

			String expectedResults = params.get("ExpectedResults");
			expectedResults=ChangString.changparams(expectedResults, variable,"Ԥ�ڽ��");
			
            // �жϽ��
			int stepresult = judgeResult(testcase, step, params, appium, taskid, expectedResults, result, caselog);
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
		if(setcaseresult==0){
			LogUtil.APP.info("������{}��ȫ������ִ�н���ɹ�...",testcase.getCaseSign());
	        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����ȫ������ִ�н���ɹ�","info", "ending","");
		}else{
			LogUtil.APP.warn("������{}������ִ�й�����ʧ�ܻ�������...��鿴����ԭ�򣡡�{}��",testcase.getCaseSign(),casenote);
	        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����ִ�й�����ʧ�ܻ�������"+casenote,"error", "ending","");
		}
		//LogOperation.UpdateTastdetail(taskid, 0);
	}

	public static String androidRunStep(Map<String, String> params, AndroidDriver<AndroidElement> appium,String taskid,Integer caseId,int stepno,serverOperation caselog) {
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
			caselog.insertTaskCaseLog(taskid, caseId, "�������:"+operation+"; ����ֵ:"+operationValue,"info", String.valueOf(stepno),"");
		} catch (Exception e) {
			LogUtil.APP.error("���ν������������׳��쳣��",e);
			return "����ִ��ʧ�ܣ���������ʧ��!";
		}

		try {		
			//���ýӿ�����
			if(null != operationValue && "runcase".equals(operation)){
				String[] temp=operationValue.split(",",-1);
				TestCaseExecution testCaseExecution=new TestCaseExecution();
				String ex = testCaseExecution.oneCaseExecuteForUICase(temp[0], taskid, caselog, appium);
				if(!ex.contains("CallCase���ó���") && !ex.contains("������������") && !ex.contains("ʧ��")){
					return ex;
				}else{
					return "����ִ��ʧ�ܣ����ýӿ���������ʧ��";
				}
			}
			
			AndroidElement ae;
			// ҳ��Ԫ�ز�
			if (null != property && null != propertyValue) { 
				ae = isElementExist(appium, property, propertyValue);
				// �жϴ�Ԫ���Ƿ����
				if (null==ae) {
					LogUtil.APP.warn("��λ����ʧ�ܣ�isElementExistΪnull!");
					return "����ִ��ʧ�ܣ�isElementExist��λԪ�ع���ʧ�ܣ�";
				}

				if (operation.contains("select")) {
					result = AndroidEncapsulateOperation.selectOperation(ae, operation, operationValue);
				} else if (operation.contains("get")){
					result = AndroidEncapsulateOperation.getOperation(ae, operation,operationValue);
				} else {
					result = AndroidEncapsulateOperation.objectOperation(appium, ae, operation, operationValue, property, propertyValue);
				}
				// Driver�����
			} else if (null==property && null != operation) { 				
				// ���������¼�
				if (operation.contains("alert")){
					result = AndroidEncapsulateOperation.alertOperation(appium, operation);
				}else{
					result = AndroidEncapsulateOperation.driverOperation(appium, operation, operationValue);
				} 				
			}else{
				LogUtil.APP.warn("Ԫ�ز�������ʧ�ܣ�");
				result =  "����ִ��ʧ�ܣ�Ԫ�ز�������ʧ�ܣ�";
			}
		} catch (Exception e) {
			LogUtil.APP.error("Ԫ�ض�λ���̻��ǲ�������ʧ�ܻ��쳣��",e);
			return "����ִ��ʧ�ܣ�Ԫ�ض�λ���̻��ǲ�������ʧ�ܻ��쳣��" + e.getMessage();
		}
		caselog.insertTaskCaseLog(taskid, caseId, result,"info", String.valueOf(stepno),"");
		
		if(result.contains("��ȡ����ֵ�ǡ�") && result.contains("��")){
			result = result.substring(result.indexOf("��ȡ����ֵ�ǡ�")+7, result.length()-1);
		}
		return result;

	}
    
	public static AndroidElement isElementExist(AndroidDriver<AndroidElement> appium, String property, String propertyValue) {
		try {
			AndroidElement ae = null;
			property=property.toLowerCase();
			// ����WebElement����λ
			switch (property) {
			case "id":
				ae = appium.findElementById(propertyValue);
				break;
			case "name":
				ae = appium.findElementByAndroidUIAutomator("text(\""+propertyValue+"\")");
				break;
			case "androiduiautomator":
				ae = appium.findElementByAndroidUIAutomator(propertyValue);
				break;
			case "xpath":
				ae = appium.findElementByXPath(propertyValue);
				break;
			case "linktext":
				ae = appium.findElementByLinkText(propertyValue);
				break;
			case "tagname":
				ae = appium.findElementByTagName(propertyValue);
				break;
			case "cssselector":
				ae = appium.findElementByCssSelector(propertyValue);
				break;
			case "classname":
				ae = appium.findElementByClassName(propertyValue);
				break;
			case "partiallinktext":
				ae = appium.findElementByPartialLinkText(propertyValue);
				break;
			default:
				break;
			}

			return ae;

		} catch (Exception e) {
			LogUtil.APP.error("��ǰ����λʧ�ܣ������쳣��",e);
			return null;
		}
		
	}

    public static int judgeResult(ProjectCase testcase, ProjectCaseSteps step, Map<String, String> params, AndroidDriver<AndroidElement> appium, String taskid, String expect, String result, serverOperation caselog) {
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
                    LogUtil.APP.info("������{} ��{}���������Խ����{}����ֵ��������{}��",testcase.getCaseSign(),step.getStepSerialNumber(),result,expect.substring(Constants.ASSIGNMENT_SIGN.length()));
                    caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�����Խ����" + result + "����ֵ��������" + expect.substring(Constants.ASSIGNMENT_SIGN.length()) + "��", "info", String.valueOf(step.getStepSerialNumber()), "");
                }
                // ��ֵȫ�ֱ���
                else if (expect.length() > Constants.ASSIGNMENT_GLOBALSIGN.length() && expect.startsWith(Constants.ASSIGNMENT_GLOBALSIGN)) {
                	variable.put(expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), result);
                	ParamsManageForSteps.GLOBAL_VARIABLE.put(expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), result);
                    LogUtil.APP.info("������{} ��{}���������Խ����{}����ֵ��ȫ�ֱ�����{}��",testcase.getCaseSign(),step.getStepSerialNumber(),result,expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()));
                    caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�����Խ����" + result + "����ֵ��ȫ�ֱ�����" + expect.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()) + "��", "info", String.valueOf(step.getStepSerialNumber()), "");
                }
                // �ƶ��� UI���ģʽ
                else if (3 == step.getStepType() && params.get("checkproperty") != null && params.get("checkproperty_value") != null) {
                    String checkproperty = params.get("checkproperty");
                    String checkPropertyValue = params.get("checkproperty_value");

                    AndroidElement ae = isElementExist(appium, checkproperty, checkPropertyValue);
                    if (null != ae) {
                        LogUtil.APP.info("������{} ��{}�����ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ�гɹ���",testcase.getCaseSign(),step.getStepSerialNumber());
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ�гɹ���", "info", String.valueOf(step.getStepSerialNumber()), "");
                    } else {
                        casenote = "��" + step.getStepSerialNumber() + "����û���ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж���ִ��ʧ�ܣ�";
                        setresult = 1;
                        AndroidBaseAppium.screenShot(appium, imagname);
                        LogUtil.APP.warn("������{} ��{}����û���ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ��ʧ�ܣ�",testcase.getCaseSign(),step.getStepSerialNumber());
                        caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "�ڵ�ǰҳ����û���ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ��ʧ�ܣ�" + "checkproperty��" + checkproperty + "��  checkproperty_value��" + checkPropertyValue + "��", "error", String.valueOf(step.getStepSerialNumber()), imagname);
                    }
                }
                // ����ƥ��ģʽ
                else {
                    // ģ��ƥ��Ԥ�ڽ��ģʽ
                    if (expect.length() > Constants.FUZZY_MATCHING_SIGN.length() && expect.startsWith(Constants.FUZZY_MATCHING_SIGN)) {
                        if (result.contains(expect.substring(Constants.FUZZY_MATCHING_SIGN.length()))) {
                            LogUtil.APP.info("������{} ��{}����ģ��ƥ��Ԥ�ڽ���ɹ���ִ�н����{}",testcase.getCaseSign(),step.getStepSerialNumber(),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "ģ��ƥ��Ԥ�ڽ���ɹ���ִ�н����" + result, "info", String.valueOf(step.getStepSerialNumber()), "");
                        } else {
                            casenote = "��" + step.getStepSerialNumber() + "����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�";
                            setresult = 1;
                            AndroidBaseAppium.screenShot(appium, imagname);
                            LogUtil.APP.warn("������{} ��{}����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����{}�����Խ����{}",testcase.getCaseSign(),step.getStepSerialNumber(),expect.substring(Constants.FUZZY_MATCHING_SIGN.length()),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expect.substring(Constants.FUZZY_MATCHING_SIGN.length()) + "�����Խ����" + result, "error", String.valueOf(step.getStepSerialNumber()), imagname);
                        }
                    }
                    // ����ƥ��Ԥ�ڽ��ģʽ
                    else if (expect.length() > Constants.REGULAR_MATCHING_SIGN.length() && expect.startsWith(Constants.REGULAR_MATCHING_SIGN)) {
                        Pattern pattern = Pattern.compile(expect.substring(Constants.REGULAR_MATCHING_SIGN.length()));
                        Matcher matcher = pattern.matcher(result);
                        if (matcher.find()) {
                            LogUtil.APP.info("������{} ��{}��������ƥ��Ԥ�ڽ���ɹ���ִ�н����{}",testcase.getCaseSign(),step.getStepSerialNumber(),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����ƥ��Ԥ�ڽ���ɹ���", "info", String.valueOf(step.getStepSerialNumber()), "");
                        } else {
                            casenote = "��" + step.getStepSerialNumber() + "��������ƥ��Ԥ�ڽ��ʧ�ܣ�";
                            setresult = 1;
                            AndroidBaseAppium.screenShot(appium, imagname);
                            LogUtil.APP.warn("������{} ��{}��������ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����{}�����Խ����{}",testcase.getCaseSign(),step.getStepSerialNumber(),expect.substring(Constants.REGULAR_MATCHING_SIGN.length()),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "����ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expect.substring(Constants.REGULAR_MATCHING_SIGN.length()) + "�����Խ����" + result, "error", String.valueOf(step.getStepSerialNumber()), imagname);
                        }
                    }
                    // ��ȷƥ��Ԥ�ڽ��ģʽ
                    else {
                        if (expect.equals(result)) {
                            LogUtil.APP.info("������{} ��{}������ȷƥ��Ԥ�ڽ���ɹ���ִ�н����{}",testcase.getCaseSign(),step.getStepSerialNumber(),result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "��ȷƥ��Ԥ�ڽ���ɹ���", "info", String.valueOf(step.getStepSerialNumber()), "");
                        } else {
                            casenote = "��" + step.getStepSerialNumber() + "������ȷƥ��Ԥ�ڽ��ʧ�ܣ�";
                            setresult = 1;
                            AndroidBaseAppium.screenShot(appium, imagname);
                            LogUtil.APP.warn("������{} ��{}������ȷƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ���ǣ���{}��  ִ�н������{}��",testcase.getCaseSign(),step.getStepSerialNumber(),expect,result);
                            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "��ȷƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ���ǣ���"+expect+"��  ִ�н������"+ result+"��", "error", String.valueOf(step.getStepSerialNumber()), imagname);
                        }
                    }
                }
            }
        } else {
            casenote = (null != result) ? result : "";
            setresult = 2;
            AndroidBaseAppium.screenShot(appium, imagname);
            LogUtil.APP.warn("������{} ��{}����ִ�н����{}",testcase.getCaseSign(),step.getStepSerialNumber(),casenote);
            caselog.insertTaskCaseLog(taskid, testcase.getCaseId(), "��ǰ������ִ�й����н���|��λԪ��|��������ʧ�ܣ�" + casenote, "error", String.valueOf(step.getStepSerialNumber()), imagname);
        }
        
        return setresult;
    }

}
