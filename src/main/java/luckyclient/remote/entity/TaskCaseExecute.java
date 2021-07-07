package luckyclient.remote.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ��������ִ�м�¼�� task_case_execute
 * 
 * @author luckyframe
 * @date 2019-04-08
 */
public class TaskCaseExecute extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ����ִ��ID */
	private Integer taskCaseId;
	/** ����ID */
	private Integer taskId;
	/** ��ĿID */
	private Integer projectId;
	/** ����ID */
	private Integer caseId;
	/** ������ʶ */
	private String caseSign;
	/** �������� */
	private String caseName;
	/** 0ͨ�� 1ʧ�� 2���� 3ִ���� 4δִ�� */
	private Integer caseStatus;
	/** ��������ʱ�� */
	private Date finishTime;

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public void setTaskCaseId(Integer taskCaseId) 
	{
		this.taskCaseId = taskCaseId;
	}

	public Integer getTaskCaseId() 
	{
		return taskCaseId;
	}
	public void setTaskId(Integer taskId) 
	{
		this.taskId = taskId;
	}

	public Integer getTaskId() 
	{
		return taskId;
	}
	public void setProjectId(Integer projectId) 
	{
		this.projectId = projectId;
	}

	public Integer getProjectId() 
	{
		return projectId;
	}
	public void setCaseSign(String caseSign) 
	{
		this.caseSign = caseSign;
	}

	public String getCaseSign() 
	{
		return caseSign;
	}
	public void setCaseName(String caseName) 
	{
		this.caseName = caseName;
	}

	public String getCaseName() 
	{
		return caseName;
	}
	public void setCaseStatus(Integer caseStatus) 
	{
		this.caseStatus = caseStatus;
	}

	public Integer getCaseStatus() 
	{
		return caseStatus;
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
            .append("taskCaseId", getTaskCaseId())
            .append("taskId", getTaskId())
            .append("projectId", getProjectId())
            .append("caseSign", getCaseSign())
            .append("caseName", getCaseName())
            .append("caseStatus", getCaseStatus())
            .append("finishTime", getFinishTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
