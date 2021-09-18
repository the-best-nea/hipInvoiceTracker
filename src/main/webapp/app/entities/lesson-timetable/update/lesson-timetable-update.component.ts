import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILessonTimetable, LessonTimetable } from '../lesson-timetable.model';
import { LessonTimetableService } from '../service/lesson-timetable.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';

@Component({
  selector: 'jhi-lesson-timetable-update',
  templateUrl: './lesson-timetable-update.component.html',
})
export class LessonTimetableUpdateComponent implements OnInit {
  isSaving = false;

  locationsSharedCollection: ILocation[] = [];
  subjectsSharedCollection: ISubject[] = [];

  editForm = this.fb.group({
    id: [],
    lessonName: [null, [Validators.required]],
    startAt: [null, [Validators.required]],
    endAt: [null, [Validators.required]],
    dayOfWeek: [null, [Validators.required]],
    description: [],
    createdAt: [null, [Validators.required]],
    active: [],
    location: [],
    subject: [],
  });

  constructor(
    protected lessonTimetableService: LessonTimetableService,
    protected locationService: LocationService,
    protected subjectService: SubjectService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lessonTimetable }) => {
      if (lessonTimetable.id === undefined) {
        const today = dayjs().startOf('day');
        lessonTimetable.startAt = today;
        lessonTimetable.endAt = today;
        lessonTimetable.createdAt = today;
      }

      this.updateForm(lessonTimetable);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lessonTimetable = this.createFromForm();
    if (lessonTimetable.id !== undefined) {
      this.subscribeToSaveResponse(this.lessonTimetableService.update(lessonTimetable));
    } else {
      this.subscribeToSaveResponse(this.lessonTimetableService.create(lessonTimetable));
    }
  }

  trackLocationById(index: number, item: ILocation): number {
    return item.id!;
  }

  trackSubjectById(index: number, item: ISubject): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILessonTimetable>>): void {
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

  protected updateForm(lessonTimetable: ILessonTimetable): void {
    this.editForm.patchValue({
      id: lessonTimetable.id,
      lessonName: lessonTimetable.lessonName,
      startAt: lessonTimetable.startAt ? lessonTimetable.startAt.format(DATE_TIME_FORMAT) : null,
      endAt: lessonTimetable.endAt ? lessonTimetable.endAt.format(DATE_TIME_FORMAT) : null,
      dayOfWeek: lessonTimetable.dayOfWeek,
      description: lessonTimetable.description,
      createdAt: lessonTimetable.createdAt ? lessonTimetable.createdAt.format(DATE_TIME_FORMAT) : null,
      active: lessonTimetable.active,
      location: lessonTimetable.location,
      subject: lessonTimetable.subject,
    });

    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing(
      this.locationsSharedCollection,
      lessonTimetable.location
    );
    this.subjectsSharedCollection = this.subjectService.addSubjectToCollectionIfMissing(
      this.subjectsSharedCollection,
      lessonTimetable.subject
    );
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing(locations, this.editForm.get('location')!.value)
        )
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));

    this.subjectService
      .query()
      .pipe(map((res: HttpResponse<ISubject[]>) => res.body ?? []))
      .pipe(
        map((subjects: ISubject[]) => this.subjectService.addSubjectToCollectionIfMissing(subjects, this.editForm.get('subject')!.value))
      )
      .subscribe((subjects: ISubject[]) => (this.subjectsSharedCollection = subjects));
  }

  protected createFromForm(): ILessonTimetable {
    return {
      ...new LessonTimetable(),
      id: this.editForm.get(['id'])!.value,
      lessonName: this.editForm.get(['lessonName'])!.value,
      startAt: this.editForm.get(['startAt'])!.value ? dayjs(this.editForm.get(['startAt'])!.value, DATE_TIME_FORMAT) : undefined,
      endAt: this.editForm.get(['endAt'])!.value ? dayjs(this.editForm.get(['endAt'])!.value, DATE_TIME_FORMAT) : undefined,
      dayOfWeek: this.editForm.get(['dayOfWeek'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      active: this.editForm.get(['active'])!.value,
      location: this.editForm.get(['location'])!.value,
      subject: this.editForm.get(['subject'])!.value,
    };
  }
}
