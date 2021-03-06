package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/
@Controller
public class LoginController {

    /*
     * private - so that no other class can hijack your logger
     * static - so there is only one logger instance per class, also avoiding attempts to serialize loggers
     * final - no need to change the logger over the lifetime of the class
     */
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = "/reg", method = {RequestMethod.POST})
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "next", defaultValue = "/") String next,
                      HttpServletResponse response,
                      HttpServletRequest request
                      ) {

        // 这里用request接收username和password参数也是可以的，@requestParam也可以
        // String pwd = request.getParameter("password");
        // System.out.println(pwd);

        try {
            Map<String, String> map = userService.register(username, password);
            logger.info("map:" + map);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                logger.info("Cookie:" + cookie);

                return "redirect:" + next;
            } else {
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }

            // 这里如果不捕获错误，服务器会发生什么？
            // 刚才尝试了一下，服务器直接出错，出现white_erro_page；
            // 加了catch语句，会重新跳转回login页面
        } catch (Exception e) {
            logger.error("注册异常", e.getMessage());
            return "login";
        }
    }


    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String reglogin(Model model, @RequestParam(value = "next", defaultValue = "/") String next) {
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(path = {"/login"}, method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberMe,
                        @RequestParam(value = "next", defaultValue = "/") String next,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                // cookie.setPath的用法
                cookie.setPath("/");

                if (rememberMe) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }

                response.addCookie(cookie);

                // 登录后把一个登录事件添加到阻塞队列中
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExts("username", username)
                        .setExts("email", "13122386386@163.com")
                        .setActorId((int)map.get("userId"))
                );

                return "redirect:" + next;

            } else {
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            logger.error("登录异常", e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
