package luckyclient.driven;

import java.util.Properties;

import luckyclient.utils.DbOperation;
import luckyclient.utils.config.DrivenConfig;

/**
 * �ṩ���ݿ��ѯ������Ĭ�ϲ�������
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2020��2��17��
 */
public class DbDriven {

	/**
	 * ִ��SQL���
	 * @param sql ִ��SQL���
	 * @return ����ִ�н�������ż���ʾ
	 */
	public String executeSql(String sql) {
		Properties properties = DrivenConfig.getConfiguration();
		String url = properties.getProperty("db.url");
		String username = properties.getProperty("db.username");
		String password = properties.getProperty("db.password");
		DbOperation db=new DbOperation(url,username,password);
		return db.executeSql(sql);
	}

	/**
	 * ��ѯSQL���
	 * @param sql ��ѯSQL
	 * @return ���ز�ѯ���
	 * @throws Exception �쳣��Ϣ
	 */
	public String executeQuery(String sql) throws Exception{
		Properties properties = DrivenConfig.getConfiguration();
		String url = properties.getProperty("db.url");
		String username = properties.getProperty("db.username");
		String password = properties.getProperty("db.password");
		DbOperation db=new DbOperation(url,username,password);
		return db.executeQuery(sql);
	}

}
