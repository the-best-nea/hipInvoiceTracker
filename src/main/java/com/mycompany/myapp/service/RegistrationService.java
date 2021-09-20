package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.StudentRegisterRepository;

import java.time.*;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrationService {

    private final StudentRegisterRepository studentRegisterRepository;

    public RegistrationService(StudentRegisterRepository studentRegisterRepository) {
        this.studentRegisterRepository = studentRegisterRepository;
    }

    public void takeRegister(LessonInstanceRequest lessonInstance) {
        System.out.println(">>> --");
        lessonInstance.getRegistrationDetails().forEach(System.out::println);
        System.out.println("<<< --");
        List<RegistrationDetails> registrationDetails = lessonInstance.getRegistrationDetails();
        registrationDetails.forEach(
            detail -> {
                Student student = new Student();
                student.setId(detail.getStudentId());

                StudentRegister studentRegister = new StudentRegister();
                if (detail.getId() != null) {
                    studentRegister.setId(detail.getId());
                }
                studentRegister.setCreatedOn(Instant.now());

                LessonInstance lessonInstance1 = new LessonInstance();
                lessonInstance1.setId(lessonInstance.getId());

                studentRegister.setLessonTimetable(lessonInstance1);
                studentRegister.setStudent(student);
                studentRegister.setAttended(detail.isAttended());
                studentRegister.setDateOfLesson(Instant.now());
                studentRegister.setUpdatedOn(Instant.now());

                studentRegisterRepository.save(studentRegister);
            }
        );
    }

    public void createRegister(LessonInstance savedLessonInstance) {
        savedLessonInstance
            .getStudents()
            .forEach(
                studentItem -> {
                    Student student = new Student();
                    student.setId(studentItem.getId());

                    StudentRegister studentRegister = new StudentRegister();

                    studentRegister.setLessonTimetable(savedLessonInstance);
                    studentRegister.setStudent(student);
                    studentRegister.setAttended(false);
                    studentRegister.setDateOfLesson(Instant.now());
                    studentRegister.setCreatedOn(Instant.now());
                    studentRegister.setUpdatedOn(Instant.now());
                    //studentRegister.setPay();

                    studentRegisterRepository.save(studentRegister);
                }
            );
    }

    public List<StudentRegister> getStudents(Long id) {
        List<StudentRegister> allStudentsForRegister = studentRegisterRepository.findAllStudentsForRegister(id);
        allStudentsForRegister.forEach(System.out::println);
        return allStudentsForRegister;
    }
}
