package luckyclient.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ����ģ��ʵ��
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��4��13��
 */
public class ProjectCaseModule extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ģ��ID */
	private Integer moduleId;
	/** ģ������ */
	private String moduleName;
	/** ��ĿID */
	private Integer projectId;
	/** ��ģ��id */
	private Integer parentId;
	/** ��ģ���б� */
	private String ancestors;
	/** ��ʾ˳�� */
	private Integer orderNum;
	/** ������Ŀ���� */
	private String projectName;
	
	public void setModuleId(Integer moduleId) 
	{
		this.moduleId = moduleId;
	}

	public Integer getModuleId() 
	{
		return moduleId;
	}
	public void setModuleName(String moduleName) 
	{
		this.moduleName = moduleName;
	}

	public String getModuleName() 
	{
		return moduleName;
	}
	public void setProjectId(Integer projectId) 
	{
		this.projectId = projectId;
	}

	public Integer getProjectId() 
	{
		return projectId;
	}
	public void setParentId(Integer parentId) 
	{
		this.parentId = parentId;
	}

	public Integer getParentId() 
	{
		return parentId;
	}
	public void setAncestors(String ancestors) 
	{
		this.ancestors = ancestors;
	}

	public String getAncestors() 
	{
		return ancestors;
	}
	public void setOrderNum(Integer orderNum) 
	{
		this.orderNum = orderNum;
	}

	public Integer getOrderNum() 
	{
		return orderNum;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("moduleId", getModuleId())
            .append("moduleName", getModuleName())
            .append("projectId", getProjectId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("orderNum", getOrderNum())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("projectName", getProjectName())
            .toString();
    }
}
