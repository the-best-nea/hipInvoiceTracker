jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILessonTimetableStudent, LessonTimetableStudent } from '../lesson-timetable-student.model';
import { LessonTimetableStudentService } from '../service/lesson-timetable-student.service';

import { LessonTimetableStudentRoutingResolveService } from './lesson-timetable-student-routing-resolve.service';

describe('Service Tests', () => {
  describe('LessonTimetableStudent routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LessonTimetableStudentRoutingResolveService;
    let service: LessonTimetableStudentService;
    let resultLessonTimetableStudent: ILessonTimetableStudent | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LessonTimetableStudentRoutingResolveService);
      service = TestBed.inject(LessonTimetableStudentService);
      resultLessonTimetableStudent = undefined;
    });

    describe('resolve', () => {
      it('should return ILessonTimetableStudent returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLessonTimetableStudent = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLessonTimetableStudent).toEqual({ id: 123 });
      });

      it('should return new ILessonTimetableStudent if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLessonTimetableStudent = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLessonTimetableStudent).toEqual(new LessonTimetableStudent());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LessonTimetableStudent })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLessonTimetableStudent = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLessonTimetableStudent).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
