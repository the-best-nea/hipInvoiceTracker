package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.StudentRegister;
import com.mycompany.myapp.repository.LessonInstanceRepository;
import com.mycompany.myapp.repository.LessonTimetableRepository;
import com.mycompany.myapp.repository.StudentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final LessonInstanceRepository lessonInstanceRepository;
    private final LessonTimetableRepository lessonTimetableRepository;
    private final RegistrationService registrationService;

    public StudentService(
        StudentRepository studentRepository,
        LessonInstanceRepository lessonInstanceRepository,
        LessonTimetableRepository lessonTimetableRepository,
        RegistrationService registrationService
    ) {
        this.studentRepository = studentRepository;
        this.lessonInstanceRepository = lessonInstanceRepository;
        this.lessonTimetableRepository = lessonTimetableRepository;
        this.registrationService = registrationService;
    }

    public List<StudentRegister> getAllStudentsByLesson(Long id) {
        return registrationService.getStudents(id);
    }
}
