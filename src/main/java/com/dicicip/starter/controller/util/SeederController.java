package com.dicicip.starter.controller.util;

import com.dicicip.starter.model.User;
import com.dicicip.starter.repository.UserRepository;
import com.dicicip.starter.util.APIResponse;
import com.dicicip.starter.util.query.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/seeders")
public class SeederController {

    @Autowired
    private UserRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DB db;

    @RequestMapping(method = RequestMethod.POST, path = "/starter-data")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> store(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody(required = false) HashMap<Object, String> requestBody
    ) {

        Optional<User> user = repository.findFirstByEmail("admin@mail.com");

        User u = new User();
        if (user.isPresent()) {
            u = user.get();
        }

        u.email = "admin@mail.com";
        u.password = this.passwordEncoder.encode("123456");
        u.name = "ADMINISTRATOR";

        return new APIResponse<>(repository.save(u));

    }
}