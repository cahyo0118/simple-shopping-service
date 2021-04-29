package com.dicicip.starter.repository;

import com.dicicip.starter.model.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {

}
