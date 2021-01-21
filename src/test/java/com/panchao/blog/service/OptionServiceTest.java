package com.panchao.blog.service;

import com.panchao.blog.BlogWorldApplicationTests;
import com.panchao.blog.model.entity.Option;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OptionServiceTest extends BlogWorldApplicationTests {
    @Autowired
    private OptionService optionService;

    @Test
    public void save() {
        Option option = new Option();
        option.setOptionName("blogname");
        option.setOptionValue("潘超博客");
        optionService.save(option);
    }

    @Test
    public void saveAll() {
        Option option = new Option();
        option.setOptionName("blogdescription");
        option.setOptionValue("这是我的个人博客");
        Option option1 = new Option();
        option1.setOptionName("home");
        option1.setOptionValue("http://localhost:8080");
        Option option2 = new Option();
        option2.setOptionName("siteurl");
        option2.setOptionValue("http://localhost:8080");
        List<Option> options = new ArrayList<Option>(){{
            add(option);
            add(option1);
            add(option2);
        }};
        optionService.saveAll(options);
    }

    @Test
    public void listAll() {
        List<Option> options = optionService.listAll();
        System.out.println(options.size());
    }
}
