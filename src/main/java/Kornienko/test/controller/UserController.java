package Kornienko.test.controller;

import Kornienko.test.api.UsersApi;
import Kornienko.test.model.User;
import Kornienko.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController implements UsersApi {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepository.getUsers());
    }

}
