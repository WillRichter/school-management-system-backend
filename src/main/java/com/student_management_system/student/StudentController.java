package com.student_management_system.student;

import com.student_management_system.address.AddressDTO;
import com.student_management_system.address.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/students")
public class StudentController {

    private final StudentService studentService;
    private final AddressService addressService;

    public StudentController(StudentService studentService, AddressService addressService) {
        this.studentService = studentService;
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getStudents() {
        return new ResponseEntity<>(studentService.getStudents(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudentDTO> registerStudent(
            @RequestParam("name") String name,
            @RequestParam("email") String email ,
            @RequestParam("dateOfBirth") String dateOfBirth,
            @RequestParam("file")MultipartFile file){
        return new ResponseEntity<>(
                studentService.registerStudent(name, email, dateOfBirth, file),
                HttpStatus.CREATED);
    }

    @GetMapping(path = "/{studentID}")
    public ResponseEntity<StudentDTO> getStudentByID(@PathVariable UUID studentID) {
        return new ResponseEntity<>(studentService.getStudentByID(studentID), HttpStatus.OK);
    }

    @PutMapping(path = "/{studentID}")
    public ResponseEntity<StudentDTO> updateStudentByID(@PathVariable UUID studentID, @RequestBody Student studentDetails) {
        return new ResponseEntity<>(studentService.updateStudentByID(studentID, studentDetails), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{studentID}")
    public ResponseEntity<Void> unregisterStudent(@PathVariable UUID studentID) {
        studentService.removeStudent(studentID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/{studentID}/address")
    public ResponseEntity<StudentDTO> addAddressToStudent(
            @PathVariable UUID studentID, @RequestBody AddressDTO addressDTO) {
        return new ResponseEntity<>(studentService.addAddressToStudent(studentID, addressDTO), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{studentID}/address")
    public ResponseEntity<StudentDTO> updateAddressStudent(
            @PathVariable UUID studentID, @RequestBody AddressDTO addressDTO) {
        return new ResponseEntity<>(studentService.updateAddressForStudent(studentID, addressDTO), HttpStatus.OK);
    }

}
