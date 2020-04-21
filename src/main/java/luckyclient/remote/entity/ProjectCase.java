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
public class ProjectCase extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ��������ID */
	private Integer caseId;
	/** ����������� */
	private Integer caseSerialNumber;
	/** ������ʶ */
	private String caseSign;
	/** �������� */
	private String caseName;
	/** ������ĿID */
	private Integer projectId;
	/** ������Ŀģ��ID */
	private Integer moduleId;
	/** Ĭ������ 0 HTTP�ӿ� 1 Web UI 2 API����  3�ƶ��� */
	private Integer caseType;
	/** ǰ�ò���ʧ�ܣ����������Ƿ������0���жϣ�1������ */
	private Integer failcontinue;
	/** ������Ŀʵ�� */
	private Project project;
	/** ��������ģ��ʵ�� */
	private ProjectCaseModule projectCaseModule;
	/** ����ѡ�б�� */
	private boolean flag = false;
	/** �������ȼ� */
    private int priority;
	/** �����ƻ�ID��ʶ */
	private Integer planId;
	/** �����ƻ�����ID��ʶ */
	private Integer planCaseId;

	public Integer getPlanCaseId() {
		return planCaseId;
	}

	public void setPlanCaseId(Integer planCaseId) {
		this.planCaseId = planCaseId;
	}

	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public ProjectCaseModule getProjectCaseModule() {
		return projectCaseModule;
	}

	public void setProjectCaseModule(ProjectCaseModule projectCaseModule) {
		this.projectCaseModule = projectCaseModule;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setCaseId(Integer caseId) 
	{
		this.caseId = caseId;
	}

	public Integer getCaseId() 
	{
		return caseId;
	}
	public void setCaseSerialNumber(Integer caseSerialNumber) 
	{
		this.caseSerialNumber = caseSerialNumber;
	}

	public Integer getCaseSerialNumber() 
	{
		return caseSerialNumber;
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
	public void setProjectId(Integer projectId) 
	{
		this.projectId = projectId;
	}

	public Integer getProjectId() 
	{
		return projectId;
	}
	public void setModuleId(Integer moduleId) 
	{
		this.moduleId = moduleId;
	}

	public Integer getModuleId() 
	{
		return moduleId;
	}
	public void setCaseType(Integer caseType) 
	{
		this.caseType = caseType;
	}

	public Integer getCaseType() 
	{
		return caseType;
	}
	public void setFailcontinue(Integer failcontinue) 
	{
		this.failcontinue = failcontinue;
	}

	public Integer getFailcontinue() 
	{
		return failcontinue;
	}
	
	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("caseId", getCaseId())
            .append("caseSerialNumber", getCaseSerialNumber())
            .append("caseSign", getCaseSign())
            .append("caseName", getCaseName())
            .append("projectId", getProjectId())
            .append("moduleId", getModuleId())
            .append("caseType", getCaseType())
            .append("failcontinue", getFailcontinue())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("project", getProject())
            .append("projectCaseModule", getProjectCaseModule())            
            .toString();
    }
}
