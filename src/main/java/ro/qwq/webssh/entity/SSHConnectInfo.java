package ro.qwq.webssh.entity;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

/**
 * <p>
 * SSHConnectInfo - ssh连接信息
 * </p>
 *
 * @since 2022/1/5
 * @author JiuyeXD
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SSHConnectInfo {
    
    /** websocket session */ 
    private WebSocketSession webSocketSession;
    
    /** jsch */ 
    private JSch jSch;
    
    /** 通道 */
    private Channel channel;

    public SSHConnectInfo(WebSocketSession webSocketSession, JSch jSch) {
        this.webSocketSession = webSocketSession;
        this.jSch = jSch;
    }
}
