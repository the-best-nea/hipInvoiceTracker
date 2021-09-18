import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IStudent, Student } from '../student.model';
import { StudentService } from '../service/student.service';
import { ILessonTimetable } from 'app/entities/lesson-timetable/lesson-timetable.model';
import { LessonTimetableService } from 'app/entities/lesson-timetable/service/lesson-timetable.service';

@Component({
  selector: 'jhi-student-update',
  templateUrl: './student-update.component.html',
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;

  lessonTimetablesSharedCollection: ILessonTimetable[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    yearGroup: [null, [Validators.required]],
    email: [null, [Validators.required]],
    phoneNumber: [],
    startDate: [null, [Validators.required]],
    endDate: [],
    active: [],
    createdAt: [null, [Validators.required]],
    lessonTimetables: [],
  });

  constructor(
    protected studentService: StudentService,
    protected lessonTimetableService: LessonTimetableService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      if (student.id === undefined) {
        const today = dayjs().startOf('day');
        student.startDate = today;
        student.endDate = today;
        student.createdAt = today;
      }

      this.updateForm(student);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.createFromForm();
    if (student.id !== undefined) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  trackLessonTimetableById(index: number, item: ILessonTimetable): number {
    return item.id!;
  }

  getSelectedLessonTimetable(option: ILessonTimetable, selectedVals?: ILessonTimetable[]): ILessonTimetable {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
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

  protected updateForm(student: IStudent): void {
    this.editForm.patchValue({
      id: student.id,
      firstName: student.firstName,
      lastName: student.lastName,
      yearGroup: student.yearGroup,
      email: student.email,
      phoneNumber: student.phoneNumber,
      startDate: student.startDate ? student.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: student.endDate ? student.endDate.format(DATE_TIME_FORMAT) : null,
      active: student.active,
      createdAt: student.createdAt ? student.createdAt.format(DATE_TIME_FORMAT) : null,
      lessonTimetables: student.lessonTimetables,
    });

    this.lessonTimetablesSharedCollection = this.lessonTimetableService.addLessonTimetableToCollectionIfMissing(
      this.lessonTimetablesSharedCollection,
      ...(student.lessonTimetables ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.lessonTimetableService
      .query()
      .pipe(map((res: HttpResponse<ILessonTimetable[]>) => res.body ?? []))
      .pipe(
        map((lessonTimetables: ILessonTimetable[]) =>
          this.lessonTimetableService.addLessonTimetableToCollectionIfMissing(
            lessonTimetables,
            ...(this.editForm.get('lessonTimetables')!.value ?? [])
          )
        )
      )
      .subscribe((lessonTimetables: ILessonTimetable[]) => (this.lessonTimetablesSharedCollection = lessonTimetables));
  }

  protected createFromForm(): IStudent {
    return {
      ...new Student(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      yearGroup: this.editForm.get(['yearGroup'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? dayjs(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? dayjs(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      active: this.editForm.get(['active'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      lessonTimetables: this.editForm.get(['lessonTimetables'])!.value,
    };
  }
}
