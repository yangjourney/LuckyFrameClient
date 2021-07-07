package luckyclient.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ������������ʵ��
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��4��13��
 */
public class ProjectCaseSteps extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ����ID */
	private Integer stepId;
	/** ����ID */
	private Integer caseId;
	/** ��ĿID */
	private Integer projectId;
	/** ������� */
	private Integer stepSerialNumber;
	/** ��·��|��λ·�� */
	private String stepPath;
	/** ������|���� */
	private String stepOperation;
	/** ���� */
	private String stepParameters;
	/** ���趯�� */
	private String action;
	/** Ԥ�ڽ�� */
	private String expectedResult;
	/** 0 API�ӿ� 1 Web UI 2 HTTP�ӿ� 3�ƶ��� */
	private Integer stepType;
	/** ��չ�ֶΣ������ڱ�ע���洢HTTPģ��� */
	private String extend;

	public void setStepId(Integer stepId) 
	{
		this.stepId = stepId;
	}

	public Integer getStepId() 
	{
		return stepId;
	}
	public void setCaseId(Integer caseId) 
	{
		this.caseId = caseId;
	}

	public Integer getCaseId() 
	{
		return caseId;
	}
	public void setProjectId(Integer projectId) 
	{
		this.projectId = projectId;
	}

	public Integer getProjectId() 
	{
		return projectId;
	}
	public void setStepSerialNumber(Integer stepSerialNumber) 
	{
		this.stepSerialNumber = stepSerialNumber;
	}

	public Integer getStepSerialNumber() 
	{
		return stepSerialNumber;
	}
	public void setStepPath(String stepPath) 
	{
		this.stepPath = stepPath;
	}

	public String getStepPath() 
	{
		return stepPath;
	}
	public void setStepOperation(String stepOperation) 
	{
		this.stepOperation = stepOperation;
	}

	public String getStepOperation() 
	{
		return stepOperation;
	}
	public void setStepParameters(String stepParameters) 
	{
		this.stepParameters = stepParameters;
	}

	public String getStepParameters() 
	{
		return stepParameters;
	}
	public void setAction(String action) 
	{
		this.action = action;
	}

	public String getAction() 
	{
		return action;
	}
	public void setExpectedResult(String expectedResult) 
	{
		this.expectedResult = expectedResult;
	}

	public String getExpectedResult() 
	{
		return expectedResult;
	}
	public void setStepType(Integer stepType) 
	{
		this.stepType = stepType;
	}

	public Integer getStepType() 
	{
		return stepType;
	}
	public void setExtend(String extend) 
	{
		this.extend = extend;
	}

	public String getExtend() 
	{
		return extend;
	}
	
	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("stepId", getStepId())
            .append("caseId", getCaseId())
            .append("projectId", getProjectId())
            .append("stepSerialNumber", getStepSerialNumber())
            .append("stepPath", getStepPath())
            .append("stepOperation", getStepOperation())
            .append("stepParameters", getStepParameters())
            .append("action", getAction())
            .append("expectedResult", getExpectedResult())
            .append("stepType", getStepType())
            .append("extend", getExtend())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
