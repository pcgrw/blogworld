package com.panchao.blog.repository;


import com.panchao.blog.model.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OptionRepository extends JpaRepository<Option, Long>, JpaSpecificationExecutor<Option> {
}
