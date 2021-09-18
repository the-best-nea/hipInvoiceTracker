import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LessonInstanceService } from '../service/lesson-instance.service';

import { LessonInstanceComponent } from './lesson-instance.component';

describe('Component Tests', () => {
  describe('LessonInstance Management Component', () => {
    let comp: LessonInstanceComponent;
    let fixture: ComponentFixture<LessonInstanceComponent>;
    let service: LessonInstanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonInstanceComponent],
      })
        .overrideTemplate(LessonInstanceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LessonInstanceComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LessonInstanceService);

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
      expect(comp.lessonInstances?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
