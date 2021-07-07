package luckyclient.tool.jenkins;

import com.offbytwo.jenkins.model.BuildResult;

/**
 * 
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��12��2��
 */
public class ThreadForBuildJob extends Thread{
	
	private String jobName;
	
	public ThreadForBuildJob(String jobName){
		this.jobName = jobName;
	}
	
	@Override
	public void run(){
		JobBuildApi jobBuildApi=new JobBuildApi();
		BuildResult buildResult = jobBuildApi.buildAndGetResultForJobName(jobName);
		if(BuildResult.SUCCESS.equals(buildResult)){
			BuildingInitialization.THREAD_SUCCOUNT++;
		}
		BuildingInitialization.THREAD_COUNT--;        //���̼߳���--�����ڼ���߳��Ƿ�ȫ��ִ����
	}

}
