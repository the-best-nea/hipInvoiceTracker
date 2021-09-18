import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISubject, Subject } from '../subject.model';
import { SubjectService } from '../service/subject.service';

@Component({
  selector: 'jhi-subject-update',
  templateUrl: './subject-update.component.html',
})
export class SubjectUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    subjectName: [null, [Validators.required]],
    description: [],
    createdAt: [null, [Validators.required]],
    active: [],
  });

  constructor(protected subjectService: SubjectService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subject }) => {
      if (subject.id === undefined) {
        const today = dayjs().startOf('day');
        subject.createdAt = today;
      }

      this.updateForm(subject);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subject = this.createFromForm();
    if (subject.id !== undefined) {
      this.subscribeToSaveResponse(this.subjectService.update(subject));
    } else {
      this.subscribeToSaveResponse(this.subjectService.create(subject));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubject>>): void {
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

  protected updateForm(subject: ISubject): void {
    this.editForm.patchValue({
      id: subject.id,
      subjectName: subject.subjectName,
      description: subject.description,
      createdAt: subject.createdAt ? subject.createdAt.format(DATE_TIME_FORMAT) : null,
      active: subject.active,
    });
  }

  protected createFromForm(): ISubject {
    return {
      ...new Subject(),
      id: this.editForm.get(['id'])!.value,
      subjectName: this.editForm.get(['subjectName'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      active: this.editForm.get(['active'])!.value,
    };
  }
}
