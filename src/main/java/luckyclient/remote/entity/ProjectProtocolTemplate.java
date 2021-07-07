package luckyclient.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Э��ģ��ʵ��
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��4��13��
 */
public class ProjectProtocolTemplate extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ģ��ID */
	private Integer templateId;
	/** ģ������ */
	private String templateName;
	/** ��ĿID */
	private Integer projectId;
	/** ��Ϣͷ */
	private String headMsg;
	/** �ͻ����е�֤��·�� */
	private String cerificatePath;
	/** �����ʽ */
	private String encoding;
	/** ��ʱʱ�� */
	private Integer timeout;
	/** ������Ӧ����ֵ�Ƿ��ͷ����Ϣ 0���� 1�� */
	private Integer isResponseHead;
	/** ������Ӧ����ֵ�Ƿ��״̬�� 0���� 1�� */
	private Integer isResponseCode;
	/** ������Ŀʵ�� */
	private Project project;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setTemplateId(Integer templateId) 
	{
		this.templateId = templateId;
	}

	public Integer getTemplateId() 
	{
		return templateId;
	}
	public void setTemplateName(String templateName) 
	{
		this.templateName = templateName;
	}

	public String getTemplateName() 
	{
		return templateName;
	}
	public void setProjectId(Integer projectId) 
	{
		this.projectId = projectId;
	}

	public Integer getProjectId() 
	{
		return projectId;
	}
	public void setHeadMsg(String headMsg) 
	{
		this.headMsg = headMsg;
	}

	public String getHeadMsg() 
	{
		return headMsg;
	}
	public void setCerificatePath(String cerificatePath) 
	{
		this.cerificatePath = cerificatePath;
	}

	public String getCerificatePath() 
	{
		return cerificatePath;
	}
	public void setEncoding(String encoding) 
	{
		this.encoding = encoding;
	}

	public String getEncoding() 
	{
		return encoding;
	}
	public void setTimeout(Integer timeout) 
	{
		this.timeout = timeout;
	}

	public Integer getTimeout() 
	{
		return timeout;
	}
	public void setIsResponseHead(Integer isResponseHead) 
	{
		this.isResponseHead = isResponseHead;
	}

	public Integer getIsResponseHead() 
	{
		return isResponseHead;
	}
	public void setIsResponseCode(Integer isResponseCode) 
	{
		this.isResponseCode = isResponseCode;
	}

	public Integer getIsResponseCode() 
	{
		return isResponseCode;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("templateId", getTemplateId())
            .append("templateName", getTemplateName())
            .append("projectId", getProjectId())
            .append("headMsg", getHeadMsg())
            .append("cerificatePath", getCerificatePath())
            .append("encoding", getEncoding())
            .append("timeout", getTimeout())
            .append("isResponseHead", getIsResponseHead())
            .append("isResponseCode", getIsResponseCode())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("project", getProject())
            .toString();
    }
}