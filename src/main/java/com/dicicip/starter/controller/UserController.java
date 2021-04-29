package com.dicicip.starter.controller;

import com.dicicip.starter.model.User;
import com.dicicip.starter.repository.UserRepository;
import com.dicicip.starter.util.APIResponse;
import com.dicicip.starter.util.file.FileUtil;
import com.dicicip.starter.util.query.DB;
import com.dicicip.starter.util.query.QueryHelpers;
import com.dicicip.starter.util.validator.Validator;
import com.dicicip.starter.util.validator.ValidatorItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

//    @Autowired
//    FileUtil fileUtil;

    @Autowired
    DB db;

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET)
    public APIResponse<?> getAll(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return new APIResponse<>(QueryHelpers.getData(request.getParameterMap(), "users", db));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/detail")
    public APIResponse<?> getOne(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<User> user = this.repository.findById(id);

        if (user.isPresent()) {
            return new APIResponse<>(user.get());
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
            @RequestBody User requestBody
    ) {

        Validator<User> validator = new Validator<>(
                requestBody,
                new ValidatorItem("name", "required"),
                new ValidatorItem("email", "required"),
                new ValidatorItem("password", "required")
        );

        if (validator.valid()) {

            Optional<User> userByEmail = this.repository.findFirstByEmail(requestBody.email);

            if (userByEmail.isPresent()) {
                response.setStatus(400);
                return new APIResponse<>(
                        null,
                        false,
                        "Failed save data, email already taken"
                );
            }

            requestBody.password = this.passwordEncoder.encode(requestBody.password);
//            requestBody.photo = this.fileUtil.storeBase64ToTemp(requestBody.photo).path;

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
            @RequestBody User requestBody
    ) {

        Validator<User> validator = new Validator<>(
                requestBody,
                new ValidatorItem("name", "required"),
                new ValidatorItem("email", "required")
        );

        if (validator.valid()) {

            Optional<User> user = this.repository.findById(id);

            if (user.isPresent()) {

                requestBody.id = user.get().id;

                if (requestBody.password == null) {
                    requestBody.password = user.get().password;
                } else {
                    requestBody.password = this.passwordEncoder.encode(requestBody.password);
                }

//                requestBody.photo = this.fileUtil.storeBase64ToTemp(requestBody.photo).path;

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

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}/delete")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> delete(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<User> user = this.repository.findById(id);

        if (user.isPresent()) {

            this.repository.delete(user.get());

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

}
