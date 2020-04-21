package luckyclient.execution;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;

import luckyclient.execution.httpinterface.TestControl;
import luckyclient.execution.httpinterface.WebTestCaseDebug;
import luckyclient.utils.LogUtil;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 * 
 * @author�� seagull
 * @date 2017��12��1�� ����9:29:40
 * 
 */
public class WebDebugExecute extends TestControl{

	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator + "log4j.conf");
	 		String caseIdStr = args[0];
	 		String userIdStr = args[1];
	 		WebTestCaseDebug.oneCaseDebug(caseIdStr, userIdStr);
		} catch (Exception e) {
			LogUtil.APP.error("�����������������������쳣�����飡",e);
		} finally{
			System.exit(0);
		}
	}
}
