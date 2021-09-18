jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LessonTimetableService } from '../service/lesson-timetable.service';
import { ILessonTimetable, LessonTimetable } from '../lesson-timetable.model';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';

import { LessonTimetableUpdateComponent } from './lesson-timetable-update.component';

describe('Component Tests', () => {
  describe('LessonTimetable Management Update Component', () => {
    let comp: LessonTimetableUpdateComponent;
    let fixture: ComponentFixture<LessonTimetableUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lessonTimetableService: LessonTimetableService;
    let locationService: LocationService;
    let subjectService: SubjectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonTimetableUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LessonTimetableUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LessonTimetableUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lessonTimetableService = TestBed.inject(LessonTimetableService);
      locationService = TestBed.inject(LocationService);
      subjectService = TestBed.inject(SubjectService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Location query and add missing value', () => {
        const lessonTimetable: ILessonTimetable = { id: 456 };
        const location: ILocation = { id: 18964 };
        lessonTimetable.location = location;

        const locationCollection: ILocation[] = [{ id: 48192 }];
        jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
        const additionalLocations = [location];
        const expectedCollection: ILocation[] = [...additionalLocations, ...locationCollection];
        jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lessonTimetable });
        comp.ngOnInit();

        expect(locationService.query).toHaveBeenCalled();
        expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(locationCollection, ...additionalLocations);
        expect(comp.locationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Subject query and add missing value', () => {
        const lessonTimetable: ILessonTimetable = { id: 456 };
        const subject: ISubject = { id: 71 };
        lessonTimetable.subject = subject;

        const subjectCollection: ISubject[] = [{ id: 68889 }];
        jest.spyOn(subjectService, 'query').mockReturnValue(of(new HttpResponse({ body: subjectCollection })));
        const additionalSubjects = [subject];
        const expectedCollection: ISubject[] = [...additionalSubjects, ...subjectCollection];
        jest.spyOn(subjectService, 'addSubjectToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lessonTimetable });
        comp.ngOnInit();

        expect(subjectService.query).toHaveBeenCalled();
        expect(subjectService.addSubjectToCollectionIfMissing).toHaveBeenCalledWith(subjectCollection, ...additionalSubjects);
        expect(comp.subjectsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lessonTimetable: ILessonTimetable = { id: 456 };
        const location: ILocation = { id: 52318 };
        lessonTimetable.location = location;
        const subject: ISubject = { id: 84570 };
        lessonTimetable.subject = subject;

        activatedRoute.data = of({ lessonTimetable });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lessonTimetable));
        expect(comp.locationsSharedCollection).toContain(location);
        expect(comp.subjectsSharedCollection).toContain(subject);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonTimetable>>();
        const lessonTimetable = { id: 123 };
        jest.spyOn(lessonTimetableService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonTimetable });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lessonTimetable }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lessonTimetableService.update).toHaveBeenCalledWith(lessonTimetable);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonTimetable>>();
        const lessonTimetable = new LessonTimetable();
        jest.spyOn(lessonTimetableService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonTimetable });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lessonTimetable }));
        saveSubject.complete();

        // THEN
        expect(lessonTimetableService.create).toHaveBeenCalledWith(lessonTimetable);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LessonTimetable>>();
        const lessonTimetable = { id: 123 };
        jest.spyOn(lessonTimetableService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lessonTimetable });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lessonTimetableService.update).toHaveBeenCalledWith(lessonTimetable);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLocationById', () => {
        it('Should return tracked Location primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLocationById(0, entity);
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
  });
});
