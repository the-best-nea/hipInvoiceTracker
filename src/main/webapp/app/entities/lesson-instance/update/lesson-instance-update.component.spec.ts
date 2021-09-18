jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LessonInstanceService } from '../service/lesson-instance.service';
import { ILessonInstance, LessonInstance } from '../lesson-instance.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';

import { LessonInstanceUpdateComponent } from './lesson-instance-update.component';

describe('Component Tests', () => {
  describe('LessonInstance Management Update Component', () => {
    let comp: LessonInstanceUpdateComponent;
    let fixture: ComponentFixture<LessonInstanceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lessonInstanceService: LessonInstanceService;
    let userService: UserService;
    let studentService: StudentService;
    let subjectService: SubjectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonInstanceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LessonInstanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LessonInstanceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lessonInstanceService = TestBed.inject(LessonInstanceService);
      userService = TestBed.inject(UserService);
      studentService = TestBed.inject(StudentService);
      subjectService = TestBed.inject(SubjectService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const lessonInstance: ILessonInstance = { id: 456 };
        const internalUser: IUser = { id: 36917 };
        lessonInstance.internalUser = internalUser;

        const userCollection: IUser[] = [{ id: 91754 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [internalUser];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lessonInstance });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Student query and add missing value', () => {
        const lessonInstance: ILessonInstance = { id: 456 };
        const students: IStudent[] = [{ id: 39679 }];
        lessonInstance.students = students;

        const studentCollection: IStudent[] = [{ id: 44946 }];
        jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
        const additionalStudents = [...students];
        const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
        jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lessonInstance });
        comp.ngOnInit();

        expect(studentService.query).toHaveBeenCalled();
        expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(studentCollection, ...additionalStudents);
        expect(comp.studentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Subject query and add missing value', () => {
        const lessonInstance: ILessonInstance = { id: 456 };
        const subject: ISubject = { id: 90094 };
        lessonInstance.subject = subject;

        const subjectCollection: ISubject[] = [{ id: 10441 }];
        jest.spyOn(subjectService, 'query').mockReturnValue(of(new HttpResponse({ body: subjectCollection })));
        const additionalSubjects = [subject];
        const expectedCollection: ISubject[] = [...additionalSubjects, ...subjectCollection];
        jest.spyOn(subjectService, 'addSubjectToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lessonInstance });
        comp.ngOnInit();

        expect(subjectService.query).toHaveBeenCalled();
        expect(subjectService.addSubjectToCollectionIfMissing).toHaveBeenCalledWith(subjectCollection, ...additionalSubjects);
        expect(comp.subjectsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lessonInstance: ILessonInstance = { id: 456 };
        const internalUser: IUser = { id: 27830 };
        lessonInstance.internalUser = internalUser;
        const students: IStudent = { id: 11227 };
        lessonInstance.students = [students];
        const subject: ISubject = { id: 83628 };
        lessonInstance.subject = subject;

        activatedRoute.data = of({ lessonInstance });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lessonInstance));
        expect(comp.usersSharedCollection).toContain(internalUser);
        expect(comp.studentsSharedCollection).toContain(students);
        expect(comp.subjectsSharedCollection).toContain(subject);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonInstance>>();
        const lessonInstance = { id: 123 };
        jest.spyOn(lessonInstanceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonInstance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lessonInstance }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lessonInstanceService.update).toHaveBeenCalledWith(lessonInstance);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonInstance>>();
        const lessonInstance = new LessonInstance();
        jest.spyOn(lessonInstanceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonInstance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lessonInstance }));
        saveSubject.complete();

        // THEN
        expect(lessonInstanceService.create).toHaveBeenCalledWith(lessonInstance);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonInstance>>();
        const lessonInstance = { id: 123 };
        jest.spyOn(lessonInstanceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonInstance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lessonInstanceService.update).toHaveBeenCalledWith(lessonInstance);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackStudentById', () => {
        it('Should return tracked Student primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackStudentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSubjectById', () => {
        it('Should return tracked Subject primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSubjectById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedStudent', () => {
        it('Should return option if no Student is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedStudent(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Student for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedStudent(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Student is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedStudent(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
