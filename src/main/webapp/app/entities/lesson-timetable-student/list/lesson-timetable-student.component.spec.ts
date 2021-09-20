import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LessonTimetableStudentService } from '../service/lesson-timetable-student.service';

import { LessonTimetableStudentComponent } from './lesson-timetable-student.component';

describe('Component Tests', () => {
  describe('LessonTimetableStudent Management Component', () => {
    let comp: LessonTimetableStudentComponent;
    let fixture: ComponentFixture<LessonTimetableStudentComponent>;
    let service: LessonTimetableStudentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonTimetableStudentComponent],
      })
        .overrideTemplate(LessonTimetableStudentComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LessonTimetableStudentComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LessonTimetableStudentService);

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
      expect(comp.lessonTimetableStudents?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
