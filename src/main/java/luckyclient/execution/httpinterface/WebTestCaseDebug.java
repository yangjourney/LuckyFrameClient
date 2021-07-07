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
import luckyclient.remote.api.GetServerApi;
import luckyclient.remote.api.PostServerApi;
import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.ProjectCase;
import luckyclient.remote.entity.ProjectCaseParams;
import luckyclient.remote.entity.ProjectCaseSteps;
import luckyclient.utils.Constants;
import luckyclient.utils.InvokeMethod;
import luckyclient.utils.LogUtil;
import org.apache.commons.lang.StringUtils;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 *
 * @ClassName: WebTestCaseDebug
 * @Description: �ṩWeb�˵��Խӿ�
 * @author�� seagull
 * @date 2018��3��1��
 */
public class WebTestCaseDebug {
    /**
     * ������WEBҳ���ϵ�������ʱ�ṩ�Ľӿ�
     * @param caseIdStr ����ID
     * @param userIdStr  �û�ID
     */
    public static void oneCaseDebug(String caseIdStr, String userIdStr) {
        Map<String, String> variable = new HashMap<>(0);
        serverOperation.exetype=1;
        String packagename;
        String functionname;
        String expectedresults;
        int setcaseresult = 0;
        int stepJumpNo=0;
        Object[] getParameterValues;
        String testnote = "��ʼ�����Խ��";
        int k = 0;
        Integer caseId = Integer.valueOf(caseIdStr);
        Integer userId = Integer.valueOf(userIdStr);
        ProjectCase testcase = GetServerApi.cGetCaseByCaseId(caseId);
        // ��ʼ��д��������Լ���־ģ��
        serverOperation caselog = new serverOperation();

        String sign = testcase.getCaseSign();
        List<ProjectCaseParams> pcplist = GetServerApi.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
        // �ѹ����������뵽MAP��
        for (ProjectCaseParams pcp : pcplist) {
            variable.put(pcp.getParamsName(), pcp.getParamsValue());
        }
        List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
        //����ѭ���������������в���
        for (int i = 0; i < steps.size(); i++) {
            //��������ת�﷨
            if(stepJumpNo!=0&&setcaseresult!=0){
                if(stepJumpNo==i+1){
                    setcaseresult = 0;
                    PostServerApi.cPostDebugLog(userId, caseId, "INFO", "��ת����ǰ������"+(i+1)+"��",0);
                    LogUtil.APP.info("��ת����ǰ������{}��",i+1);
                }else if(stepJumpNo>i+1){
                    PostServerApi.cPostDebugLog(userId, caseId, "INFO", "��ǰ������"+(i+1)+"��,����ִ��...",0);
                    LogUtil.APP.info("��ǰ������{}��,����ִ��...",i+1);
                    continue;
                }else{
                    PostServerApi.cPostDebugLog(userId, caseId, "INFO", "��ת���衾"+stepJumpNo+"��С�ڵ�ǰ���衾"+(i+1)+"����ֱ�����¼���ִ��...",0);
                    LogUtil.APP.info("��ת���衾{}��С�ڵ�ǰ���衾{}����ֱ�����¼���ִ��...",stepJumpNo,(i+1));
                }
            }

            Map<String, String> casescript = InterfaceAnalyticCase.analyticCaseStep(testcase, steps.get(i), "888888", null,variable);
            try {
                packagename = casescript.get("PackageName");
                functionname = casescript.get("FunctionName");
            } catch (Exception e) {
                LogUtil.APP.error("�����������Ƿ����������쳣��",e);
                PostServerApi.cPostDebugLog(userId, caseId, "ERROR", "�����������Ƿ�����ʧ�ܣ����飡",2);
                break;        //ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
            }
            //�������ƽ��������쳣���ǵ���������������쳣
            if ((null != functionname && functionname.contains("�����쳣")) || k == 1) {
                testnote = "������" + (i + 1) + "��������������";
                break;
            }
            expectedresults = casescript.get("ExpectedResults");
            //�жϷ����Ƿ������
            if (casescript.size() > 4) {
                //��ȡ������������������
                getParameterValues = new Object[casescript.size() - 4];
                for (int j = 0; j < casescript.size() - 4; j++) {
                    if (casescript.get("FunctionParams" + (j + 1)) == null) {
                        k = 1;
                        break;
                    }

                    String parameterValues = casescript.get("FunctionParams" + (j + 1));
                    PostServerApi.cPostDebugLog(userId, caseId, "INFO", "����������" + packagename + " ��������" + functionname + " ��" + (j + 1) + "��������" + parameterValues, 0);
                    getParameterValues[j] = parameterValues;
                }
            } else {
                getParameterValues = null;
            }
            //���ö�̬������ִ�в�������
            try {
                PostServerApi.cPostDebugLog(userId, caseId, "INFO", "��ʼ���÷�����" + functionname + " .....",0);

                // �ӿ�����֧��ʹ��runcase�ؼ���
                if ((null != functionname && "runcase".equals(functionname))) {
                    TestCaseExecution testCaseExecution=new TestCaseExecution();
                    testnote = testCaseExecution.oneCaseExecuteForCase(getParameterValues[0].toString(), "888888", variable, caselog, null);
                }else{
                    testnote = InvokeMethod.callCase(packagename, functionname, getParameterValues, steps.get(i).getStepType(), steps.get(i).getExtend());
                }
                testnote = ActionManageForSteps.actionManage(casescript.get("Action"), testnote);

                if (null != expectedresults && !expectedresults.isEmpty()) {
                    //��������ת
                    if (expectedresults.length() > Constants.IFFAIL_JUMP.length() && expectedresults.startsWith(Constants.IFFAIL_JUMP)) {
                        PostServerApi.cPostDebugLog(userId, caseId, "INFO", "Ԥ�ڽ���д����ж�������ת���裬����ǰԭʼ�ַ�����"+expectedresults,0);
                        LogUtil.APP.info("Ԥ�ڽ���д����ж�������ת���裬����ǰԭʼ�ַ�����{}",expectedresults);
                        String expectedTemp = expectedresults.substring(Constants.IFFAIL_JUMP.length());
                        if(expectedTemp.contains(Constants.SYMLINK)){
                            expectedresults = expectedTemp.substring(expectedTemp.indexOf(Constants.SYMLINK)+2);
                            try{
                                stepJumpNo =  Integer.parseInt(expectedTemp.substring(0,expectedTemp.indexOf(Constants.SYMLINK)));
                            }catch (NumberFormatException nfe){
                                LogUtil.APP.error("������ת�﷨����ʧ�ܣ������Ų������֣���ȷ��:{}",expectedTemp.substring(0,expectedTemp.indexOf(Constants.SYMLINK)));
                            }
                        }else{
                            PostServerApi.cPostDebugLog(userId, caseId, "INFO", "����Ԥ�ڽ�������ж�ʧ�ܣ���ȷ��Ԥ�ڽ���﷨�ṹ����"+Constants.IFFAIL_JUMP+">>Ԥ�ڽ������ԭʼԤ�ڽ��ֵ��"+expectedresults,0);
                            LogUtil.APP.warn("����Ԥ�ڽ�������ж�ʧ�ܣ���ȷ��Ԥ�ڽ���﷨�ṹ����"+Constants.IFFAIL_JUMP+">>Ԥ�ڽ������ԭʼԤ�ڽ��ֵ��{}",expectedresults);
                        }
                    }
                    // ��ֵ����
                    if (expectedresults.length() > Constants.ASSIGNMENT_SIGN.length() && expectedresults.startsWith(Constants.ASSIGNMENT_SIGN)) {
                        variable.put(expectedresults.substring(Constants.ASSIGNMENT_SIGN.length()), testnote);
                        PostServerApi.cPostDebugLog(userId, caseId, "INFO", "�����Խ����" + testnote + "����ֵ��������" + expectedresults.substring(Constants.ASSIGNMENT_SIGN.length()) + "��",0);
                    }
                    // ��ֵȫ�ֱ���
                    else if (expectedresults.length() > Constants.ASSIGNMENT_GLOBALSIGN.length() && expectedresults.startsWith(Constants.ASSIGNMENT_GLOBALSIGN)) {
                        variable.put(expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), testnote);
                        ParamsManageForSteps.GLOBAL_VARIABLE.put(expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()), testnote);
                        LogUtil.APP.info("����:{} ��{}���������Խ����{}����ֵ��ȫ�ֱ�����{}��",testcase.getCaseSign(),(i+1),testnote,expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()));
                        PostServerApi.cPostDebugLog(userId, caseId, "INFO", "�����Խ����" + testnote + "����ֵ��ȫ�ֱ�����" + expectedresults.substring(Constants.ASSIGNMENT_GLOBALSIGN.length()) + "��",0);
                    }
                    // ģ��ƥ��
                    else if (expectedresults.length() > Constants.FUZZY_MATCHING_SIGN.length() && expectedresults.startsWith(Constants.FUZZY_MATCHING_SIGN)) {
                        if (testnote.contains(expectedresults.substring(Constants.FUZZY_MATCHING_SIGN.length()))) {
                            PostServerApi.cPostDebugLog(userId, caseId, "INFO", "ģ��ƥ��Ԥ�ڽ���ɹ���ִ�н����" + testnote,0);
                        } else {
                            setcaseresult = 1;
                            PostServerApi.cPostDebugLog(userId, caseId, "ERROR", "��" + (i + 1) + "����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults.substring(Constants.FUZZY_MATCHING_SIGN.length()) + "�����Խ����" + testnote,0);
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
                            PostServerApi.cPostDebugLog(userId, caseId, "INFO", "����ƥ��Ԥ�ڽ���ɹ���ִ�н����" + testnote,0);
                        } else {
                            setcaseresult = 1;
                            PostServerApi.cPostDebugLog(userId, caseId, "ERROR", "��" + (i + 1) + "��������ƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults.substring(Constants.REGULAR_MATCHING_SIGN.length()) + "�����Խ����" + testnote,0);
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
                            PostServerApi.cPostDebugLog(userId, caseId, "INFO", "jsonpath����Ԥ�ڽ���ɹ���Ԥ�ڽ����" + exceptResult + " ���Խ��: " + result + "У����: true", 0);
                        } else {
                            setcaseresult = 1;
                            PostServerApi.cPostDebugLog(userId, caseId, "ERROR", "��" + (i + 1) + "����jsonpath����Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + exceptResult + "�����Խ����" + result,0);
                            testnote = "������" + (i + 1) + "����jsonpath����Ԥ�ڽ��ʧ�ܣ�";
                            if (testcase.getFailcontinue() == 0) {
                                LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ��жϱ���������������ִ�У����뵽��һ������ִ����......",testcase.getCaseSign(),(i+1));
                                break;
                            } else {
                                LogUtil.APP.warn("������{}���ڡ�{}������ִ��ʧ�ܣ���������������������ִ�У������¸�����ִ����......",testcase.getCaseSign(),(i+1));
                            }
                        }
                    }
                    // ��ȫ���
                    else {
                        if (expectedresults.equals(testnote)) {
                            PostServerApi.cPostDebugLog(userId, caseId, "INFO", "��ȷƥ��Ԥ�ڽ���ɹ���ִ�н����" + testnote,0);
                        } else if(expectedresults.trim().equals("NULL")&& StringUtils.isBlank(testnote)){
                            testnote = "���ؽ��Ϊ�գ�ƥ��NULL�ɹ�";
                            PostServerApi.cPostDebugLog(userId, caseId, "INFO", "��ȷƥ��Ԥ�ڽ���ɹ���ִ�н����" + testnote,0);
                        } else {
                            setcaseresult = 1;
                            PostServerApi.cPostDebugLog(userId, caseId, "ERROR", "��" + (i + 1) + "������ȷƥ��Ԥ�ڽ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults + "�����Խ����" + testnote,0);
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
                setcaseresult = 1;
                LogUtil.APP.error("����ִ�г����쳣��",e);
                PostServerApi.cPostDebugLog(userId, caseId, "ERROR", "���÷������̳�����������" + functionname + " �����¼��ű����������Լ�������",0);
                testnote = "CallCase���ó���";
                if (testcase.getFailcontinue() == 0) {
                    LogUtil.APP.error("������{}���ڡ�{}������ִ��ʧ�ܣ��жϱ���������������ִ�У����뵽��һ������ִ����......",testcase.getCaseSign(),(i+1));
                    break;
                } else {
                    LogUtil.APP.error("������{}���ڡ�{}������ִ��ʧ�ܣ���������������������ִ�У������¸�����ִ����......",testcase.getCaseSign(),(i+1));
                }
            }
        }
        variable.clear();               //��մ���MAP
        //������÷���������δ�����������ò��Խ������
        if (testnote.contains("CallCase���ó���") && testnote.contains("������������")) {
            PostServerApi.cPostDebugLog(userId, caseId, "ERRORover", "���� " + sign + "�������ǵ��ò����еķ�������",1);
        }
        if (0 == setcaseresult) {
            PostServerApi.cPostDebugLog(userId, caseId, "INFOover", "���� " + sign + "����ȫ��ִ����ɣ�",1);
        } else {
            PostServerApi.cPostDebugLog(userId, caseId, "ERRORover", "���� " + sign + "��ִ�й�����ʧ�ܣ����飡",1);
        }
    }

}