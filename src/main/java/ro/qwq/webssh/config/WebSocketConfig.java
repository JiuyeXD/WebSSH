package ro.qwq.webssh.config;

import ro.qwq.webssh.interceptor.WebSocketInterceptor;
import ro.qwq.webssh.websocket.SystemWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


/**
 * <p>
 * WebSocketConfig - webSocket配置类
 * </p>
 *
 * @since 2022/1/5
 * @author JiuyeXD
 * @version 1.0
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

    @Autowired
    private SystemWebSocketHandler webSocketHandler;

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 9:20
     * @Description: 指定 websocket 处理器路径
     * @param webSocketHandlerRegistry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(webSocketHandler, "/webssh")
                .addInterceptors(new WebSocketInterceptor())
                .setAllowedOrigins("*");
    }

}
