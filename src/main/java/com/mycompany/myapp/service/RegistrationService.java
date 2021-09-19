package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.StudentRegisterRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final StudentRegisterRepository studentRegisterRepository;

    public RegistrationService(StudentRegisterRepository studentRegisterRepository) {
        this.studentRegisterRepository = studentRegisterRepository;
    }

    public void takeRegister(LessonInstanceRequest lessonInstance) {
        List<RegistrationDetails> registrationDetails = lessonInstance.getRegistrationDetails();
        registrationDetails.forEach(
            detail -> {
                Student student = new Student();
                student.setId(detail.getStudentId());

                StudentRegister studentRegister = new StudentRegister();

                LessonInstance lessonInstance1 = new LessonInstance();
                lessonInstance1.setId(lessonInstance.getId());

                studentRegister.setLessonTimetable(lessonInstance1);
                studentRegister.setStudent(student);
                studentRegister.setAttended(detail.isAttended());
                studentRegister.setDateOfLesson(Instant.now());
                studentRegister.setCreatedOn(Instant.now());
                studentRegister.setUpdatedOn(Instant.now());

                studentRegisterRepository.save(studentRegister);
            }
        );
    }
}
