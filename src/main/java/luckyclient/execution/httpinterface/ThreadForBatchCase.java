package luckyclient.execution.httpinterface;

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
public class ThreadForBatchCase extends Thread{

	private Integer caseId;
	private String taskid;
	
	public ThreadForBatchCase(Integer caseId,String taskid){
		this.caseId = caseId;
		this.taskid = taskid;
	}
	
	@Override
	public void run(){
		TestCaseExecution testCaseExecution=new TestCaseExecution();
		testCaseExecution.oneCaseExecuteForTask(caseId, taskid);
		TestControl.THREAD_COUNT--;        //���̼߳���--�����ڼ���߳��Ƿ�ȫ��ִ����
	}

}
