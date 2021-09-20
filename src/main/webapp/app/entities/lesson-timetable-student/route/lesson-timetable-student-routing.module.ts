import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LessonTimetableStudentComponent } from '../list/lesson-timetable-student.component';
import { LessonTimetableStudentDetailComponent } from '../detail/lesson-timetable-student-detail.component';
import { LessonTimetableStudentUpdateComponent } from '../update/lesson-timetable-student-update.component';
import { LessonTimetableStudentRoutingResolveService } from './lesson-timetable-student-routing-resolve.service';

const lessonTimetableStudentRoute: Routes = [
  {
    path: '',
    component: LessonTimetableStudentComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LessonTimetableStudentDetailComponent,
    resolve: {
      lessonTimetableStudent: LessonTimetableStudentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LessonTimetableStudentUpdateComponent,
    resolve: {
      lessonTimetableStudent: LessonTimetableStudentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LessonTimetableStudentUpdateComponent,
    resolve: {
      lessonTimetableStudent: LessonTimetableStudentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lessonTimetableStudentRoute)],
  exports: [RouterModule],
})
export class LessonTimetableStudentRoutingModule {}
