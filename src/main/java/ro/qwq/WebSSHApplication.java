package ro.qwq;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * <p>
 * WebSSHApplication - 项目启动类
 * </p>
 *
 * @since 2022/1/4
 * @author JiuyeXD
 * @version 1.0
 */
@SpringBootApplication
@Log4j2
public class WebSSHApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSSHApplication.class);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        log.info("            _.._         \n" +
                "           (_.-.\\        \n" +
                "       .-,       `       \n" +
                "  .--./ /     _.-\"\"-.    \n" +
                "   '-. (__..-\"       \\   \n" +
                "      \\          a    |  \n" +
                "       ',.__.   ,__.-'/  \n" +
                "         '--/_.'----'`   \n" +
                "     WebSSHApplication启动成功......");
    }
}
