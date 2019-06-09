package org.nn.iface.service;

import org.apache.tomcat.util.buf.StringUtils;
import org.nn.iface.core.SpiderUtil;
import org.nn.iface.dao.CommentDao;
import org.nn.iface.dao.StarDao;
import org.nn.iface.dao.UserDao;
import org.nn.iface.domain.Comment;
import org.nn.iface.domain.Star;
import org.nn.iface.domain.User;
import org.nn.iface.dto.CommentDto;
import org.nn.iface.dto.StarDto;
import org.nn.iface.dto.UserCommentDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private StarDao starDao;

    @Resource
    private CommentDao commentDao;
    private InputStream stream;
    private String path;
    private String filename;

    public static String imagePath = "/Users/liwei/Desktop/commitImage";

    public User findUser(String name) {
        return userDao.findPasswordByName(name);
    }

    public User findUserById(Long id) {
        return userDao.findById(id);
    }

    public void insertStar() {
        List<Star> list = SpiderUtil.getWebPage();
        for(int i = 0; i < list.size(); i++) {
            starDao.insertStar(list.get(i));
        }
    }

    public void insert(User user) {
        userDao.insertUser(user);
    }

    public List<CommentDto> getComments(String name) {
        User user = userDao.findPasswordByName(name);
        List<Comment> list = commentDao.findCommentByUserId(user.getId());
       List<CommentDto> commentDtos = new ArrayList<>();
       for(int i = 0; i < list.size(); i++) {
           CommentDto commentDto = new CommentDto();
           commentDto.setName(user.getName());
           commentDto.setTime(list.get(i).getCreated());
           commentDto.setContentText(list.get(i).getContext());
           commentDto.setAvatar(user.getPic());
           commentDto.setHeartCount(list.get(i).getStarCount());
           if(list.get(i).getPicture() != null) {
               commentDto.setContentPhotos(new ArrayList<>(Arrays.asList(list.get(i).getPicture().split(","))));
           }
           commentDtos.add(commentDto);
       }
       return commentDtos;
    }

    public StarDto findStar(String name) {
        Star star = starDao.findStarByName(name);
        List<Comment> comments = commentDao.findCommentByStarId(star.getId());
        StarDto starDto = new StarDto();
        starDto.setName(star.getName());
        starDto.setUrl(star.getPic());
        starDto.setIntroduce(star.getIntroduce());
        starDto.setWorks(star.getWorks());
        List<CommentDto> commentDtos = new ArrayList<>();
        for(int i = 0; i < comments.size(); i++) {
            CommentDto commentDto = new CommentDto();
            User user = userDao.findById(comments.get(i).getUserId());
            commentDto.setAvatar(user.getPic());
            commentDto.setContentText(comments.get(i).getContext());
            commentDto.setTime(comments.get(i).getCreated());
            commentDto.setName(user.getName());
            if(comments.get(i).getPicture() != null) {
                commentDto.setContentPhotos(new ArrayList<>(Arrays.asList(comments.get(i).getPicture().split(","))));
            }
            commentDtos.add(commentDto);
        }
        starDto.setCommentList(commentDtos);
        return starDto;
    }

    public void addComment(UserCommentDto userCommentDto) {
        User user = userDao.findPasswordByName(userCommentDto.getUserName());
        Star star = starDao.findStarByName(userCommentDto.getStarName());
        Comment comment = new Comment();
        comment.setStarId(star.getId());
        comment.setUserId(user.getId());
        comment.setContext(userCommentDto.getContext());
        if(userCommentDto.getFiles() != null) {
            List<String> fileStr = new ArrayList<>();
            for(int i = 0; i < userCommentDto.getFiles().length; i++) {
                MultipartFile file = userCommentDto.getFiles()[i];
                try {
                    saveFileFromInputStream(file.getInputStream(), file.getOriginalFilename());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileStr.add("http://localhost:8080" + "/" + file.getOriginalFilename());
            }
            String pic = StringUtils.join(fileStr, ',');
            comment.setPicture(pic);
        }
        commentDao.insertComment(comment);
    }


    public void saveFileFromInputStream(InputStream stream, String filename) throws IOException
    {
        this.stream = stream;
        this.filename = filename;
        FileOutputStream fs=new FileOutputStream( imagePath + "/"+ filename);
        byte[] buffer =new byte[1024*1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread=stream.read(buffer))!=-1)
        {
           bytesum+=byteread;
           fs.write(buffer,0,byteread);
           fs.flush();
        }
        fs.close();
        stream.close();
    }
}

