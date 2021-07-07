package luckyclient.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ����������־��¼�� project_case_debug
 * 
 * @author luckyframe
 * @date 2019-03-14
 */
public class ProjectCaseDebug extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ����ID */
	private Integer debugId;
	/** ����ID */
	private Integer caseId;
	/** �û�ID */
	private Integer userId;
	/** ���Խ�����ʶ 0 ������ 1���� 2�쳣 */
	private Integer debugIsend;
	/** ��־���� info ��¼ warning ���� error �쳣 */
	private String logLevel;
	/** ��־ */
	private String logDetail;
	/** �ͻ���ID */
	private Integer clientId;
	/** �ͻ�������·�� */
	private String driverPath;

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getDriverPath() {
		return driverPath;
	}

	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}

	public void setDebugId(Integer debugId) 
	{
		this.debugId = debugId;
	}

	public Integer getDebugId() 
	{
		return debugId;
	}
	public void setCaseId(Integer caseId) 
	{
		this.caseId = caseId;
	}

	public Integer getCaseId() 
	{
		return caseId;
	}
	public void setUserId(Integer userId) 
	{
		this.userId = userId;
	}

	public Integer getUserId() 
	{
		return userId;
	}
	public void setDebugIsend(Integer debugIsend) 
	{
		this.debugIsend = debugIsend;
	}

	public Integer getDebugIsend() 
	{
		return debugIsend;
	}
	public void setLogLevel(String logLevel) 
	{
		this.logLevel = logLevel;
	}

	public String getLogLevel() 
	{
		return logLevel;
	}
	public void setLogDetail(String logDetail) 
	{
		this.logDetail = logDetail;
	}

	public String getLogDetail() 
	{
		return logDetail;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("debugId", getDebugId())
            .append("caseId", getCaseId())
            .append("userId", getUserId())
            .append("debugIsend", getDebugIsend())
            .append("logLevel", getLogLevel())
            .append("logDetail", getLogDetail())
            .toString();
    }
}
