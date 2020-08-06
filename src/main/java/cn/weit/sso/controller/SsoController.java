package cn.weit.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author weitong
 */
@Controller
public class SsoController {

    @GetMapping(value = "/login/gateway")
    public String login(@RequestParam(value = "url", required = false) String url) {
        return "login";
    }
}
