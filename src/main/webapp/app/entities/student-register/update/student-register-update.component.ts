import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IStudentRegister, StudentRegister } from '../student-register.model';
import { StudentRegisterService } from '../service/student-register.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { ILessonInstance } from 'app/entities/lesson-instance/lesson-instance.model';
import { LessonInstanceService } from 'app/entities/lesson-instance/service/lesson-instance.service';

@Component({
  selector: 'jhi-student-register-update',
  templateUrl: './student-register-update.component.html',
})
export class StudentRegisterUpdateComponent implements OnInit {
  isSaving = false;

  studentsSharedCollection: IStudent[] = [];
  lessonInstancesSharedCollection: ILessonInstance[] = [];

  editForm = this.fb.group({
    id: [],
    dateOfLesson: [null, [Validators.required]],
    attended: [],
    createdOn: [null, [Validators.required]],
    updatedOn: [null, [Validators.required]],
    student: [],
    lessonTimetable: [],
  });

  constructor(
    protected studentRegisterService: StudentRegisterService,
    protected studentService: StudentService,
    protected lessonInstanceService: LessonInstanceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentRegister }) => {
      if (studentRegister.id === undefined) {
        const today = dayjs().startOf('day');
        studentRegister.dateOfLesson = today;
        studentRegister.createdOn = today;
        studentRegister.updatedOn = today;
      }

      this.updateForm(studentRegister);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studentRegister = this.createFromForm();
    if (studentRegister.id !== undefined) {
      this.subscribeToSaveResponse(this.studentRegisterService.update(studentRegister));
    } else {
      this.subscribeToSaveResponse(this.studentRegisterService.create(studentRegister));
    }
  }

  trackStudentById(index: number, item: IStudent): number {
    return item.id!;
  }

  trackLessonInstanceById(index: number, item: ILessonInstance): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentRegister>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(studentRegister: IStudentRegister): void {
    this.editForm.patchValue({
      id: studentRegister.id,
      dateOfLesson: studentRegister.dateOfLesson ? studentRegister.dateOfLesson.format(DATE_TIME_FORMAT) : null,
      attended: studentRegister.attended,
      createdOn: studentRegister.createdOn ? studentRegister.createdOn.format(DATE_TIME_FORMAT) : null,
      updatedOn: studentRegister.updatedOn ? studentRegister.updatedOn.format(DATE_TIME_FORMAT) : null,
      student: studentRegister.student,
      lessonTimetable: studentRegister.lessonTimetable,
    });

    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing(
      this.studentsSharedCollection,
      studentRegister.student
    );
    this.lessonInstancesSharedCollection = this.lessonInstanceService.addLessonInstanceToCollectionIfMissing(
      this.lessonInstancesSharedCollection,
      studentRegister.lessonTimetable
    );
  }

  protected loadRelationshipsOptions(): void {
    this.studentService
      .query()
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) => this.studentService.addStudentToCollectionIfMissing(students, this.editForm.get('student')!.value))
      )
      .subscribe((students: IStudent[]) => (this.studentsSharedCollection = students));

    this.lessonInstanceService
      .query()
      .pipe(map((res: HttpResponse<ILessonInstance[]>) => res.body ?? []))
      .pipe(
        map((lessonInstances: ILessonInstance[]) =>
          this.lessonInstanceService.addLessonInstanceToCollectionIfMissing(lessonInstances, this.editForm.get('lessonTimetable')!.value)
        )
      )
      .subscribe((lessonInstances: ILessonInstance[]) => (this.lessonInstancesSharedCollection = lessonInstances));
  }

  protected createFromForm(): IStudentRegister {
    return {
      ...new StudentRegister(),
      id: this.editForm.get(['id'])!.value,
      dateOfLesson: this.editForm.get(['dateOfLesson'])!.value
        ? dayjs(this.editForm.get(['dateOfLesson'])!.value, DATE_TIME_FORMAT)
        : undefined,
      attended: this.editForm.get(['attended'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedOn: this.editForm.get(['updatedOn'])!.value ? dayjs(this.editForm.get(['updatedOn'])!.value, DATE_TIME_FORMAT) : undefined,
      student: this.editForm.get(['student'])!.value,
      lessonTimetable: this.editForm.get(['lessonTimetable'])!.value,
    };
  }
}
