package ro.qwq.webssh.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ro.qwq.webssh.Utils.NetworkUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * RouterController - 请求路由控制器
 * </p>
 *
 * @since 2022/1/5
 * @author JiuyeXD
 * @version 1.0
 */
@Controller
@Log4j2
public class RouterController {

    @GetMapping({"/", "/index"})
    public String index(HttpServletRequest request){
        log.info(NetworkUtils.getIpAddr(request));
        return "index";
    }
}
