package luckyclient.utils.httputils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * 
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��8��8��
 */
public class MyX509TrustManager implements X509TrustManager  
{  
    @Override  
    public void checkClientTrusted(X509Certificate[] ax509certificate, String s) {
        //TODO nothing  
    }  
  
    @Override  
    public void checkServerTrusted(X509Certificate[] ax509certificate, String s) {
        //TODO nothing  
    }  
  
    @Override  
    public X509Certificate[] getAcceptedIssuers()  
    {  
        return new X509Certificate[]{};  
    }  
}  
