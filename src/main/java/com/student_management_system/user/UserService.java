package com.student_management_system.user;

import com.student_management_system.exception.ApiRequestException;
import com.student_management_system.requests.RegistrationRequest;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Transactional
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException("Username not found: " +username));
        return new SecurityUser(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User registerUser(RegistrationRequest request) {
        if(userRepository.findUserByUsername(request.getUsername()).isPresent()) {
            throw new ApiRequestException("Username already taken: " + request.getUsername());
        }
        if(request.getUsername().length() < 4 || request.getUsername().length() > 15 || request.getUsername().isBlank()) {
            throw new ApiRequestException("Username must be between 4 and 15 characters in length");
        }
        if( !Pattern.matches("^[a-zA-Z]+$", request.getUsername())) {
            throw new ApiRequestException("Username must only contain letters");
        }
        if(userRepository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new ApiRequestException("Email already taken: " + request.getEmail());
        }
        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            throw new ApiRequestException("Email is not valid: " + request.getEmail());
        }
        if(request.getPassword().length() < 6) {
            throw new ApiRequestException("Password needs to be longer than 6 characters in length");
        }
        if(request.getRole().isBlank()) {
            throw new ApiRequestException("Role cannot be blank");
        }
        return userRepository.save(new User(
                request.getUsername(),
                request.getEmail(),
                bCryptPasswordEncoder.encode(request.getPassword()),
                request.getRole()
        ));
    }

    public void deleteUserByID(UUID userID) {
        Optional<User> userOptional = userRepository.findById(userID);
        if(userOptional.isPresent()) {
            userRepository.deleteById(userID);
        } else {
            throw new NoSuchElementException("User id not found: " + userID);
        }
    }

    public User getUserByID(UUID userID) {
        return userRepository.findById(userID)
                .orElseThrow( () -> new NoSuchElementException("User id not found: " + userID));
    }

}
