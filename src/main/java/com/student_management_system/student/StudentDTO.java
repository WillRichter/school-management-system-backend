package com.student_management_system.student;

import com.student_management_system.address.Address;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

public class StudentDTO {
    private UUID id;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private byte[] image;
    private Address address;
    private Integer age;

    public StudentDTO(UUID id, String name, String email, LocalDate dateOfBirth, byte[] image, Address address, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.image = image;
        this.address = address;
        this.age = age;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", image=" + Arrays.toString(image) +
                ", address=" + address +
                ", age=" + age +
                '}';
    }
}
