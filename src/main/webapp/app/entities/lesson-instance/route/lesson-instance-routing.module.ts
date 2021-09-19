import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LessonInstanceComponent } from '../list/lesson-instance.component';
import { LessonInstanceDetailComponent } from '../detail/lesson-instance-detail.component';
import { LessonInstanceUpdateComponent } from '../update/lesson-instance-update.component';
import { LessonInstanceRegisterComponent } from '../register/lesson-instance-register.component';
import { LessonInstanceRoutingResolveService } from './lesson-instance-routing-resolve.service';

const lessonInstanceRoute: Routes = [
  {
    path: '',
    component: LessonInstanceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LessonInstanceDetailComponent,
    resolve: {
      lessonInstance: LessonInstanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LessonInstanceUpdateComponent,
    resolve: {
      lessonInstance: LessonInstanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LessonInstanceUpdateComponent,
    resolve: {
      lessonInstance: LessonInstanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
      path: ':id/register',
      component: LessonInstanceRegisterComponent,
      resolve: {
        lessonInstance: LessonInstanceRoutingResolveService,
      },
      canActivate: [UserRouteAccessService],
    },
];

@NgModule({
  imports: [RouterModule.forChild(lessonInstanceRoute)],
  exports: [RouterModule],
})
export class LessonInstanceRoutingModule {}
