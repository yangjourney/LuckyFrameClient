package luckyclient.execution.appium.iosex;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.Augmenter;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import luckyclient.utils.LogUtil;
import springboot.RunService;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull1985
 * =================================================================
 * @author�� seagull 
 * @date 2018��2��2��
 * 
 */
public class IosBaseAppium {

	/**
	 * IOS�ֻ������ͼ
	 * @param appium appium����
	 * @param imagname ��ͼ����
	 */
	public static void screenShot(IOSDriver<IOSElement> appium, String imagname){
		imagname = imagname + ".png";
		String relativelyPath = RunService.APPLICATION_HOME;
		String pngpath=relativelyPath +File.separator+ "log"+File.separator+"ScreenShot" +File.separator+ imagname;
		
		try {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				LogUtil.APP.error("IOS�ֻ������ͼ���߳����쳣", e);
			}
			File imageFile = ((TakesScreenshot) (new Augmenter().augment(appium))).getScreenshotAs(OutputType.FILE);
			File screenFile = new File(pngpath);
			FileUtils.copyFile(imageFile, screenFile);
			imageFile.deleteOnExit();
			LogUtil.APP
			.info("�ѶԵ�ǰ������н�ͼ��������ͨ������ִ�н������־��ϸ�鿴��Ҳ����ǰ���ͻ����ϲ鿴...��{}��",pngpath);
		} catch (IOException e) {
			LogUtil.APP.error("IOS�ֻ������ͼ�����쳣", e);
		}
	}

	/**
	 * appium��֧���������� �ο���robotium����js��ʽΪԪ��ֱ������value������
	 * ����Selenium��Webdriverִ��js����ʵ����������
	 * @param appium appium����
	 * @param preferences ������������
	 * @param value ����ֵ
	 */
	public static void sendChinese(IOSDriver<IOSElement> appium, String preferences, String value) {
		((JavascriptExecutor) appium).executeScript("document.getElementByName('" + preferences + "').value='" + value + "'");
	}

	/**
	 * js webview ֧��4.1��4.4 ҳ�滬��
	 * @param appium appium��ʼ������
	 * @param sX ��ʼX����
	 * @param sY ��ʼY����
	 * @param eX ����X����
	 * @param eY ����Y����
	 * @param duration ����ʱ��
	 */
	public static void webViewSwipe(IOSDriver<IOSElement> appium, Double sX, Double sY, Double eX, Double eY, Double duration) {
		JavascriptExecutor js;
		HashMap<String, Double> swipeObject;
		try {
			// ����
			js = appium;
			swipeObject = new HashMap<>(5);
			swipeObject.put("startX", sX);
			swipeObject.put("startY", sY);
			swipeObject.put("endX", eX);
			swipeObject.put("endY", eY);
			swipeObject.put("duration", duration);
			js.executeScript("mobile: swipe", swipeObject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("IOS�ֻ����������쳣", e);
		}
	}

	/**
	 * ���� ADBֱ�ӻ��� ֧��4.1��4.4
	 * @param appium appium��ʼ������
	 * @param sX ��ʼX����
	 * @param sY ��ʼY����
	 * @param eX ����X����
	 * @param eY ����Y����
	 */
	public static void adbSwipe(IOSDriver<IOSElement> appium, Double sX, Double sY, Double eX, Double eY) {
		int xLine;
		int yLine;
		int sX2;
		int sY2;
		int eX2;
		int eY2;
		try {
			// ����
			xLine = appium.manage().window().getSize().getWidth();
			yLine = appium.manage().window().getSize().getHeight();

			sX2 = (int) (xLine * sX);
			sY2 = (int) (yLine * sY);
			eX2 = (int) (xLine * eX);
			eY2 = (int) (yLine * eY);
			// logger.info("����11111111");
			Runtime.getRuntime()
					.exec("adb -s " + "IOS" + " shell input swipe " + sX2 + " " + sY2 + " " + eX2 + " " + eY2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("IOS�ֻ����� ADBֱ�ӻ��������쳣", e);
		}
	}

	/**
	 * ��Ļ����¼�
	 * @param drivers appium��ʼ������
	 * @param x ���X����
	 * @param y ���Y����
	 * @param duration ����ʱ��
	 */
	public static void clickScreenForJs(IOSDriver<IOSElement> drivers, int x, int y, int duration) {
		HashMap<String, Integer> tapObject = new HashMap<>(3);
		tapObject.put("x", x);
		tapObject.put("y", y);
		tapObject.put("duration", duration);
		((JavascriptExecutor) drivers).executeScript("mobile: tap", tapObject);
	}

	/**
	 * ��סҳ�水��Ļ�������ϻ���(��ָ���£�ҳ������)
	 * @param driver appium��ʼ������
	 * @param second ����ʱ��
	 * @param num ��������
	 */
	public static void swipePageUp(IOSDriver<IOSElement> driver, Double second, int num) {
		int nanos = (int) (second * 1000);
		Duration duration = Duration.ofNanos(nanos);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		IOSTouchAction action = new IOSTouchAction(driver);
		
		for (int i = 0; i <= num; i++) {
			action.press(PointOption.point(width / 2, 20)).waitAction(WaitOptions.waitOptions(duration))
					.moveTo(PointOption.point(width / 2, height-20)).release().perform();
		}
	}

	/**
	 * ��סҳ�水��Ļ�������»���(��ָ���ϣ�ҳ������)
	 * @param driver appium��ʼ������
	 * @param second ����ʱ��
	 * @param num ��������
	 */
	public static void swipePageDown(IOSDriver<IOSElement> driver,Double second,int num){
		int nanos = (int) (second * 1000);
		Duration duration = Duration.ofNanos(nanos);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		IOSTouchAction action = new IOSTouchAction(driver);
		for (int i = 0; i <= num; i++) {
			action.press(PointOption.point(width / 2, height-20)).waitAction(WaitOptions.waitOptions(duration))
					.moveTo(PointOption.point(width / 2, 20)).release().perform();
		}
	}

	/**
	 * ��סҳ�水��Ļ�������󻬶�(��ָ����ҳ���������)
	 * @param driver appium��ʼ������
	 * @param second ����ʱ��
	 * @param num ��������
	 */
	public static void swipePageLeft(IOSDriver<IOSElement> driver, Double second, int num) {
		int nanos = (int) (second * 1000);
		Duration duration = Duration.ofNanos(nanos);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		IOSTouchAction action = new IOSTouchAction(driver);
		for (int i = 0; i <= num; i++) {
			action.press(PointOption.point(width - 10, height / 2)).waitAction(WaitOptions.waitOptions(duration))
					.moveTo(PointOption.point(10, height / 2)).release().perform();
		}
	}

	/**
	 * ��סҳ�水��Ļ�������һ���(��ָ���ң�ҳ������)
	 * @param driver appium��ʼ������
	 * @param second ����ʱ��
	 * @param num ��������
	 */
	public static void swipePageRight(IOSDriver<IOSElement> driver, Double second, int num) {
		int nanos = (int) (second * 1000);
		Duration duration = Duration.ofNanos(nanos);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		IOSTouchAction action = new IOSTouchAction(driver);
		for (int i = 0; i <= num; i++) {
			action.press(PointOption.point(10, height / 2)).waitAction(WaitOptions.waitOptions(duration))
					.moveTo(PointOption.point(width - 10, height / 2)).release().perform();
		}
	}
     
}
