jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentService } from '../service/student.service';
import { IStudent, Student } from '../student.model';
import { ILessonTimetable } from 'app/entities/lesson-timetable/lesson-timetable.model';
import { LessonTimetableService } from 'app/entities/lesson-timetable/service/lesson-timetable.service';

import { StudentUpdateComponent } from './student-update.component';

describe('Component Tests', () => {
  describe('Student Management Update Component', () => {
    let comp: StudentUpdateComponent;
    let fixture: ComponentFixture<StudentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentService: StudentService;
    let lessonTimetableService: LessonTimetableService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StudentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      studentService = TestBed.inject(StudentService);
      lessonTimetableService = TestBed.inject(LessonTimetableService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call LessonTimetable query and add missing value', () => {
        const student: IStudent = { id: 456 };
        const lessonTimetables: ILessonTimetable[] = [{ id: 23169 }];
        student.lessonTimetables = lessonTimetables;

        const lessonTimetableCollection: ILessonTimetable[] = [{ id: 28587 }];
        jest.spyOn(lessonTimetableService, 'query').mockReturnValue(of(new HttpResponse({ body: lessonTimetableCollection })));
        const additionalLessonTimetables = [...lessonTimetables];
        const expectedCollection: ILessonTimetable[] = [...additionalLessonTimetables, ...lessonTimetableCollection];
        jest.spyOn(lessonTimetableService, 'addLessonTimetableToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(lessonTimetableService.query).toHaveBeenCalled();
        expect(lessonTimetableService.addLessonTimetableToCollectionIfMissing).toHaveBeenCalledWith(
          lessonTimetableCollection,
          ...additionalLessonTimetables
        );
        expect(comp.lessonTimetablesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const student: IStudent = { id: 456 };
        const lessonTimetables: ILessonTimetable = { id: 25348 };
        student.lessonTimetables = [lessonTimetables];

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(student));
        expect(comp.lessonTimetablesSharedCollection).toContain(lessonTimetables);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Student>>();
        const student = { id: 123 };
        jest.spyOn(studentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: student }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(studentService.update).toHaveBeenCalledWith(student);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Student>>();
        const student = new Student();
        jest.spyOn(studentService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: student }));
        saveSubject.complete();

        // THEN
        expect(studentService.create).toHaveBeenCalledWith(student);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Student>>();
        const student = { id: 123 };
        jest.spyOn(studentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(studentService.update).toHaveBeenCalledWith(student);
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
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedLessonTimetable', () => {
        it('Should return option if no LessonTimetable is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedLessonTimetable(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected LessonTimetable for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedLessonTimetable(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this LessonTimetable is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedLessonTimetable(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
