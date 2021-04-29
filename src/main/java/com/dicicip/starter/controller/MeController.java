package com.dicicip.starter.controller;

import com.dicicip.starter.config.security.annotation.CurrentUser;
import com.dicicip.starter.config.security.model.UserPrincipal;
import com.dicicip.starter.model.User;
import com.dicicip.starter.repository.UserRepository;
import com.dicicip.starter.util.APIResponse;
import com.dicicip.starter.util.file.FileUtil;
import com.dicicip.starter.util.validator.Validator;
import com.dicicip.starter.util.validator.ValidatorItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api/me")
public class MeController {

    @Autowired
    private UserRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FileUtil fileUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET)
    public APIResponse<?> getOne(
            @CurrentUser UserPrincipal currentUser,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        Optional<User> user = this.repository.findById(currentUser.getId());

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

    @RequestMapping(method = RequestMethod.PUT, path = "/update")
    public APIResponse<?> update(
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

            Optional<User> user = this.repository.findById(new Long(23));

            if (user.isPresent()) {

                requestBody.id = user.get().id;

                if (requestBody.password == null) {
                    requestBody.password = user.get().password;
                } else {
                    requestBody.password = this.passwordEncoder.encode(requestBody.password);
                }

                requestBody.photo = this.fileUtil.storeBase64ToTemp(requestBody.photo).path;

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
