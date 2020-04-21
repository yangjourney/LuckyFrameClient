package luckyclient.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ������־��ϸ�� task_case_log
 * 
 * @author luckyframe
 * @date 2019-04-08
 */
public class TaskCaseLog extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ����ִ��ID */
	private Integer logId;
	/** ����ID */
	private Integer caseId;
	/** ����ִ��ID */
	private Integer taskCaseId;
	/** ����ID */
	private Integer taskId;
	/** ��־��ϸ */
	private String logDetail;
	/** ��־���� */
	private String logGrade;
	/** ��־�������� */
	private String logStep;
	/** UI�Զ����Զ���ͼ��ַ */
	private String imgname;

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public void setLogId(Integer logId) 
	{
		this.logId = logId;
	}

	public Integer getLogId() 
	{
		return logId;
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
	public void setLogDetail(String logDetail) 
	{
		this.logDetail = logDetail;
	}

	public String getLogDetail() 
	{
		return logDetail;
	}
	public void setLogGrade(String logGrade) 
	{
		this.logGrade = logGrade;
	}

	public String getLogGrade() 
	{
		return logGrade;
	}
	public void setLogStep(String logStep) 
	{
		this.logStep = logStep;
	}

	public String getLogStep() 
	{
		return logStep;
	}
	public void setImgname(String imgname) 
	{
		this.imgname = imgname;
	}

	public String getImgname() 
	{
		return imgname;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("taskCaseId", getTaskCaseId())
            .append("taskId", getTaskId())
            .append("logDetail", getLogDetail())
            .append("logGrade", getLogGrade())
            .append("logStep", getLogStep())
            .append("imgname", getImgname())
            .append("createTime", getCreateTime())
            .toString();
    }
}
