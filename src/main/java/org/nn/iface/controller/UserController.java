package org.nn.iface.controller;

import org.apache.http.protocol.HTTP;
import org.apache.ibatis.annotations.Param;
import org.nn.iface.domain.Comment;
import org.nn.iface.domain.User;
import org.nn.iface.dto.CommentDto;
import org.nn.iface.dto.StarDto;
import org.nn.iface.dto.UserCommentDto;
import org.nn.iface.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/login")
    @ResponseBody
    public User login(@Param("name") String name, @Param("password") String password) {
        User user = userService.findUser(name);
        if(Objects.isNull(user) || !user.getPassword().equals(password)) {
            return user;
        }
        return user;
    }

    @GetMapping("/doSync")
    public String sync(HttpServletRequest request) {
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        String code = request.getParameter("code");
        List<String> list = new ArrayList();
        return echostr;
    }

    @GetMapping("/getStar")
    @ResponseBody
    public StarDto findStar(@RequestParam(value = "name") String name) {
       return userService.findStar(name);
    }

    @GetMapping("/getComments")
    @ResponseBody
    public List<CommentDto> getCommentList(@RequestParam(value = "name") String name) {
        return userService.getComments(name);
    }

    @GetMapping("/getUser")
    @ResponseBody
    public User getUser(@RequestParam(value = "name") String name) {
        return userService.findUser(name);
    }

    @PostMapping("/comment")
    @ResponseBody
    public void comment(UserCommentDto userCommentDto) {
        userService.addComment(userCommentDto);
    }


    @PostMapping("/uploadCommit")
    @ResponseBody
    public void uploadCommit(UserCommentDto userCommentDto) {
        userService.addComment(userCommentDto);
    }

}
