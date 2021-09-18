import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LessonInstanceDetailComponent } from './lesson-instance-detail.component';

describe('Component Tests', () => {
  describe('LessonInstance Management Detail Component', () => {
    let comp: LessonInstanceDetailComponent;
    let fixture: ComponentFixture<LessonInstanceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LessonInstanceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lessonInstance: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LessonInstanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LessonInstanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lessonInstance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lessonInstance).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
