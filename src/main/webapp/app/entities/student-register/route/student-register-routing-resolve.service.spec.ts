jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStudentRegister, StudentRegister } from '../student-register.model';
import { StudentRegisterService } from '../service/student-register.service';

import { StudentRegisterRoutingResolveService } from './student-register-routing-resolve.service';

describe('Service Tests', () => {
  describe('StudentRegister routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: StudentRegisterRoutingResolveService;
    let service: StudentRegisterService;
    let resultStudentRegister: IStudentRegister | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(StudentRegisterRoutingResolveService);
      service = TestBed.inject(StudentRegisterService);
      resultStudentRegister = undefined;
    });

    describe('resolve', () => {
      it('should return IStudentRegister returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentRegister = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentRegister).toEqual({ id: 123 });
      });

      it('should return new IStudentRegister if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentRegister = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultStudentRegister).toEqual(new StudentRegister());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as StudentRegister })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudentRegister = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudentRegister).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
