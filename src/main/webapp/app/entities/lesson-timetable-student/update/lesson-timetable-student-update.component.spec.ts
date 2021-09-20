jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LessonTimetableStudentService } from '../service/lesson-timetable-student.service';
import { ILessonTimetableStudent, LessonTimetableStudent } from '../lesson-timetable-student.model';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { ILessonTimetable } from 'app/entities/lesson-timetable/lesson-timetable.model';
import { LessonTimetableService } from 'app/entities/lesson-timetable/service/lesson-timetable.service';

import { LessonTimetableStudentUpdateComponent } from './lesson-timetable-student-update.component';

describe('Component Tests', () => {
  describe('LessonTimetableStudent Management Update Component', () => {
    let comp: LessonTimetableStudentUpdateComponent;
    let fixture: ComponentFixture<LessonTimetableStudentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lessonTimetableStudentService: LessonTimetableStudentService;
    let studentService: StudentService;
    let lessonTimetableService: LessonTimetableService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonTimetableStudentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LessonTimetableStudentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LessonTimetableStudentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lessonTimetableStudentService = TestBed.inject(LessonTimetableStudentService);
      studentService = TestBed.inject(StudentService);
      lessonTimetableService = TestBed.inject(LessonTimetableService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Student query and add missing value', () => {
        const lessonTimetableStudent: ILessonTimetableStudent = { id: 456 };
        const student: IStudent = { id: 13173 };
        lessonTimetableStudent.student = student;

        const studentCollection: IStudent[] = [{ id: 65488 }];
        jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
        const additionalStudents = [student];
        const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
        jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lessonTimetableStudent });
        comp.ngOnInit();

        expect(studentService.query).toHaveBeenCalled();
        expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(studentCollection, ...additionalStudents);
        expect(comp.studentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LessonTimetable query and add missing value', () => {
        const lessonTimetableStudent: ILessonTimetableStudent = { id: 456 };
        const lessonTimetable: ILessonTimetable = { id: 85351 };
        lessonTimetableStudent.lessonTimetable = lessonTimetable;

        const lessonTimetableCollection: ILessonTimetable[] = [{ id: 9604 }];
        jest.spyOn(lessonTimetableService, 'query').mockReturnValue(of(new HttpResponse({ body: lessonTimetableCollection })));
        const additionalLessonTimetables = [lessonTimetable];
        const expectedCollection: ILessonTimetable[] = [...additionalLessonTimetables, ...lessonTimetableCollection];
        jest.spyOn(lessonTimetableService, 'addLessonTimetableToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lessonTimetableStudent });
        comp.ngOnInit();

        expect(lessonTimetableService.query).toHaveBeenCalled();
        expect(lessonTimetableService.addLessonTimetableToCollectionIfMissing).toHaveBeenCalledWith(
          lessonTimetableCollection,
          ...additionalLessonTimetables
        );
        expect(comp.lessonTimetablesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lessonTimetableStudent: ILessonTimetableStudent = { id: 456 };
        const student: IStudent = { id: 888 };
        lessonTimetableStudent.student = student;
        const lessonTimetable: ILessonTimetable = { id: 80445 };
        lessonTimetableStudent.lessonTimetable = lessonTimetable;

        activatedRoute.data = of({ lessonTimetableStudent });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lessonTimetableStudent));
        expect(comp.studentsSharedCollection).toContain(student);
        expect(comp.lessonTimetablesSharedCollection).toContain(lessonTimetable);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonTimetableStudent>>();
        const lessonTimetableStudent = { id: 123 };
        jest.spyOn(lessonTimetableStudentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonTimetableStudent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lessonTimetableStudent }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lessonTimetableStudentService.update).toHaveBeenCalledWith(lessonTimetableStudent);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonTimetableStudent>>();
        const lessonTimetableStudent = new LessonTimetableStudent();
        jest.spyOn(lessonTimetableStudentService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonTimetableStudent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lessonTimetableStudent }));
        saveSubject.complete();

        // THEN
        expect(lessonTimetableStudentService.create).toHaveBeenCalledWith(lessonTimetableStudent);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonTimetableStudent>>();
        const lessonTimetableStudent = { id: 123 };
        jest.spyOn(lessonTimetableStudentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonTimetableStudent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lessonTimetableStudentService.update).toHaveBeenCalledWith(lessonTimetableStudent);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackStudentById', () => {
        it('Should return tracked Student primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackStudentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLessonTimetableById', () => {
        it('Should return tracked LessonTimetable primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLessonTimetableById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
