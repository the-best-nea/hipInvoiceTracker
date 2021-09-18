import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentRegister } from '../student-register.model';
import { StudentRegisterService } from '../service/student-register.service';
import { StudentRegisterDeleteDialogComponent } from '../delete/student-register-delete-dialog.component';

@Component({
  selector: 'jhi-student-register',
  templateUrl: './student-register.component.html',
})
export class StudentRegisterComponent implements OnInit {
  studentRegisters?: IStudentRegister[];
  isLoading = false;

  constructor(protected studentRegisterService: StudentRegisterService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.studentRegisterService.query().subscribe(
      (res: HttpResponse<IStudentRegister[]>) => {
        this.isLoading = false;
        this.studentRegisters = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IStudentRegister): number {
    return item.id!;
  }

  delete(studentRegister: IStudentRegister): void {
    const modalRef = this.modalService.open(StudentRegisterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.studentRegister = studentRegister;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
