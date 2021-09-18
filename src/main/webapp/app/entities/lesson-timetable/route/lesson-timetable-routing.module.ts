import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LessonTimetableComponent } from '../list/lesson-timetable.component';
import { LessonTimetableDetailComponent } from '../detail/lesson-timetable-detail.component';
import { LessonTimetableUpdateComponent } from '../update/lesson-timetable-update.component';
import { LessonTimetableRoutingResolveService } from './lesson-timetable-routing-resolve.service';

const lessonTimetableRoute: Routes = [
  {
    path: '',
    component: LessonTimetableComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LessonTimetableDetailComponent,
    resolve: {
      lessonTimetable: LessonTimetableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LessonTimetableUpdateComponent,
    resolve: {
      lessonTimetable: LessonTimetableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LessonTimetableUpdateComponent,
    resolve: {
      lessonTimetable: LessonTimetableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lessonTimetableRoute)],
  exports: [RouterModule],
})
export class LessonTimetableRoutingModule {}
