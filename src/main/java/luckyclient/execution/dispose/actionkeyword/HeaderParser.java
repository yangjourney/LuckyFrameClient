package luckyclient.execution.dispose.actionkeyword;


import com.alibaba.fastjson.JSONObject;

import luckyclient.utils.LogUtil;

/**
 * �����ؼ��ֵĴ���ӿڵ�ʵ���ࣺ����Ӧheader��ȡ��ĳ��headerֵ
 * @author: sunshaoyan
 * @date: Created on 2019/4/13
 */
@Action(name="header")
public class HeaderParser implements ActionKeyWordParser {


    /**
     * �����ؼ���
     * @param actionParams �ؼ��ֲ���
     * @param testResult ������Ĳ��Խ��
     * @return ���ش�����
     */
    @Override
    public String parse(String actionParams, String testResult) {
        String pre = "RESPONSE_HEAD:��";
        String headerStr = testResult.substring(testResult.indexOf(pre) + pre.length(), testResult.indexOf("�� RESPONSE_CODE"));
        String getHeader = JSONObject.parseObject(headerStr).getJSONArray(actionParams).getString(0);
        LogUtil.APP.info("Action(header):����Ӧheader��ȡ��ָ��headerֵ��:{}",getHeader);
        return getHeader;

    }
}
