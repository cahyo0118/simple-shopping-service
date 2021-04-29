package com.dicicip.starter.controller;

import com.dicicip.starter.config.security.jwt.JwtTokenProvider;
import com.dicicip.starter.model.User;
import com.dicicip.starter.repository.UserRepository;
import com.dicicip.starter.util.APIResponse;
import com.dicicip.starter.util.file.FileUtil;
import com.dicicip.starter.util.validator.Validator;
import com.dicicip.starter.util.validator.ValidatorItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public APIResponse<?> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody HashMap<String, String> requestBody
    ) {

        Validator<HashMap<String, String>> validator = new Validator<>(
                requestBody,
                new ValidatorItem("username", "required"),
                new ValidatorItem("password", "required")
        );

        if (validator.valid()) {

            Optional<User> user = this.repository.findFirstByEmail(requestBody.get("username"));

            if (user.isPresent()) {

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestBody.get("username"),
                                requestBody.get("password")
                        )
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                String jwt = tokenProvider.generateToken(authentication);
//                String jwt = tokenProvider.generateTokenByUserId(objectMapper.convertValue(user.get(), HashMap.class));

                return new APIResponse<>(jwt);

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
