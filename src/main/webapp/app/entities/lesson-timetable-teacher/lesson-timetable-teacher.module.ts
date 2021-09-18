import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LessonTimetableTeacherComponent } from './list/lesson-timetable-teacher.component';
import { LessonTimetableTeacherDetailComponent } from './detail/lesson-timetable-teacher-detail.component';
import { LessonTimetableTeacherUpdateComponent } from './update/lesson-timetable-teacher-update.component';
import { LessonTimetableTeacherDeleteDialogComponent } from './delete/lesson-timetable-teacher-delete-dialog.component';
import { LessonTimetableTeacherRoutingModule } from './route/lesson-timetable-teacher-routing.module';

@NgModule({
  imports: [SharedModule, LessonTimetableTeacherRoutingModule],
  declarations: [
    LessonTimetableTeacherComponent,
    LessonTimetableTeacherDetailComponent,
    LessonTimetableTeacherUpdateComponent,
    LessonTimetableTeacherDeleteDialogComponent,
  ],
  entryComponents: [LessonTimetableTeacherDeleteDialogComponent],
})
export class LessonTimetableTeacherModule {}
