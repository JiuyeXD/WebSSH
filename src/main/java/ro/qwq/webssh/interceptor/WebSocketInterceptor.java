package ro.qwq.webssh.interceptor;

import ro.qwq.webssh.Utils.NetworkUtils;
import ro.qwq.webssh.constant.MagicString;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * WebSocketInterceptor - websocket 拦截器
 * </p>
 *
 * @since 2022/1/5
 * @author JiuyeXD
 * @version 1.0
 */
public class WebSocketInterceptor implements HandshakeInterceptor {

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 9:29
     * @Description: Handler处理之前调用
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param map
     * @return boolean
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
            /* 生成一个uuid */ 
            String uuid = UUID.randomUUID().toString().replace("-","");
            /* 存储用户的uuid */ 
            map.put(MagicString.USER_UUID_KEY, uuid);

            String ip = NetworkUtils.getIpAddr(((ServletServerHttpRequest) serverHttpRequest).getServletRequest());
            String addressAndOperator = NetworkUtils.getAddressByBD2(ip);
            String address = NetworkUtils.removeNetworkOperator(addressAndOperator);
            map.put(MagicString.VISITOR_ADDRESS, address);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/5 9:30
     * @Description: Handler处理之后调用
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param e
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }

}
