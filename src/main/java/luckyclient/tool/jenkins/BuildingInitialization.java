package luckyclient.tool.jenkins;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.offbytwo.jenkins.model.BuildResult;

import luckyclient.remote.api.serverOperation;
import luckyclient.utils.LogUtil;

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
public class BuildingInitialization {
	
	protected static int THREAD_COUNT = 0;
	protected static int THREAD_SUCCOUNT = 0;

	public static BuildResult buildingRun(String tastid) {
		try {
			String[] jobName = serverOperation.getBuildName(tastid);

			if (jobName != null) {
				ThreadPoolExecutor	threadExecute	= new ThreadPoolExecutor(jobName.length, 10, 3, TimeUnit.SECONDS,
						new ArrayBlockingQueue<>(1000),
			            new ThreadPoolExecutor.CallerRunsPolicy());
				
				LogUtil.APP.info("׼�������õĲ�����Ŀ���й��������Եȡ�������");
				for (String s : jobName) {
					BuildingInitialization.THREAD_COUNT++;   //���̼߳���++�����ڼ���߳��Ƿ�ȫ��ִ����
					threadExecute.execute(new ThreadForBuildJob(s));
				}
				
				//���̼߳��������ڼ���߳��Ƿ�ȫ��ִ����
				int k=0;
				while(BuildingInitialization.THREAD_COUNT!=0){
					k++;
					//��ȴ�����ʱ��45����
					if(k>2700){
						break;
					}
					Thread.sleep(1000);
				}
				threadExecute.shutdown();
				
				if(jobName.length!=THREAD_SUCCOUNT){
					LogUtil.APP.info("��������Ŀ{}���������ɹ�����Ŀ{}�����й��������쳣��ʧ��״̬��������鿴������־...",jobName.length,THREAD_SUCCOUNT);
					return BuildResult.FAILURE;
				}else{
					LogUtil.APP.info("�ܹ������ɹ�����Ŀ{}����ȫ�������ɹ���������鿴������־...",THREAD_SUCCOUNT);
				}
				
			} else {
				LogUtil.APP.info("��ǰ����û���ҵ���Ҫ��������Ŀ��");
			}
		} catch (Exception e) {
			LogUtil.APP.error("��Ŀ���������г����쳣", e);
			return BuildResult.UNSTABLE;
		}
		return BuildResult.SUCCESS;

	}

}
