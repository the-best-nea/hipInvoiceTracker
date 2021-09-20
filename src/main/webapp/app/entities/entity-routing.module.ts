import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'subject',
        data: { pageTitle: 'Subjects' },
        loadChildren: () => import('./subject/subject.module').then(m => m.SubjectModule),
      },
      {
        path: 'lesson-timetable',
        data: { pageTitle: 'LessonTimetables' },
        loadChildren: () => import('./lesson-timetable/lesson-timetable.module').then(m => m.LessonTimetableModule),
      },
      {
        path: 'student',
        data: { pageTitle: 'Students' },
        loadChildren: () => import('./student/student.module').then(m => m.StudentModule),
      },
      {
        path: 'lesson-timetable-teacher',
        data: { pageTitle: 'LessonTimetableTeachers' },
        loadChildren: () => import('./lesson-timetable-teacher/lesson-timetable-teacher.module').then(m => m.LessonTimetableTeacherModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'Locations' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'student-register',
        data: { pageTitle: 'StudentRegisters' },
        loadChildren: () => import('./student-register/student-register.module').then(m => m.StudentRegisterModule),
      },
      {
        path: 'lesson-instance',
        data: { pageTitle: 'LessonInstances' },
        loadChildren: () => import('./lesson-instance/lesson-instance.module').then(m => m.LessonInstanceModule),
      },
      {
        path: 'lesson-timetable-student',
        data: { pageTitle: 'LessonTimetableStudents' },
        loadChildren: () => import('./lesson-timetable-student/lesson-timetable-student.module').then(m => m.LessonTimetableStudentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
