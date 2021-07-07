package luckyclient.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 *
 * @ClassName: DBOperation
 * @Description: ��װ�Զ��������У������ݿ�Ĳ��ֲ���
 * @author�� seagull
 * @date 2014��8��24�� ����9:29:40
 */
public class DbOperation {

    DbToolkit dbt;

    /**
     * �������ӳأ�ע��˷�������new��Σ��ᵼ�¶�δ������ӳأ���÷�����������������
     */
    public DbOperation(String urlBase, String usernameBase, String passwordBase) {
        dbt = new DbToolkit(urlBase, usernameBase, passwordBase);
    }


    /**
     * ִ��SQL
     *
     * @param sql ִ��SQL���
     */
    public String executeSql(String sql) {
        Connection conn = null;
        String result;
        try {
            conn = dbt.getBaseConnection();
            int resultnum = DbToolkit.executeSQL(conn, sql);
            if (resultnum > 0) {
                result = "�ɹ�ִ��SQL,��������" + resultnum + "�У�";
            } else {
                result = "�ɹ�ִ��SQL,û�и��µ����ݣ�";
            }
            return result;
        } catch (Exception e) {
            return e.toString();
        } finally {
            DbToolkit.closeConnection(conn);
        }
    }

    /**
     * ִ��SQL��ˮ��ѯ
     *
     * @param sql ��ѯSQL���
     */
    public String executeQuery(String sql) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = dbt.getBaseConnection();
            StringBuilder sb = new StringBuilder();
            rs = DbToolkit.executeQuery(conn, sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int colum = metaData.getColumnCount();
            int count = 0;
            //����
            while (rs.next()) {
                count++;
                if (count > 1) {
                    sb.append("#");
                }
                //����
                for (int i = 1; i <= colum; i++) {
                    if (rs.getObject(metaData.getColumnName(i)) == null) {
                        sb.append("null").append("%");
                        continue;
                    }
                    sb.append(rs.getObject(metaData.getColumnName(i)).toString()).append("%");
                }
/*				if(DbOperation.sumString(sb.toString(), "%")>500){
					sb.delete(0,sb.length());
					sb.append("��ѯ����������̫����(����100��)������ʾ������Ŷ��������");
					break;
				}*/
            }
            return sb.toString();
        } finally {
            if (rs != null) {
                rs.close();
            }
            DbToolkit.closeConnection(conn);
        }
    }


    /**
     * ��ȡ�ַ���
     *
     * @return String
     * @Description: ��ȡ�ַ���
     */
    public String subString(String str, String begin, String end) {
        try {
            return str.substring(str.indexOf(begin) + begin.length(), str.lastIndexOf(end));
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * ͳ���ַ����ַ����г��ֵĴ���
     *
     * @return int
     */
    public static int sumString(String str, String a) {
        char[] chs = a.toCharArray();
        int num = 0;
        char[] chars = str.toCharArray();
        for (char aChar : chars) {
            if (chs[0] == aChar) {
                num++;
            }
        }
        return num;
    }

    /**
     * Wait�ȴ�ʱ��
     *
     * @return int
     */
    public static void stepWait(String s) throws InterruptedException {
        int second = Integer.parseInt(s);
        Thread.sleep(second * 1000);
    }

}
