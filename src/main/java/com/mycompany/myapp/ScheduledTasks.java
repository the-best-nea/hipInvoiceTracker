package com.mycompany.myapp;

import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.service.LessonTimetableService;
import com.mycompany.myapp.service.MailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {

    private final LessonTimetableService lessonTimetableService;
    private final MailService mailService;
    public static final String DEST = "/generatedPDF/hello.pdf";

    public ScheduledTasks(LessonTimetableService lessonTimetableService, MailService mailService) {
        this.lessonTimetableService = lessonTimetableService;
        this.mailService = mailService;
    }

    @Scheduled(cron = "0 59 23 * * SUN") //real: Set to "0 59 23 * * SUN"
    public void resetRegisterTaken() {
        System.out.println(">> Scheduled: resetRegisterTaken()");
        List<LessonTimetable> allLessons = lessonTimetableService.findAll();
        for (LessonTimetable lessonTimetable: allLessons) {
            //System.out.println(lessonTimetable);
            lessonTimetable.setRegisterTaken(false);
            lessonTimetableService.partialUpdate(lessonTimetable);
        }
    }

    @Scheduled(cron = "0 0 0 1 */1 *") //At the start of every month
    public void sendEmail() {
        System.out.println(">> Scheduled: sendEmail()");
    }

    //@Scheduled(cron = "0 * * * * *") //only for test - run every minute
    public static void sendEmailTest() {
        System.out.println(">> Scheduled: sendEmailTest()");
        PdfDocument pdf = new PdfDocument(new PdfWriter(DEST));
        Document document = new Document(pdf);
        String line = "Hello! Welcome to iTextPdf";
        document.add(new Paragraph(line));
        document.close();

        System.out.println("Awesome PDF just got created.");

        //mailService.sendEmail("pallavhingu@gmail.com","TestEmail","TestContent",false,false);
    }

    //@Scheduled(cron = "0 * * * * *") //only for test - run every minute
    public  void resetRegisterTakenTest(){
        System.out.println(">>>> Scheduled Cron Test: resetRegisterTakenTest()");
        List<LessonTimetable> allLessons = lessonTimetableService.findAll();
        for (LessonTimetable lessonTimetable: allLessons) {
            //System.out.println(lessonTimetable);
            lessonTimetable.setRegisterTaken(false);
            lessonTimetableService.partialUpdate(lessonTimetable);
        }
    }

}
