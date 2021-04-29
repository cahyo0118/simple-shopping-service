package com.dicicip.starter.controller;

import com.dicicip.starter.model.Role;
import com.dicicip.starter.repository.RoleRepository;
import com.dicicip.starter.util.APIResponse;
import com.dicicip.starter.util.query.DB;
import com.dicicip.starter.util.query.QueryHelpers;
import com.dicicip.starter.util.validator.Validator;
import com.dicicip.starter.util.validator.ValidatorItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("isFullyAuthenticated()")
public class RoleController {

    @Autowired
    private RoleRepository repository;

    @Autowired
    DB db;

    @RequestMapping(method = RequestMethod.GET)
    public APIResponse<?> getAll(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return new APIResponse<>(QueryHelpers.getData(request.getParameterMap(), "roles", db));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/detail")
    public APIResponse<?> getOne(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<Role> role = this.repository.findById(id);

        if (role.isPresent()) {
            return new APIResponse<>(role.get());
        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    null,
                    false,
                    "Failed get data"
            );
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/store")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> store(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Role requestBody
    ) throws Exception {

        Validator<Role> validator = new Validator<>(
                requestBody,
                new ValidatorItem("name", "required"),
                new ValidatorItem("description")
        );

        if (validator.valid()) {
            return new APIResponse<>(repository.save(requestBody));
        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    validator.getErrorsList(),
                    false,
                    "Failed save data"
            );
        }

    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/update")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> update(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Role requestBody
    ) {

        Validator<Role> validator = new Validator<>(
                requestBody,
                new ValidatorItem("name", "required"),
                new ValidatorItem("description")
        );

        if (validator.valid()) {

            Optional<Role> role = this.repository.findById(id);

            if (role.isPresent()) {

                requestBody.id = role.get().id;

                return new APIResponse<>(this.repository.save(requestBody));

            } else {
                response.setStatus(400);
                return new APIResponse<>(
                        null,
                        false,
                        "Failed update data, data not found"
                );
            }

        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    validator.getErrorsList(),
                    false,
                    "Failed update data"
            );
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}/delete")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> delete(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<Role> role = this.repository.findById(id);

        if (role.isPresent()) {

            this.repository.delete(role.get());

            return new APIResponse<>(null);

        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    null,
                    false,
                    "Failed delete data"
            );
        }
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/update-permissions")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> updatePermissions(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Role requestBody
    ) {

        Validator<Role> validator = new Validator<>(
                requestBody,
                new ValidatorItem("is_permit_all", "required")
        );

        if (validator.valid()) {

            Optional<Role> role = this.repository.findById(id);

            if (role.isPresent()) {

                requestBody.id = role.get().id;

                return new APIResponse<>(this.repository.save(requestBody));

            } else {
                response.setStatus(400);
                return new APIResponse<>(
                        null,
                        false,
                        "Failed get data"
                );
            }

        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    validator.getErrorsList(),
                    false,
                    "Failed save data"
            );
        }

    }

}
