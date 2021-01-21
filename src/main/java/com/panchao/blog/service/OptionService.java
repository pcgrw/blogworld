package com.panchao.blog.service;

import com.panchao.blog.model.entity.Option;

import java.util.List;

public interface OptionService {
    Option save(Option option);

    List<Option> saveAll(Iterable<Option> options);

    List<Option> listAll();
}
