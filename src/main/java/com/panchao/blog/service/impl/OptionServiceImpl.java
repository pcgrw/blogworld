package com.panchao.blog.service.impl;

import com.panchao.blog.model.entity.Option;
import com.panchao.blog.repository.OptionRepository;
import com.panchao.blog.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OptionServiceImpl implements OptionService {
    @Autowired
    private OptionRepository optionRepository;
    @Resource(name = "thymeleafViewResolver")
    private ThymeleafViewResolver resolver;

    @PostConstruct
    public void init() {
        List<Option> options = this.listAll();
        Map<String, String> optionMap = options.stream()
            .collect(Collectors.toMap(Option::getOptionName, Option::getOptionValue));
        resolver.addStaticVariable("options", optionMap);
    }

    @Override
    @Transactional
    public Option save(Option option) {
        return optionRepository.save(option);
    }

    @Override
    @Transactional
    public List<Option> saveAll(Iterable<Option> options) {
        return optionRepository.saveAll(options);
    }

    @Override
    public List<Option> listAll() {
        return optionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("valid"), true));
            return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
        });
    }
}
