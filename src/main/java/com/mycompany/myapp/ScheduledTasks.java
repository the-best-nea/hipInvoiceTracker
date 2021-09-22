package com.mycompany.myapp;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.domain.LessonTimetableStudent;
import com.mycompany.myapp.domain.LessonTimetableTeacher;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.service.LessonTimetableService;
import com.mycompany.myapp.service.LessonTimetableStudentService;
import com.mycompany.myapp.service.MailService;
import com.mycompany.myapp.service.StudentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    private final LessonTimetableService lessonTimetableService;
    private final StudentService studentService;
    private final MailService mailService;
    public static final String DEST = "/generatedPDF/hello.pdf";
    private final LessonTimetableStudentService lessonTimetableStudentService;

    public ScheduledTasks(LessonTimetableService lessonTimetableService, StudentService studentService, MailService mailService, LessonTimetableStudentService lessonTimetableStudentService) {
        this.lessonTimetableService = lessonTimetableService;
        this.studentService = studentService;
        this.mailService = mailService;
        this.lessonTimetableStudentService = lessonTimetableStudentService;
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

    @Scheduled(cron = "0 59 23 25 * *") // todo: At the last 25th of every month
    public void sendEmail() {
        System.out.println(">> Scheduled: sendEmail()");
    }

    @Scheduled(cron = "0 * * * * *") //only for test - run every minute
    public void sendEmailTest() {
        System.out.println(">> Scheduled: sendEmailTest()");
        try {

            //Make Directory
            File directory = new File(System.getProperty("user.dir") + "/reports/StudentInvoice/"+ LocalDate.now());
            if (!directory.exists()){
                System.out.println(directory.mkdirs());
                System.out.println("Current Directory " + System.getProperty("user.dir"));
            }

            //get all students
            List<Student> students = this.studentService.findAll();

            for (Student student: students) {

                //Check if student is active
                if(student.getActive()) {

                    //CreatePdf in directory for student
                    PdfDocument pdf = null;
                    pdf = new PdfDocument(new PdfWriter(System.getProperty("user.dir") + "/reports/StudentInvoice/"+ LocalDate.now() +"/"+ student.getId().toString() +student.getFirstName() + student.getLastName()+".pdf"));
                    Document document = new Document(pdf);

                    // -- Document Contents --

                    //logo
                    ImageData logoData = ImageDataFactory.create(System.getProperty("user.dir").toString() + "/logo.png");
                    Image logo = new com.itextpdf.layout.element.Image(logoData);
                    document.add(logo);
                    document.add(new Paragraph().add(new Text(" ")));
                    document.add(new Paragraph().add(new Text(" ")));
                    document.add(new Paragraph().add(new Text(" ")));

                    String studentNameLine1 = "Student Name: ";
                    String studentNameLine2 = student.getFirstName() + " " + student.getLastName();
                    document.add(new Paragraph().add(new Text(studentNameLine1).setBold()).add(new Text(studentNameLine2)));

                    String studentBalanceLine1 = "Student Balance: ";
                    String studentBalanceLine2 = student.getBalance().toString();
                    document.add(new Paragraph().add(new Text(studentBalanceLine1).setBold()).add(studentBalanceLine2));


                    // Invoice Date Range
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MONTH, 1);
                    calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                    Date nextMonthFirstDay = calendar.getTime();
                    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    Date nextMonthLastDay = calendar.getTime();
                    System.out.println(nextMonthFirstDay.toString() + " - " + nextMonthLastDay.toString());
                    Number numOfMondays = noOfWeekDaysBetween(nextMonthFirstDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), nextMonthLastDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),DayOfWeek.MONDAY);
                    Number numOfTuesdays = noOfWeekDaysBetween(nextMonthFirstDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), nextMonthLastDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),DayOfWeek.TUESDAY);
                    Number numOfWednesdays = noOfWeekDaysBetween(nextMonthFirstDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), nextMonthLastDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),DayOfWeek.WEDNESDAY);
                    Number numOfThursdays = noOfWeekDaysBetween(nextMonthFirstDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), nextMonthLastDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),DayOfWeek.THURSDAY);
                    Number numOfFridays = noOfWeekDaysBetween(nextMonthFirstDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), nextMonthLastDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),DayOfWeek.FRIDAY);
                    Number numOfSaturdays = noOfWeekDaysBetween(nextMonthFirstDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), nextMonthLastDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),DayOfWeek.SATURDAY);
                    Number numOfSundays = noOfWeekDaysBetween(nextMonthFirstDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), nextMonthLastDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),DayOfWeek.SUNDAY);


                    String dateRangeLine1 = "Invoice For:  ";
                    String dateRangeLine2 = nextMonthFirstDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString() + "    to    " + nextMonthLastDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
                    document.add(new Paragraph().add(new Text(dateRangeLine1).setBold()).add(new Text(dateRangeLine2).setItalic()));
                    document.add(new Paragraph());

                    String subscription = "Your Lessons: ";
                    document.add(new Paragraph().add(new Text(subscription).setBold()));

                    float [] pointColumnWidths = {150F, 100F, 150F, 100F};
                    Table table = new Table(pointColumnWidths);

                    table.addCell(new Cell().add(new Paragraph("Lesson").setFontSize(14).setBold()).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addCell(new Cell().add(new Paragraph("Rate").setFontSize(14).setBold()).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addCell(new Cell().add(new Paragraph("No. of Lesson").setFontSize(14).setBold()).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addCell(new Cell().add(new Paragraph("Total").setFontSize(14).setBold()).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBackgroundColor(ColorConstants.LIGHT_GRAY));

                    Number total = 0;

                    // find all timetables lessons that the student is part of
                    List<LessonTimetableStudent> lessonTimetableStudents = lessonTimetableStudentService.findAllByStudent(student);

                    for (LessonTimetableStudent lessonTimetableStudent: lessonTimetableStudents) {

                        table.addCell(new Cell().add(new Paragraph(lessonTimetableStudent.getLessonTimetable().getLessonName()).setFontSize(12)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

                        Number payPerLesson = lessonTimetableStudent.getPay();
                        table.addCell(new Cell().add(new Paragraph(payPerLesson.toString()).setFontSize(12)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

                        Number subTotal = 0;
                        String lessonTimetableDayOfWeek = lessonTimetableStudent.getLessonTimetable().getDayOfWeek().toString();
                        if (lessonTimetableDayOfWeek.equals("MONDAY")){
                            table.addCell(new Cell().add(new Paragraph(numOfMondays.toString()).setFontSize(12)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                            subTotal = payPerLesson.floatValue() * numOfMondays.floatValue();
                        }
                        if (lessonTimetableDayOfWeek.equals("TUESDAY")){
                            table.addCell(new Cell().add(new Paragraph(numOfTuesdays.toString()).setFontSize(12)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                            subTotal = payPerLesson.floatValue() * numOfTuesdays.floatValue();
                        }
                        if (lessonTimetableDayOfWeek.equals("WEDNESDAY")){
                            table.addCell(new Cell().add(new Paragraph(numOfWednesdays.toString()).setFontSize(12)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                            subTotal = payPerLesson.floatValue() * numOfWednesdays.floatValue();
                        }
                        if (lessonTimetableDayOfWeek.equals("THURSDAY")){
                            table.addCell(new Cell().add(new Paragraph(numOfThursdays.toString()).setFontSize(12)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                            subTotal = payPerLesson.floatValue() * numOfThursdays.floatValue();
                        }
                        if (lessonTimetableDayOfWeek.equals("FRIDAY")){
                            table.addCell(new Cell().add(new Paragraph(numOfFridays.toString()).setFontSize(12)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                            subTotal = payPerLesson.floatValue() * numOfFridays.floatValue();
                        }
                        if (lessonTimetableDayOfWeek.equals("SATURDAY")){
                            table.addCell(new Cell().add(new Paragraph(numOfSaturdays.toString()).setFontSize(12)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                            subTotal = payPerLesson.floatValue() * numOfSaturdays.floatValue();
                        }
                        if (lessonTimetableDayOfWeek.equals("SUNDAY")){
                            table.addCell(new Cell().add(new Paragraph(numOfSundays.toString()).setFontSize(12)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
                            subTotal = payPerLesson.floatValue() * numOfSundays.floatValue();
                        }

                        table.addCell(new Cell().add(new Paragraph(subTotal.toString()).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));

                        total = total.floatValue() + subTotal.floatValue();
                    }

                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph("Total").setFontSize(12).setBold()).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addCell(new Cell().add(new Paragraph(total.toString()).setFontSize(12).setBold()).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBackgroundColor(ColorConstants.LIGHT_GRAY));


                    document.add(table);


                    document.close();
                    System.out.println("Awesome PDF just got created.");


                    String studentEmail = student.getEmail();

                    String invoicePdfFilePath = System.getProperty("user.dir") + "/reports/StudentInvoice/"+ LocalDate.now() +"/"+ student.getId().toString() +student.getFirstName() + student.getLastName()+".pdf";
                    File invoicePdfFile = new File(invoicePdfFilePath);
                    //mailService.sendEmailWithAttachment(studentEmail,"Invoice for: " + student.getFirstName() + student.getLastName(),"TestContent",true,false,"Invoice", invoicePdfFile);
                }

            }



        } catch (FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        }

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




    public static long noOfWeekDaysBetween(LocalDate first, LocalDate last, DayOfWeek dayOfWeek) {
        if (last.isBefore(first)) {
            throw new IllegalArgumentException("first " + first + " was after last " + last);
        }
        // find first Monday in interval
        LocalDate firstday = first.with(TemporalAdjusters.next(dayOfWeek));
        // similarly find last Monday
        LocalDate lastday = last.with(TemporalAdjusters.previous(dayOfWeek));
        // count
        long number = ChronoUnit.WEEKS.between(firstday, lastday);
        // add one to count both first Monday and last Monday in
        return number + 1;
    }

}
