package ro.qwq.webssh.constant;

/**
 * <p>
 * MagicString - 魔法值
 * </p>
 *
 * @since 2022/1/5
 * @author JiuyeXD
 * @version 1.0
 */
public class MagicString {

    /** 随机生成uuid的key名 */
    public static final String USER_UUID_KEY = "uid";

    /** 发送指令：连接 */
    public static final String WEBSSH_OPERATE_CONNECT = "connect";

    /** 发送指令：命令 */
    public static final String WEBSSH_OPERATE_COMMAND = "command";

    /** 发送指令：心跳 */
    public static final String WEBSSH_OPERATE_HEARTBEAT = "heartbeat";

    public static final String ESC_KEY = "\u001B";
}
