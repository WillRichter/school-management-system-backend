package com.student_management_system.student;

public class StudentMapping {

    public static StudentDTO mapStudentToDTO(Student student, byte[] image) {
        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getDateOfBirth(),
                image,
                student.getAddress(),
                student.getAge()
        );
    }

}
