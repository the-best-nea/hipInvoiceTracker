import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LessonTimetableStudentComponent } from './list/lesson-timetable-student.component';
import { LessonTimetableStudentDetailComponent } from './detail/lesson-timetable-student-detail.component';
import { LessonTimetableStudentUpdateComponent } from './update/lesson-timetable-student-update.component';
import { LessonTimetableStudentDeleteDialogComponent } from './delete/lesson-timetable-student-delete-dialog.component';
import { LessonTimetableStudentRoutingModule } from './route/lesson-timetable-student-routing.module';

@NgModule({
  imports: [SharedModule, LessonTimetableStudentRoutingModule],
  declarations: [
    LessonTimetableStudentComponent,
    LessonTimetableStudentDetailComponent,
    LessonTimetableStudentUpdateComponent,
    LessonTimetableStudentDeleteDialogComponent,
  ],
  entryComponents: [LessonTimetableStudentDeleteDialogComponent],
})
export class LessonTimetableStudentModule {}
