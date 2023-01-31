package com.student_management_system.student;

import com.student_management_system.student.imageConfig.FileStore;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class StudentImageMethods {

    private final String BUCKET_NAME = "student-management-system-student-image-upload";
    private final StudentRepository studentRepository;
    private final FileStore fileStore;;

    public StudentImageMethods(StudentRepository studentRepository, FileStore fileStore) {
        this.studentRepository = studentRepository;
        this.fileStore = fileStore;
    }

    public String uploadStudentImage(UUID studentID, MultipartFile file) {
        if(file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + " ]");
        }

        if(!Arrays.asList(
                ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_PNG.getMimeType()).contains((file.getContentType()))) {
            throw new IllegalStateException("File must be an image " + file.getContentType());
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        String path = String.format("%s/%s", BUCKET_NAME, studentID);
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try{
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return fileName;
    }

    public byte[] downloadStudentImage(UUID studentID) {
        Student student = studentRepository.findById(studentID)
                .orElseThrow( () -> new IllegalStateException("Student cannot be found"));

        String path = String.format("%s/%s", BUCKET_NAME, studentID);

        return fileStore.download(path, student.getImageLink());
    }

    public void deleteStudentImage(UUID studentID) {
        Student student = studentRepository.findById(studentID)
                .orElseThrow( () -> new IllegalStateException("Student cannot be found"));

        String path = String.format("%s/%s", BUCKET_NAME, studentID);

        fileStore.delete(path, student.getImageLink());
    }
}
