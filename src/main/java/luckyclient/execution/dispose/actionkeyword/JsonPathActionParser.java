package luckyclient.execution.dispose.actionkeyword;

import luckyclient.driven.SubString;
import luckyclient.utils.LogUtil;

/**
 * �����ؼ��ֵĴ���ӿڵ�ʵ���ࣺʹ��jsonpath����json�ַ���
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��8��26��
 */
@Action(name="jsonpath")
public class JsonPathActionParser implements ActionKeyWordParser {


    /**
     * ͨ��jsonPath���ʽ��ȡJSON�ַ���ָ��ֵ
     * ��֧�ַ���ֵ��String���ͣ���֧��List,���jsonPath���ʽ���ص���List���׳��쳣
     * @param actionParams �����ؼ���
     * @param testResult ���Խ��
     */
	@Override
    public String parse(String actionParams, String testResult) {
    	LogUtil.APP.info("Action(jsonPath):��ʼ����jsonPath����...��������{}��   ������json�ַ�������{}��",actionParams,testResult);
    	testResult = SubString.jsonPathGetParams(actionParams, testResult);
        LogUtil.APP.info("Action(jsonPath):����jsonPath�������...��������{}��",testResult);
        return testResult;
    }
}
