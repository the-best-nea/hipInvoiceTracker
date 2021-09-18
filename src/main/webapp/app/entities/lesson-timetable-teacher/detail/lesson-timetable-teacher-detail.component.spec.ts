import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LessonTimetableTeacherDetailComponent } from './lesson-timetable-teacher-detail.component';

describe('Component Tests', () => {
  describe('LessonTimetableTeacher Management Detail Component', () => {
    let comp: LessonTimetableTeacherDetailComponent;
    let fixture: ComponentFixture<LessonTimetableTeacherDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LessonTimetableTeacherDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lessonTimetableTeacher: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LessonTimetableTeacherDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LessonTimetableTeacherDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lessonTimetableTeacher on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lessonTimetableTeacher).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
