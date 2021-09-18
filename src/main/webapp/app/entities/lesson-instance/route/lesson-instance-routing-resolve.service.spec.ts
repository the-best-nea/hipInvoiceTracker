jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILessonInstance, LessonInstance } from '../lesson-instance.model';
import { LessonInstanceService } from '../service/lesson-instance.service';

import { LessonInstanceRoutingResolveService } from './lesson-instance-routing-resolve.service';

describe('Service Tests', () => {
  describe('LessonInstance routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LessonInstanceRoutingResolveService;
    let service: LessonInstanceService;
    let resultLessonInstance: ILessonInstance | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LessonInstanceRoutingResolveService);
      service = TestBed.inject(LessonInstanceService);
      resultLessonInstance = undefined;
    });

    describe('resolve', () => {
      it('should return ILessonInstance returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLessonInstance = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLessonInstance).toEqual({ id: 123 });
      });

      it('should return new ILessonInstance if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLessonInstance = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLessonInstance).toEqual(new LessonInstance());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LessonInstance })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLessonInstance = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLessonInstance).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
