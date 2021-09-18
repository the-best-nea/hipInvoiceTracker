import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentRegister } from '../student-register.model';
import { StudentRegisterService } from '../service/student-register.service';

@Component({
  templateUrl: './student-register-delete-dialog.component.html',
})
export class StudentRegisterDeleteDialogComponent {
  studentRegister?: IStudentRegister;

  constructor(protected studentRegisterService: StudentRegisterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentRegisterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
