package luckyclient.execution.webdriver.ex;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import luckyclient.execution.dispose.ChangString;
import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.ProjectCase;
import luckyclient.remote.entity.ProjectCaseSteps;
import luckyclient.utils.LogUtil;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull1985
 * =================================================================
 * 
 * @ClassName: AnalyticCase
 * @Description: ���������������������ֵĽű� @author�� seagull
 * @date 2016��9��18��
 * 
 */
public class WebDriverAnalyticCase {
	// private static String splitFlag = "\\|";

	/**
	 * Web UI���͵Ĳ������
	 * 
	 * @param projectcase ��������
	 * @param step �����������
	 * @param taskid ����ID
	 * @param caselog ��־����
	 * @return ���ؽ������MAP
	 * @author Seagull
	 * @date 2019��1��17��
	 */
	public static Map<String, String> analyticCaseStep(ProjectCase projectcase, ProjectCaseSteps step, String taskid,
			serverOperation caselog, Map<String, String> variable) {
		Map<String, String> params = new HashMap<>(0);

		String resultstr;
		try {
			// ����ֵ����
			String path = ChangString.changparams(step.getStepPath(), variable, "��·��|��λ·��");
			if (null != path && path.contains("=")) {
				String property = path.substring(0, path.indexOf("=")).trim();
				String propertyValue = path.substring(path.indexOf("=") + 1).trim();
				// set����
				params.put("property", property.toLowerCase());
				// set����ֵ
				params.put("property_value", propertyValue);
				LogUtil.APP.info("�������Խ��������property:{};  property_value:{}", property, propertyValue);
			}
			// set��������,����ֵ����
			String operation = ChangString.changparams(step.getStepOperation().toLowerCase(), variable, "����");
			params.put("operation", operation);
			// set����ֵ,����ֵ����
			String operationValue = ChangString.changparams(step.getStepParameters(), variable, "��������");
			if (StringUtils.isNotEmpty(operationValue)) {
				params.put("operation_value", operationValue);
			}
			LogUtil.APP.info("����������������operation:{};  operation_value:{}", operation,operationValue);
			// ��ȡԤ�ڽ���ַ���
			resultstr = step.getExpectedResult();

			// setԤ�ڽ��
			if (null == resultstr || "".equals(resultstr)) {
				params.put("ExpectedResults", "");
			} else {
				String expectedResults = subComment(resultstr);

				// ����check�ֶ�
				if (expectedResults.toLowerCase().startsWith("check(")) {
					expectedResults = expectedResults.replace("Check(", "check(");
					params.put("checkproperty", expectedResults.substring(expectedResults.indexOf("check(") + 6,
							expectedResults.indexOf("=")));
					params.put("checkproperty_value", expectedResults.substring(expectedResults.indexOf("=") + 1,
							expectedResults.lastIndexOf(")")));
				}
				//����ֵ����
	            expectedResults = ChangString.changparams(expectedResults, variable, "Ԥ�ڽ��");
				params.put("ExpectedResults", expectedResults);
				LogUtil.APP.info("Ԥ�ڽ��������ExpectedResults:{}", expectedResults);
			}

			LogUtil.APP.info("�������:{} ��{}���������Զ�����������ű���ɣ�", projectcase.getCaseSign(), step.getStepSerialNumber());
			if (null != caselog) {
				caselog.insertTaskCaseLog(taskid, projectcase.getCaseId(),
						"�����ţ�" + step.getStepSerialNumber() + " �����Զ�����������ű���ɣ�", "info",
						String.valueOf(step.getStepSerialNumber()), "");
			}
		} catch (Exception e) {
			LogUtil.APP.error("�������:{} ��{}���������Զ�����������ű������쳣��", projectcase.getCaseSign(), step.getStepSerialNumber(),
					e);
			if (null != caselog) {
				caselog.insertTaskCaseLog(taskid, projectcase.getCaseId(),
						"�����ţ�" + step.getStepSerialNumber() + " �����Զ�����������ű�����", "error",
						String.valueOf(step.getStepSerialNumber()), "");
			}
			params.put("exception", "������ţ�" + projectcase.getCaseSign() + "|�����쳣,��������Ϊ�ջ��������ű�����");
			return params;
		}
		return params;
	}

	private static String subComment(String htmlStr) {
		// ����script��������ʽ
		String regExScript = "<script[^>]*?>[\\s\\S]*?</script>";
		// ����style��������ʽ
		String regExStyle = "<style[^>]*?>[\\s\\S]*?</style>";
		// ����HTML��ǩ��������ʽ
		String regExHtml = "<[^>]+>";
		// ����ո�س����з�
		String regExSpace = "[\t\r\n]";

		String scriptstr;
		if (htmlStr != null) {
			Pattern pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
			Matcher mScript = pScript.matcher(htmlStr);
			// ����script��ǩ
			htmlStr = mScript.replaceAll("");

			Pattern pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
			Matcher mStyle = pStyle.matcher(htmlStr);
			// ����style��ǩ
			htmlStr = mStyle.replaceAll("");

			Pattern pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
			Matcher mHtml = pHtml.matcher(htmlStr);
			// ����html��ǩ
			htmlStr = mHtml.replaceAll("");

			Pattern pSpace = Pattern.compile(regExSpace, Pattern.CASE_INSENSITIVE);
			Matcher mSpace = pSpace.matcher(htmlStr);
			// ���˿ո�س���ǩ
			htmlStr = mSpace.replaceAll("");

		}
		assert htmlStr != null;
		if (htmlStr.contains("/*") && htmlStr.contains("*/")) {
			String commentstr = htmlStr.substring(htmlStr.trim().indexOf("/*"), htmlStr.indexOf("*/") + 2);
			// ȥע��
			scriptstr = htmlStr.replace(commentstr, "");
		} else {
			scriptstr = htmlStr;
		}
		// ȥ���ַ���ǰ��Ŀո�
		scriptstr = trimInnerSpaceStr(scriptstr);
		// �滻�ո�ת��
		scriptstr = scriptstr.replaceAll("&nbsp;", " ");
		// ת��˫����
		scriptstr = scriptstr.replaceAll("&quot;", "\"");
		// ת�嵥����
		scriptstr = scriptstr.replaceAll("&#39;", "'");
		// ת�����ӷ�
		scriptstr = scriptstr.replaceAll("&amp;", "&");
		scriptstr = scriptstr.replaceAll("&lt;", "<");
		scriptstr = scriptstr.replaceAll("&gt;", ">");

		return scriptstr;
	}

	/***
	 * ȥ���ַ���ǰ��Ŀո��м�Ŀո���
	 * 
	 * @param str ԭʼ�ַ���
	 * @return ����ȥ�����
	 */
	public static String trimInnerSpaceStr(String str) {
		str = str.trim();
		while (str.startsWith(" ")) {
			str = str.substring(1).trim();
		}
		while (str.startsWith("&nbsp;")) {
			str = str.substring(6).trim();
		}
		while (str.endsWith(" ")) {
			str = str.substring(0, str.length() - 1).trim();
		}
		while (str.endsWith("&nbsp;")) {
			str = str.substring(0, str.length() - 6).trim();
		}
		return str;
	}

}
