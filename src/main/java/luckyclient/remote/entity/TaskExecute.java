package luckyclient.remote.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ����ִ��ʵ��
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��4��13��
 */
public class TaskExecute extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ����ID */
	private Integer taskId;
	/** ����ID */
	private Integer schedulingId;
	/** ��ĿID */
	private Integer projectId;
	/** �������� */
	private String taskName;
	/** ״̬ 0δִ�� 1ִ���� 2 �ɹ� 4ʧ�� 5 ����ͻ���ʧ�� */
	private Integer taskStatus;
	/** �������� */
	private Integer caseTotalCount;
	/** �ɹ��� */
	private Integer caseSuccCount;
	/** ʧ���� */
	private Integer caseFailCount;
	/** ������ */
	private Integer caseLockCount;
	/** δִ������ */
	private Integer caseNoexecCount;
	/** �������ʱ�� */
	private Date finishTime;
	/** ������Ŀʵ�� */
	private Project project;
	/** ����ִ�аٷֱ� */
	private Integer taskProgress;

	public Integer getTaskProgress() {
		return taskProgress;
	}

	public void setTaskProgress(Integer taskProgress) {
		this.taskProgress = taskProgress;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setTaskId(Integer taskId) 
	{
		this.taskId = taskId;
	}

	public Integer getTaskId() 
	{
		return taskId;
	}
	public void setSchedulingId(Integer schedulingId) 
	{
		this.schedulingId = schedulingId;
	}

	public Integer getSchedulingId() 
	{
		return schedulingId;
	}
	public void setProjectId(Integer projectId) 
	{
		this.projectId = projectId;
	}

	public Integer getProjectId() 
	{
		return projectId;
	}
	public void setTaskName(String taskName) 
	{
		this.taskName = taskName;
	}

	public String getTaskName() 
	{
		return taskName;
	}
	public void setTaskStatus(Integer taskStatus) 
	{
		this.taskStatus = taskStatus;
	}

	public Integer getTaskStatus() 
	{
		return taskStatus;
	}
	public void setCaseTotalCount(Integer caseTotalCount) 
	{
		this.caseTotalCount = caseTotalCount;
	}

	public Integer getCaseTotalCount() 
	{
		return caseTotalCount;
	}
	public void setCaseSuccCount(Integer caseSuccCount) 
	{
		this.caseSuccCount = caseSuccCount;
	}

	public Integer getCaseSuccCount() 
	{
		return caseSuccCount;
	}
	public void setCaseFailCount(Integer caseFailCount) 
	{
		this.caseFailCount = caseFailCount;
	}

	public Integer getCaseFailCount() 
	{
		return caseFailCount;
	}
	public void setCaseLockCount(Integer caseLockCount) 
	{
		this.caseLockCount = caseLockCount;
	}

	public Integer getCaseLockCount() 
	{
		return caseLockCount;
	}
	public void setCaseNoexecCount(Integer caseNoexecCount) 
	{
		this.caseNoexecCount = caseNoexecCount;
	}

	public Integer getCaseNoexecCount() 
	{
		return caseNoexecCount;
	}
	public void setFinishTime(Date finishTime) 
	{
		this.finishTime = finishTime;
	}

	public Date getFinishTime() 
	{
		return finishTime;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("taskId", getTaskId())
            .append("schedulingId", getSchedulingId())
            .append("projectId", getProjectId())
            .append("taskName", getTaskName())
            .append("taskStatus", getTaskStatus())
            .append("caseTotalCount", getCaseTotalCount())
            .append("caseSuccCount", getCaseSuccCount())
            .append("caseFailCount", getCaseFailCount())
            .append("caseLockCount", getCaseLockCount())
            .append("caseNoexecCount", getCaseNoexecCount())
            .append("finishTime", getFinishTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
