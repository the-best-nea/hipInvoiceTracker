import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudent } from '../student.model';
import { StudentService } from '../service/student.service';
import { StudentDeleteDialogComponent } from '../delete/student-delete-dialog.component';

@Component({
  selector: 'jhi-student',
  templateUrl: './student.component.html',
})
export class StudentComponent implements OnInit {
  students?: IStudent[];
  isLoading = false;

  filterId?: number;
  filterFirstName?: string;
  filterLastName?: string;
  filterYearGroup?: number;
  filterEmail?: string;
  filterActive?: string;

  filterQuery: any = {};

  constructor(protected studentService: StudentService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.studentService.query(this.filterQuery).subscribe(
      (res: HttpResponse<IStudent[]>) => {
        this.isLoading = false;
        this.students = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IStudent): number {
    return item.id!;
  }

  delete(student: IStudent): void {
    const modalRef = this.modalService.open(StudentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.student = student;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
  updateStudent(): void {
      this.filterQuery = {};
      if (this.filterId != null) {
        this.filterQuery['id.equals'] = this.filterId;
      }
      if (this.filterFirstName != null && this.filterFirstName !== "") {
        this.filterQuery['firstName.contains'] = this.filterFirstName;
      }
      if (this.filterLastName != null && this.filterLastName !== "") {
        this.filterQuery['lastName.contains'] = this.filterLastName;
      }
      if (this.filterYearGroup != null) {
        this.filterQuery['yearGroup.equals'] = this.filterYearGroup;
      }
      if (this.filterEmail != null && this.filterEmail !== "") {
        this.filterQuery['email.contains'] = this.filterEmail;
      }
      if (this.filterActive === "Active") {
        this.filterQuery['active.equals'] = true;
      }
      if (this.filterActive === "Inactive") {
        this.filterQuery['active.equals'] = false;
      }

      this.loadAll();
    }



}
