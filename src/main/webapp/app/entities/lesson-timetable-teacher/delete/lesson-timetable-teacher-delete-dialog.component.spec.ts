jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LessonTimetableTeacherService } from '../service/lesson-timetable-teacher.service';

import { LessonTimetableTeacherDeleteDialogComponent } from './lesson-timetable-teacher-delete-dialog.component';

describe('Component Tests', () => {
  describe('LessonTimetableTeacher Management Delete Component', () => {
    let comp: LessonTimetableTeacherDeleteDialogComponent;
    let fixture: ComponentFixture<LessonTimetableTeacherDeleteDialogComponent>;
    let service: LessonTimetableTeacherService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LessonTimetableTeacherDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(LessonTimetableTeacherDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LessonTimetableTeacherDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LessonTimetableTeacherService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
