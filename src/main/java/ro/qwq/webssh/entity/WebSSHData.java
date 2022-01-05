package ro.qwq.webssh.entity;

import lombok.Data;

/**
 * <p>
 * WebSSHData - webssh数据传输
 * </p>
 *
 * @since 2022/1/5
 * @author JiuyeXD
 * @version 1.0
 */
@Data
public class WebSSHData {

    /** 主机域名/ip */
    private String host;
    /** 端口号默认为22 */
    private Integer port = 22;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;

    /**
     * 操作类型
     * {@link ro.qwq.webssh.constant.MagicString}
     */
    private String operate;
    /** 发送命令 */
    private String command = "";

    /** 列数 */
    private int cols = 80;
    /** 行数 */
    private int rows = 24;
    /** 宽度 */
    private int width = 640;
    /** 高度 */
    private int height = 480;

}
