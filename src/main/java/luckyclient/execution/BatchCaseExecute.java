package luckyclient.execution;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import luckyclient.execution.appium.androidex.AndroidBatchExecute;
import luckyclient.execution.appium.iosex.IosBatchExecute;
import luckyclient.execution.httpinterface.BatchTestCaseExecution;
import luckyclient.execution.httpinterface.TestControl;
import luckyclient.execution.webdriver.ex.WebBatchExecute;
import luckyclient.remote.api.GetServerApi;
import luckyclient.remote.entity.TaskExecute;
import luckyclient.remote.entity.TaskScheduling;
import luckyclient.utils.LogUtil;
import luckyclient.utils.config.AppiumConfig;

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
public class BatchCaseExecute extends TestControl {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator + "log4j.conf");
			String taskid = args[0];
			String batchcase = args[1];
			TaskExecute task = GetServerApi.cgetTaskbyid(Integer.parseInt(taskid));
			TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(Integer.parseInt(taskid));
			if (taskScheduling.getTaskType() == 0) {
					BatchTestCaseExecution.batchCaseExecuteForTast(
                            String.valueOf(task.getTaskId()), batchcase);
			} else if (taskScheduling.getTaskType() == 1) {
					// UI����
					WebBatchExecute.batchCaseExecuteForTast(
							String.valueOf(task.getTaskId()), batchcase);

			} else if (taskScheduling.getTaskType() == 2) {
				Properties properties = AppiumConfig.getConfiguration();

				if ("Android".equals(properties.getProperty("platformName"))) {
					AndroidBatchExecute.batchCaseExecuteForTast(
                            String.valueOf(task.getTaskId()), batchcase);
				} else if ("IOS".equals(properties.getProperty("platformName"))) {
					IosBatchExecute.batchCaseExecuteForTast(
                            String.valueOf(task.getTaskId()), batchcase);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("���������������������������쳣�����飡",e);
		} finally{
			System.exit(0);
		}
	}

}
