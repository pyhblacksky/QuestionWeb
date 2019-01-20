package com.nowcoder.service;

import com.nowcoder.DAO.CommentDAO;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: pyh
 * @Date: 2019/1/18 10:18
 * @Version 1.0
 * @Function:   实现评论服务
 */
@Service
public class CommentService {

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveService sensitiveService;

    //获取评论
    public List<Comment> getCommentsByEntity(int entityId, int entityType){
        return commentDAO.selectCommentByEntity(entityId, entityType);
    }

    //添加评论
    public int addComment(Comment comment){
        //进行敏感词和安全过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    //获取评论数
    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }

    //删除某条评论,底层更新status,置为1，表示删除
    public boolean deleteComment(int commentId){
        return commentDAO.updateStatus(commentId, 1) > 0;
    }

    //通过id获取Comment
    public Comment getCommentById(int id){
        return commentDAO.selectCommentById(id);
    }
}
