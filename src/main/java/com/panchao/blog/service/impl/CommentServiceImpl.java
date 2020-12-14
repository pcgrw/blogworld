package com.panchao.blog.service.impl;

import com.panchao.blog.model.entity.Comment;
import com.panchao.blog.repository.CommentRepository;
import com.panchao.blog.service.CommentService;
import com.panchao.blog.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Comment Service Impl
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> listByPostId(Long postId) {
        Sort sort = Sort.by("createTime");
        List<Comment> comments = commentRepository.findByPostIdAndParentCommentNull(postId, sort);
        return this.eachComment(comments);
    }

    /*
    循环每个顶级的评论节点
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsVisew = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.updateProperties(comment, c);
            commentsVisew.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsVisew);
        return commentsVisew;
    }

    /*
    root根节点，blog不为空的对象集合
     */
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComment();
            for (Comment reply1 : replys1) {
                //循环迭代,找出子代,并存放在tempReplys中
                recursively(reply1);
            }
            comment.setReplyComment(tempReplys);
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代集合
    private List<Comment> tempReplys = new ArrayList<>();

    /*
    递归迭代，剥洋葱
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if (comment.getReplyComment().size() > 0) {
            List<Comment> replys = comment.getReplyComment();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComment().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }
}
