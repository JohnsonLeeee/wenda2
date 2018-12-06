package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping(path = "/reg", method = {RequestMethod.POST})
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password) {

        try {
            Map<String, String> map = userService.register(username, username);
            if (map.containsKey("msg")) {
                model.addAttribute("msg", map.get("msg"));
            }
            return "redirect:/";
        } catch (Exception e) {
            logger.error("注册异常", e.getMessage());
            return "login";
        }
    }


    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String reglogin(Model model) {
        return "login";
    }

    @RequestMapping(path = {"/login"}, method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme) {
        try {
            Map<String, String> map = userService.login(username, username);
            if (map.containsKey("msg")) {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
            return "redirect:/";
        } catch (Exception e) {
            logger.error("注册异常", e.getMessage());
            return "login";
        }
    }
}
