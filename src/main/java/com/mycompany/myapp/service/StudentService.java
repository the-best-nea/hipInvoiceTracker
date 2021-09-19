package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Lesson;
import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.repository.LessonInstanceRepository;
import com.mycompany.myapp.repository.LessonTimetableRepository;
import com.mycompany.myapp.repository.StudentRepository;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final LessonInstanceRepository lessonInstanceRepository;
    private final LessonTimetableRepository lessonTimetableRepository;

    public StudentService(StudentRepository studentRepository, LessonInstanceRepository lessonInstanceRepository,
                          LessonTimetableRepository lessonTimetableRepository) {
        this.studentRepository = studentRepository;
        this.lessonInstanceRepository = lessonInstanceRepository;
        this.lessonTimetableRepository = lessonTimetableRepository;
    }

    public List<Student> getAllStudentsByLesson (Long id) {
        LessonInstance lessonInstance = lessonInstanceRepository.getOne(id);
        List<Student> students = List.copyOf(lessonInstance.getStudents());
        return students;
    }
}
