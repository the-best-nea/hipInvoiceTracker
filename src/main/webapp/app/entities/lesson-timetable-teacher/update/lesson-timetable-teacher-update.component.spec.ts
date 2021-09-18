jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LessonTimetableTeacherService } from '../service/lesson-timetable-teacher.service';
import { ILessonTimetableTeacher, LessonTimetableTeacher } from '../lesson-timetable-teacher.model';
import { ILessonTimetable } from 'app/entities/lesson-timetable/lesson-timetable.model';
import { LessonTimetableService } from 'app/entities/lesson-timetable/service/lesson-timetable.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { LessonTimetableTeacherUpdateComponent } from './lesson-timetable-teacher-update.component';

describe('Component Tests', () => {
  describe('LessonTimetableTeacher Management Update Component', () => {
    let comp: LessonTimetableTeacherUpdateComponent;
    let fixture: ComponentFixture<LessonTimetableTeacherUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lessonTimetableTeacherService: LessonTimetableTeacherService;
    let lessonTimetableService: LessonTimetableService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonTimetableTeacherUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LessonTimetableTeacherUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LessonTimetableTeacherUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lessonTimetableTeacherService = TestBed.inject(LessonTimetableTeacherService);
      lessonTimetableService = TestBed.inject(LessonTimetableService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call LessonTimetable query and add missing value', () => {
        const lessonTimetableTeacher: ILessonTimetableTeacher = { id: 456 };
        const lessonTimetable: ILessonTimetable = { id: 69454 };
        lessonTimetableTeacher.lessonTimetable = lessonTimetable;

        const lessonTimetableCollection: ILessonTimetable[] = [{ id: 64323 }];
        jest.spyOn(lessonTimetableService, 'query').mockReturnValue(of(new HttpResponse({ body: lessonTimetableCollection })));
        const additionalLessonTimetables = [lessonTimetable];
        const expectedCollection: ILessonTimetable[] = [...additionalLessonTimetables, ...lessonTimetableCollection];
        jest.spyOn(lessonTimetableService, 'addLessonTimetableToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lessonTimetableTeacher });
        comp.ngOnInit();

        expect(lessonTimetableService.query).toHaveBeenCalled();
        expect(lessonTimetableService.addLessonTimetableToCollectionIfMissing).toHaveBeenCalledWith(
          lessonTimetableCollection,
          ...additionalLessonTimetables
        );
        expect(comp.lessonTimetablesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const lessonTimetableTeacher: ILessonTimetableTeacher = { id: 456 };
        const internalUser: IUser = { id: 30817 };
        lessonTimetableTeacher.internalUser = internalUser;

        const userCollection: IUser[] = [{ id: 55948 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [internalUser];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lessonTimetableTeacher });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lessonTimetableTeacher: ILessonTimetableTeacher = { id: 456 };
        const lessonTimetable: ILessonTimetable = { id: 43799 };
        lessonTimetableTeacher.lessonTimetable = lessonTimetable;
        const internalUser: IUser = { id: 72097 };
        lessonTimetableTeacher.internalUser = internalUser;

        activatedRoute.data = of({ lessonTimetableTeacher });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lessonTimetableTeacher));
        expect(comp.lessonTimetablesSharedCollection).toContain(lessonTimetable);
        expect(comp.usersSharedCollection).toContain(internalUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonTimetableTeacher>>();
        const lessonTimetableTeacher = { id: 123 };
        jest.spyOn(lessonTimetableTeacherService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonTimetableTeacher });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lessonTimetableTeacher }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lessonTimetableTeacherService.update).toHaveBeenCalledWith(lessonTimetableTeacher);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonTimetableTeacher>>();
        const lessonTimetableTeacher = new LessonTimetableTeacher();
        jest.spyOn(lessonTimetableTeacherService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonTimetableTeacher });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lessonTimetableTeacher }));
        saveSubject.complete();

        // THEN
        expect(lessonTimetableTeacherService.create).toHaveBeenCalledWith(lessonTimetableTeacher);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonTimetableTeacher>>();
        const lessonTimetableTeacher = { id: 123 };
        jest.spyOn(lessonTimetableTeacherService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonTimetableTeacher });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lessonTimetableTeacherService.update).toHaveBeenCalledWith(lessonTimetableTeacher);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLessonTimetableById', () => {
        it('Should return tracked LessonTimetable primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLessonTimetableById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
