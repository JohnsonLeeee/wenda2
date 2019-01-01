package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.LikeService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999, "未登录");
        }
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.COMMENT, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999, "未登录");
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.COMMENT, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }

}
