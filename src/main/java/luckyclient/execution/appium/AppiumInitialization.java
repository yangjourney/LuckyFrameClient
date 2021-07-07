package luckyclient.execution.appium;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull1985
 * =================================================================
 * 
 * @author�� seagull
 * 
 * @date 2017��12��1�� ����9:29:40
 * 
 */
public class AppiumInitialization {
	/**
	 * ��ʼ��AndroidAppium
	 * @param properties �����ļ�����
	 * @return ���ذ�׿appium����
	 * @throws IOException ���쳣
	 */
	public static AndroidDriver<AndroidElement> setAndroidAppium(Properties properties) throws IOException {
		AndroidDriver<AndroidElement> appium;
		DesiredCapabilities capabilities = new DesiredCapabilities();
		File directory = new File("");
		File app = new File(directory.getCanonicalPath() + File.separator + properties.getProperty("appname"));
		capabilities.setCapability("app", app.getAbsolutePath());
		// �Զ������Է���
		capabilities.setCapability("automationName", properties.getProperty("automationName"));
		// �豸����
		capabilities.setCapability("deviceName", properties.getProperty("deviceName"));
		// ƽ̨����
		capabilities.setCapability("platformName", properties.getProperty("platformName"));
		// ϵͳ�汾
		capabilities.setCapability("platformVersion", properties.getProperty("platformVersion"));
		// ģ�����ϵ�ip��ַ
		capabilities.setCapability("udid", properties.getProperty("udid"));
		// AndroidӦ�õİ���
		capabilities.setCapability("appPackage", properties.getProperty("appPackage"));
		// ������Android Activity
		capabilities.setCapability("appActivity", properties.getProperty("appActivity"));
		// ֧���������룬���Զ���װUnicode����
		capabilities.setCapability("unicodeKeyboard", Boolean.valueOf(properties.getProperty("unicodeKeyboard")));
		// �������뷨��ԭ��״̬
		capabilities.setCapability("resetKeyboard", Boolean.valueOf(properties.getProperty("resetKeyboard")));
		// ������ǩ��apk
		capabilities.setCapability("noSign", Boolean.valueOf(properties.getProperty("noSign")));
		// �Ƿ�������°�װAPP
		capabilities.setCapability("noReset", Boolean.valueOf(properties.getProperty("noReset")));
		// �ȴ���ʱû���յ�����ر�appium
		capabilities.setCapability("newCommandTimeout", properties.getProperty("newCommandTimeout"));
		String url="http://" + properties.getProperty("appiumsever") + "/wd/hub";
		appium = new AndroidDriver<>(new URL(url), capabilities);
		int waittime = Integer.parseInt(properties.getProperty("implicitlyWait"));
		appium.manage().timeouts().implicitlyWait(waittime, TimeUnit.SECONDS);
		return appium;
	}

	/**
	 * ��ʼ��IOSAppium
	 * @param properties �����ļ�����
	 * @return ����IOS appium����
	 * @throws IOException �׳�IO�쳣
	 */
	public static IOSDriver<IOSElement> setIosAppium(Properties properties) throws IOException {
		IOSDriver<IOSElement> appium;
		DesiredCapabilities capabilities = new DesiredCapabilities();
		File directory = new File("");
		File app = new File(directory.getCanonicalPath() + File.separator + properties.getProperty("appname"));
		capabilities.setCapability("app", app.getAbsolutePath());
		// �Զ������Է���
		capabilities.setCapability("automationName", properties.getProperty("automationName"));
		// �豸����
		capabilities.setCapability("deviceName", properties.getProperty("deviceName"));
		// ƽ̨����
		capabilities.setCapability("platformName", properties.getProperty("platformName"));
		// ϵͳ�汾
		capabilities.setCapability("platformVersion", properties.getProperty("platformVersion"));
		// ģ�����ϵ�ip��ַ
		capabilities.setCapability("udid", properties.getProperty("udid"));
		// ֧���������룬���Զ���װUnicode����
		capabilities.setCapability("unicodeKeyboard", Boolean.valueOf(properties.getProperty("unicodeKeyboard")));
		// �������뷨��ԭ��״̬
		capabilities.setCapability("resetKeyboard", Boolean.valueOf(properties.getProperty("resetKeyboard")));
		// ������ǩ��apk
		capabilities.setCapability("noSign", Boolean.valueOf(properties.getProperty("noSign")));
		// �Ƿ�������°�װAPP
		capabilities.setCapability("noReset", Boolean.valueOf(properties.getProperty("noReset")));
		// �ȴ���ʱû���յ�����ر�appium
		capabilities.setCapability("newCommandTimeout", properties.getProperty("newCommandTimeout"));
		String url="http://" + properties.getProperty("appiumsever") + "/wd/hub";
		appium = new IOSDriver<>(new URL(url), capabilities);
		int waittime = Integer.parseInt(properties.getProperty("implicitlyWait"));
		appium.manage().timeouts().implicitlyWait(waittime, TimeUnit.SECONDS);
		return appium;
	}
	
}
