package ro.qwq.webssh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;
import ro.qwq.webssh.constant.MagicString;
import ro.qwq.webssh.entity.SSHConnectInfo;
import ro.qwq.webssh.entity.WebSSHData;
import ro.qwq.webssh.service.WebSSHService;
import com.jcraft.jsch.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * <p>
 * WebSSHServiceImpl - webssh业务逻辑层实现类
 * </p>
 *
 * @since 2022/1/5
 * @author JiuyeXD
 * @version 1.0
 */
@Service
@Log4j2
public class WebSSHServiceImpl implements WebSSHService {

    /** 存放ssh连接信息的map */
    private static Map<String, Object> sshMap = new ConcurrentHashMap<>();

    /** 线程池 */
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
    private ExecutorService executorService = new ThreadPoolExecutor(10,20,200L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(),namedThreadFactory);

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 10:02
     * @Description: 连接初始化
     * @param session
     */
    @Override
    public void initConnection(WebSocketSession session) {
        String uuid = String.valueOf(session.getAttributes().get(MagicString.USER_UUID_KEY));
        /* 将这个ssh连接信息放入map中 */
        sshMap.put(uuid, new SSHConnectInfo(session, new JSch()));
    }

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 10:03
     * @Description: 处理客户端发送的数据
     * @param session
     */
    @Override
    public void recvHandle(String buffer, WebSocketSession session) {
        /* Json数据转换为webSSH数据实体类 */
        WebSSHData webSSHData = JSONObject.parseObject(buffer, WebSSHData.class);
        String userId = session.getAttributes().get(MagicString.USER_UUID_KEY).toString();
        if (MagicString.WEBSSH_OPERATE_CONNECT.equals(webSSHData.getOperate())) {
            /* 找到刚才存储的ssh连接对象 */
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
            /* 启动线程异步处理 */
            executorService.execute(() -> {
                try {
                    connectToSSH(sshConnectInfo, webSSHData, session);
                    if (sshConnectInfo.getChannel().isClosed()) {
                        close(session);
                    }
                } catch (JSchException | IOException e) {
                    log.error("Connection error");
                    log.error("msg: {}, detail: {}" + e.getMessage(), e);
                    try {
                        sendMessage(session, ("ERROR : "+e.getMessage()).getBytes());
                    } catch (IOException ex) {
                        log.error("Failed to send message");
                        log.error("msg: {}, detail: {}" + ex.getMessage(), ex);
                    }
                    close(session);
                }
            });
        } else if (MagicString.WEBSSH_OPERATE_COMMAND.equals(webSSHData.getOperate())) {
            String command = webSSHData.getCommand();
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
            if (sshConnectInfo != null) {
                try {
                    ChannelShell channel = (ChannelShell) sshConnectInfo.getChannel();
                    if (channel != null) {
                        channel.setPtySize(webSSHData.getCols(),webSSHData.getRows(),webSSHData.getWidth(),webSSHData.getHeight());
                        transToSSH(channel, command);
                        if (channel.isClosed()) {
                            close(session);
                        }
                    }
                } catch (IOException e) {
                    log.error("Connection error");
                    log.error("msg: {}, detail: {}" + e.getMessage(), e);
                    try {
                        sendMessage(session, ("ERROR : "+e.getMessage()).getBytes());
                    } catch (IOException ex) {
                        log.error("Failed to send message");
                        log.error("msg: {}, detail: {}" + ex.getMessage(), ex);
                    }
                    close(session);
                }
            }
        } else if (MagicString.WEBSSH_OPERATE_HEARTBEAT.equals(webSSHData.getOperate())) {
            /* 检查心跳 */
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
            if (sshConnectInfo != null) {
                try {
                    /* 处于连接状态则发送健康数据，不能为空，空则断开连接。*/
                    if (sshConnectInfo.getChannel().isConnected()) {
                        sendMessage(session, "Heartbeat healthy".getBytes());
                    }
                } catch (IOException e) {
                    log.error("Failed to send message");
                    log.error("msg: {}, detail: {}" + e.getMessage(), e);
                }
            }
        } else {
            log.error("Unsupported operation");
            close(session);
        }
    }

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 10:04
     * @Description: 将数据回显给前端
     * @param session
     */
    @Override
    public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException {
        session.sendMessage(new TextMessage(buffer));
    }

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 10:05
     * @Description: 关闭连接
     * @param session
     */
    @Override
    public void close(WebSocketSession session) {
        String userId = String.valueOf(session.getAttributes().get(MagicString.USER_UUID_KEY));
        SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
        if (sshConnectInfo != null) {
            //断开连接
            if (sshConnectInfo.getChannel() != null) {
                sshConnectInfo.getChannel().disconnect();
            }
            //map中移除
            sshMap.remove(userId);
        }
        try {
            session.close();
        } catch (IOException e) {
            log.error("msg: {}, detail: {}" + e.getMessage(), e);
        }
    }

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 10:05
     * @Description: 使用jsch连接终端
     * @param sshConnectInfo ssh连接信息
     * @param webSSHData ssh连接数据
     * @param webSocketSession websocket session
     */
    private void connectToSSH(SSHConnectInfo sshConnectInfo, WebSSHData webSSHData, WebSocketSession webSocketSession) throws JSchException, IOException {
        /* 获取jsch的会话 */ 
        Session session = sshConnectInfo.getJSch().getSession(webSSHData.getUsername(), webSSHData.getHost(), webSSHData.getPort());
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        /* 设置密码 */ 
        session.setPassword(webSSHData.getPassword());
        /* 设置连接超时时间 */ 
        session.connect(30000);

        /* 开启shell通道 */
        Channel channels = session.openChannel("shell");
        ChannelShell channel = (ChannelShell) channels;
        channel.setPtySize(webSSHData.getCols(),webSSHData.getRows(),webSSHData.getWidth(),webSSHData.getHeight());
        /* 设置通道连接超时时间 */
        channel.connect(3000);
        /* 设置channel */
        sshConnectInfo.setChannel(channel);
        /* 转发消息 */
        transToSSH(channel, "curl wttr.in/青岛\n");

        /* 读取终端返回的信息流 */
        try (InputStream inputStream = channel.getInputStream()) {
            /* 循环读取 */
            byte[] buffer = new byte[1024];
            int i = 0;
            /* 如果没有数据来 线程会一直阻塞在这个地方等待数据 */
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
            }
        } finally {
            /* 断开连接后关闭会话 */
            session.disconnect();
            channel.disconnect();
        }

    }

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 10:05
     * @Description: 将消息转发到终端
     * @param channel channel
     * @param command 指令
     */
    private void transToSSH(Channel channel, String command) throws IOException {
        log.info("command: {}", command);
        if (channel != null) {
            if(command.equals(MagicString.ESC_KEY)){
                command = "\rclear\r";
            }
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }

}
