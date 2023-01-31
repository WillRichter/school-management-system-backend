package com.student_management_system.user;

import com.student_management_system.requests.RegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> RegisterUser(@RequestBody RegistrationRequest request) {
        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{userID}")
    public ResponseEntity<User> getUserByID(@PathVariable UUID userID) {
        return new ResponseEntity<>(userService.getUserByID(userID), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userID}")
    public ResponseEntity<Void> deleteUserByID(@PathVariable UUID userID) {
        userService.deleteUserByID(userID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
