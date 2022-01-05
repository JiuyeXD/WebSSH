package ro.qwq.webssh.service;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * <p>
 * WebSSHService - webssh业务逻辑层
 * </p>
 *
 * @since 2022/1/5
 * @author JiuyeXD
 * @version 1.0
 */
public interface WebSSHService {

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 9:57
     * @Description: 连接初始化
     * @param session
     */
    public void initConnection(WebSocketSession session);


    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 9:57
     * @Description: 处理客户段发的数据
     * @param session
     */
    public void recvHandle(String buffer, WebSocketSession session);

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 9:57
     * @Description: 将数据回显给前端
     * @param session
     */
    public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException;

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 9:58
     * @Description: 关闭连接
     * @param session session
     */
    public void close(WebSocketSession session);
}
