package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import com.mycompany.myapp.security.AuthoritiesConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LessonTimetableService {
    protected List<LessonTimetable> lessonTimetables = new ArrayList<LessonTimetable>();

    private String getDayOfWeek(int value){
        String day = "";
        switch(value){case 1:day="SUNDAY";break;case 2:day="MONDAY";break;case 3:day="TUESDAY";break;case 4:day="WEDNESDAY";break;case 5:day="THURSDAY";break;case 6:day="FRIDAY";break;case 7:day="SATURDAY";break;}
        return day;
    }

    private final LessonTimetableRepository lessonTimetableRepository;
    private final LessonTimetableTeacherRepository lessonTimetableTeacherRepository;

    public LessonTimetableService(LessonTimetableRepository lessonTimetableRepository,
                                  LessonTimetableTeacherRepository lessonTimetableTeacherRepository) {
        this.lessonTimetableRepository = lessonTimetableRepository;
        this.lessonTimetableTeacherRepository = lessonTimetableTeacherRepository;
    }

    public List<LessonTimetable> getAllLessonTimetableByUser() {
        lessonTimetables.clear();

        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(currentUser.getAuthorities());
        if (currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            System.out.println("---------------------------------");
            return lessonTimetableRepository.findAll();
        } else {
            List<LessonTimetableTeacher> lessonTimetableTeacher = lessonTimetableTeacherRepository.findByInternalUserIsCurrentUser();
            System.out.println("lessonTimetableTeacher");
            System.out.println(lessonTimetableTeacher);
            for (LessonTimetableTeacher lessonTimetableTeacher1: lessonTimetableTeacher){
                System.out.println("lessonTimetableTeacher1.getLessonTimetable()");
                System.out.println(lessonTimetableTeacher1.getLessonTimetable());
                if(lessonTimetableTeacher1.getLessonTimetable() != null &&
                    lessonTimetableTeacher1.getLessonTimetable().getDayOfWeek().toString() == getDayOfWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))  &&
                    lessonTimetableTeacher1.getLessonTimetable().getActive())
                {
                    System.out.println("InsideForLoop");
                    System.out.println(lessonTimetableTeacher1.getLessonTimetable().getDayOfWeek().toString());
                    this.lessonTimetables.add(lessonTimetableTeacher1.getLessonTimetable());
                    System.out.println(this.lessonTimetables);
                }
            }
            System.out.println("lessonTimetableSet");
            System.out.println(this.lessonTimetables);
            return this.lessonTimetables;
        }
    }
}
