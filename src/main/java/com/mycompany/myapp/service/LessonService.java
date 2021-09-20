package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.LessonInstanceRepository;
import com.mycompany.myapp.repository.LessonTimetableRepository;
import com.mycompany.myapp.repository.LessonTimetableStudentRepository;
import com.mycompany.myapp.repository.LessonTimetableTeacherRepository;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LessonService {

    private final LessonTimetableRepository lessonTimetableRepository;
    private final LessonInstanceRepository lessonInstanceRepository;
    private final LessonTimetableStudentRepository lessonTimetableStudentRepository;
    private final LessonTimetableTeacherRepository lessonTimetableTeacherRepository;
    private final RegistrationService registrationService;

    public LessonService(
        LessonTimetableRepository lessonTimetableRepository,
        LessonInstanceRepository lessonInstanceRepository,
        LessonTimetableStudentRepository lessonTimetableStudentRepository,
        LessonTimetableTeacherRepository lessonTimetableTeacherRepository,
        RegistrationService registrationService
    ) {
        this.lessonTimetableRepository = lessonTimetableRepository;
        this.lessonInstanceRepository = lessonInstanceRepository;
        this.lessonTimetableStudentRepository = lessonTimetableStudentRepository;
        this.lessonTimetableTeacherRepository = lessonTimetableTeacherRepository;
        this.registrationService = registrationService;
    }

    public LessonInstance createInstance(Long id) {
        LessonTimetable lessonTimetable = lessonTimetableRepository.getOne(id);

        List<LessonTimetableStudent> lessonTimetableStudents = lessonTimetableStudentRepository.findAllByLessonTimetable(lessonTimetable); //todo: findAllByLessonTimetable

        //System.out.println(lessonTimetableStudents);
        Set<Student> newInstanceStudents = new HashSet<Student>();
        for (LessonTimetableStudent lts: lessonTimetableStudents) {
            if (lts.getStudent() != null && lts.getLessonTimetable().getId() == id ) {
                newInstanceStudents.add(lts.getStudent());
            }
        }
        LessonInstance newInstance = new LessonInstance();

        newInstance.setLessonName(lessonTimetable.getLessonName());
        newInstance.setDescription(lessonTimetable.getDescription());
        newInstance.setDayOfWeek(lessonTimetable.getDayOfWeek());
        newInstance.setCretedOn(Instant.now());
        newInstance.setSubject(lessonTimetable.getSubject());
        newInstance.setStudents(newInstanceStudents);

        LessonTimetableTeacher lessonTimetableTeacher = lessonTimetableTeacherRepository.findByLessonTimetable(lessonTimetable);
        newInstance.setInternalUser(lessonTimetableTeacher.getInternalUser());

        newInstance.setStartAt(lessonTimetable.getStartAt());
        newInstance.setEndAt(lessonTimetable.getEndAt());

        System.out.println(newInstance);

        LessonInstance savedLessonInstance = lessonInstanceRepository.save(newInstance);
        System.out.println(savedLessonInstance);

        registrationService.createRegister(savedLessonInstance);

        return savedLessonInstance;
    }
}
