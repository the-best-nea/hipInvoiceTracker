import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILessonTimetableTeacher, LessonTimetableTeacher } from '../lesson-timetable-teacher.model';
import { LessonTimetableTeacherService } from '../service/lesson-timetable-teacher.service';
import { ILessonTimetable } from 'app/entities/lesson-timetable/lesson-timetable.model';
import { LessonTimetableService } from 'app/entities/lesson-timetable/service/lesson-timetable.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-lesson-timetable-teacher-update',
  templateUrl: './lesson-timetable-teacher-update.component.html',
})
export class LessonTimetableTeacherUpdateComponent implements OnInit {
  isSaving = false;

  lessonTimetablesSharedCollection: ILessonTimetable[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    pay: [null, [Validators.required]],
    lessonTimetable: [],
    internalUser: [],
  });

  constructor(
    protected lessonTimetableTeacherService: LessonTimetableTeacherService,
    protected lessonTimetableService: LessonTimetableService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lessonTimetableTeacher }) => {
      this.updateForm(lessonTimetableTeacher);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lessonTimetableTeacher = this.createFromForm();
    if (lessonTimetableTeacher.id !== undefined) {
      this.subscribeToSaveResponse(this.lessonTimetableTeacherService.update(lessonTimetableTeacher));
    } else {
      this.subscribeToSaveResponse(this.lessonTimetableTeacherService.create(lessonTimetableTeacher));
    }
  }

  trackLessonTimetableById(index: number, item: ILessonTimetable): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILessonTimetableTeacher>>): void {
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

  protected updateForm(lessonTimetableTeacher: ILessonTimetableTeacher): void {
    this.editForm.patchValue({
      id: lessonTimetableTeacher.id,
      pay: lessonTimetableTeacher.pay,
      lessonTimetable: lessonTimetableTeacher.lessonTimetable,
      internalUser: lessonTimetableTeacher.internalUser,
    });

    this.lessonTimetablesSharedCollection = this.lessonTimetableService.addLessonTimetableToCollectionIfMissing(
      this.lessonTimetablesSharedCollection,
      lessonTimetableTeacher.lessonTimetable
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      lessonTimetableTeacher.internalUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.lessonTimetableService
      .query()
      .pipe(map((res: HttpResponse<ILessonTimetable[]>) => res.body ?? []))
      .pipe(
        map((lessonTimetables: ILessonTimetable[]) =>
          this.lessonTimetableService.addLessonTimetableToCollectionIfMissing(lessonTimetables, this.editForm.get('lessonTimetable')!.value)
        )
      )
      .subscribe((lessonTimetables: ILessonTimetable[]) => (this.lessonTimetablesSharedCollection = lessonTimetables));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('internalUser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ILessonTimetableTeacher {
    return {
      ...new LessonTimetableTeacher(),
      id: this.editForm.get(['id'])!.value,
      pay: this.editForm.get(['pay'])!.value,
      lessonTimetable: this.editForm.get(['lessonTimetable'])!.value,
      internalUser: this.editForm.get(['internalUser'])!.value,
    };
  }
}
