import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILessonTimetableStudent, LessonTimetableStudent } from '../lesson-timetable-student.model';
import { LessonTimetableStudentService } from '../service/lesson-timetable-student.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { ILessonTimetable } from 'app/entities/lesson-timetable/lesson-timetable.model';
import { LessonTimetableService } from 'app/entities/lesson-timetable/service/lesson-timetable.service';

@Component({
  selector: 'jhi-lesson-timetable-student-update',
  templateUrl: './lesson-timetable-student-update.component.html',
})
export class LessonTimetableStudentUpdateComponent implements OnInit {
  isSaving = false;

  studentsSharedCollection: IStudent[] = [];
  lessonTimetablesSharedCollection: ILessonTimetable[] = [];

  editForm = this.fb.group({
    id: [],
    pay: [],
    student: [],
    lessonTimetable: [],
  });

  constructor(
    protected lessonTimetableStudentService: LessonTimetableStudentService,
    protected studentService: StudentService,
    protected lessonTimetableService: LessonTimetableService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lessonTimetableStudent }) => {
      this.updateForm(lessonTimetableStudent);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lessonTimetableStudent = this.createFromForm();
    if (lessonTimetableStudent.id !== undefined) {
      this.subscribeToSaveResponse(this.lessonTimetableStudentService.update(lessonTimetableStudent));
    } else {
      this.subscribeToSaveResponse(this.lessonTimetableStudentService.create(lessonTimetableStudent));
    }
  }

  trackStudentById(index: number, item: IStudent): number {
    return item.id!;
  }

  trackLessonTimetableById(index: number, item: ILessonTimetable): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILessonTimetableStudent>>): void {
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

  protected updateForm(lessonTimetableStudent: ILessonTimetableStudent): void {
    this.editForm.patchValue({
      id: lessonTimetableStudent.id,
      pay: lessonTimetableStudent.pay,
      student: lessonTimetableStudent.student,
      lessonTimetable: lessonTimetableStudent.lessonTimetable,
    });

    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing(
      this.studentsSharedCollection,
      lessonTimetableStudent.student
    );
    this.lessonTimetablesSharedCollection = this.lessonTimetableService.addLessonTimetableToCollectionIfMissing(
      this.lessonTimetablesSharedCollection,
      lessonTimetableStudent.lessonTimetable
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

    this.lessonTimetableService
      .query()
      .pipe(map((res: HttpResponse<ILessonTimetable[]>) => res.body ?? []))
      .pipe(
        map((lessonTimetables: ILessonTimetable[]) =>
          this.lessonTimetableService.addLessonTimetableToCollectionIfMissing(lessonTimetables, this.editForm.get('lessonTimetable')!.value)
        )
      )
      .subscribe((lessonTimetables: ILessonTimetable[]) => (this.lessonTimetablesSharedCollection = lessonTimetables));
  }

  protected createFromForm(): ILessonTimetableStudent {
    return {
      ...new LessonTimetableStudent(),
      id: this.editForm.get(['id'])!.value,
      pay: this.editForm.get(['pay'])!.value,
      student: this.editForm.get(['student'])!.value,
      lessonTimetable: this.editForm.get(['lessonTimetable'])!.value,
    };
  }
}
