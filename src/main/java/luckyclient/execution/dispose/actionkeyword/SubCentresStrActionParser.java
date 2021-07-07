package luckyclient.execution.dispose.actionkeyword;

import luckyclient.driven.SubString;
import luckyclient.utils.LogUtil;

/**
 * �����ؼ��ֵĴ���ӿڵ�ʵ���ࣺ��ȡ���Խ��ָ����ʼ������λ���ַ���
 * @author: sunshaoyan
 * @date: Created on 2019/4/13
 */
@Action(name="subcentrestr")
public class SubCentresStrActionParser implements ActionKeyWordParser {


    /**
     * ��ȡ���Խ��ָ����ʼ������λ���ַ���
     * @param actionParams �����ؼ���
     * @param testResult ���Խ��
     */
    @Override
    public String parse(String actionParams, String testResult) {
        String startstr;
        String endstr;
        if(actionParams.startsWith("[")&&actionParams.endsWith("]")){
            startstr=actionParams.substring(actionParams.indexOf("[")+1, actionParams.indexOf("]"));
            endstr=actionParams.substring(actionParams.lastIndexOf("[")+1, actionParams.lastIndexOf("]"));
            testResult= SubString.subCentreStr(testResult, startstr, endstr);
            LogUtil.APP.info("Action(subCentreStr):��ȡ���Խ��ָ����ʼ������λ���ַ���:{}",testResult);
        }else{
            testResult="���趯����subCentreStr ������[\"��ʼ�ַ�\"][\"�����ַ�\"]#subCentreStr ��ʽ���������Ĳ��趯������:"+actionParams;
            LogUtil.APP.warn("���趯����subCentreStr ������[\"��ʼ�ַ�\"][\"�����ַ�\"]#subCentreStr ��ʽ���������Ĳ��趯������:{}",actionParams);
        }
        return testResult;
    }
}
