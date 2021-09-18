jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentRegisterService } from '../service/student-register.service';
import { IStudentRegister, StudentRegister } from '../student-register.model';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { ILessonInstance } from 'app/entities/lesson-instance/lesson-instance.model';
import { LessonInstanceService } from 'app/entities/lesson-instance/service/lesson-instance.service';

import { StudentRegisterUpdateComponent } from './student-register-update.component';

describe('Component Tests', () => {
  describe('StudentRegister Management Update Component', () => {
    let comp: StudentRegisterUpdateComponent;
    let fixture: ComponentFixture<StudentRegisterUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentRegisterService: StudentRegisterService;
    let studentService: StudentService;
    let lessonInstanceService: LessonInstanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentRegisterUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StudentRegisterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentRegisterUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      studentRegisterService = TestBed.inject(StudentRegisterService);
      studentService = TestBed.inject(StudentService);
      lessonInstanceService = TestBed.inject(LessonInstanceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Student query and add missing value', () => {
        const studentRegister: IStudentRegister = { id: 456 };
        const student: IStudent = { id: 68365 };
        studentRegister.student = student;

        const studentCollection: IStudent[] = [{ id: 85662 }];
        jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
        const additionalStudents = [student];
        const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
        jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ studentRegister });
        comp.ngOnInit();

        expect(studentService.query).toHaveBeenCalled();
        expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(studentCollection, ...additionalStudents);
        expect(comp.studentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LessonInstance query and add missing value', () => {
        const studentRegister: IStudentRegister = { id: 456 };
        const lessonTimetable: ILessonInstance = { id: 82826 };
        studentRegister.lessonTimetable = lessonTimetable;

        const lessonInstanceCollection: ILessonInstance[] = [{ id: 50915 }];
        jest.spyOn(lessonInstanceService, 'query').mockReturnValue(of(new HttpResponse({ body: lessonInstanceCollection })));
        const additionalLessonInstances = [lessonTimetable];
        const expectedCollection: ILessonInstance[] = [...additionalLessonInstances, ...lessonInstanceCollection];
        jest.spyOn(lessonInstanceService, 'addLessonInstanceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ studentRegister });
        comp.ngOnInit();

        expect(lessonInstanceService.query).toHaveBeenCalled();
        expect(lessonInstanceService.addLessonInstanceToCollectionIfMissing).toHaveBeenCalledWith(
          lessonInstanceCollection,
          ...additionalLessonInstances
        );
        expect(comp.lessonInstancesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const studentRegister: IStudentRegister = { id: 456 };
        const student: IStudent = { id: 4266 };
        studentRegister.student = student;
        const lessonTimetable: ILessonInstance = { id: 11565 };
        studentRegister.lessonTimetable = lessonTimetable;

        activatedRoute.data = of({ studentRegister });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(studentRegister));
        expect(comp.studentsSharedCollection).toContain(student);
        expect(comp.lessonInstancesSharedCollection).toContain(lessonTimetable);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<StudentRegister>>();
        const studentRegister = { id: 123 };
        jest.spyOn(studentRegisterService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentRegister });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentRegister }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(studentRegisterService.update).toHaveBeenCalledWith(studentRegister);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<StudentRegister>>();
        const studentRegister = new StudentRegister();
        jest.spyOn(studentRegisterService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentRegister });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentRegister }));
        saveSubject.complete();

        // THEN
        expect(studentRegisterService.create).toHaveBeenCalledWith(studentRegister);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<StudentRegister>>();
        const studentRegister = { id: 123 };
        jest.spyOn(studentRegisterService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentRegister });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(studentRegisterService.update).toHaveBeenCalledWith(studentRegister);
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

      describe('trackLessonInstanceById', () => {
        it('Should return tracked LessonInstance primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLessonInstanceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
