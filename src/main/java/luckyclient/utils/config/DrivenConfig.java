package luckyclient.utils.config;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import luckyclient.utils.LogUtil;

/**
 * ��ʼ�����ݿ���������
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2020��2��17��
 */
public class DrivenConfig {
	private static final Properties SYS_CONFIG = new Properties();
	private static final String SYS_CONFIG_FILE = "/TestDriven/driven_config.properties";
	static{
		try {
		    InputStream in = new BufferedInputStream(DrivenConfig.class.getResourceAsStream(SYS_CONFIG_FILE));
			SYS_CONFIG.load(new InputStreamReader(in, StandardCharsets.UTF_8));
		} catch (IOException e) {
			LogUtil.APP.error("��ȡ��������driven_config.properties�����ļ������쳣�����飡", e);
		}
	}
	private DrivenConfig(){}
	public static Properties getConfiguration(){
		return SYS_CONFIG;
	}
}
