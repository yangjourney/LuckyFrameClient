package luckyclient.execution.httpinterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import luckyclient.driven.SubString;
import luckyclient.execution.dispose.ActionManageForSteps;
import luckyclient.execution.dispose.ParamsManageForSteps;
import luckyclient.execution.httpinterface.analyticsteps.InterfaceAnalyticCase;
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
 * @ClassName: ThreadForExecuteCase
 * @Description: �̳߳ط�ʽִ������
 * @author�� seagull
 * @date 2018��3��1��
 */
public class ThreadForExecuteCase extends Thread {
    private Integer caseId;
    private String caseSign;
    private ProjectCase testcase;
    private String taskid;
    private Integer projectId;
    private List<ProjectCaseSteps> steps;
    private List<ProjectCaseParams> pcplist;
    private serverOperation caselog;

    public ThreadForExecuteCase(ProjectCase projectcase, List<ProjectCaseSteps> steps, String taskid, List<ProjectCaseParams> pcplist, serverOperation caselog) {
        this.caseId = projectcase.getCaseId();
        this.testcase = projectcase;
        this.projectId = projectcase.getProjectId();
        this.caseSign = projectcase.getCaseSign();
        this.taskid = taskid;
        this.steps = steps;
        this.pcplist = pcplist;
        this.caselog = caselog;
    }

    @Override
    public void run() {
        Map<String, String> variable = new HashMap<>(0);
        // �ѹ����������뵽MAP��
        for (ProjectCaseParams pcp : pcplist) {
            variable.put(pcp.getParamsName(), pcp.getParamsValue());
        }
        // ����ȫ�ֱ���
        variable.putAll(ParamsManageForSteps.GLOBAL_VARIABLE);
        String functionname;
        String packagename;
        String expectedresults;
        int setcaseresult = 0;
        Object[] getParameterValues;
        String testnote = "��ʼ�����Խ��";
        int k = 0;
        // ����ѭ�������������������в���
        // ���뿪ʼִ�е�����
        caselog.insertTaskCaseExecute(taskid, projectId, caseId, caseSign, testcase.getCaseName(), 3);
        for (int i = 0; i < steps.size(); i++) {
            // �������������еĽű�
            Map<String, String> casescript = InterfaceAnalyticCase.analyticCaseStep(testcase, steps.get(i), taskid, caselog,variable);
            try {
                packagename = casescript.get("PackageName");
                functionname = casescript.get("FunctionName");
            } catch (Exception e) {
                LogUtil.APP.error("����:{} �����������Ƿ����������쳣�����飡",testcase.getCaseSign(),e);
                caselog.insertTaskCaseLog(taskid, caseId, "�����������Ƿ�����ʧ�ܣ����飡", "error", String.valueOf(i + 1), "");
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
                // ��ȡ������������������
                getParameterValues = new Object[casescript.size() - 4];
                for (int j = 0; j < casescript.size() - 4; j++) {
                    if (casescript.get("FunctionParams" + (j + 1)) == null) {
                        k = 1;
                        break;
                    }
                    String parameterValues = casescript.get("FunctionParams" + (j + 1));
                    LogUtil.APP.info("����:{} ��������:{} ������:{} ��{}������:{}",testcase.getCaseSign(),packagename,functionname,(j+1),parameterValues);
                    caselog.insertTaskCaseLog(taskid, caseId, "����������" + packagename + " ��������" + functionname + " ��" + (j + 1) + "��������" + parameterValues, "info", String.valueOf(i + 1), "");
                    getParameterValues[j] = parameterValues;
                }
            } else {
                getParameterValues = null;
            }
            // ���ö�̬������ִ�в�������
            try {
                LogUtil.APP.info("����:{}��ʼ���÷���:{} .....",testcase.getCaseSign(),functionname);
                caselog.insertTaskCaseLog(taskid, caseId, "��ʼ���÷�����" + functionname + " .....", "info", String.valueOf(i + 1), "");

                testnote = InvokeMethod.callCase(packagename, functionname, getParameterValues, steps.get(i).getStepType(), steps.get(i).getExtend());
                testnote = ActionManageForSteps.actionManage(casescript.get("Action"), testnote);
                if (null != expectedresults && !expectedresults.isEmpty()) {
                    LogUtil.APP.info("expectedResults=��{}��",expectedresults);
                    // ��ֵ����
                    if (expectedresults.length() > Constants.ASSIGNMENT_SIGN.length() && expectedresults.startsWith(Constants.ASSIGNMENT_SIGN)) {
                        variable.put(expectedresults.substring(Constants.ASSIGNMENT_SIGN.length()), testnote);
                        LogUtil.APP.info("����:{} ��{}���������Խ����{}����ֵ��������{}��",testcase.getCaseSign(),(i+1),testnote,expectedresults.substring(Constants.ASSIGNMENT_SIGN.length()));
                        caselog.insertTaskCaseLog(taskid, caseId, "�����Խ����" + testnote + "����ֵ��������" + expectedresults.substring(Constants.ASSIGNMENT_SIGN.length()) + "��", "info", String.valueOf(i + 1), "");
                    }
                    // ��ֵȫ�ֱ���
                    else if (expectedresults.length() > Constants.ASSIGNMENT_GLOBALSIGN.length() && expectedresults.startsWith(Constants.ASSIGNMENT_GLOBALSIGN)) {
                        variable.put(expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), testnote);
                        ParamsManageForSteps.GLOBAL_VARIABLE.put(expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), testnote);
                        LogUtil.APP.info("����:{} ��{}���������Խ����{}����ֵ��ȫ�ֱ�����{}��",testcase.getCaseSign(),(i+1),testnote,expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()));
                        caselog.insertTaskCaseLog(taskid, caseId, "�����Խ����" + testnote + "����ֵ��ȫ�ֱ�����" + expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()) + "��", "info", String.valueOf(i + 1), "");
                    }
                    // ģ��ƥ��
                    else if (expectedresults.length() > Constants.FUZZY_MATCHING_SIGN.length() && expectedresults.startsWith(Constants.FUZZY_MATCHING_SIGN)) {
                        if (testnote.contains(expectedresults.substring(Constants.FUZZY_MATCHING_SIGN.length()))) {
                            LogUtil.APP.info("����:{} ��{}����ģ��ƥ��Ԥ�ڽ���ɹ���ִ�н��:{}",testcase.getCaseSign(),(i+1),testnote);
                            caselog.insertTaskCaseLog(taskid, caseId, "ģ��ƥ��Ԥ�ڽ���ɹ���ִ�н����" + testnote, "info", String.valueOf(i + 1), "");
                        } else {
                            setcaseresult = 1;
                            LogUtil.APP.warn("����:{} ��{}����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ��:{}�����Խ��:{}",testcase.getCaseSign(),(i+1),expectedresults.substring(Constants.FUZZY_MATCHING_SIGN.length()),testnote);
                            caselog.insertTaskCaseLog(taskid, caseId, "��" + (i + 1) + "����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults.substring(Constants.FUZZY_MATCHING_SIGN.length()) + "�����Խ����" + testnote, "error", String.valueOf(i + 1), "");
                            testnote = "������" + (i + 1) + "����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�";
                            if (testcase.getFailcontinue() == 0) {
                                LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ��жϱ���������������ִ�У����뵽��һ������ִ����......",testcase.getCaseSign(),(i+1));
                                break;
                            } else {
                                LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ���������������������ִ�У������¸�����ִ����......",testcase.getCaseSign(),(i+1));
                            }
                        }
                    }
                    // ����ƥ��
                    else if (expectedresults.length() > Constants.REGULAR_MATCHING_SIGN.length() && expectedresults.startsWith(Constants.REGULAR_MATCHING_SIGN)) {
                        Pattern pattern = Pattern.compile(expectedresults.substring(Constants.REGULAR_MATCHING_SIGN.length()));
                        Matcher matcher = pattern.matcher(testnote);
                        if (matcher.find()) {
                            LogUtil.APP.info("����:{} ��{}��������ƥ��Ԥ�ڽ���ɹ���ִ�н��:{}",testcase.getCaseSign(),(i+1),testnote);
                            caselog.insertTaskCaseLog(taskid, caseId, "����ƥ��Ԥ�ڽ���ɹ���ִ�н����" + testnote, "info", String.valueOf(i + 1), "");
                        } else {
                            setcaseresult = 1;
                            LogUtil.APP.warn("����:{} ��{}��������ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ��:{}�����Խ��:{}",testcase.getCaseSign(),(i+1),expectedresults.substring(Constants.REGULAR_MATCHING_SIGN.length()),testnote);
                            caselog.insertTaskCaseLog(taskid, caseId, "��" + (i + 1) + "��������ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults.substring(Constants.REGULAR_MATCHING_SIGN.length()) + "�����Խ����" + testnote, "error", String.valueOf(i + 1), "");
                            testnote = "������" + (i + 1) + "��������ƥ��Ԥ�ڽ��ʧ�ܣ�";
                            if (testcase.getFailcontinue() == 0) {
                                LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ��жϱ���������������ִ�У����뵽��һ������ִ����......",testcase.getCaseSign(),(i+1));
                                break;
                            } else {
                                LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ���������������������ִ�У������¸�����ִ����......",testcase.getCaseSign(),(i+1));
                            }
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
                            setcaseresult = 0;
                            LogUtil.APP.info("������{}�� �ڡ�{}������jsonpath����Ԥ�ڽ���ɹ���Ԥ�ڽ��:{} ���Խ��: {} ִ�н��:true",testcase.getCaseSign(),(i+1),exceptResult,result);
                            caselog.insertTaskCaseLog(taskid, caseId, "jsonpath����Ԥ�ڽ���ɹ���Ԥ�ڽ��:"+ expectedresults + "���Խ��:" + result + "ִ�н��:true","info", String.valueOf(i + 1), "");
                        } else {
                            setcaseresult = 1;
                            LogUtil.APP.warn("����:{} ��{}����jsonpath����Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ��:{}�����Խ��:{}",testcase.getCaseSign(),(i+1),expectedresults,result);
                            caselog.insertTaskCaseLog(taskid, caseId, "��" + (i + 1) + "��������ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + exceptResult + "�����Խ����" + result, "error", String.valueOf(i + 1), "");
                            testnote = "������" + (i + 1) + "����jsonpath����Ԥ�ڽ��ʧ�ܣ�";
                            if (testcase.getFailcontinue() == 0) {
                                LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ��жϱ���������������ִ�У����뵽��һ������ִ����......",testcase.getCaseSign(),(i+1));
                                break;
                            } else {
                                LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ���������������������ִ�У������¸�����ִ����......",testcase.getCaseSign(),(i+1));
                            }

                            // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
                            break;
                        }
                    }
                    // ��ȫ���
                    else {
                        if (expectedresults.equals(testnote)) {
                            LogUtil.APP.info("����:{} ��{}������ȷƥ��Ԥ�ڽ���ɹ���ִ�н��:{}",testcase.getCaseSign(),(i+1),testnote);
                            caselog.insertTaskCaseLog(taskid, caseId, "��ȷƥ��Ԥ�ڽ���ɹ���ִ�н����" + testnote, "info", String.valueOf(i + 1), "");
                        } else {
                            setcaseresult = 1;
                            LogUtil.APP.warn("����:{} ��{}������ȷƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ��:{}�����Խ��:{}",testcase.getCaseSign(),(i+1),expectedresults,testnote);
                            caselog.insertTaskCaseLog(taskid, caseId, "��" + (i + 1) + "������ȷƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults + "�����Խ����" + testnote, "error", String.valueOf(i + 1), "");
                            testnote = "������" + (i + 1) + "������ȷƥ��Ԥ�ڽ��ʧ�ܣ�";
                            if (testcase.getFailcontinue() == 0) {
                                LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ��жϱ���������������ִ�У����뵽��һ������ִ����......",testcase.getCaseSign(),(i+1));
                                break;
                            } else {
                                LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ���������������������ִ�У������¸�����ִ����......",testcase.getCaseSign(),(i+1));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.APP.error("����:{}���÷������̳���������:{} �����¼��ű����������Լ�������",testcase.getCaseSign(),functionname,e);
                caselog.insertTaskCaseLog(taskid, caseId, "���÷������̳�����������" + functionname + " �����¼��ű����������Լ�������", "error", String.valueOf(i + 1), "");
                testnote = "CallCase���ó������÷������̳�����������" + functionname + " �����¼��ű����������Լ�������";
                setcaseresult = 1;
                if (testcase.getFailcontinue() == 0) {
                    LogUtil.APP.error("������{}���ڡ�{}������ִ��ʧ�ܣ��жϱ���������������ִ�У����뵽��һ������ִ����......",testcase.getCaseSign(),(i+1));
                    break;
                } else {
                    LogUtil.APP.error("������{}���ڡ�{}������ִ��ʧ�ܣ���������������������ִ�У������¸�����ִ����......",testcase.getCaseSign(),(i+1));
                }
            }
        }
        // ������÷���������δ�����������ò��Խ������
        try {
            // �ɹ���ʧ�ܵ������ߴ�����
            if (testnote.contains("CallCase���ó���") || testnote.contains("������������")) {
                // �����������ǵ��÷�������ȫ����������Ϊ����
                LogUtil.APP.warn("����:{} ����ִ�н��Ϊ��������ο�������־��������������ԭ��.....", testcase.getCaseSign());
                caselog.insertTaskCaseLog(taskid, caseId, "����ִ�н��Ϊ��������ο�������־��������������ԭ��.....", "error", "SETCASERESULT...", "");
                setcaseresult = 2;
            }
            caselog.updateTaskCaseExecuteStatus(taskid, caseId, setcaseresult);
            if (0 == setcaseresult) {
                LogUtil.APP.info("����:{}ִ�н���ɹ�......",testcase.getCaseSign());
                caselog.insertTaskCaseLog(taskid, caseId, "��������ִ��ȫ���ɹ�......", "info", "ending", "");
                LogUtil.APP.info("*********������{}��ִ�����,���Խ�����ɹ�*********",testcase.getCaseSign());
            } else if (1 == setcaseresult) {
                LogUtil.APP.warn("����:{}ִ�н��ʧ��......",testcase.getCaseSign());
                caselog.insertTaskCaseLog(taskid, caseId, "����ִ�н��ʧ��......", "error", "ending", "");
                LogUtil.APP.warn("*********������{}��ִ�����,���Խ����ʧ��*********",testcase.getCaseSign());
            } else {
                LogUtil.APP.warn("������" + testcase.getCaseSign() + "ִ�н������......");
                caselog.insertTaskCaseLog(taskid, caseId, "����ִ�н������......", "error", "ending", "");
                LogUtil.APP.warn("*********������{}��ִ�����,���Խ��������*********",testcase.getCaseSign());
            }
        } catch (Exception e) {
            LogUtil.APP.error("����:{}����ִ�н�����̳���......",testcase.getCaseSign(),e);
            caselog.insertTaskCaseLog(taskid, caseId, "����ִ�н�����̳���......", "error", "ending", "");
        } finally {
            variable.clear(); // һ��������������ձ����洢�ռ�
            TestControl.THREAD_COUNT--; // ���̼߳���--�����ڼ���߳��Ƿ�ȫ��ִ����
        }
    }

}
