package com.ec.svg.generator.app.model.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PathRepository extends CrudRepository<Path, Integer> {

    List<Path> findByUnicode(Integer unicode);
}
