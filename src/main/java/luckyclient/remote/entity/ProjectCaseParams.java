package luckyclient.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ��������ʵ��
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��4��13��
 */
public class ProjectCaseParams extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ��������ID */
	private Integer paramsId;
	/** �������� */
	private String paramsName;
	/** ����ֵ */
	private String paramsValue;
	/** ��ĿID */
	private Integer projectId;
	/** ������Ŀʵ�� */
	private Project project;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setParamsId(Integer paramsId) 
	{
		this.paramsId = paramsId;
	}

	public Integer getParamsId() 
	{
		return paramsId;
	}
	public void setParamsName(String paramsName) 
	{
		this.paramsName = paramsName;
	}

	public String getParamsName() 
	{
		return paramsName;
	}
	public void setParamsValue(String paramsValue) 
	{
		this.paramsValue = paramsValue;
	}

	public String getParamsValue() 
	{
		return paramsValue;
	}
	public void setProjectId(Integer projectId) 
	{
		this.projectId = projectId;
	}

	public Integer getProjectId() 
	{
		return projectId;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("paramsId", getParamsId())
            .append("paramsName", getParamsName())
            .append("paramsValue", getParamsValue())
            .append("projectId", getProjectId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
