jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILessonTimetableTeacher, LessonTimetableTeacher } from '../lesson-timetable-teacher.model';
import { LessonTimetableTeacherService } from '../service/lesson-timetable-teacher.service';

import { LessonTimetableTeacherRoutingResolveService } from './lesson-timetable-teacher-routing-resolve.service';

describe('Service Tests', () => {
  describe('LessonTimetableTeacher routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LessonTimetableTeacherRoutingResolveService;
    let service: LessonTimetableTeacherService;
    let resultLessonTimetableTeacher: ILessonTimetableTeacher | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LessonTimetableTeacherRoutingResolveService);
      service = TestBed.inject(LessonTimetableTeacherService);
      resultLessonTimetableTeacher = undefined;
    });

    describe('resolve', () => {
      it('should return ILessonTimetableTeacher returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLessonTimetableTeacher = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLessonTimetableTeacher).toEqual({ id: 123 });
      });

      it('should return new ILessonTimetableTeacher if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLessonTimetableTeacher = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLessonTimetableTeacher).toEqual(new LessonTimetableTeacher());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LessonTimetableTeacher })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLessonTimetableTeacher = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLessonTimetableTeacher).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
