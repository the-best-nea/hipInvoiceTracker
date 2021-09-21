jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LessonService } from '../service/lesson.service';
import { ILesson, Lesson } from '../lesson.model';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';

import { LessonUpdateComponent } from './lesson-update.component';

describe('Component Tests', () => {
  describe('Lesson Management Update Component', () => {
    let comp: LessonUpdateComponent;
    let fixture: ComponentFixture<LessonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lessonService: LessonService;
    let subjectService: SubjectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LessonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LessonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lessonService = TestBed.inject(LessonService);
      subjectService = TestBed.inject(SubjectService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Subject query and add missing value', () => {
        const lesson: ILesson = { id: 456 };
        const subject: ISubject = { id: 3609 };
        lesson.subject = subject;

        const subjectCollection: ISubject[] = [{ id: 50445 }];
        jest.spyOn(subjectService, 'query').mockReturnValue(of(new HttpResponse({ body: subjectCollection })));
        const additionalSubjects = [subject];
        const expectedCollection: ISubject[] = [...additionalSubjects, ...subjectCollection];
        jest.spyOn(subjectService, 'addSubjectToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lesson });
        comp.ngOnInit();

        expect(subjectService.query).toHaveBeenCalled();
        expect(subjectService.addSubjectToCollectionIfMissing).toHaveBeenCalledWith(subjectCollection, ...additionalSubjects);
        expect(comp.subjectsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lesson: ILesson = { id: 456 };
        const subject: ISubject = { id: 19551 };
        lesson.subject = subject;

        activatedRoute.data = of({ lesson });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lesson));
        expect(comp.subjectsSharedCollection).toContain(subject);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Lesson>>();
        const lesson = { id: 123 };
        jest.spyOn(lessonService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lesson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lesson }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lessonService.update).toHaveBeenCalledWith(lesson);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Lesson>>();
        const lesson = new Lesson();
        jest.spyOn(lessonService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lesson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lesson }));
        saveSubject.complete();

        // THEN
        expect(lessonService.create).toHaveBeenCalledWith(lesson);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Lesson>>();
        const lesson = { id: 123 };
        jest.spyOn(lessonService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lesson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lessonService.update).toHaveBeenCalledWith(lesson);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSubjectById', () => {
        it('Should return tracked Subject primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSubjectById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
