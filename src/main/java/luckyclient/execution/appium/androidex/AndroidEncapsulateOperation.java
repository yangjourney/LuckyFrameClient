package luckyclient.execution.appium.androidex;

import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select;

import cn.hutool.core.util.StrUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import luckyclient.execution.dispose.ChangString;
import luckyclient.utils.LogUtil;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull1985
 * =================================================================
 * 
 * @author Seagull
 * @date 2017��1��29�� ����9:31:42
 */
public class AndroidEncapsulateOperation {
	/**
	 * select�ؼ��ؼ��ִ���
	 * @param ae UI����
	 * @param operation �����ؼ���
	 * @param operationValue ����ֵ
	 * @return ���ز������
	 */
	public static String selectOperation(AndroidElement ae, String operation, String operationValue) {
		String result = "";
		// �����������
		Select select = new Select(ae);

		// �����������¼�
		switch (operation) {
		case "selectbyvisibletext":
			select.selectByVisibleText(operationValue);
			LogUtil.APP
					.info("���������ͨ��VisibleText����ѡ��...��VisibleText����ֵ:{}��",operationValue);
			break;
		case "selectbyvalue":
			select.selectByValue(operationValue);
			LogUtil.APP.info("���������ͨ��Value����ѡ��...��Value����ֵ:{}��",operationValue);
			break;
		case "selectbyindex":
			select.selectByIndex(Integer.parseInt(operationValue));
			LogUtil.APP.info("���������ͨ��Index����ѡ��...��Index����ֵ:{}��",operationValue);
			break;
		case "isselect":
			result = "��ȡ����ֵ�ǡ�" + ae.isSelected() + "��";
			LogUtil.APP.info("�ж϶����Ƿ��Ѿ���ѡ��...�����ֵ:{}��",ae.isSelected());
			break;
		default:
			break;
		}
		return result;
	}

	public static String getOperation(AndroidElement ae, String operation, String value) {
		String result = "";
		// ��ȡ������
		switch (operation) {
		case "gettext":
			result = "��ȡ����ֵ�ǡ�" + ae.getText() + "��";
			LogUtil.APP.info("getText��ȡ����text����...��text����ֵ:{}��",result);
			break; // ��ȡ���������
		case "gettagname":
			result = "��ȡ����ֵ�ǡ�" + ae.getTagName() + "��";
			LogUtil.APP.info("getTagName��ȡ����tagname����...��tagname����ֵ:{}��",result);
			break;
		case "getattribute":
			result = "��ȡ����ֵ�ǡ�" + ae.getAttribute(value) + "��";
			LogUtil.APP
					.info("getAttribute��ȡ����{}������...��{}����ֵ:{}��",value,value,result);
			break;
		case "getcssvalue":
			result = "��ȡ����ֵ�ǡ�" + ae.getCssValue(value) + "��";
			LogUtil.APP
					.info("getCssValue��ȡ����{}������...��{}����ֵ:{}��",value,value,result);
			break;
		default:
			break;
		}
		return result;
	}

	public static String objectOperation(AndroidDriver<AndroidElement> appium, AndroidElement ae, String operation,
			String operationValue, String property, String propertyValue) {
		String result = "";
		AndroidTouchAction action = new AndroidTouchAction(appium);

		// ����WebElement�������
		switch (operation) {
		case "click":
			ae.click();
			result = "click�������...������λ����:" + property + "; ��λ����ֵ:" + propertyValue + "��";
			LogUtil.APP
					.info("click�������...������λ����:{}; ��λ����ֵ:{}��",property,propertyValue);
			break;
		case "sendkeys":
			ae.sendKeys(operationValue);
			result = "sendKeys��������...������λ����:" + property + "; ��λ����ֵ:" + propertyValue + "; ����ֵ:" + operationValue
					+ "��";
			LogUtil.APP.info("sendkeys��������...������λ����:{}; ��λ����ֵ:{}; ����ֵ:{}��",property,propertyValue,operationValue);
			break;
		case "clear":
			ae.clear();
			result = "clear��������...������λ����:" + property + "; ��λ����ֵ:" + propertyValue + "��";
			LogUtil.APP
					.info("clear��������...������λ����:{}; ��λ����ֵ:{}��",property,propertyValue);
			break; // ��������
		case "isenabled":
			result = "��ȡ����ֵ�ǡ�" + ae.isEnabled() + "��";
			LogUtil.APP.info("��ǰ�����ж��Ƿ���ò���ֵΪ��{}��",ae.isEnabled());
			break;
		case "isdisplayed":
			result = "��ȡ����ֵ�ǡ�" + ae.isDisplayed() + "��";
			LogUtil.APP.info("��ǰ�����ж��Ƿ�ɼ�����ֵΪ��{}��",ae.isDisplayed());
			break;
		case "exjsob":
			((JavascriptExecutor) appium).executeScript(operationValue, ae);
			result = "ִ��JS...��" + operationValue + "��";
			LogUtil.APP.info("ִ��JS...��{}��",operationValue);
			break;
		case "longpresselement":
			LongPressOptions lpoptions = new LongPressOptions();
			lpoptions.withElement(ElementOption.element(ae));
			if (null != operationValue && ChangString.isNumeric(operationValue)) {
				int nanos = Integer.parseInt(operationValue) * 1000;
				Duration duration = Duration.ofNanos(nanos);
				lpoptions.withDuration(duration);
			}
			action.longPress(lpoptions).release().perform();
			result = "longpresselement����Ļָ��Ԫ���ϰ�ס" + operationValue + "��...������λ����:" + property + "; ��λ����ֵ:"
					+ propertyValue + "��";
			LogUtil.APP.info("longpresselement����Ļָ��Ԫ���ϰ�ס{}��...������λ����:{}; ��λ����ֵ:{}��",operationValue,property,propertyValue);
			break;
		default:
			break;
		}
		return result;
	}

	public static String alertOperation(AndroidDriver<AndroidElement> appium, String operation) {
		String result = "";
		Alert alert = appium.switchTo().alert();
		switch (operation) {
		case "alertaccept":
			alert.accept();
			LogUtil.APP.info("�����������ͬ��...");
			break;
		case "alertdismiss":
			alert.dismiss();
			LogUtil.APP.info("�����������ȡ��...");
			break;
		case "alertgettext":
			result = "��ȡ����ֵ�ǡ�" + alert.getText() + "��";
			LogUtil.APP.info("���������ͨ��getText��ȡ����text����...��Text����ֵ:{}��",alert.getText());
			break;
		default:
			break;
		}
		return result;
	}

	public static String driverOperation(AndroidDriver<AndroidElement> appium, String operation, String operationValue)
			throws Exception {
		String result = "";
		AndroidTouchAction action = new AndroidTouchAction(appium);
		// ����ҳ��������
		switch (operation) {
		case "getcontexthandles":
			Set<String> handles = appium.getContextHandles();
			int handlenum = 1;
			for (String handle : handles) {
				if (String.valueOf(handlenum).equals(operationValue)) {
					if (appium.getContext().equals(handle)) {
						result = "��ע�⣬��ָ����ContextHandle���ǵ�ǰҳ��Ŷ����ȡ����ֵ�ǡ�" + handle + "��";
					} else {
						result = "ָ��ContextHandler��˳��ֵ��" + operationValue + ",��ȡ����ֵ�ǡ�" + handle + "��";
					}
					break;
				}
				handlenum++;
			}
			LogUtil.APP.info("getContext��ȡ���ھ��...{}",result);
			break;
		case "exjs":
			((JavascriptExecutor) appium).executeScript(operationValue);
			result = "ִ��JS...��" + operationValue + "��";
			LogUtil.APP.info("ִ��JS...��{}��",operationValue);
			break;
		case "exAdbShell":
			Runtime.getRuntime().exec(operationValue);
			result = "ִ�а�׿adb����...��" + operationValue + "��";
			LogUtil.APP.info("ִ�а�׿adb����...��{}��",operationValue);		   
		    break;
		case "androidkeycode":
			// ģ���ֻ�����
			try {
				if (StrUtil.isNotBlank(operationValue)) {
					KeyEvent keyEvent = new KeyEvent();
					appium.pressKey(keyEvent.withKey(AndroidKey.valueOf(operationValue)));
					result = "ģ���ֻ����̷���ָ��...keycode��" + operationValue + "��";
					LogUtil.APP.info("ģ���ֻ����̷���ָ��...keycode��{}��", operationValue);
				} else {
					result = "ģ���ֻ�����ʧ�ܣ����̲���Ϊ�գ�������Ĳ�����";
					LogUtil.APP.info("ģ���ֻ�����ʧ�ܣ����̲���Ϊ�գ�������Ĳ�����");
				}
			} catch (IllegalArgumentException ae) {
				result = "ģ���ֻ�����ʧ�ܣ�û���ҵ���Ӧ�İ���������������Ĳ�����";
				LogUtil.APP.info("ģ���ֻ�����ʧ�ܣ�û���ҵ���Ӧ�İ���������������Ĳ�����");
			}
			break;
		// �����ֻ�����
		case "hidekeyboard":
			appium.hideKeyboard();
			result = "�����ֻ�����...��hideKeyboard��";
			LogUtil.APP.info("�����ֻ�����...��hideKeyboard��");
			break;
		case "gotocontext":
			Set<String> ctNames = appium.getContextHandles();
			int flag = 0;
			for (String contextName : ctNames) {
				if (contextName.contains(operationValue)) {
					flag = 1;
					appium.context(contextName);
					break;
				}
			}
			if (flag == 1) {
				result = "�л�context����" + operationValue + "��";
				LogUtil.APP.info("�л�context����{}��",operationValue);
			} else {
				result = "�л�contextʧ�ܣ�δ�ҵ�contextNameֵΪ��" + operationValue + "���Ķ���";
				LogUtil.APP.info("�л�contextʧ�ܣ�δ�ҵ�contextNameֵΪ��{}���Ķ���",operationValue);
			}
			break;
		case "getcontext":
			result = "��ȡ����ֵ�ǡ�" + appium.getContext() + "��";
			LogUtil.APP.info("��ȡҳ��Context...��{}��",appium.getContext());
			break;
		case "gettitle":
			result = "��ȡ����ֵ�ǡ�" + appium.getTitle() + "��";
			LogUtil.APP.info("��ȡҳ��gettitle...��{}��",appium.getTitle());
			break;
		case "swipeup":
			String[] tempup = operationValue.split("\\|", -1);
			if (null != tempup[0] && ChangString.isNumeric(tempup[0])) {
				Double second = Double.valueOf(tempup[0]);
				if (null != tempup[1] && ChangString.isNumeric(tempup[1])) {
					int num = Integer.parseInt(tempup[1]);
					AndroidBaseAppium.swipePageDown(appium, second, num);
					result = "swipeup��ָ���ϻ�������...��|������" + second + "|" + num + "��";
					LogUtil.APP.info("swipeup��ָ���ϻ�������...��|������{}|{}��",second,num);
				} else {
					result = "swipeup��ָ���ϻ��������жϴ��������쳣��" + tempup[1] + "��";
					LogUtil.APP.info("swipeup��ָ���ϻ��������жϴ��������쳣��{}��",tempup[1]);
				}
			} else {
				result = "swipeup��ָ���ϻ��������ж�ʱ������쳣��" + tempup[0] + "��";
				LogUtil.APP.info("swipeup��ָ���ϻ��������ж�ʱ������쳣��{}��",tempup[0]);
			}
			break;
		case "swipedown":
			String[] tempdown = operationValue.split("\\|", -1);
			if (null != tempdown[0] && ChangString.isNumeric(tempdown[0])) {
				Double second = Double.valueOf(tempdown[0]);
				if (null != tempdown[1] && ChangString.isNumeric(tempdown[1])) {
					int num = Integer.parseInt(tempdown[1]);
					AndroidBaseAppium.swipePageUp(appium, second, num);
					result = "swipedown��ָ���»�������...��|������" + second + "|" + num + "��";
					LogUtil.APP.info("swipedown��ָ���»�������...��|������{}|{}��",second,num);
				} else {
					result = "swipedown��ָ���»��������жϴ��������쳣��" + tempdown[1] + "��";
					LogUtil.APP.info("swipedown��ָ���»��������жϴ��������쳣��{}��",tempdown[1]);
				}
			} else {
				result = "swipedown��ָ���»��������ж�ʱ������쳣��" + tempdown[0] + "��";
				LogUtil.APP.info("swipedown��ָ���»��������ж�ʱ������쳣��{}��",tempdown[0]);
			}
			break;
		case "swipeleft":
			String[] templeft = operationValue.split("\\|", -1);
			if (null != templeft[0] && ChangString.isNumeric(templeft[0])) {
				Double second = Double.valueOf(templeft[0]);
				if (null != templeft[1] && ChangString.isNumeric(templeft[1])) {
					int num = Integer.parseInt(templeft[1]);
					AndroidBaseAppium.swipePageRight(appium, second, num);
					result = "swipleft��ָ���󻬶�����...��|������" + second + "|" + num + "��";
					LogUtil.APP.info("swipleft��ָ���󻬶�����...��|������{}|{}��",second,num);
				} else {
					result = "swipleft��ָ���󻬶������жϴ��������쳣��" + templeft[1] + "��";
					LogUtil.APP.info("swipleft��ָ���󻬶������жϴ��������쳣��{}��",templeft[1]);
				}
			} else {
				result = "swipleft��ָ���󻬶������ж�ʱ������쳣��" + templeft[0] + "��";
				LogUtil.APP.info("swipleft��ָ���󻬶������ж�ʱ������쳣��{}��",templeft[0]);
			}
			break;
		case "swiperight":
			String[] tempright = operationValue.split("\\|", -1);
			if (null != tempright[0] && ChangString.isNumeric(tempright[0])) {
				Double second = Double.valueOf(tempright[0]);
				if (null != tempright[1] && ChangString.isNumeric(tempright[1])) {
					int num = Integer.parseInt(tempright[1]);
					AndroidBaseAppium.swipePageLeft(appium, second, num);
					result = "swipright��ָ���һ�������...��|������" + second + "|" + num + "��";
					LogUtil.APP.info("swipright��ָ���һ�������...��|������{}|{}��",second,num);
				} else {
					result = "swipright��ָ���һ��������жϴ��������쳣��" + tempright[1] + "��";
					LogUtil.APP.info("swipright��ָ���һ��������жϴ��������쳣��{}��",tempright[1]);
				}
			} else {
				result = "swipright��ָ���һ��������ж�ʱ������쳣��" + tempright[0] + "��";
				LogUtil.APP.info("swipright��ָ���һ��������ж�ʱ������쳣��{}��",tempright[0]);
			}
			break;
		case "swipepageup":
			String[] tempPageUp = operationValue.split("\\|", -1);
			if (null != tempPageUp[0] && ChangString.isNumeric(tempPageUp[0])) {
				Double second = Double.valueOf(tempPageUp[0]);
				if (null != tempPageUp[1] && ChangString.isNumeric(tempPageUp[1])) {
					int num = Integer.parseInt(tempPageUp[1]);
					AndroidBaseAppium.swipePageUp(appium, second, num);
					result = "swipeupҳ�����ϻ�������...��|������" + second + "|" + num + "��";
					LogUtil.APP.info("swipeupҳ�����ϻ�������...��|������{}|{}��",second,num);
				} else {
					result = "swipeupҳ�����ϻ��������жϴ��������쳣��" + tempPageUp[1] + "��";
					LogUtil.APP.info("swipeupҳ�����ϻ��������жϴ��������쳣��{}��",tempPageUp[1]);
				}
			} else {
				result = "swipeupҳ�����ϻ��������ж�ʱ������쳣��" + tempPageUp[0] + "��";
				LogUtil.APP.info("swipeupҳ�����ϻ��������ж�ʱ������쳣��{}��",tempPageUp[0]);
			}
			break;
		case "swipepagedown":
			String[] tempPageDown = operationValue.split("\\|", -1);
			if (null != tempPageDown[0] && ChangString.isNumeric(tempPageDown[0])) {
				Double second = Double.valueOf(tempPageDown[0]);
				if (null != tempPageDown[1] && ChangString.isNumeric(tempPageDown[1])) {
					int num = Integer.parseInt(tempPageDown[1]);
					AndroidBaseAppium.swipePageDown(appium, second, num);
					result = "swipedownҳ�����»�������...��|������" + second + "|" + num + "��";
					LogUtil.APP.info("swipedownҳ�����»�������...��|������{}|{}��",second,num);
				} else {
					result = "swipedownҳ�����»��������жϴ��������쳣��" + tempPageDown[1] + "��";
					LogUtil.APP.info("swipedownҳ�����»��������жϴ��������쳣��{}��",tempPageDown[1]);
				}
			} else {
				result = "swipedownҳ�����»��������ж�ʱ������쳣��" + tempPageDown[0] + "��";
				LogUtil.APP.info("swipedownҳ�����»��������ж�ʱ������쳣��{}��",tempPageDown[0]);
			}
			break;
		case "swipepageleft":
			String[] tempPageLeft = operationValue.split("\\|", -1);
			if (null != tempPageLeft[0] && ChangString.isNumeric(tempPageLeft[0])) {
				Double second = Double.valueOf(tempPageLeft[0]);
				if (null != tempPageLeft[1] && ChangString.isNumeric(tempPageLeft[1])) {
					int num = Integer.parseInt(tempPageLeft[1]);
					AndroidBaseAppium.swipePageLeft(appium, second, num);
					result = "swipleftҳ�����󻬶�����...��|������" + second + "|" + num + "��";
					LogUtil.APP.info("swipleftҳ�����󻬶�����...��|������{}|{}��",second,num);
				} else {
					result = "swipleftҳ�����󻬶������жϴ��������쳣��" + tempPageLeft[1] + "��";
					LogUtil.APP.info("swipleftҳ�����󻬶������жϴ��������쳣��{}��",tempPageLeft[1]);
				}
			} else {
				result = "swipleftҳ�����󻬶������ж�ʱ������쳣��" + tempPageLeft[0] + "��";
				LogUtil.APP.info("swipleftҳ�����󻬶������ж�ʱ������쳣��{}��",tempPageLeft[0]);
			}
			break;
		case "swipepageright":
			String[] tempPageRight = operationValue.split("\\|", -1);
			if (null != tempPageRight[0] && ChangString.isNumeric(tempPageRight[0])) {
				Double second = Double.valueOf(tempPageRight[0]);
				if (null != tempPageRight[1] && ChangString.isNumeric(tempPageRight[1])) {
					int num = Integer.parseInt(tempPageRight[1]);
					AndroidBaseAppium.swipePageRight(appium, second, num);
					result = "swiprightҳ�����һ�������...��|������" + second + "|" + num + "��";
					LogUtil.APP.info("swiprightҳ�����һ�������...��|������{}|{}��",second,num);
				} else {
					result = "swiprightҳ�����һ��������жϴ��������쳣��" + tempPageRight[1] + "��";
					LogUtil.APP.info("swiprightҳ�����һ��������жϴ��������쳣��{}��",tempPageRight[1]);
				}
			} else {
				result = "swiprightҳ�����һ��������ж�ʱ������쳣��" + tempPageRight[0] + "��";
				LogUtil.APP.info("swiprightҳ�����һ��������ж�ʱ������쳣��{}��",tempPageRight[0]);
			}
			break;
		case "longpressxy":
			String[] longpressxy = operationValue.split("\\|", -1);
			if (null != longpressxy[0] && ChangString.isNumeric(longpressxy[0])) {
				int longpressx = Integer.parseInt(longpressxy[0]);
				if (null != longpressxy[1] && ChangString.isNumeric(longpressxy[1])) {
					int longpressy = Integer.parseInt(longpressxy[1]);
					if (null != longpressxy[2] && ChangString.isNumeric(longpressxy[2])) {
						LongPressOptions lpoptions = new LongPressOptions();
						lpoptions.withPosition(PointOption.point(longpressx, longpressy));
						int nanos = Integer.parseInt(longpressxy[2]) * 1000;
						Duration duration = Duration.ofNanos(nanos);
						lpoptions.withDuration(duration);
						action.longPress(lpoptions).release().perform();
						result = "longpressxy����Ļָ��XY�����ϰ�ס" + longpressxy[2] + "��...X|Y��" + longpressx + "|" + longpressy
								+ "��";
						LogUtil.APP.info("longpressxy����Ļָ��XY�����ϰ�ס{}��...X|Y��{}|{}��",longpressxy[2],longpressx,longpressy);
					} else {
						action.longPress(PointOption.point(longpressx, longpressy)).release().perform();
						result = "longpressxy����Ļָ��XY�����ϳ���...X|Y��" + longpressx + "|" + longpressy + "��";
						LogUtil.APP.info("longpressxy����Ļָ��XY�����ϳ���...X|Y��{}|{}��",longpressx,longpressy);
					}
				} else {
					result = "longpressxy����ָ����Y���괦������쳣��" + longpressxy[1] + "��";
					LogUtil.APP.info("longpressxy����ָ����Y���괦������쳣��{}��",longpressxy[1]);
				}
			} else {
				result = "longpressxy����ָ����X���괦������쳣��" + longpressxy[0] + "��";
				LogUtil.APP.info("longpressxy����ָ����X���괦������쳣��{}��",longpressxy[0]);
			}
			break;
		case "pressxy":
			String[] pressxy = operationValue.split("\\|", -1);
			if (null != pressxy[0] && ChangString.isNumeric(pressxy[0])) {
				int pressx = Integer.parseInt(pressxy[0]);
				if (null != pressxy[1] && ChangString.isNumeric(pressxy[1])) {
					int pressy = Integer.parseInt(pressxy[1]);
					action.press(PointOption.point(pressx, pressy)).release().perform();
					result = "pressxy����Ļָ��XY�����ϵ��...X|Y��" + pressx + "|" + pressy + "��";
					LogUtil.APP.info("pressxy����Ļָ��XY�����ϵ��...X|Y��{}|{}��",pressx,pressy);
				} else {
					result = "pressxy����ָ����Y���괦������쳣��" + pressxy[1] + "��";
					LogUtil.APP.info("pressxy����ָ����Y���괦������쳣��{}��",pressxy[1]);
				}
			} else {
				result = "pressxy����ָ����X���괦������쳣��" + pressxy[0] + "��";
				LogUtil.APP.info("pressxy����ָ����X���괦������쳣��{}��",pressxy[0]);
			}
			break;
		case "tapxy":
			String[] tapxy = operationValue.split("\\|", -1);
			if (null != tapxy[0] && ChangString.isNumeric(tapxy[0])) {
				int tapx = Integer.parseInt(tapxy[0]);
				if (null != tapxy[1] && ChangString.isNumeric(tapxy[1])) {
					int tapy = Integer.parseInt(tapxy[1]);
					action.tap(PointOption.point(tapx, tapy)).release().perform();
					result = "tapxy����Ļָ��XY���������...X|Y��" + tapx + "|" + tapy + "��";
					LogUtil.APP.info("tapxy����Ļָ��XY���������...X|Y��{}|{}��",tapx,tapy);
				} else {
					result = "tapxy����ָ����Y���괦������쳣��" + tapxy[1] + "��";
					LogUtil.APP.info("tapxy����ָ����Y���괦������쳣��{}��",tapxy[1]);
				}
			} else {
				result = "tapxy����ָ����X���괦������쳣��" + tapxy[0] + "��";
				LogUtil.APP.info("tapxy����ָ����X���괦������쳣��{}��",tapxy[0]);
			}
			break;
		case "jspressxy":
			String[] jspressxy = operationValue.split("\\|", -1);
			if (null != jspressxy[0] && ChangString.isNumeric(jspressxy[0])) {
				int jspressx = Integer.parseInt(jspressxy[0]);
				if (null != jspressxy[1] && ChangString.isNumeric(jspressxy[1])) {
					int jspressy = Integer.parseInt(jspressxy[1]);
					if (null != jspressxy[2] && ChangString.isNumeric(jspressxy[2])) {
						AndroidBaseAppium.clickScreenForJs(appium, jspressx, jspressy, Integer.parseInt(jspressxy[2]));
						result = "jspressxy����Ļָ��XY�����ϰ�" + jspressxy[2] + "��...X|Y��" + jspressx + "|" + jspressy + "��";
						LogUtil.APP.info("jspressxy����Ļָ��XY�����ϰ�{}��...X|Y��{}|{}��",jspressxy[2],jspressx,jspressy);
					} else {
						AndroidBaseAppium.clickScreenForJs(appium, jspressx, jspressy, 2);
						result = "jspressxy����Ļָ��XY�����ϰ�2��(����ʱ���ж��쳣��ʹ��Ĭ��2��ʱ��)...X|Y��" + jspressx + "|" + jspressy + "��";
						LogUtil.APP.info("jspressxy����Ļָ��XY�����ϰ�2��(����ʱ���ж��쳣��ʹ��Ĭ��2��ʱ��)...X|Y��{}|{}��",jspressx,jspressy);
					}
				} else {
					result = "jspressxy����ָ����Y���괦������쳣��" + jspressxy[1] + "��";
					LogUtil.APP.info("jspressxy����ָ����Y���괦������쳣��{}��",jspressxy[1]);
				}
			} else {
				result = "jspressxy����ָ����X���괦������쳣��" + jspressxy[0] + "��";
				LogUtil.APP.info("jspressxy����ָ����X���괦������쳣��{}��",jspressxy[0]);
			}
			break;
		case "moveto":
			String[] movexy = operationValue.split("\\|", -1);
			if (null != movexy[0] && !"".equals(movexy[0])) {
				String[] startxy = movexy[0].split(",", -1);
				int startx = Integer.parseInt(startxy[0]);
				int starty = Integer.parseInt(startxy[1]);
				for (int movexyi = 1; movexyi < movexy.length; movexyi++) {
					if (null != movexy[movexyi] && !"".equals(movexy[movexyi])) {
						String[] endxy = movexy[movexyi].split(",", -1);
						int endx = Integer.parseInt(endxy[0]);
						int endy = Integer.parseInt(endxy[1]);
						// ��ȷ���Ƿ���API��bug,ֻ��һ��MOVEʱ����XY���꣬������һ��MOVEʱ��������ƫ������
						if (movexy.length < 3) {
							action.press(PointOption.point(startx, starty))
									.waitAction(WaitOptions.waitOptions(Duration.ofNanos(1500)))
									.moveTo(PointOption.point(endx, endy));
							LogUtil.APP.info("�ӿ�ʼ���꡾{},{}���϶������꡾{},{}��",startxy[0],startxy[1],endxy[0],endxy[1]);
						} else {
							if (movexyi == 1) {
								action.press(PointOption.point(startx, starty))
										.waitAction(WaitOptions.waitOptions(Duration.ofNanos(1500)))
										.moveTo(PointOption.point(endx - startx, endy - starty));
								LogUtil.APP.info("�ӿ�ʼ���꡾{},{}���϶������꡾{},{}��",startxy[0],startxy[1],endxy[0],endxy[1]);
							} else {
								action.waitAction(WaitOptions.waitOptions(Duration.ofNanos(1500)))
										.moveTo(PointOption.point(endx - startx, endy - starty));
								LogUtil.APP
										.info("��{}���϶������꡾{},{}��",movexyi,endxy[0],endxy[1]);
							}
							startx = endx;
							starty = endy;
						}
					} else {
						LogUtil.APP.warn("�жϽ�������λ�ó����쳣���������������{}��",movexy[movexyi]);
					}
				}
				action.release().perform();
				result = "movetoȫ���϶��ͷŲ���ɷ���...��ʼλ�á�" + startxy[0] + "," + startxy[1] + "��";
				LogUtil.APP.info("movetoȫ���϶��ͷŲ���ɷ���...��ʼλ�á�{},{}��",startxy[0],startxy[1]);
			} else {
				result = "moveto����ָ������ʼ���괦������쳣��" + movexy[0] + "��,���飡";
				LogUtil.APP.info("moveto����ָ������ʼ���괦������쳣��{}��,���飡",movexy[0]);
			}
			break;
		case "timeout":
			if (null != operationValue && ChangString.isNumeric(operationValue)) {
				// ����ҳ��������ʱ��30��
				appium.manage().timeouts().pageLoadTimeout(Integer.parseInt(operationValue), TimeUnit.SECONDS);
				// ����Ԫ�س������ʱ��30��
				appium.manage().timeouts().implicitlyWait(Integer.parseInt(operationValue), TimeUnit.SECONDS);
				result = "����ȫ��ҳ�����&Ԫ�س������ȴ�ʱ�䡾" + operationValue + "����...";
				LogUtil.APP.info("����ȫ��ҳ�����&Ԫ�س������ȴ�ʱ�䡾{}����...",operationValue);
			} else {
				result = "���ȴ�ʱ��ת���������������";
				LogUtil.APP.info(result + "ԭ������Ϊ�ж���ĵȴ�ʱ�䲻������...");
			}
			break;
		case "screenshot":
			java.text.DateFormat timeformat = new java.text.SimpleDateFormat("MMdd-HHmmss");
			String imagname = "FunctionScreenShot_" + timeformat.format(new Date());
			AndroidBaseAppium.screenShot(appium, imagname);
			result = "��ͼ���ơ�" + imagname + "��...";
			LogUtil.APP.info("ʹ�÷���������ȡ��ǰ��Ļ...{}",result);
			break;
		default:
			break;
		}
		return result;
	}

}
