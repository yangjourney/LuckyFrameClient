package luckyclient.execution.dispose.actionkeyword;


import luckyclient.driven.SubString;
import luckyclient.utils.LogUtil;

/**
 * �����ؼ��ֵĴ���ӿڵ�ʵ���ࣺ��ȡ���Խ��ָ����ʼ������λ���ַ���
 * @author: sunshaoyan
 * @date: Created on 2019/4/13
 */
@Action(name="subcentrenum")
public class SubCentreNumActionParser implements ActionKeyWordParser {


    /**
     * ��ȡ���Խ��ָ����ʼ������λ���ַ���
     * @param actionParams �����ؼ���
     * @param testResult ���Խ��
     */
    @Override
    public String parse(String actionParams, String testResult) {
        if(actionParams.startsWith("[")&&actionParams.endsWith("]")){
            String startnum=actionParams.substring(actionParams.indexOf("[")+1, actionParams.indexOf("]"));
            String endnum=actionParams.substring(actionParams.lastIndexOf("[")+1, actionParams.lastIndexOf("]"));
            testResult= SubString.subCentreNum(testResult, startnum, endnum);
            LogUtil.APP.info("Action(subCentreNum):��ȡ���Խ��ָ����ʼ������λ���ַ���:{}",testResult);
        }else{
            testResult="���趯����subCentreNum ������[\"��ʼ�ַ�\"][\"�����ַ�\"]#subCentreNum ��ʽ���������Ĳ��趯������:"+actionParams;
            LogUtil.APP.warn("���趯����subCentreNum ������[\"��ʼλ��(����)\"][\"����λ��(����)\"]#subCentreNum ��ʽ���������Ĳ��趯������:{}",actionParams);
        }
        return testResult;
    }
}
