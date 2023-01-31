package com.student_management_system.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.student_management_system.address.Address;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Entity
@Table(name = "student_table")
public class Student {

    @Id
    private UUID id;

    @Column(name = "name",
            nullable = false,
            columnDefinition = "TEXT")
    private String name;

    @Email
    @Column(name = "email",
            nullable = false,
            columnDefinition = "TEXT")
    private String email;

    @Column
    private String imageLink; // S3 Key

    @Column(name = "dateOfBirth",
            columnDefinition = "TEXT")
    @JsonFormat(pattern = "dd/MM/YYYY")
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "address_id",
            referencedColumnName = "id")
    private Address address;

    @Transient
    private Integer age;

    public Student(){}

    public Student(UUID id, String name, String email, String dateOfBirth, String imageLink) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
        this.imageLink = imageLink;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address=" + address +
                ", age=" + age +
                '}';
    }
}
