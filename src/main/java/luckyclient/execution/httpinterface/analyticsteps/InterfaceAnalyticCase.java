package luckyclient.execution.httpinterface.analyticsteps;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import luckyclient.execution.dispose.ChangString;
import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.ProjectCase;
import luckyclient.remote.entity.ProjectCaseSteps;
import luckyclient.utils.LogUtil;
/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 * @ClassName: AnalyticCase 
 * @Description: ���������������������ֵĽű�
 * @author�� seagull
 * @date 2017��7��14�� ����9:29:40  
 * 
 */
public class InterfaceAnalyticCase{
	/**
	 * ������������
	 * @param projectcase ��������������
	 * @param step �����������
	 * @param taskid ����ID
	 * @param caselog ��־����
	 * @return ���ؽ���������MAP
	 */
	public static Map<String,String> analyticCaseStep(ProjectCase projectcase,ProjectCaseSteps step,String taskid,serverOperation caselog, Map<String, String> variable){
		Map<String,String> params = new HashMap<>(0);

		try {
	    String resultstr = step.getExpectedResult();
		params.put("Action", step.getAction());
		// ����ֵ����
		String packageName = ChangString.changparams(step.getStepPath().trim(), variable, "��·��");
	    params.put("PackageName", packageName);
	 // ����ֵ����
	    String functionName = ChangString.changparams(step.getStepOperation().trim(), variable, "������");
		params.put("FunctionName", functionName);
		String stepParams = replaceSpi(step.getStepParameters(),0);
		String[] temp=stepParams.split("\\|",-1);
		for(int i=0;i<temp.length;i++){
            if("".equals(temp[i])){
				continue;
			}if(" ".equals(temp[i])){
				 //��һ���ո��ʱ�򣬴�����ַ���
				params.put("FunctionParams"+(i+1), "");  
			}else{
				 //set��N���������
				String parameterValues = ChangString.changparams(replaceSpi(temp[i],1), variable, "��������");
				params.put("FunctionParams"+(i+1), parameterValues);  
			}
		}
		//setԤ�ڽ��
		if(null==resultstr||"".equals(resultstr)){
			params.put("ExpectedResults", "");
		}else{
			String expectedResults = ChangString.changparams(subComment(resultstr), variable, "Ԥ�ڽ��");
			params.put("ExpectedResults", expectedResults);
		}
		LogUtil.APP.info("�������:{} ������:{} �����Զ�����������ű���ɣ�",projectcase.getCaseSign(),step.getStepSerialNumber());
		if(null!=caselog){
			caselog.insertTaskCaseLog(taskid, projectcase.getCaseId(),"�����ţ�"+step.getStepSerialNumber()+" �����Զ�����������ű���ɣ�","info",String.valueOf(step.getStepSerialNumber()),"");
		}
		}catch(Exception e) {
			if(null!=caselog){
			caselog.insertTaskCaseLog(taskid, projectcase.getCaseId(),"�����ţ�"+step.getStepSerialNumber()+" �����Զ�����������ű�����","error",String.valueOf(step.getStepSerialNumber()),"");
			}
			LogUtil.APP.error("������ţ�{} �����ţ�{} �����Զ�����������ű�����",projectcase.getCaseSign(),step.getStepSerialNumber(),e);
			params.put("exception","������ţ�"+projectcase.getCaseSign()+"|�����쳣,��������Ϊ�ջ��������ű�����");
			return params;
     }
 	 return params;

	}
	
	public static String subComment(String htmlStr) {
		// ����script��������ʽ
    	String regExscript = "<script[^>]*?>[\\s\\S]*?</script>";
    	// ����style��������ʽ
        String regExstyle = "<style[^>]*?>[\\s\\S]*?</style>";
        // ����HTML��ǩ��������ʽ
        String regExhtml = "<[^>]+>";
        //����ո�س����з�
        String regExspace = "[\t\r\n]";
        
        String scriptstr;
        if (htmlStr!=null) {
            Pattern pScript = Pattern.compile(regExscript, Pattern.CASE_INSENSITIVE);
            Matcher mScript = pScript.matcher(htmlStr);
            // ����script��ǩ
            htmlStr = mScript.replaceAll(""); 
       
            Pattern pStyle = Pattern.compile(regExstyle, Pattern.CASE_INSENSITIVE);
            Matcher mStyle = pStyle.matcher(htmlStr);
            // ����style��ǩ
            htmlStr = mStyle.replaceAll(""); 
       
            Pattern pHtml = Pattern.compile(regExhtml, Pattern.CASE_INSENSITIVE);
            Matcher mHtml = pHtml.matcher(htmlStr);
            // ����html��ǩ
            htmlStr = mHtml.replaceAll(""); 
       
            Pattern pSpace = Pattern.compile(regExspace, Pattern.CASE_INSENSITIVE);
            Matcher mSpace = pSpace.matcher(htmlStr);
            // ���˿ո�س���ǩ
            htmlStr = mSpace.replaceAll(""); 
            
        }
		assert htmlStr != null;
		if(htmlStr.contains("/*") && htmlStr.contains("*/")){
    		String commentstr = htmlStr.substring(htmlStr.trim().indexOf("/*"),htmlStr.indexOf("*/")+2);
    		//ȥע��
    		scriptstr = htmlStr.replace(commentstr, "");     
        }else{
        	scriptstr = htmlStr;
        }
        //ȥ���ַ���ǰ��Ŀո�
        scriptstr = trimInnerSpaceStr(scriptstr);   
      //�滻�ո�ת��
        scriptstr = scriptstr.replaceAll("&nbsp;", " "); 
      //ת��˫����
        scriptstr = scriptstr.replaceAll("&quot;", "\""); 
      //ת�嵥����
        scriptstr = scriptstr.replaceAll("&#39;", "'");
      //ת�����ӷ�
        scriptstr = scriptstr.replaceAll("&amp;", "&");  
        scriptstr = scriptstr.replaceAll("&lt;", "<");  
        scriptstr = scriptstr.replaceAll("&gt;", ">"); 
        
		return scriptstr;
	}

	/***
     * ȥ���ַ���ǰ��Ŀո��м�Ŀո���
     * @param str �������ַ���
     * @return ����ȥ���ո��Ľ��
     */
    public static String trimInnerSpaceStr(String str){
        str = str.trim();
        while(str.startsWith(" ")){
        str = str.substring(1).trim();
        }
        while(str.startsWith("&nbsp;")){
        str = str.substring(6).trim();
        }
        while(str.endsWith(" ")){
        str = str.substring(0,str.length()-1).trim();
        }
        while(str.endsWith("&nbsp;")){
            str = str.substring(0,str.length()-6).trim();
            }
        return str;
    } 
    
    /**
     * �����������д���|�ַ���ʱ���ڽ���\\|����ת��
     * @param str �������ַ���
     * @param flag �����ʶ
     * @return ���ش������
     */
    private static String replaceSpi(String str,int flag){
    	String replacestr="&brvbar_rep;";
    	if(null==str){
    		str = "";
    	}
    	String result=str;
    	if(str.contains("\\\\|")&&flag==0){
    		result=str.replace("\\\\|", replacestr);
    	}
    	if(str.contains(replacestr)&&flag==1){
    		result=str.replace(replacestr,"|");
    	}
    	return result;
    }

}
