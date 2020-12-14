package com.panchao.blog.service.impl;

import com.panchao.blog.exception.NotFoundException;
import com.panchao.blog.model.entity.Post;
import com.panchao.blog.model.param.PostQuery;
import com.panchao.blog.repository.PostRepository;
import com.panchao.blog.service.PostService;
import com.panchao.blog.util.BeanUtils;
import com.panchao.blog.util.MarkdownUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Post Service Impl
 */
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public Page<Post> page(PostQuery postQuery, Pageable pageable) {
        return postRepository.findAll(buildSpecByQuery(postQuery), pageable);
    }

    @Override
    public Page<Post> page(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public List<Post> recommendTop(Integer size) {
        Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "updateTime");
        return postRepository.findAll(pageable).getContent();
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post update(Long id, Post post) {
        Post b = postRepository.getOne(id);
        if (Objects.isNull(b)) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.updateProperties(post, b);
        return postRepository.save(b);
    }

    @Override
    public Post get(Long id) {
        return postRepository.getOne(id);
    }

    @Override
    public Post getAndConvert(Long id) {
        Post post = this.get(id);
        if (Objects.isNull(post)) {
            throw new NotFoundException("该博客不存在");
        }
        String content = post.getContent();
        post.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        postRepository.updateViews(id);
        return post;
    }

    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    private Specification<Post> buildSpecByQuery(PostQuery postQuery) {
        return (Specification<Post>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(postQuery.getTitle())) {
                String likeTitle = String.format("%%%s%%", StringUtils.strip(postQuery.getTitle()));
                Predicate predicate = criteriaBuilder.like(root.get("title"), likeTitle);
                predicates.add(predicate);
            }
            if (!Objects.isNull(postQuery.getCategoryId())) {
                Predicate predicate = criteriaBuilder.equal(root.get("category").get("id"), postQuery.getCategoryId());
                predicates.add(predicate);
            }
            if (postQuery.isRecommend()) {
                Predicate predicate = criteriaBuilder.equal(root.get("recommend"), postQuery.isRecommend());
                predicates.add(predicate);
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }
}
