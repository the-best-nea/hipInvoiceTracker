import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILessonInstance, LessonInstance } from '../lesson-instance.model';
import { LessonInstanceService } from '../service/lesson-instance.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';
import { IRegistrationDetails } from '../../registration-detail/registration-details.model';

@Component({
  selector: 'jhi-lesson-instance-register',
  templateUrl: './lesson-instance-register.component.html',
})
export class LessonInstanceRegisterComponent implements OnInit {
  students?: IStudent[];
  registrationDetails?: IRegistrationDetails[];

  isSaving = false;

  usersSharedCollection: IUser[] = [];
  studentsSharedCollection: IStudent[] = [];
  subjectsSharedCollection: ISubject[] = [];

  editForm = this.fb.group({
    id: [],
    lessonName: [null, [Validators.required]],
    startAt: [null, [Validators.required]],
    endAt: [null, [Validators.required]],
    dayOfWeek: [null, [Validators.required]],
    description: [],
    cretedOn: [null, [Validators.required]],
    internalUser: [],
    students: [],
    subject: [],
    registrationDetails: [],
  });

  constructor(
    protected lessonInstanceService: LessonInstanceService,
    protected userService: UserService,
    protected studentService: StudentService,
    protected subjectService: SubjectService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lessonInstance }) => {
      if (lessonInstance.id === undefined) {
        const today = dayjs().startOf('day');
        lessonInstance.startAt = today;
        lessonInstance.endAt = today;
        lessonInstance.cretedOn = today;
      }

      this.updateForm(lessonInstance);

      this.loadRelationshipsOptions();

      this.studentService.queryByLessonId(lessonInstance.id).subscribe((res: HttpResponse<IStudent[]>) => {
        this.students = res.body ?? [];
      });
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lessonInstance = this.createFromForm();
    if (lessonInstance.id !== undefined) {
      this.subscribeToSaveResponse(this.lessonInstanceService.update(lessonInstance));
    } else {
      this.subscribeToSaveResponse(this.lessonInstanceService.create(lessonInstance));
    }
  }

  register(): void {
    this.isSaving = true;
    const lessonInstance = this.createFromForm();
    if (lessonInstance.id !== undefined) {
      this.subscribeToSaveResponse(this.lessonInstanceService.register(lessonInstance));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackStudentById(index: number, item: IStudent): number {
    return item.id!;
  }

  trackSubjectById(index: number, item: ISubject): number {
    return item.id!;
  }

  getSelectedStudent(option: IStudent, selectedVals?: IStudent[]): IStudent {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILessonInstance>>): void {
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

  protected updateForm(lessonInstance: ILessonInstance): void {
    this.editForm.patchValue({
      id: lessonInstance.id,
      lessonName: lessonInstance.lessonName,
      startAt: lessonInstance.startAt ? lessonInstance.startAt.format(DATE_TIME_FORMAT) : null,
      endAt: lessonInstance.endAt ? lessonInstance.endAt.format(DATE_TIME_FORMAT) : null,
      dayOfWeek: lessonInstance.dayOfWeek,
      description: lessonInstance.description,
      cretedOn: lessonInstance.cretedOn ? lessonInstance.cretedOn.format(DATE_TIME_FORMAT) : null,
      internalUser: lessonInstance.internalUser,
      students: lessonInstance.students,
      subject: lessonInstance.subject,
      registrationDetails: lessonInstance.registrationDetails,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, lessonInstance.internalUser);
    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing(
      this.studentsSharedCollection,
      ...(lessonInstance.students ?? [])
    );
    this.subjectsSharedCollection = this.subjectService.addSubjectToCollectionIfMissing(
      this.subjectsSharedCollection,
      lessonInstance.subject
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('internalUser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.studentService
      .query()
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) =>
          this.studentService.addStudentToCollectionIfMissing(students, ...(this.editForm.get('students')!.value ?? []))
        )
      )
      .subscribe((students: IStudent[]) => (this.studentsSharedCollection = students));

    this.subjectService
      .query()
      .pipe(map((res: HttpResponse<ISubject[]>) => res.body ?? []))
      .pipe(
        map((subjects: ISubject[]) => this.subjectService.addSubjectToCollectionIfMissing(subjects, this.editForm.get('subject')!.value))
      )
      .subscribe((subjects: ISubject[]) => (this.subjectsSharedCollection = subjects));
  }

  protected createFromForm(): ILessonInstance {
    return {
      ...new LessonInstance(),
      id: this.editForm.get(['id'])!.value,
      lessonName: this.editForm.get(['lessonName'])!.value,
      startAt: this.editForm.get(['startAt'])!.value ? dayjs(this.editForm.get(['startAt'])!.value, DATE_TIME_FORMAT) : undefined,
      endAt: this.editForm.get(['endAt'])!.value ? dayjs(this.editForm.get(['endAt'])!.value, DATE_TIME_FORMAT) : undefined,
      dayOfWeek: this.editForm.get(['dayOfWeek'])!.value,
      description: this.editForm.get(['description'])!.value,
      cretedOn: this.editForm.get(['cretedOn'])!.value ? dayjs(this.editForm.get(['cretedOn'])!.value, DATE_TIME_FORMAT) : undefined,
      internalUser: this.editForm.get(['internalUser'])!.value,
      students: this.editForm.get(['students'])!.value,
      subject: this.editForm.get(['subject'])!.value,
    };
  }
}
