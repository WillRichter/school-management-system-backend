package com.student_management_system;

import com.student_management_system.address.AddressService;
import com.student_management_system.student.StudentService;
import com.student_management_system.user.User;
import com.student_management_system.user.UserRepository;
import com.student_management_system.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;

@SpringBootApplication
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentService studentService,
                                        UserService userService,
                                        BCryptPasswordEncoder bCryptPasswordEncoder,
                                        UserRepository userRepository,
                                        AddressService addressService) {
        return args -> {

            User user = new User(
                    "user",
                    "user@gmail.com",
                    bCryptPasswordEncoder.encode("123456"),
                    "ROLE_USER"
            );

            User admin = new User(
                    "admin",
                    "admin@gmail.com",
                    bCryptPasswordEncoder.encode("123456"),
                    "ROLE_ADMIN"
            );

            userRepository.saveAll(List.of(user, admin));

        };
    }

}
