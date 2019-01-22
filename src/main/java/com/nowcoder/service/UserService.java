package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nowcoder.util.WendaUtil;

import java.util.*;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/
@Service
public class UserService {

    /*
     * private - so that no other class can hijack your logger
     * static - so there is only one logger instance per class, also avoiding attempts to serialize loggers
     * final - no need to change the logger over the lifetime of the class
     */
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public User getUser(String name) {
        return userDAO.selectByName(name);
    }

    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<>();

        // org.apache.commons.lang.StringUtils类是用于操作Java.lang.String类的，而且此类是null安全的，
        // 即如果输入参数String为null，则不会抛出NullPointerException异常。
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user != null) {
            map.put("msg", "用户名已经被注册。");
            return map;
        }

        user = new User();
        user.setName(username);
        String salt = UUID.randomUUID().toString().substring(0, 5);
        user.setSalt(salt);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password+salt));
        userDAO.addUser(user);
        // logger.info("userDAO执行成功");

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        // logger.info("addLoginTicket执行成功");

        return map;
    }


    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "输入密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码错误");
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        map.put("userId", user.getId());

        return map;
    }


    public String addLoginTicket(int userId) {
        // logger.info("开始执行addLoginTicket");
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(now.getTime()+3600*24*100);
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        // logger.info("即将执行loginTicketDAO.addTicket,loginTicket:" + loginTicket);
        loginTicketDAO.addTicket(loginTicket);
        // logger.info("addLoginTicket执行结束");
        return loginTicket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
