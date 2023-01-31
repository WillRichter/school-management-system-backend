package com.student_management_system.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.UUID;
import javax.validation.constraints.Email;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    private UUID id;

    @Column(name = "username",
            nullable = false,
            columnDefinition = "TEXT")
    private String username;

    @Email
    @Column(name = "email",
            nullable = false,
            columnDefinition = "TEXT")
    private String email;

    @Column(name = "password",
            nullable = false,
            columnDefinition = "TEXT")
    @JsonIgnore
    private String password;

    @Column(name = "role",
            columnDefinition = "TEXT")
    private String role;

    public User() {
    }

    public User(String username, String email, String password, String role) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
