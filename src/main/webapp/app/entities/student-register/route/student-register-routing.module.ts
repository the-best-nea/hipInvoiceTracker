import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentRegisterComponent } from '../list/student-register.component';
import { StudentRegisterDetailComponent } from '../detail/student-register-detail.component';
import { StudentRegisterUpdateComponent } from '../update/student-register-update.component';
import { StudentRegisterRoutingResolveService } from './student-register-routing-resolve.service';

const studentRegisterRoute: Routes = [
  {
    path: '',
    component: StudentRegisterComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentRegisterDetailComponent,
    resolve: {
      studentRegister: StudentRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudentRegisterUpdateComponent,
    resolve: {
      studentRegister: StudentRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudentRegisterUpdateComponent,
    resolve: {
      studentRegister: StudentRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(studentRegisterRoute)],
  exports: [RouterModule],
})
export class StudentRegisterRoutingModule {}
