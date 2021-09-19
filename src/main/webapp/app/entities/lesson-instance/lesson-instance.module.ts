import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LessonInstanceComponent } from './list/lesson-instance.component';
import { LessonInstanceDetailComponent } from './detail/lesson-instance-detail.component';
import { LessonInstanceUpdateComponent } from './update/lesson-instance-update.component';
import { LessonInstanceDeleteDialogComponent } from './delete/lesson-instance-delete-dialog.component';
import { LessonInstanceRoutingModule } from './route/lesson-instance-routing.module';

import { LessonInstanceRegisterComponent } from './register/lesson-instance-register.component';


@NgModule({
  imports: [SharedModule, LessonInstanceRoutingModule],
  declarations: [
    LessonInstanceComponent,
    LessonInstanceDetailComponent,
    LessonInstanceUpdateComponent,
    LessonInstanceRegisterComponent,
    LessonInstanceDeleteDialogComponent,
  ],
  entryComponents: [LessonInstanceDeleteDialogComponent],
})
export class LessonInstanceModule {}
