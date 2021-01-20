package luckyclient.driven;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import luckyclient.utils.Constants;
import luckyclient.utils.LogUtil;

/**
 * ��������
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��1��15��
 */
public class SubString {
	/**
	 * ��ȡָ���ַ������м��ֶ�
	 * 
	 * @param str ԭʼ�ַ���
	 * @param startstr ��ʼ�ַ�
	 * @param endstr �����ַ�
	 * @return �����ַ�����ȡ���
	 */
	public static String subCentreStr(String str, String startstr, String endstr) {
		try{
			int startnum=0;
			int endnum=str.length();
			if(!"".equals(startstr)){
				startnum=str.indexOf(startstr) + startstr.length();
			}
			if(!"".equals(endstr)){
				endnum=str.indexOf(endstr, str.indexOf(startstr) + startstr.length());
			}
			return str.substring(startnum,endnum);
		}catch(Exception e){
			LogUtil.APP.error("subCentreStr��ȡ�ַ��������쳣�����������",e);
			return "��ȡ�ַ��������쳣�����������";
		}
	}

	/**
	 * ��ȡ�ַ�����ָ���ַ���ʼ
	 * 
	 * @param str ԭʼ�ַ�
	 * @param startstr ��ʼ�ַ�
	 * @return �����ַ�����ȡ���
	 */
	public static String subStartStr(String str, String startstr) {
		try{
			return str.substring(str.indexOf(startstr) + startstr.length());
		}catch(Exception e){
			LogUtil.APP.error("subStartStr��ȡ�ַ��������쳣�����������",e);
			return "��ȡ�ַ��������쳣�����������";
		}
	}

	/**
	 * ��ȡ�ַ�����ָ���ַ�����
	 * 
	 * @param str ԭʼ�ַ�
	 * @param endstr �����ַ�
	 * @return �����ַ�����ȡ���
	 */
	public static String subEndStr(String str, String endstr) {
		try{
			return str.substring(0, str.indexOf(endstr));
		}catch(Exception e){
			LogUtil.APP.error("subEndStr��ȡ�ַ��������쳣�����������",e);
			return "��ȡ�ַ��������쳣�����������";
		}
	}

	/**
	 * ͨ���ַ���λ�ý�ȡָ���ַ������м��ֶ�
	 * 
	 * @param str ԭʼ�ַ�
	 * @param startnum ��ʼ�ַ�λ��
	 * @param endnum ���λ��
	 * @return �����ַ�����ȡ���
	 */
	public static String subCentreNum(String str, String startnum, String endnum) {
		String getstr;
		if("".equals(startnum)){
			startnum="0";
		}
		if("".equals(endnum)){
			endnum=String.valueOf(str.length());
		}
		try{
			if (isInteger(startnum) && isInteger(endnum)) {
				int start = Integer.parseInt(startnum);
				int end = Integer.parseInt(endnum);
				if (start > end) {
					getstr = "��ȡ�ַ�����ʼλ�����ֲ��ܴ��ڽ���λ������";
				} else if (start < 0) {
					getstr = "��ȡ�ַ���λ�õ����ֲ���С��0";
				} else if (start > str.length() || end > str.length()) {
					getstr = "��ȡ�ַ���λ�õ����ֲ��ܴ����ַ�������ĳ��ȡ�" + str.length() + "��";
				} else {
					getstr = str.substring(start, end);
				}
			} else {
				getstr = "ָ���Ŀ�ʼ���ǽ���λ���ַ������������ͣ����飡";
			}

			return getstr;
		}catch(Exception e){
			LogUtil.APP.error("subCentreNum��ȡ�ַ��������쳣�����������",e);
			return "��ȡ�ַ��������쳣�����������";
		}
	}

	/**
	 * ͨ���ַ���λ�ý�ȡ�ַ�����ָ���ַ���ʼ
	 * 
	 * @param str ԭʼ�ַ�
	 * @param startnum �ַ���ʼλ��
	 * @return �����ַ�����ȡ���
	 */
	public static String subStartNum(String str, String startnum) {
		String getstr;
		try{
			if (isInteger(startnum)) {
				int start = Integer.parseInt(startnum);
				if (start < 0) {
					getstr = "��ȡ�ַ���λ�õ����ֲ���С��0";
				} else if (start > str.length()) {
					getstr = "��ȡ�ַ���λ�õ����ֲ��ܴ����ַ�������ĳ��ȡ�" + str.length() + "��";
				} else {
					getstr = str.substring(start);
				}
			} else {
				getstr = "ָ���Ŀ�ʼλ���ַ������������ͣ����飡";
			}

			return getstr;
		}catch(Exception e){
			LogUtil.APP.error("subStartNum��ȡ�ַ��������쳣�����������",e);
			return "��ȡ�ַ��������쳣�����������";
		}
	}

	/**
	 * ��ȡ�ַ�����ָ���ַ�����
	 * 
	 * @param str ԭʼ�ַ�
	 * @param endnum ����λ��
	 * @return �����ַ�����ȡ���
	 */
	public static String subEndNum(String str, String endnum) {
		String getstr;
		try{
			if (isInteger(endnum)) {
				int end = Integer.parseInt(endnum);
				if (end < 0) {
					getstr = "��ȡ�ַ���λ�õ����ֲ���С��0";
				} else if (end > str.length()) {
					getstr = "��ȡ�ַ���λ�õ����ֲ��ܴ����ַ�������ĳ��ȡ�" + str.length() + "��";
				} else {
					getstr = str.substring(0, end);
				}
			} else {
				getstr = "ָ���Ľ���λ���ַ������������ͣ����飡";
			}

			return getstr;
		}catch(Exception e){
			LogUtil.APP.error("subEndNum��ȡ�ַ��������쳣�����������",e);
			return "��ȡ�ַ��������쳣�����������";
		}
	}

	/**
	 * ����ƥ���ַ���
	 * @param str ԭʼ�ַ���
	 * @param rgex ������ʽ
	 * @param num �ַ�����
	 * @return ƥ�䵽���ַ���
	 */
	public static String subStrRgex(String str, String rgex, String num) {
		List<String> list = new ArrayList<>();
		try{
			// ƥ���ģʽ
			Pattern pattern = Pattern.compile(rgex);
			Matcher m = pattern.matcher(str);
			while (m.find()) {
//				int i = 1;
				list.add(m.group());
//				i++;
			}

			String getstr;
			if (isInteger(num)) {
				int index = Integer.parseInt(num);
				if (index < 0) {
					getstr = "��ȡ�ַ����������ֲ���С��0";
				} else if (index > str.length()) {
					getstr = "��ȡ�ַ������������ֲ��ܴ����ַ�������ĳ��ȡ�" + str.length() + "��";
				} else if (index > list.size()) {
					getstr = "δ����ָ���ַ����и�������ʽ�ҵ�ƥ����ַ�������ָ�����������ִ������ҵ���ƥ���ַ���������";
				} else {
					getstr = list.get(index - 1);
				}
			} else {
				getstr = "ָ��������λ���ַ������������ͣ����飡";
			}
			return getstr;
		}catch(Exception e){
			LogUtil.APP.error("subStrRgex��ȡ�ַ��������쳣�����������",e);
			return "��ȡ�ַ��������쳣�����������";
		}
	}

	/**
	 * �ж��Ƿ�����������
	 * @param str �����ַ�
	 * @return ���ز����ͽ��
	 */
	private static boolean isInteger(String str) {
		String patternStr="^[-+]?[\\d]*$";
		Pattern pattern = Pattern.compile(patternStr);
		return pattern.matcher(str).matches();
	}

	/**
	 * ��ʼ������JSON��Value��ֵ
	 */
	private static String JSONVALUE = "����ȡJSON KEY�е�Value�쳣��";

	/**
	 * ���ڼ���KEY�����
	 */
	private static int COUNTER = 1;

	/**
	 * ����JSON����
	 * @param json ԭʼJSON
	 * @param key ��ѯkeyֵ
	 * @param keyindex keyֵ����
	 * @return ����json����
	 */
	private static JSONObject parseJsonString(String json, String key, int keyindex) {
		LinkedHashMap<String, Object> jsonMap = JSON.parseObject(json,
				new TypeReference<LinkedHashMap<String, Object>>() {
				}, Feature.OrderedField);
		for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
			parseJsonMap(entry, key, keyindex);
		}
		return new JSONObject(jsonMap);
	}

	/**
	 * ������JSON�����е�key�Լ�value
	 * @param entry json�����еĵ�key�Լ�value
	 * @param key ��Ҫ��ȡ��key
	 * @param keyindex ��ȡkey������
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void parseJsonMap(Map.Entry<String, Object> entry, String key, int keyindex) {
		// ����ǵ���map��������
		if (entry.getValue() instanceof Map) {
			LinkedHashMap<String, Object> jsonMap = JSON.parseObject(entry.getValue().toString(),
					new TypeReference<LinkedHashMap<String, Object>>() {
					}, Feature.OrderedField);
			for (Map.Entry<String, Object> entry2 : jsonMap.entrySet()) {
				parseJsonMap(entry2, key, keyindex);
			}
		}
		// �����list����ȡ����
		if (entry.getValue() instanceof List) {
			List list = (List) entry.getValue();
			for (int i = 0; i < list.size(); i++) {
				// ������У�ѭ����ȡ
				//list.set(i, parseJsonString(list.get(i).toString(), key, keyindex));
				//��λ��У�ѭ����ȡ
				try{
					list.set(i, parseJsonString(list.get(i).toString(), key, keyindex));
				}catch(JSONException jsone){
					if(key.equals(entry.getKey())){
						if(keyindex==COUNTER){
							JSONVALUE = entry.getValue().toString();
						}			
						COUNTER++;
					}
					break;
				}
			}
		}
		// ��ȡkey�е�value
		if (key.equals(entry.getKey())) {
			if (keyindex == COUNTER) {
				JSONVALUE = entry.getValue().toString();
			}
			COUNTER++;
		}

	}

	/**
	 * ��ȡJSON����JSONArray����ָ�����Key�е�Value
	 * 
	 * @param json ԭʼJSON
	 * @param key ָ��key
	 * @param indexstr key������
	 * @return ����ָ��key��value�ַ�
	 */
	public static String getJsonValue(String json, String key, String indexstr) {
		json = json.trim();
		int index;
		String result = JSONVALUE;
		if (isInteger(indexstr) && !"0".equals(indexstr)) {
			index = Integer.parseInt(indexstr);
		} else {
			result = JSONVALUE + "ָ����keyֵ��Ų��Ǵ���0������(��Ŵ�1��ʼ)�����飡";
			return result;
		}

		if (json.startsWith("{") && json.endsWith("}")) {
			try {
				JSONObject jsonStr = JSONObject.parseObject(json, Feature.OrderedField);
				parseJsonString(jsonStr.toString(), key, index);
				result = JSONVALUE;
			} catch (Exception e) {
				result = JSONVALUE + "��ʽ����JSON�쳣�����������" + json;
				return result;
			}
		} else if (json.startsWith("[") && json.endsWith("]")) {
			try {
				// JSONArray jsonarr = JSONArray.parseArray(json);
				// ֱ��ʹ��fastjson�Ľӿ�ʵ���������
				JSONArray jsonarr = JSONArray.parseObject(json.getBytes(StandardCharsets.UTF_8), JSONArray.class, Feature.OrderedField);
				for (int i = 0; i < jsonarr.size(); i++) {
					JSONObject jsonStr = jsonarr.getJSONObject(i);
					parseJsonString(jsonStr.toJSONString(), key, index);
					if (!JSONVALUE.startsWith("����ȡJSON KEY�е�Value�쳣��")) {
						result = JSONVALUE;
						break;
					}
				}
			} catch (Exception e) {
				result = JSONVALUE + "��ʽ����JSONArray�쳣�����������" + json;
				return result;
			}
		} else {
			result = JSONVALUE + "��ʽ����JSON����JSONArrayʱ�����쳣�����������" + json;
		}

		if (result.equals("����ȡJSON KEY�е�Value�쳣��")) {
			result = JSONVALUE + "û���ҵ���Ӧ��KEYֵ����ȷ�ϣ�";
		}

		COUNTER = 1;
		JSONVALUE = "����ȡJSON KEY�е�Value�쳣��";
		return result;
	}

    /**
     * ͨ��jsonPath���ʽ��ȡJSON�ַ���ָ��ֵ
     * @param expressionParams jsonPath���ʽ
     * @param jsonString jsonԭʼ�ַ���
     * @return ������ȡ�����ַ�
     * @author Seagull
     * @date 2019��8��28��
     */
    public static String jsonPathGetParams(String expressionParams, String jsonString) {
        String type;
        String expression="";
        if(expressionParams.endsWith("]")&&expressionParams.contains("[")){
        	try{
            	type=expressionParams.substring(0,expressionParams.indexOf("["));
            	expression=expressionParams.substring(expressionParams.indexOf("[")+1, expressionParams.lastIndexOf("]"));
            	if("list".equals(type.toLowerCase())){
            		//ȥ��������Ӧͷ����Ϣ
            		if(jsonString.startsWith(Constants.RESPONSE_HEAD)){
            			jsonString = jsonString.substring(jsonString.indexOf(Constants.RESPONSE_END)+1);
            		}
            		
            		//ȥ��������Ӧͷ����Ϣ
            		if(jsonString.startsWith(Constants.RESPONSE_CODE)){
            			jsonString = jsonString.substring(jsonString.indexOf(Constants.RESPONSE_END)+1);
            		}
            		
            		List<Object> list = JsonPath.parse(jsonString).read(expression);
            		jsonString="";
            		for(Object result:list){
            			result = jsonString +result+",";
            			jsonString = (String)result;
            		}    		
            	}else{                	
            		jsonString=JsonPath.parse(jsonString).read(expression).toString();
            	}
        	}catch(PathNotFoundException pnfe){
        		LogUtil.APP.error("ͨ��jsonPath��ȡJSON�ַ���ָ��ֵ�����쳣��û���ҵ���Ӧ����·������ȷ��JSON�ַ�����{}�����ʽ�Ƿ���ȷ��{}����",jsonString,expression);
        	}catch(Exception e){
        		LogUtil.APP.error("ͨ��jsonPath��ȡJSON�ַ���ָ��ֵ�����쳣���������Ķ���������ʽ(String/List[���ʽ])���Ǳ���ȡ��json�ַ����Ƿ�������");
        	}
        }else{
        	LogUtil.APP.warn("��ȡJSON�ַ���ָ��jsonPath���ʽ��{}���쳣���������Ķ���������ʽ(String/List[���ʽ])�Ƿ�������",expressionParams);
        }
        LogUtil.APP.info("��ȡJSON�ַ���ָ��jsonPath���ʽ��{}����ֵ��:{}",expression,jsonString);
        return jsonString;
    }
}
