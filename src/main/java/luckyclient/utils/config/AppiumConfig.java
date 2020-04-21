package luckyclient.utils.config;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import luckyclient.utils.LogUtil;

/**
 * ��ʼ��Appium���ò���
 * @author seagull
 *
 */
public class AppiumConfig {
	private static final Properties SYS_CONFIG = new Properties();
	private static final String SYS_CONFIG_FILE = "/appium_config.properties";
	static{
		try {
		    InputStream in = new BufferedInputStream(AppiumConfig.class.getResourceAsStream(SYS_CONFIG_FILE));
			SYS_CONFIG.load(new InputStreamReader(in, StandardCharsets.UTF_8));
		} catch (IOException e) {
			LogUtil.APP.error("��ȡ�ƶ���appium_config.properties�����ļ������쳣�����飡", e);
		}
	}
	private AppiumConfig(){}
	public static Properties getConfiguration(){
		return SYS_CONFIG;
	}
}
