import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LessonTimetableTeacherComponent } from '../list/lesson-timetable-teacher.component';
import { LessonTimetableTeacherDetailComponent } from '../detail/lesson-timetable-teacher-detail.component';
import { LessonTimetableTeacherUpdateComponent } from '../update/lesson-timetable-teacher-update.component';
import { LessonTimetableTeacherRoutingResolveService } from './lesson-timetable-teacher-routing-resolve.service';

const lessonTimetableTeacherRoute: Routes = [
  {
    path: '',
    component: LessonTimetableTeacherComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LessonTimetableTeacherDetailComponent,
    resolve: {
      lessonTimetableTeacher: LessonTimetableTeacherRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LessonTimetableTeacherUpdateComponent,
    resolve: {
      lessonTimetableTeacher: LessonTimetableTeacherRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LessonTimetableTeacherUpdateComponent,
    resolve: {
      lessonTimetableTeacher: LessonTimetableTeacherRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lessonTimetableTeacherRoute)],
  exports: [RouterModule],
})
export class LessonTimetableTeacherRoutingModule {}
