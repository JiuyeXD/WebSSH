package ro.qwq.webssh.constant;

import com.google.common.collect.ImmutableList;

import java.util.List;

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

    /** 发送指令: 连接 */
    public static final String WEBSSH_OPERATE_CONNECT = "connect";

    /** 发送指令: 命令 */
    public static final String WEBSSH_OPERATE_COMMAND = "command";

    /** 发送指令: 心跳 */
    public static final String WEBSSH_OPERATE_HEARTBEAT = "heartbeat";

    /** ESC按键编码 */
    public static final String ESC_KEY = "\u001B";

    /** ip地址最大长度(ipv4&ipv6) */
    public static final Integer IP_ADDRESS_LENGTH = 15;

    /** ipv4 local */
    public static final String LOCAL_IPV4_ADDRESS = "127.0.0.1";

    /** ipv6 local */
    public static final String LOCAL_IPV6_ADDRESS = "0:0:0:0:0:0:0:1";

    /** 常见网络运营商 */
    public static final List<String> CHINA_NETWORK_OPERATORS = ImmutableList.of("移动", "联通", "电信", "网通", "铁通", "卫通", "长城宽带");

    /** Address */
    public static final String VISITOR_ADDRESS = "city";
}
