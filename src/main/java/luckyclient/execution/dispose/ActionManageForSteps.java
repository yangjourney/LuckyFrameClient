package luckyclient.execution.dispose;

import cn.hutool.core.util.StrUtil;
import luckyclient.utils.Constants;
import luckyclient.utils.LogUtil;

/**
 * �����ؼ��ִ���
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull
 * =================================================================
 * @author Seagull
 * @date 2019��1��15��
 */
public class ActionManageForSteps {

	/**
	 * ������������
	 * @param stepsaction ����ؼ���
	 * @param testresult ��������Խ��
	 * @return ���ش������
	 */
	public static String actionManage(String stepsaction,String testresult){
		LogUtil.APP.info("Action(����)����ǰ�����Խ���ǣ�{}",testresult);
		LogUtil.APP.info("���ڽ��뵽Action(����)����......ACTIONֵ��{}",stepsaction);
		if(null==stepsaction||"".equals(stepsaction.trim())){
			LogUtil.APP.info("Action(����)���账��......");
			return testresult;
		}
		
		String responseHead="";
		String responseCode="";
		//ȥ��������Ӧͷ����Ϣ
		if(testresult.startsWith(Constants.RESPONSE_HEAD)){
			responseHead = testresult.substring(0,testresult.indexOf(Constants.RESPONSE_END)+1);
			testresult = testresult.substring(testresult.indexOf(responseHead)+responseHead.length()+1);
			responseHead = responseHead+" ";
		}

		//ȥ��������Ӧͷ����Ϣ
		if(testresult.startsWith(Constants.RESPONSE_CODE)){
			responseCode = testresult.substring(0,testresult.indexOf(Constants.RESPONSE_END)+1);
			testresult = testresult.substring(testresult.indexOf(responseCode)+responseCode.length()+1);
			responseCode = responseCode+" ";
		}
		
		stepsaction=stepsaction.trim();
		String[] temp=stepsaction.split("\\|",-1);
		for(String actionorder:temp){
			if(null!=actionorder&&!"".equals(actionorder.trim())){
				testresult=actionExecute(actionorder,testresult);
			}
		}
		
		//���ش�����ʱ���ٰ���Ӧͷ�Լ���Ӧ�����
		return responseHead+responseCode+testresult;
	}

	/**
	 * �����ؼ���ִ��
	 * @param actionKeyWord ����ؼ���
	 * @param testResult ��������Խ��
	 * @return �ؼ��ִ���󷵻ؽ��
	 */
	private static String actionExecute(String actionKeyWord,String testResult){
		try{
			String keyWord = "";
			String actionParams = "";
			if(actionKeyWord.contains("#")){
				keyWord = actionKeyWord.substring(actionKeyWord.lastIndexOf("#")+1);
				actionParams = actionKeyWord.substring(0, actionKeyWord.lastIndexOf("#"));
			}

			if(StrUtil.isNotEmpty(keyWord)&& keyWord.length()>0){
				ActionContext actionContext = new ActionContext(keyWord.toLowerCase());
				testResult = actionContext.parse(actionParams, testResult, actionKeyWord);
			}else {
				testResult="�ؼ����﷨��д��������ؼ��֣�"+actionKeyWord;
				LogUtil.APP.warn("�ؼ����﷨��д��������ؼ��֣�{}",actionKeyWord);
			}
			return testResult;
		}catch(Exception e){
			testResult="�����趯���¼������г����쳣��ֱ�ӷ��ز��Խ����"+actionKeyWord;
			LogUtil.APP.error("�����趯���¼������г����쳣��ֱ�ӷ��ز��Խ����" ,e);
			return testResult;
		}
	}

}
