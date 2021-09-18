import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LessonTimetableDetailComponent } from './lesson-timetable-detail.component';

describe('Component Tests', () => {
  describe('LessonTimetable Management Detail Component', () => {
    let comp: LessonTimetableDetailComponent;
    let fixture: ComponentFixture<LessonTimetableDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LessonTimetableDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lessonTimetable: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LessonTimetableDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LessonTimetableDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lessonTimetable on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lessonTimetable).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
