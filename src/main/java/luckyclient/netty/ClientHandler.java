package luckyclient.netty;

import java.io.*;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.util.StrUtil;
import luckyclient.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import luckyclient.utils.config.SysConfig;
import springboot.RunService;


public class ClientHandler extends ChannelHandlerAdapter {

    //��application.properties�ļ��л�ȡ�õ��Ĳ���;
    private static final Resource resource = new ClassPathResource("application.properties");
    private static Properties props;

    static {
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String port = props.getProperty("server.port");

    private String NETTY_HOST = SysConfig.getConfiguration().getProperty("netty.host");

    private static final String CLIENT_NAME = SysConfig.getConfiguration().getProperty("client.name");

    private static final String CLIENT_VERSION = SysConfig.getConfiguration().getProperty("client.verison");

    private static final String SERVER_IP = SysConfig.getConfiguration().getProperty("server.web.ip");

    private static final String SERVER_PORT = SysConfig.getConfiguration().getProperty("server.web.port");

    public static Integer clientId=-1;

    private volatile int lastLength = 0;

    public RandomAccessFile randomAccessFile;


    private static ChannelHandlerContext ctx;

    public ClientHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        //ͳһת����
        //�������Ϣ����,������յ��������񷽷�����ֱ�Ӳ���һ��http���󲢷������󵽱���
        String jsonStr = msg.toString();
        JSONObject json;
        try {
            json = JSON.parseObject(msg.toString());

        } catch (Exception e) {
            LogUtil.APP.error("�յ�����˷�Json��Ϣ,�����쳣��" + msg);
            return;
        }
        LogUtil.APP.info("�յ��������Ϣ��" + json.toString());
        //������Ϣ
        if ("run".equals(json.get("method"))) {
            //��������
            JSONObject re = new JSONObject();
            re.put("method", "return");
            Result result = new Result(1, "ͬ���ȴ���Ϣ����", json.get("uuid").toString(), null);
            //����ǵ�����������һ��HTTP����
            //��ȡ��get��������post����
            String getOrPost = json.get("getOrPost").toString();
            String urlParam = "http://127.0.0.1:" + port + "/" + json.get("url").toString();
            Integer socketTimeout = Integer.valueOf(json.get("socketTimeout").toString());
            String tmpResult = "";
            if ("get".equals(getOrPost)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> jsonparams = (Map<String, Object>) json.get("data");
                //get����
                try {
                    tmpResult = HttpRequest.httpClientGet(urlParam, jsonparams, socketTimeout);
                } catch (Exception e) {
                    LogUtil.APP.error("ת�������GET�������");
                }
            } else {
                String jsonparams = json.get("data").toString();
                //post����
                try {
                    tmpResult = HttpRequest.httpClientPost(urlParam, jsonparams, socketTimeout);
                } catch (Exception e) {
                    LogUtil.APP.error("ת�������POST�������");
                }
            }
            result.setMessage(tmpResult);
            re.put("data", result);
            sendMessage(re.toString());
        } else if ("download".equals(json.get("method"))) {
            String loadpath = json.get("path").toString();
            String path = RunService.APPLICATION_HOME + loadpath;
            String fileName = json.get("fileName").toString();
            //����http���������ļ�
            try {
                HttpRequest.downLoadFromUrl("http://" + SERVER_IP + ":" + SERVER_PORT + "/common/download?fileName=" + fileName, fileName, path);
                //��������
                JSONObject re = new JSONObject();
                Result result = new Result(1, "�ϴ��ɹ�", json.get("uuid").toString(), null);
                re.put("method", "return");
                re.put("data", result);
                sendMessage(re.toString());
                LogUtil.APP.info("�����������ɹ�,·��Ϊ��" + path + ";�ļ���Ϊ��" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                //��������
                JSONObject re = new JSONObject();
                Result result = new Result(0, "�ϴ�ʧ��", json.get("uuid").toString(), null);
                re.put("method", "return");
                re.put("data", result);
                sendMessage(re.toString());
                LogUtil.APP.error("����������ʧ��,·��Ϊ��" + path + ";�ļ���Ϊ��" + fileName);
            }
        } else if ("upload".equals(json.get("method"))) {
            try {
                //�ϴ���ͼ��������
                @SuppressWarnings("unchecked")
				Map<String, Object> jsonparams = (Map<String, Object>) json.get("data");
                String fileName = jsonparams.get("imgName").toString();
                String ctxPath = RunService.APPLICATION_HOME + File.separator + "log" + File.separator + "ScreenShot" + File.separator + fileName;
                File file = new File(ctxPath);
                int start = Integer.parseInt(json.get("start").toString());
                FileUploadFile fileUploadFile = new FileUploadFile();
                fileUploadFile.setFile(file);
                if (start != -1) {
                    if (start == 0)
                        lastLength = 1024 * 10;
                    randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(), "r");
                    randomAccessFile.seek(start); //���ļ���λ��start
                    LogUtil.APP.info("���ȣ�" + (randomAccessFile.length() - start));
                    int a = (int) (randomAccessFile.length() - start);
                    int b = (int) (randomAccessFile.length() / 1024 * 2);
                    if (a < lastLength) {
                        lastLength = a;
                    }
                    LogUtil.APP.info("�ļ����ȣ�" + (randomAccessFile.length()) + ",start:" + start + ",a:" + a + ",b:" + b + ",lastLength:" + lastLength);
                    byte[] bytes = new byte[lastLength];
                    LogUtil.APP.info("bytes�ĳ�����=" + bytes.length);
                    int byteRead;
                    if ((byteRead = randomAccessFile.read(bytes)) != -1 && (randomAccessFile.length() - start) > 0) {
                        LogUtil.APP.info("byteRead = " + byteRead);
                        fileUploadFile.setEndPos(byteRead);
                        fileUploadFile.setBytes(bytes);
                        //��������
                        JSONObject re = new JSONObject();
                        Result result = new Result(1, "�ϴ��ɹ�", json.get("uuid").toString(), fileUploadFile);
                        re.put("method", "upload");
                        re.put("data", result);
                        re.put("uuid", json.get("uuid").toString());
                        re.put("imgName", fileName);
                        re.put("start", start);
                        sendMessage(re.toString());
                        try {
                            ctx.writeAndFlush(fileUploadFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        randomAccessFile.close();
                        LogUtil.APP.info("�ļ��Ѿ�����channelRead()--------" + byteRead);
                        //��������
                        JSONObject re = new JSONObject();
                        Result result = new Result(1, "�ϴ��ɹ�", json.get("uuid").toString(), null);
                        re.put("method", "return");
                        re.put("data", result);
                        sendMessage(re.toString());

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //��������
                JSONObject re = new JSONObject();
                Result result = new Result(0, "�쳣����", json.get("uuid").toString(), null);
                re.put("method", "return");
                re.put("data", result);
                sendMessage(re.toString());
            }
        }
        else if("loginReturn".equals(json.get("method")))
        {
            try {
                //����client���
                clientId=Integer.valueOf(json.get("clientId").toString());
            } catch (Exception e){
                LogUtil.APP.error("����ClientIDʧ�ܣ������쳣",e);
            }
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //���Ϳͻ��˵�¼��Ϣ
        JSONObject json = new JSONObject();
        if(StrUtil.isEmpty(NETTY_HOST)){
            this.NETTY_HOST=RunService.CLIENT_IP;
        }
        json.put("hostName", NETTY_HOST);
        json.put("clientName", CLIENT_NAME);
        json.put("version", CLIENT_VERSION);
        json.put("ip", RunService.CLIENT_IP);
        json.put("method", "clientUp");
        ClientHandler.ctx = ctx;
        sendMessage(json.toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LogUtil.APP.info("�����ѶϿ������ڳ�������...");
        //ʹ�ù����ж�������
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(() -> {
            try {
                NettyClient.start();
            } catch (Exception e) {
                LogUtil.APP.error("���ӳ����쳣�����ڳ�������...",e);
            }
        }, 1, TimeUnit.SECONDS);

        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                //��������,���ֳ�����
                JSONObject json = new JSONObject();
                json.put("method", "ping");
                json.put("hostName", NETTY_HOST);
                //ctx.channel().writeAndFlush(json.toString() + "$_").sync();
                sendMessage(json.toString());
                //log.info("�������ͳɹ�!");
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    public static void sendMessage(String json) {
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer((json + "$_").getBytes()));
    }

}