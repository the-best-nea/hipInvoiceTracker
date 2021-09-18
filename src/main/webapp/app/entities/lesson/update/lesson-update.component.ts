import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILesson, Lesson } from '../lesson.model';
import { LessonService } from '../service/lesson.service';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';

@Component({
  selector: 'jhi-lesson-update',
  templateUrl: './lesson-update.component.html',
})
export class LessonUpdateComponent implements OnInit {
  isSaving = false;

  subjectsSharedCollection: ISubject[] = [];

  editForm = this.fb.group({
    id: [],
    lessonName: [null, [Validators.required]],
    startAt: [null, [Validators.required]],
    endAt: [null, [Validators.required]],
    dayOfWeek: [null, [Validators.required]],
    desciption: [],
    createdAt: [null, [Validators.required]],
    active: [],
    subject: [],
  });

  constructor(
    protected lessonService: LessonService,
    protected subjectService: SubjectService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lesson }) => {
      if (lesson.id === undefined) {
        const today = dayjs().startOf('day');
        lesson.startAt = today;
        lesson.endAt = today;
        lesson.createdAt = today;
      }

      this.updateForm(lesson);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lesson = this.createFromForm();
    if (lesson.id !== undefined) {
      this.subscribeToSaveResponse(this.lessonService.update(lesson));
    } else {
      this.subscribeToSaveResponse(this.lessonService.create(lesson));
    }
  }

  trackSubjectById(index: number, item: ISubject): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILesson>>): void {
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

  protected updateForm(lesson: ILesson): void {
    this.editForm.patchValue({
      id: lesson.id,
      lessonName: lesson.lessonName,
      startAt: lesson.startAt ? lesson.startAt.format(DATE_TIME_FORMAT) : null,
      endAt: lesson.endAt ? lesson.endAt.format(DATE_TIME_FORMAT) : null,
      dayOfWeek: lesson.dayOfWeek,
      desciption: lesson.desciption,
      createdAt: lesson.createdAt ? lesson.createdAt.format(DATE_TIME_FORMAT) : null,
      active: lesson.active,
      subject: lesson.subject,
    });

    this.subjectsSharedCollection = this.subjectService.addSubjectToCollectionIfMissing(this.subjectsSharedCollection, lesson.subject);
  }

  protected loadRelationshipsOptions(): void {
    this.subjectService
      .query()
      .pipe(map((res: HttpResponse<ISubject[]>) => res.body ?? []))
      .pipe(
        map((subjects: ISubject[]) => this.subjectService.addSubjectToCollectionIfMissing(subjects, this.editForm.get('subject')!.value))
      )
      .subscribe((subjects: ISubject[]) => (this.subjectsSharedCollection = subjects));
  }

  protected createFromForm(): ILesson {
    return {
      ...new Lesson(),
      id: this.editForm.get(['id'])!.value,
      lessonName: this.editForm.get(['lessonName'])!.value,
      startAt: this.editForm.get(['startAt'])!.value ? dayjs(this.editForm.get(['startAt'])!.value, DATE_TIME_FORMAT) : undefined,
      endAt: this.editForm.get(['endAt'])!.value ? dayjs(this.editForm.get(['endAt'])!.value, DATE_TIME_FORMAT) : undefined,
      dayOfWeek: this.editForm.get(['dayOfWeek'])!.value,
      desciption: this.editForm.get(['desciption'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      active: this.editForm.get(['active'])!.value,
      subject: this.editForm.get(['subject'])!.value,
    };
  }
}
