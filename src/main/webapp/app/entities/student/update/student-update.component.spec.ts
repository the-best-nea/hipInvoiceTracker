jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentService } from '../service/student.service';
import { IStudent, Student } from '../student.model';

import { StudentUpdateComponent } from './student-update.component';

describe('Component Tests', () => {
  describe('Student Management Update Component', () => {
    let comp: StudentUpdateComponent;
    let fixture: ComponentFixture<StudentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentService: StudentService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const student: IStudent = { id: 456 };

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(student));
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
  });
});
