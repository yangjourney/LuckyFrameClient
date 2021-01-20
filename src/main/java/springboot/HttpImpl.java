package springboot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import luckyclient.remote.entity.monitor.Server;
import luckyclient.utils.config.SysConfig;
import luckyclient.utils.httputils.HttpRequest;
import springboot.model.RunBatchCaseEntity;
import springboot.model.RunTaskEntity;
import springboot.model.WebDebugCaseEntity;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 * @author seagull
 * @date 2018��7��27�� ����10:28:32
 */
@RestController
public class HttpImpl {
	private static final Logger log = LoggerFactory.getLogger(HttpImpl.class);
	private static final String OS=System.getProperty("os.name").toLowerCase();
	/**
	 * �����Զ�������
	 * @param req HTTP����
	 * @return ��������������
	 */
	@PostMapping("/runTask")
	private String runTask(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = req.getReader();
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sb.append(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("��ʼת��RunTaskEntityִ������ʵ��...");
		RunTaskEntity runTaskEntity = JSONObject.parseObject(sb.toString(), RunTaskEntity.class);
		log.info("TaskId:{},SchedulingName:{},LoadPath:{}",runTaskEntity.getTaskId(),runTaskEntity.getSchedulingName(),runTaskEntity.getLoadPath());
		try{
			log.info("��ʼ��ȡ�ͻ�������·��...");
			File file =new File(RunService.APPLICATION_HOME+runTaskEntity.getLoadPath());
			log.info("�ͻ�������·��:{}",file.getAbsolutePath());
			if  (!file .isDirectory())      
			{       
				log.warn("�ͻ��˲�������׮·�������ڣ����顾{}��",file.getPath());
				return "�ͻ��˲�������׮·�������ڣ����顾"+file.getPath()+"��";
			}
			log.info("��ʼ��Runtime...");
			Runtime run = Runtime.getRuntime();
			StringBuilder sbf=new StringBuilder();
			sbf.append(runTaskEntity.getTaskId()).append(" ");
			sbf.append(runTaskEntity.getLoadPath());
			log.info("��������ģʽ���Գ���...��������:��{}��  ����ID:��{}��",runTaskEntity.getSchedulingName(),runTaskEntity.getTaskId());
			if(OS.startsWith("win")){
				log.info("��ʼ����windows�����д���...");
				run.exec("cmd.exe /k start " + "task.cmd" +" "+ sbf.toString(), null,new File(RunService.APPLICATION_HOME+File.separator));
				log.info("����windows�����д������...");
			}else{
				log.info("��ʼ����Linux����ű�...");
				Process ps = Runtime.getRuntime().exec(RunService.APPLICATION_HOME+File.separator+"task.sh"+ " " +sbf.toString());
		        ps.waitFor();
				log.info("����Linux����ű����...");
			}			
		} catch (Exception e) {
			log.error("��������ģʽ���Գ����쳣������",e);
			return "��������ģʽ���Գ����쳣������";
		}
		return "��������ģʽ���Գ�������";
	}
	
	/**
	 * ������������
	 * @param req HTTP����
	 * @return �������������������
	 */
	@PostMapping("/runBatchCase")
	private String runBatchCase(HttpServletRequest req) {
		StringBuilder sbd = new StringBuilder();
		try {
			BufferedReader reader = req.getReader();
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sbd.append(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("��ʼת��RunBatchCaseEntity����ִ������ʵ��...");
		RunBatchCaseEntity runBatchCaseEntity = JSONObject.parseObject(sbd.toString(), RunBatchCaseEntity.class);
		
		String projectName = runBatchCaseEntity.getProjectname();
		String taskId = runBatchCaseEntity.getTaskid();
		String loadPath = runBatchCaseEntity.getLoadpath();
		String batchCase = runBatchCaseEntity.getBatchcase();
		log.info("������������:{}",batchCase);
		try{
			log.info("��ʼ��ȡ�ͻ�������·��...");
			File file =new File(RunService.APPLICATION_HOME+loadPath);
			log.info("�ͻ�������·��:{}",file.getAbsolutePath());
			if  (!file .isDirectory())      
			{    
				log.warn("�ͻ��˲�������׮·�������ڣ����顾{}��",file.getPath());
				return "�ͻ��˲�������׮·�������ڣ����顾"+file.getPath()+"��";
			}
			log.info("��ʼ��Runtime...");
			Runtime run = Runtime.getRuntime();
			StringBuilder sb=new StringBuilder();
			sb.append(taskId).append(" ");
			sb.append(batchCase).append(" ");
			sb.append(loadPath);
			log.info("������������ģʽ���Գ���...������Ŀ:{}  ����ID:{}",projectName,taskId);
			if(OS.startsWith("win")){
				log.info("��ʼ����windows�����д���...");
				run.exec("cmd.exe /k start " + "task_batch.cmd" + " " +sb.toString(), null,new File(RunService.APPLICATION_HOME+File.separator));
				log.info("����windows�����д������...");
			}else{
				log.info("��ʼ����Linux����ű�...");
				Process ps = Runtime.getRuntime().exec(RunService.APPLICATION_HOME+File.separator+"task_batch.sh"+ " " +sb.toString());
		        ps.waitFor();
		        log.info("����Linux����ű����...");
			}		
		} catch (Exception e) {		
			e.printStackTrace();
			log.error("������������ģʽ���Գ����쳣������",e);
			return "������������ģʽ���Գ����쳣������";
		} 
		return "������������ģʽ���Գ�������";
	}
	
	/**
	 * web������Ƚӿ�
	 * @param req HTTP����
	 * @return ���ص����������н��
	 */
	@PostMapping("/webDebugCase")
	private String webDebugCase(HttpServletRequest req) {
		StringBuilder sbd = new StringBuilder();
		try {
			BufferedReader reader = req.getReader();
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sbd.append(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		WebDebugCaseEntity webDebugCaseEntity = JSONObject.parseObject(sbd.toString(), WebDebugCaseEntity.class);
		log.info("Web�˵�������ID:{} ������ID:{}",webDebugCaseEntity.getCaseId(),webDebugCaseEntity.getUserId());
		try{
			File file =new File(RunService.APPLICATION_HOME+webDebugCaseEntity.getLoadpath());
			if  (!file .isDirectory())      
			{    
				log.warn("�ͻ��˲�������׮·�������ڣ����顾{}��",file.getPath());
				return "�ͻ��˲�������׮·�������ڣ����顾"+file.getPath()+"��";
			}
			Runtime run = Runtime.getRuntime();
			StringBuilder sb=new StringBuilder();
			sb.append(webDebugCaseEntity.getCaseId()).append(" ");
			sb.append(webDebugCaseEntity.getUserId()).append(" ");
			sb.append(webDebugCaseEntity.getLoadpath());
			if(OS.startsWith("win")){
				run.exec("cmd.exe /k start " + "web_debugcase.cmd" + " " +sb.toString(), null,new File(RunService.APPLICATION_HOME+File.separator));
			}else{
				Process ps = Runtime.getRuntime().exec(RunService.APPLICATION_HOME+File.separator+"web_debugcase.sh"+ " " +sb.toString());
	            ps.waitFor();  
			}	
		} catch (Exception e) {		
			e.printStackTrace();
			log.error("����Web����ģʽ���Գ����쳣������",e);
			return "����Web����ģʽ���Գ����쳣������";
		} 
		return "����Web����ģʽ���Գ�������";
	}
	
	/**
	 * ��ȡ�ͻ��˱�����־
	 * @param req HTTP����
	 * @return ������־ȫ����Ϣ
	 */
	@GetMapping("/getLogdDetail")
	private String getLogdDetail(HttpServletRequest req) {
		String fileName=req.getParameter("filename");
		String ctxPath = RunService.APPLICATION_HOME+File.separator+"log";
		String downLoadPath = ctxPath +File.separator+ fileName;

		String str;
		InputStreamReader isr;
		try {
			isr = new InputStreamReader(new FileInputStream(downLoadPath), StandardCharsets.UTF_8);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("��ȡ��־·����������ͻ�����־·���Ƿ����!downLoadPath: "+downLoadPath,e);
			return "��ȡ��־·����������ͻ�����־·���Ƿ����!downLoadPath: "+downLoadPath;
		}
		BufferedReader bos = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		try {
			while ((str = bos.readLine()) != null)
			{
				sb.append(str).append("##n##");
			}
			bos.close();
			log.info("����˶�ȡ������־�ɹ�!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("�ͻ���תBufferedReaderʧ�ܣ�����ԭ��",e);
			return "�ͻ���תBufferedReaderʧ�ܣ�����ԭ��";
		}
		return sb.toString();
	}
	
	/**
	 * ��ȡ�����ͼ
	 * @param req HTTP����
	 * @return ����ͼƬ�ֽ�
	 */
	@GetMapping("/getLogImg")
	private byte[] getLogImg(HttpServletRequest req) {
		String imgName=req.getParameter("imgName");
		String ctxPath = RunService.APPLICATION_HOME+File.separator+"log"+File.separator+"ScreenShot";
		String downLoadPath = ctxPath+File.separator+imgName;
        byte[] b = null;
        try {
            File file = new File(downLoadPath);
            b = new byte[(int) file.length()];
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
            //is.read(b);
            is.close();
        	log.info("����˻�ȡ����ͼƬ:{}",downLoadPath);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.error("���ļ������ڣ�����:{}",downLoadPath,e);
            return b;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return b;
        }     
        return b;
	}
	
	/**
	 * �ϴ������ļ����ͻ���
	 */
	@PostMapping("/uploadJar")
	private String uploadJar(HttpServletRequest req, @RequestParam("jarfile") MultipartFile jarfile) {
		if (!jarfile.isEmpty()){
            if (!Objects.requireNonNull(jarfile.getOriginalFilename()).endsWith(".jar")&&!jarfile.getOriginalFilename().endsWith(".py")) {
            	log.warn("�ļ���ʽ��׺����.jar��.py���ϴ�ʧ��");
                return "�ļ���ʽ��׺����.jar��.py���ϴ�ʧ��";
            }
		}else{
			log.warn("�ϴ��ļ�Ϊ�գ����飡");
            return "�ϴ��ļ�Ϊ�գ����飡";
		}

		String name = jarfile.getOriginalFilename();
		String loadpath = req.getParameter("loadpath");
		String path = RunService.APPLICATION_HOME+loadpath;
		if  (!new File(path) .isDirectory())      
		{    
			log.warn("�ͻ��˲�������׮·�������ڣ����顾{}��",path);
			return "�ͻ��˲�������׮·�������ڣ����顾"+path+"��";
		}	
		String pathName = path +File.separator+ name;

		File file = new File(pathName);
        try { 
            if (file.exists()){
            	file.deleteOnExit();
            }
			boolean newFile = file.createNewFile();
            if(newFile){
				BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
				byte[] jarfileByte = jarfile.getBytes();
				os.write(jarfileByte);
				os.flush();
				os.close();
				log.info("�ϴ���������{}�����ͻ�������Ŀ¼��{}���ɹ�!",name,file.getAbsolutePath());
				return "�ϴ���������"+name+"�����ͻ�������Ŀ¼��"+file.getAbsolutePath()+"���ɹ�!";
			}else{
				log.error("�ϴ���������{}�����ͻ�������Ŀ¼��{}��ʧ��!",name,file.getAbsolutePath());
				return "�ϴ���������"+name+"�����ͻ�������Ŀ¼��"+file.getAbsolutePath()+"��ʧ��!";
			}
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.error("�ͻ���δ�ҵ���ȷ·�����ļ����ϴ�ʧ�ܣ��ļ�·������:{}",pathName,e);
            return "�ͻ���δ�ҵ���ȷ·�����ļ����ϴ�ʧ�ܣ��ļ�·�����ƣ�"+pathName;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("�ͻ���IOExceptiona����δ�ҵ�����·�����ļ�·������:{}",pathName,e);
            return "�ͻ���IOExceptiona����δ�ҵ�����·�����ļ�·�����ƣ�"+pathName;
        }
	}
	
	/**
	 * ���ͻ�������
	 * @return �����������
	 */
	@GetMapping("/getClientStatus")
	private String getClientStatus() {
		Properties properties = SysConfig.getConfiguration();
		String verison=properties.getProperty("client.verison");
		return "{\"status\":\"success\",\"version\":\""+verison+"\"}";
	}

	/**
	 * ��ȡ�ͻ�����Դ������
	 * @return ���ؿͻ�����Դ���
	 * @author Seagull
	 * @date 2019��5��5��
	 */
	@GetMapping("/getClientMonitorData")
	private String getClientMonitorData() {
        Server server = new Server();
        server.copyTo();
        return JSON.toJSONString(server);
	}
	
	/**
	 * ���ͻ����е�����
	 * @author Seagull
	 * @date 2019��5��6��
	 */
	public static void checkHostNet() {
		log.info("���ͻ���������,���Ժ�......");
		Properties properties = SysConfig.getConfiguration();
		String version="Version "+properties.getProperty("client.verison");
		String webip=properties.getProperty("server.web.ip");
		Integer webport=Integer.valueOf(properties.getProperty("server.web.port"));
        try {
        	String result = HttpRequest.loadJSON("/openGetApi/clientGetServerVersion.do");
        	if(version.equals(result)){
            	log.info("�ͻ��˷���Web������: {}:{} ���ͨ��......",webip,webport);
        	}else{
        		if(result.startsWith("Version")){
        			log.warn("�ͻ��˰汾:{} ����˰汾:{} �ͻ��������˰汾��һ�£��п��ܻᵼ��δ֪���⣬����...",version,result);
        		}else{
        			log.error("����ͻ������ã���ȡ����˰汾��Ϣ�����쳣��");
        		}       		
        	}

        } catch (Exception e) {
        	log.error("�ͻ������ü���쳣����ȷ������Ŀ��Ŀ¼�µĿͻ��������ļ�(sys_config.properties)�Ƿ��Ѿ���ȷ���á�",e);
		}
	}

}
