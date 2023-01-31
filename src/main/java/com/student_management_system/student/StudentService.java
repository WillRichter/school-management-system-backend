package com.student_management_system.student;

import com.student_management_system.address.AddressDTO;
import com.student_management_system.address.AddressService;
import com.student_management_system.exception.ApiRequestException;
import com.student_management_system.exception.StudentNotFoundException;
import com.student_management_system.student.imageConfig.FileStore;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.student_management_system.student.StudentMapping.mapStudentToDTO;

@Transactional
@Service
public class StudentService {

    private final StudentImageMethods studentImageMethods;
    private final StudentRepository studentRepository;
    private final AddressService addressService;

    public StudentService(StudentRepository studentRepository,
                          FileStore fileStore,
                          AddressService addressService) {
        this.studentRepository = studentRepository;
        this.studentImageMethods = new StudentImageMethods(studentRepository, fileStore);
        this.addressService = addressService;
    }

    public List<StudentDTO> getStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(student -> mapStudentToDTO(student, studentImageMethods.downloadStudentImage(student.getId())))
                .toList();
    }

    public StudentDTO registerStudent(String name, String email, String dateOfBirth, MultipartFile file) {
        if(name.length() < 4 || name.length() > 15) {
            throw new ApiRequestException("Name must be between 4 and and 15 characters in length");
        }
        if(!Pattern.matches("^[a-zA-z]+$", name)) {
            throw new ApiRequestException("Name must contain only letters");
        }
        if(!EmailValidator.getInstance().isValid(email)) {
            throw new ApiRequestException("Email not valid: " + email);
        }
        if(dateOfBirth.isBlank()) {
            throw new ApiRequestException("Date of birth must be filled in");
        }
        if(file.isEmpty()) {
            throw new ApiRequestException("Image file must be attached");
        }

        studentRepository.findStudentByEmail(email)
                .orElseThrow( () -> new ApiRequestException("Email addresss taken  by another student: " + email));

        UUID studentID = UUID.randomUUID();
        String filename = studentImageMethods.uploadStudentImage(studentID, file);
        Student student = studentRepository.save(new Student(studentID, name, email, dateOfBirth, filename));
        return mapStudentToDTO(student, studentImageMethods.downloadStudentImage(studentID));
    }

    public StudentDTO getStudentByID(UUID studentID) {
        Student student = studentRepository.findById(studentID)
                .orElseThrow( () -> new ApiRequestException("Student cannot be found: " + studentID));
        byte[] image = studentImageMethods.downloadStudentImage(studentID);
        return mapStudentToDTO(student, image);
    }

    public StudentDTO updateStudentByID(UUID studentID, Student studentDetails) {
        if(studentDetails.getName().isBlank() || studentDetails.getEmail().isBlank()) {
            throw new ApiRequestException("Fields cannot be empty");
        }
        Student student = studentRepository.findById(studentID)
                .orElseThrow( () -> new StudentNotFoundException("Student with id" + studentID + " does not exist"));
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        return mapStudentToDTO(student, studentImageMethods.downloadStudentImage(student.getId()));
    }

    public void removeStudent(UUID studentID) {
        studentRepository.findById(studentID)
                .orElseThrow( () -> new StudentNotFoundException("Student not found"));
        studentImageMethods.deleteStudentImage(studentID);
        studentRepository.deleteById(studentID);
    }

    public StudentDTO addAddressToStudent(UUID studentID, AddressDTO addressDTO) {
        Student student = addressService.addAddressToStudent(studentID, addressDTO);
        return mapStudentToDTO(student, studentImageMethods.downloadStudentImage(studentID));
    }

    public StudentDTO updateAddressForStudent(UUID studentID, AddressDTO addressDTO) {
        Student student = addressService.updateAddressForStudent(studentID, addressDTO);
        return mapStudentToDTO(student, studentImageMethods.downloadStudentImage(studentID));
    }

}
