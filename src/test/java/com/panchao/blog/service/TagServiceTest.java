package com.panchao.blog.service;

import com.panchao.blog.BlogWorldApplicationTests;
import com.panchao.blog.model.entity.Tag;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;

public class TagServiceTest extends BlogWorldApplicationTests {
    @Autowired
    private TagService tagService;

    @Test
    public void create() {
        Tag tag = new Tag();
        tag.setTagName("C");
        tag.setSlug("C");
        Tag resultTag = tagService.save(tag);
        Assert.assertEquals("与预期不符", "C", resultTag.getTagName());
    }

    @Test
    public void update() {
        Tag tag = new Tag();
        tag.setSlug("c");
        Tag resultTag = tagService.update(2L, tag);
        Assert.assertEquals("与预期不符", "c", resultTag.getSlug());
    }

    @Test
    public void listAll() {
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        List<Tag> tags = tagService.listAll(sort);
        System.out.println(tags);
    }
}
