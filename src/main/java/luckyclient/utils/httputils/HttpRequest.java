package luckyclient.utils.httputils;

import luckyclient.utils.LogUtil;
import luckyclient.utils.config.SysConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 * 
 * @author�� seagull
 * @date 2017��12��1�� ����9:29:40
 * 
 */
public class HttpRequest {
	final static Properties PROPERTIES = SysConfig.getConfiguration();
	private final static String WEB_URL = "http://" + PROPERTIES.getProperty("server.web.ip") + ":"
			+ PROPERTIES.getProperty("server.web.port")+ PROPERTIES.getProperty("server.web.path");

	/**
	 * �ַ�������
	 * @param repath ����·��
	 * @return ����������
	 */
	public static String loadJSON(String repath) {
		String charset="GBK";
		StringBuffer resultBuffer;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		BufferedReader br = null;
		// �����������
		HttpGet httpGet = new HttpGet(WEB_URL+repath);
		try {
			HttpResponse response = httpclient.execute(httpGet);
			// ��ȡ��������Ӧ����
			br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), charset));
			String temp;
			resultBuffer = new StringBuffer();
			while ((temp = br.readLine()) != null) {
				resultBuffer.append(temp);
			}
		} catch (Exception e) {
			LogUtil.APP.error("loadJSON������������쳣�����飡", e);
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LogUtil.APP.error("loadJSON���������ر�br�������쳣�����飡", e);
				}
			}
		}
		return resultBuffer.toString();
	}

	 /**
     * ��ָ�� URL ����POST����������
     * @param param
     * ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return ������Զ����Դ����Ӧ���
     */
    public static String sendPost(String repath, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(WEB_URL+repath);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������������utf-8����
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "GBK"));
            // �����������
            out.print(param);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ,����utf-8����
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
        	LogUtil.APP.error("��ָ��URL����POST��������������쳣�����飡", e);
        }
        //ʹ��finally�����ر��������������
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
            	LogUtil.APP.error("��ָ��URL����POST�����������ر��������쳣�����飡", ex);
            }
        }
        return result.toString();
    }

	/**
	 * ʹ��HttpClient��JSON��ʽ����post����
	 * @param urlParam ����·��
	 * @param params �������
	 * @return ����������
	 */
	public static String httpClientPostJson(String urlParam, String params){		
		StringBuffer resultBuffer;
		CloseableHttpClient httpclient=HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(WEB_URL+urlParam);
	    httpPost.setHeader("Content-Type", "application/json");
	    RequestConfig requestConfig = RequestConfig.custom()  
	            .setConnectTimeout(60*1000)
	            .setConnectionRequestTimeout(60*1000)  
	            //��������ʹ��䳬ʱʱ��
	            .setSocketTimeout(60*1000).build(); 
	    httpPost.setConfig(requestConfig);
		// �����������
		BufferedReader br = null;
		try {
		StringEntity entity = new StringEntity(params,"utf-8");
		httpPost.setEntity(entity);
       
		 CloseableHttpResponse response = httpclient.execute(httpPost);

		// ��ȡ��������Ӧ����
		resultBuffer = new StringBuffer();
		if(null!=response.getEntity()){
			br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
			String temp;
			while ((temp = br.readLine()) != null) {
				resultBuffer.append(temp);
			}	
		}
		} catch (Exception e) {
			LogUtil.APP.error("ʹ��HttpClient��JSON��ʽ����post��������쳣�����飡", e);
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LogUtil.APP.error("ʹ��HttpClient��JSON��ʽ����post�����ر�br�������쳣�����飡", e);
				}
			}
		}		
		return resultBuffer.toString();
	}

}
