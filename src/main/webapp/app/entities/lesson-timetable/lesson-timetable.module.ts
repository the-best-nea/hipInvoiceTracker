import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LessonTimetableComponent } from './list/lesson-timetable.component';
import { LessonTimetableDetailComponent } from './detail/lesson-timetable-detail.component';
import { LessonTimetableUpdateComponent } from './update/lesson-timetable-update.component';
import { LessonTimetableDeleteDialogComponent } from './delete/lesson-timetable-delete-dialog.component';
import { LessonTimetableRoutingModule } from './route/lesson-timetable-routing.module';

@NgModule({
  imports: [SharedModule, LessonTimetableRoutingModule],
  declarations: [
    LessonTimetableComponent,
    LessonTimetableDetailComponent,
    LessonTimetableUpdateComponent,
    LessonTimetableDeleteDialogComponent,
  ],
  entryComponents: [LessonTimetableDeleteDialogComponent],
})
export class LessonTimetableModule {}
