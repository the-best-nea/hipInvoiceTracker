import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LessonTimetableService } from '../service/lesson-timetable.service';

import { LessonTimetableComponent } from './lesson-timetable.component';

describe('Component Tests', () => {
  describe('LessonTimetable Management Component', () => {
    let comp: LessonTimetableComponent;
    let fixture: ComponentFixture<LessonTimetableComponent>;
    let service: LessonTimetableService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonTimetableComponent],
      })
        .overrideTemplate(LessonTimetableComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LessonTimetableComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LessonTimetableService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.lessonTimetables?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
