package luckyclient.execution.dispose.actionkeyword;

/**
 * �����ؼ��ֵĴ���ӿ�
 * @author: sunshaoyan
 * @date: Created on 2019/4/13
 */
public interface ActionKeyWordParser {

	/**
	 * ��Թؼ��ֵĳ��󷽷�
	 * @param actionParams �ؼ���
	 * @param testResult ������Ĳ��Խ��
	 * @return ���ش�����
	 * @author Seagull
	 * @date 2019��8��8��
	 */
    String parse(String actionParams, String testResult);
}
