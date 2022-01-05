package ro.qwq.webssh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
public class RouterController {

    @GetMapping({"/", "/index"})
    public String index(){
        return "index";
    }
}
