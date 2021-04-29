package com.dicicip.starter.repository;

import com.dicicip.starter.model.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ModuleRepository extends CrudRepository<Module, Long> {

}
