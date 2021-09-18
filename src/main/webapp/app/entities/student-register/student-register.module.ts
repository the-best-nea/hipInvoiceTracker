import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StudentRegisterComponent } from './list/student-register.component';
import { StudentRegisterDetailComponent } from './detail/student-register-detail.component';
import { StudentRegisterUpdateComponent } from './update/student-register-update.component';
import { StudentRegisterDeleteDialogComponent } from './delete/student-register-delete-dialog.component';
import { StudentRegisterRoutingModule } from './route/student-register-routing.module';

@NgModule({
  imports: [SharedModule, StudentRegisterRoutingModule],
  declarations: [
    StudentRegisterComponent,
    StudentRegisterDetailComponent,
    StudentRegisterUpdateComponent,
    StudentRegisterDeleteDialogComponent,
  ],
  entryComponents: [StudentRegisterDeleteDialogComponent],
})
export class StudentRegisterModule {}
