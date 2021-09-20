import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LessonTimetableStudentDetailComponent } from './lesson-timetable-student-detail.component';

describe('Component Tests', () => {
  describe('LessonTimetableStudent Management Detail Component', () => {
    let comp: LessonTimetableStudentDetailComponent;
    let fixture: ComponentFixture<LessonTimetableStudentDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LessonTimetableStudentDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lessonTimetableStudent: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LessonTimetableStudentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LessonTimetableStudentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lessonTimetableStudent on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lessonTimetableStudent).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
