package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.repository.LessonInstanceRepository;
import com.mycompany.myapp.repository.LessonTimetableRepository;
import java.time.Instant;
import java.util.HashSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LessonService {

    private final LessonTimetableRepository lessonTimetableRepository;
    private final LessonInstanceRepository lessonInstanceRepository;
    private final RegistrationService registrationService;

    public LessonService(
        LessonTimetableRepository lessonTimetableRepository,
        LessonInstanceRepository lessonInstanceRepository,
        RegistrationService registrationService
    ) {
        this.lessonTimetableRepository = lessonTimetableRepository;
        this.lessonInstanceRepository = lessonInstanceRepository;
        this.registrationService = registrationService;
    }

    public LessonInstance createInstance(Long id) {
        LessonTimetable lessonTimetable = lessonTimetableRepository.getOne(id);

        LessonInstance newInstance = new LessonInstance();

        newInstance.setLessonName(lessonTimetable.getLessonName());
        newInstance.setDescription(lessonTimetable.getDescription());
        newInstance.setDayOfWeek(lessonTimetable.getDayOfWeek());
        newInstance.setCretedOn(Instant.now());
        newInstance.setSubject(lessonTimetable.getSubject());
        newInstance.setStudents(lessonTimetable.getStudents());

        newInstance.setStudents(new HashSet<>(lessonTimetable.getStudents()));
        newInstance.setStartAt(lessonTimetable.getStartAt());
        newInstance.setEndAt(lessonTimetable.getEndAt());

        LessonInstance savedLessonInstance = lessonInstanceRepository.save(newInstance);

        registrationService.createRegister(savedLessonInstance);

        return savedLessonInstance;
    }
}
