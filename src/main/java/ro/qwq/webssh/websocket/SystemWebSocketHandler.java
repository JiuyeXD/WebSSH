package ro.qwq.webssh.websocket;

import ro.qwq.webssh.constant.MagicString;
import ro.qwq.webssh.service.WebSSHService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

/**
 * <p>
 * SystemWebSocketHandler - websocket处理器
 * </p>
 *
 * @since 2022/1/4
 * @author JiuyeXD
 * @version 1.0
 */
@Component
@Log4j2
public class SystemWebSocketHandler implements WebSocketHandler{
    @Autowired
    private WebSSHService webSSHService;

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/4 18:02
     * @Description: 连接回调
     * @param wss WebSocketSession
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession wss) throws Exception {
        log.info("\nUser: {} Connected", wss.getAttributes().get(MagicString.USER_UUID_KEY));
        /* 初始化连接 */
        webSSHService.initConnection(wss);
    }


    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/4 18:03
     * @Description: 消息回调
     * @param wss
     * @param wsm
     */
    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        if (wsm instanceof TextMessage) {
            log.info("\nUser: {} \nSendCommand:{}", wss.getAttributes().get(MagicString.USER_UUID_KEY), wsm.getPayload().toString());
            /* 接收消息 */
            webSSHService.recvHandle(((TextMessage) wsm).getPayload(), wss);
        }else if(!(wsm instanceof BinaryMessage) && !(wsm instanceof PongMessage)) {
            log.info("Unexpected WebSocket message type: {}", wsm);
        }
    }


    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/4 18:03
     * @Description: 错误回调
     * @param wss
     * @param throwable
     */
    @Override
    public void handleTransportError(WebSocketSession wss, Throwable throwable) throws Exception {
        log.error("Data transmission error");
    }


    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/4 18:04
     * @Description: 连接关闭回调
     * @param wss
     * @param closeStatus
     */
    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus closeStatus) throws Exception {
        log.info("\nUser: {} Disconnected", String.valueOf(wss.getAttributes().get(MagicString.USER_UUID_KEY)));
        /* 关闭连接 */
        webSSHService.close(wss);
    }

    /**
     * @return false;
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
