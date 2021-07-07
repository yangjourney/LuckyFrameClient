package luckyclient.tool.jenkins;

import java.io.IOException;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.ConsoleLog;
import com.offbytwo.jenkins.model.JobWithDetails;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import luckyclient.utils.LogUtil;

/**
 * * Job Build(���񹹽�) ��ز��� ��������� Build ��ص���Ϣ���л�ȡ�����������ȡ������־
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * 
 * @author Seagull
 * @date 2019��10��30��
 */
public class JobBuildApi {

	// Jenkins ����
	private JenkinsServer jenkinsServer;
	// http �ͻ��˶���
	//private JenkinsHttpClient jenkinsHttpClient;

	/**
	 * ���췽���е������� Jenkins ����
	 * 
	 * 2019��10��30��
	 */
	JobBuildApi() {
		JenkinsConnect jenkinsConnect = new JenkinsConnect();
		// ���� Jenkins
		jenkinsServer = jenkinsConnect.connection();
		// ���ÿͻ������� Jenkins
		//jenkinsHttpClient = jenkinsConnect.getClient();
	}

	/**
	 * ͨ��job���ƴ�����������ȡ�������
	 * @param jobName ��������
	 * @return ���ع������
	 * @author Seagull
	 * @date 2019��11��29��
	 */
	public BuildResult buildAndGetResultForJobName(String jobName) {
		BuildResult buildResult = null;
		try {
			//��������
			jenkinsServer.getJob(jobName).build(false);
			// ��ȡ Job ��Ϣ
			JobWithDetails job = jenkinsServer.getJob(jobName);
			// ���������һ�α�����ʾ��
			BuildWithDetails build = job.getLastBuild().details();
			// ��ȡ��������ʾ����
			LogUtil.APP.info("������Ŀ��{}, ��������:{}", jobName,build.getDisplayName());
			// ��ȡ�����Ĳ�����Ϣ
			LogUtil.APP.info("������Ŀ��{}, ��������:{}", jobName,build.getParameters());
			// ��ȡ�������
			LogUtil.APP.info("������Ŀ��{}, �������:{}", jobName,build.getNumber());
			// ��ȡִ�й����Ļ��Ϣ
			LogUtil.APP.info("������Ŀ��{}, �������Ϣ:{}", jobName,build.getActions());
			// ��ȡ������ʼʱ���
			LogUtil.APP.info("������Ŀ��{}, ����ʱ��:{}", jobName,DateUtil.format(DateUtil.date(build.getTimestamp()), "yyyy-MM-dd HH:mm:ss"));
			// ��ǰ��־
			ConsoleLog currentLog = build.getConsoleOutputText(0);
			// �����ǰ��ȡ��־��Ϣ
			//LogUtil.APP.info(currentLog.getConsoleLog());
			// ����Ƿ��и�����־,����������ѭ����ȡ
			while (currentLog.getHasMoreData()) {
				// ��ȡ������־��Ϣ
				ConsoleLog newLog = build.getConsoleOutputText(currentLog.getCurrentBufferSize());
				// ���������־
				if(!StrUtil.isBlank(newLog.getConsoleLog())){
					LogUtil.APP.info("������Ŀ��{}, ������־��{}",jobName,newLog.getConsoleLog());
				}
				currentLog = newLog;

			}
			buildResult = job.getBuildByNumber(build.getNumber()).details().getResult();
			LogUtil.APP.info("������Ŀ��{}, ���������>>>>>>>>>{}",jobName,buildResult.toString());
		} catch (IOException e) {
			LogUtil.APP.error("��ȡִ������״̬�����쳣", e);
		}
		return buildResult;
	}
    
}
