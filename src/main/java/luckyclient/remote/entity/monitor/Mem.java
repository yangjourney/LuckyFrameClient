package luckyclient.remote.entity.monitor;

/**
 * �ȴ������Ϣ
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��5��5��
 */
public class Mem
{
    /**
     * �ڴ�����
     */
    private double total;

    /**
     * �����ڴ�
     */
    private double used;

    /**
     * ʣ���ڴ�
     */
    private double free;

    public double getTotal()
    {
        return Arith.div(total, (1024 * 1024 * 1024), 2);
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public double getUsed()
    {
        return Arith.div(used, (1024 * 1024 * 1024), 2);
    }

    public void setUsed(long used)
    {
        this.used = used;
    }

    public double getFree()
    {
        return Arith.div(free, (1024 * 1024 * 1024), 2);
    }

    public void setFree(long free)
    {
        this.free = free;
    }

    public double getUsage()
    {
        return Arith.mul(Arith.div(used, total, 4), 100);
    }
}
