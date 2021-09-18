import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentRegister } from '../student-register.model';

@Component({
  selector: 'jhi-student-register-detail',
  templateUrl: './student-register-detail.component.html',
})
export class StudentRegisterDetailComponent implements OnInit {
  studentRegister: IStudentRegister | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentRegister }) => {
      this.studentRegister = studentRegister;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
