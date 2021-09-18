import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LessonTimetableTeacherService } from '../service/lesson-timetable-teacher.service';

import { LessonTimetableTeacherComponent } from './lesson-timetable-teacher.component';

describe('Component Tests', () => {
  describe('LessonTimetableTeacher Management Component', () => {
    let comp: LessonTimetableTeacherComponent;
    let fixture: ComponentFixture<LessonTimetableTeacherComponent>;
    let service: LessonTimetableTeacherService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonTimetableTeacherComponent],
      })
        .overrideTemplate(LessonTimetableTeacherComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LessonTimetableTeacherComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LessonTimetableTeacherService);

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
      expect(comp.lessonTimetableTeachers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
