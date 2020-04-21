package luckyclient.execution.dispose;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import luckyclient.utils.LogUtil;

/**
 * �Բ����滻���д���
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull
 * =================================================================
 * @author Seagull
 * @date 2019��1��15��
 */
public class ChangString {

	/**
	 * �滻�����е��ַ�
	 * 
	 * @param str �������ַ���
	 * @param variable ������������������ȫ�ֱ������ֲ�������
	 * @param changname ����key
	 * @return �����滻����ַ���
	 */
	public static String changparams(String str, Map<String, String> variable, String changname) {
		try {
			if (null == str) {
				return null;
			}
			str = str.replace("&quot;", "\"");
			str = str.replace("&#39;", "'");
			// @@����ע��@����������
			int varcount = counter(str, "@") - counter(str, "@@") * 2;

			// ������ڴ��Σ����д���
			if (varcount > 0) {
				LogUtil.APP.info("��{}��{}�����ҵ�{}�����滻����",changname,str,varcount);
				int changcount = 0;

				// ׼����HASHMAP����LINKMAP����KEY�������򣬽��Ҫ���滻�KEY������
				List<Map.Entry<String, String>> list = new ArrayList<>(variable.entrySet());
				// Ȼ��ͨ���Ƚ�����ʵ������
				// ��KEY���Ƚ�������
				// Ȼ��ͨ���Ƚ�����ʵ������
				// ��KEY���Ƚ�������
				list.sort((o1, o2) -> o2.getKey().length() - o1.getKey().length());

				Map<String, String> aMap2 = new LinkedHashMap<>();
				for (Map.Entry<String, String> mapping : list) {
					aMap2.put(mapping.getKey(), mapping.getValue());
				}

				// �Ӳ����б��в���ƥ�����
				for (Map.Entry<String, String> entry : aMap2.entrySet()) {
					if (str.contains("@" + entry.getKey())) {
						if (str.contains("@@" + entry.getKey())) {
							str = str.replace("@@" + entry.getKey(), "////CHANG////");
						}
						// �����滻�ַ����д���\"����\'�ᵼ��\��ʧ������
						// entry.setValue(entry.getValue().replaceAll("\\\\\"",
						// "\\&quot;"));
						// entry.setValue(entry.getValue().replaceAll("\\\\\'",
						// "\\\\&#39;"));
						int viewcount = counter(str, "@" + entry.getKey());
						str = str.replace("@" + entry.getKey(), entry.getValue());
						LogUtil.APP.info("��{}���ñ�����@{}���滻��ֵ��{}��",changname,entry.getKey(),entry.getValue());
						str = str.replace("////CHANG////", "@@" + entry.getKey());
						changcount = changcount + viewcount;
					}
				}

				if (varcount != changcount) {
					LogUtil.APP.warn(changname + "�����ñ���δ�ڲ��������ҵ������飡��������{}��",str);
				}
			}
			str = str.replace("@@", "@");
			//�����ú������д���
			str=ParamsManageForSteps.paramsManage(str);
			return str;
		} catch (Exception e) {
			LogUtil.APP.error("�滻���������г����쳣�����飡",e);
			return "";
		}
	}

	/**
	 * ͳ���ַ�
	 * @param str1 ԭʼ�ַ���
	 * @param str2 ��ͳ���ַ���
	 * @return ���ظ���
	 */
	public static int counter(String str1, String str2) {
		int total = 0;
		for (String tmp = str1; tmp != null && tmp.length() >= str2.length();) {
			if (tmp.indexOf(str2) == 0) {
				total++;
				tmp = tmp.substring(str2.length());
			} else {
				tmp = tmp.substring(1);
			}
		}
		return total;
	}

	/**
	 * �ж��Ƿ�������
	 * @param str �����ַ�
	 * @return ���ز���ֵ
	 */
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * �ж��Ƿ�������
	 * @param str �����ַ�
	 * @return ���ز���ֵ
	 */
	public static boolean isInteger(String str) {
		String patternStr="^[-+]?[\\d]*$";
		Pattern pattern = Pattern.compile(patternStr);
		return pattern.matcher(str).matches();
	}

	/**
	 * �滻��������
	 * @param object �滻����
	 * @param str �滻�ַ���
	 * @return ���ض���
	 */
	public static Object settype(Object object, String str) {
		if (object instanceof Integer) {
			return Integer.valueOf(str);
		} else if (object instanceof Boolean) {
			return Boolean.valueOf(str);
		} else if (object instanceof Long) {
			return Long.valueOf(str);
		} else if (object instanceof Timestamp) {
			return Timestamp.valueOf(str);
		} else if (object instanceof JSONObject) {
			return JSONObject.parseObject(str);
		} else if (object instanceof JSONArray) {
			return JSONArray.parseArray(str);
		} else {
			return str;
		}
	}

	/**
	 * ���ڼ����滻KEY�����
	 */
	private static int COUNTER=1;
	/**
	 * ���ڷֱ��Ƿ�Ѳ����滻�ɹ�
	 */
	private static Boolean BCHANG=false;
	/**
	 * ����JSON����
	 * @param json ԭʼjson
	 * @param key �滻key
	 * @param value �滻ֵ
	 * @param keyindex �滻key����
	 * @return ����json����
	 */
	public static JSONObject parseJsonString(String json,String key,String value,int keyindex){
		LinkedHashMap<String, Object> jsonMap = JSON.parseObject(json, new TypeReference<LinkedHashMap<String, Object>>(){});
		for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
			parseJsonMap(entry,key,value,keyindex);
			}
		return new JSONObject(jsonMap);
		}
	
	/**
	 * �滻������JSON�����е�KEY
	 * @param entry json����ת����MAP
	 * @param key ���滻key
	 * @param value �滻ֵ
	 * @param keyindex �滻key����
	 */
	@SuppressWarnings("unchecked")
	public static void parseJsonMap(Entry<String, Object> entry, String key, String value, int keyindex){
		//������ַ����͵�nullֱ�ӰѶ�������Ϊ����null
		if("NULL".equals(value)){
			value = null;
		}
		//����ǵ���map��������
		if(entry.getValue() instanceof Map){
		LinkedHashMap<String, Object> jsonMap = JSON.parseObject(entry.getValue().toString(), new TypeReference<LinkedHashMap<String, Object>>(){});
		for (Map.Entry<String, Object> entry2 : jsonMap.entrySet()) {
			parseJsonMap(entry2,key,value,keyindex);
			}
		entry.setValue(jsonMap);
		}
		//�����list����ȡ����
		if(entry.getValue() instanceof List){
			if(key.equals(entry.getKey())){
				if(keyindex==COUNTER){
					LogUtil.APP.info("����ԭʼStringֵ:��{}��",entry.getValue());
					JSONArray jsonarr = JSONArray.parseArray(value);
					entry.setValue(jsonarr);
					LogUtil.APP.info("�����滻��Stringֵ:��{}��",entry.getValue());
					BCHANG=true;
				}			
				COUNTER++;
			}else{
				@SuppressWarnings("rawtypes")
				List list = (List)entry.getValue();
				for (int i = 0; i < list.size(); i++) {
					//��λ��У�ѭ����ȡ
					try{
						list.set(i, parseJsonString(list.get(i).toString(),key,value,keyindex));
						entry.setValue(list);
					}catch(JSONException jsone){
						if(key.equals(entry.getKey())){
							if(keyindex==COUNTER){
								LogUtil.APP.info("����ԭʼListֵ:��{}��",entry.getValue());
								JSONArray jsonarr = JSONArray.parseArray(value);
								entry.setValue(jsonarr);
								LogUtil.APP.info("�����滻��Listֵ:��{}��",entry.getValue());
								BCHANG=true;
							}			
							COUNTER++;
						}
						break;
					}
					}
			  }
			}
		//�����String�ͻ�ȡ����ֵ
		if(entry.getValue() instanceof String){
			if(key.equals(entry.getKey())){
				if(keyindex==COUNTER){
					LogUtil.APP.info("����ԭʼStringֵ:��{}��",entry.getValue());
					entry.setValue(value);
					LogUtil.APP.info("�����滻��Stringֵ:��{}��",entry.getValue());
					BCHANG=true;
				}			
				COUNTER++;
			}
		}
		//�����Integer�ͻ�ȡ����ֵ
		if(entry.getValue() instanceof Integer){
			if(key.equals(entry.getKey())){
				if(keyindex==COUNTER){
					LogUtil.APP.info("����ԭʼIntegerֵ:��{}��",entry.getValue());
					assert value != null;
					entry.setValue(Integer.valueOf(value));
					LogUtil.APP.info("�����滻��Integerֵ:��{}��",entry.getValue());
					BCHANG=true;
				}
				COUNTER++;
			}
		}
		//�����Long�ͻ�ȡ����ֵ
		if(entry.getValue() instanceof Long){
			if(key.equals(entry.getKey())){
				if(keyindex==COUNTER){
					LogUtil.APP.info("����ԭʼLongֵ:��{}��",entry.getValue());
					assert value != null;
					entry.setValue(Long.valueOf(value));
					LogUtil.APP.info("�����滻��Longֵ:��{}��",entry.getValue());
					BCHANG=true;
				}
				COUNTER++;
			}
		}
		//�����Double�ͻ�ȡ����ֵ
		if(entry.getValue() instanceof BigDecimal){
			if(key.equals(entry.getKey())){
				if(keyindex==COUNTER){
					LogUtil.APP.info("����ԭʼBigDecimalֵ:��{}��",entry.getValue());
					assert value != null;
					BigDecimal bd = new BigDecimal(value);
					entry.setValue(bd);
					LogUtil.APP.info("�����滻��BigDecimalֵ:��{}��",entry.getValue());
					BCHANG=true;
				}
				COUNTER++;
			}
		}
		//�����Boolean�ͻ�ȡ����ֵ
		if(entry.getValue() instanceof Boolean){
			if(key.equals(entry.getKey())){
				if(keyindex==COUNTER){
					LogUtil.APP.info("����ԭʼBooleanֵ:��{}��",entry.getValue());
					entry.setValue(Boolean.valueOf(value));
					LogUtil.APP.info("�����滻��Booleanֵ:��{}��",entry.getValue());
					BCHANG=true;
				}
				COUNTER++;
			}
		}

	}

	/**
	 * �滻json������ָ��KEY��ڷ���
	 * @param json ���滻ԭʼjson
	 * @param key �滻key
	 * @param value �滻ֵ
	 * @param index �滻key����
	 * @return �����滻���MAP����
	 */
	public static Map<String, String> changjson(String json, String key, String value,int index) {
		json=json.trim();
		LogUtil.APP.info("ԭʼJSON:��{}�������滻JSON KEY:��{}�������滻JSON VALUE:��{}�������滻JSON KEY���:��{}��",json,key,value,index);
		Map<String, String> map = new HashMap<>(0);
		map.put("json", json);
		map.put("boolean", BCHANG.toString().toLowerCase());
		
		if (json.startsWith("{") && json.endsWith("}")) {
			try {
				JSONObject jsonStr;
				jsonStr=parseJsonString(json,key,value,index);
				if (BCHANG) {
					LogUtil.APP.info("JSON�ַ����滻�ɹ�����JSON:��{}��",jsonStr.toJSONString());
				}
				map.put("json", jsonStr.toJSONString());
			} catch (Exception e) {
				LogUtil.APP.error("��ʽ����JSON�쳣���������:{}",json, e);
				return map;
			}
		} else if (json.startsWith("[") && json.endsWith("]")) {
			try {
				JSONArray jsonarr = JSONArray.parseArray(json);
				
				for(int i=0;i<jsonarr.size();i++){
					JSONObject jsonStr = jsonarr.getJSONObject(i);		
					jsonStr=parseJsonString(jsonStr.toJSONString(),key,value,index);
					if(BCHANG){
						jsonarr.set(i, jsonStr);
						LogUtil.APP.info("JSONARRAY�ַ����滻�ɹ�����JSONARRAY:��{}��",jsonarr.toJSONString());
						break;
					}
				}
				map.put("json", jsonarr.toJSONString());
				
			} catch (Exception e) {
				LogUtil.APP.error("��ʽ����JSONArray�쳣���������:{}",json, e);
				return map;
			}
		}
		map.put("boolean", BCHANG.toString().toLowerCase());
		BCHANG=false;
		COUNTER=1;
		return map;
	}

}
