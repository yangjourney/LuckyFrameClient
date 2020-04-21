package luckyclient.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Э��ģ�����ʵ��
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��4��13��
 */
public class ProjectTemplateParams extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ģ�����ID */
	private Integer paramsId;
	/** ģ��ID */
	private Integer templateId;
	/** ������ */
	private String paramName;
	/** ����Ĭ��ֵ */
	private String paramValue;
	/** 0 String 1 JSON���� 2 JSONARR���� 3 �ļ����� */
	private Integer paramType;

	public void setParamsId(Integer paramsId) 
	{
		this.paramsId = paramsId;
	}

	public Integer getParamsId() 
	{
		return paramsId;
	}
	public void setTemplateId(Integer templateId) 
	{
		this.templateId = templateId;
	}

	public Integer getTemplateId() 
	{
		return templateId;
	}
	public void setParamName(String paramName) 
	{
		this.paramName = paramName;
	}

	public String getParamName() 
	{
		return paramName;
	}
	public void setParamValue(String paramValue) 
	{
		this.paramValue = paramValue;
	}

	public String getParamValue() 
	{
		return paramValue;
	}
	public void setParamType(Integer paramType) 
	{
		this.paramType = paramType;
	}

	public Integer getParamType() 
	{
		return paramType;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("paramsId", getParamsId())
            .append("templateId", getTemplateId())
            .append("paramName", getParamName())
            .append("paramValue", getParamValue())
            .append("paramType", getParamType())
            .toString();
    }
}
