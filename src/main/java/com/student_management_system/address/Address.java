package com.student_management_system.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.student_management_system.student.Student;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "address_table")
public class Address {

    @Id
    private UUID id;
    @Column(name = "house_number", columnDefinition = "TEXT")
    private String houseNumber;
    @Column(name = "street", columnDefinition = "TEXT")
    private String street;
    @Column(name = "city", columnDefinition = "TEXT")
    private String city;
    @Column(name = "postcode", columnDefinition = "TEXT")
    private String postcode;
    @JsonIgnore
    @OneToMany(mappedBy = "address")
    private List<Student> students = new ArrayList<>();

    public Address() {
    }

    public Address(String houseNumber, String street, String city, String postcode) {
        this.id = UUID.randomUUID();
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postCode) {
        this.postcode = postcode;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudent(List<Student> students) {
        this.students = students;
    }

    public void addStudentToAddress(Student student) {
        students.add(student);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", houseNumber='" + houseNumber + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postcode='" + postcode + '\'' +
                ", students=" + students +
                '}';
    }
}
