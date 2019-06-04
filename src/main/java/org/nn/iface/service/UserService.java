package org.nn.iface.service;

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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private StarDao starDao;

    @Resource
    private CommentDao commentDao;

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

    public List<CommentDto> getComments(String name) {
        User user = userDao.findPasswordByName(name);
        List<Comment> list = commentDao.findCommentByUserId(user.getId());
       List<CommentDto> commentDtos = new ArrayList<>();
       for(int i = 0; i < list.size(); i++) {
           CommentDto commentDto = new CommentDto();
           commentDto.setContentPhotos(list.get(i).getPicture());
           commentDto.setName(user.getName());
           commentDto.setTime(list.get(i).getCreated());
           commentDto.setContentText(list.get(i).getContext());
           commentDto.setAvatar(user.getPic());
           commentDto.setHeartCount(list.get(i).getStarCount());
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
            commentDto.setContentPhotos(comments.get(i).getPicture());
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
        commentDao.insertComment(comment);
    }
}
