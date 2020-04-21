package luckyclient.utils;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ϵͳ��־��¼
 *
 * @author Seagull
 */
public class LogUtil {

    /**
     * ��Ҫʹ��������־����info,warn,error
     * info ��¼�ͻ���ϵͳ��־����ؿͻ����������
     * warn ��¼�ͻ���ҵ���ϵĸ澯��־
     * error ��¼�ͻ�����ִ�й������׳����쳣�Լ����ش���
     */
    public static final Logger APP = LoggerFactory.getLogger("info");

    public static StringBuffer getFieldValue(Object bean) {
        StringBuffer sb = new StringBuffer();

        try {
            if (bean == null) {
                return null;
            }
            Field[] fieldArray = bean.getClass().getDeclaredFields();
            int indexId = 0;
            Object obj;
            for (Field field : fieldArray) {
                field.setAccessible(true);
                obj = field.get(bean);
                if (!(obj instanceof List) && !"serialVersionUID".equals(field.getName())) {
                    if (indexId > 0) {
                        sb.append(",");
                    }
                    if (obj != null) {
                        sb.append(field.getName()).append("=").append(obj.toString());
                    } else {
                        sb.append(field.getName()).append("=");
                    }
                    indexId += 1;
                }
            }
        } catch (Exception ex) {
            LogUtil.APP.error("��־�쳣", ex);
        }
        return sb;
    }
}
