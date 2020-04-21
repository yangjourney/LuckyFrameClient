package luckyclient.execution.dispose.actionkeyword;

import luckyclient.driven.SubString;
import luckyclient.utils.LogUtil;

/**
 * �����ؼ��ֵĴ���ӿڵ�ʵ���ࣺ��ȡJSON�ַ���ָ��Key��ֵ��
 * @author: sunshaoyan
 * @date: Created on 2019/4/13
 */
@Action(name="getjv")
public class GetJsonActionParser implements ActionKeyWordParser {


    /**
     * ��ȡJSON�ַ���ָ��Key��ֵ��
     * @param actionParams �����ؼ���
     * @param testResult ���Խ��
     */
    @Override
    public String parse(String actionParams, String testResult) {
        String key;
        String index="1";
        if(actionParams.endsWith("]")&&actionParams.contains("[")){
            key=actionParams.substring(0,actionParams.indexOf("["));
            index=actionParams.substring(actionParams.indexOf("[")+1, actionParams.lastIndexOf("]"));
        }else{
            key=actionParams;
        }
        testResult= SubString.getJsonValue(testResult, key, index);
        LogUtil.APP.info("Action(getJV):��ȡJSON�ַ���ָ��Key��ֵ��:{}",testResult);
        return testResult;
    }
}
