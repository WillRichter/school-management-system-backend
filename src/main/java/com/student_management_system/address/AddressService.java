package com.student_management_system.address;

import com.student_management_system.exception.ApiRequestException;
import com.student_management_system.exception.StudentNotFoundException;
import com.student_management_system.student.Student;
import com.student_management_system.student.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final StudentRepository studentRepository;

    public AddressService(AddressRepository addressRepository, StudentRepository studentRepository) {
        this.addressRepository = addressRepository;
        this.studentRepository = studentRepository;
    }

    public Student addAddressToStudent(UUID studentID, AddressDTO addressDTO) {
        Student student = studentRepository.findById(studentID)
                .orElseThrow( () -> new StudentNotFoundException("ID not found: " + studentID));

        if(addressDTO.getHouseNumber().isBlank()
                || addressDTO.getStreet().isBlank()
                || addressDTO.getCity().isBlank()
                || addressDTO.getPostcode().isBlank()){
            throw new ApiRequestException("All address fields need to be filled in");
        }

        Address address = addressRepository.save(new Address(
                addressDTO.getHouseNumber(),
                addressDTO.getStreet(),
                addressDTO.getCity(),
                addressDTO.getPostcode()));

        student.setAddress(address);
        address.addStudentToAddress(student);
        return student;
    }

    public Student updateAddressForStudent(UUID studentID, AddressDTO addressDTO) {
        Student student = studentRepository.findById(studentID)
                .orElseThrow( () -> new StudentNotFoundException("ID not found: " + studentID));

        if(addressDTO.getHouseNumber().isBlank()
                || addressDTO.getStreet().isBlank()
                || addressDTO.getCity().isBlank()
                || addressDTO.getPostcode().isBlank()){
            throw new ApiRequestException("All address fields need to be filled in");
        }

        Address address = student.getAddress();
        address.setHouseNumber(addressDTO.getHouseNumber());
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setPostcode(addressDTO.getPostcode());

        return student;
    }

}
