package luckyclient.tool.mail;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import luckyclient.remote.entity.ProjectProtocolTemplate;
import luckyclient.utils.LogUtil;
import luckyclient.utils.config.SysConfig;
import luckyclient.utils.httputils.HttpClientTools;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 *
 * @author�� seagull
 * @date 2017��12��1�� ����9:29:40
 */
public class HtmlMail {
    static FreemarkerEmailTemplate fet = new FreemarkerEmailTemplate();

    public static String htmlContentFormat(int[] taskCount, String taskId, String buildStatus, String restartStatus, String time, String jobName) {
        Map<String, Object> parameters = new HashMap<>(0);
        parameters.put("buildstatus", buildStatus);
        parameters.put("restartstatus", restartStatus);
        parameters.put("casecount", taskCount[0]);
        parameters.put("casesuc", taskCount[1]);
        parameters.put("casefail", taskCount[2]);
        parameters.put("caselock", taskCount[3]);
        parameters.put("caseunex", taskCount[4]);
        parameters.put("time", time);
        parameters.put("taskid", taskId);
        parameters.put("jobname", jobName);

        return fet.getText("task-body", parameters);
    }

    public static String htmlSubjectFormat(String jobname) {
        Map<String, Object> parameters = new HashMap<>(0);
        parameters.put("jobname", jobname);
        return fet.getText("task-title", parameters);
    }
}
