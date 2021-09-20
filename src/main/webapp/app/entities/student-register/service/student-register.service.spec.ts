import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IStudentRegister, StudentRegister } from '../student-register.model';

import { StudentRegisterService } from './student-register.service';

describe('Service Tests', () => {
  describe('StudentRegister Service', () => {
    let service: StudentRegisterService;
    let httpMock: HttpTestingController;
    let elemDefault: IStudentRegister;
    let expectedResult: IStudentRegister | IStudentRegister[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StudentRegisterService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateOfLesson: currentDate,
        pay: 0,
        attended: false,
        createdOn: currentDate,
        updatedOn: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateOfLesson: currentDate.format(DATE_TIME_FORMAT),
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            updatedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a StudentRegister', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateOfLesson: currentDate.format(DATE_TIME_FORMAT),
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            updatedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfLesson: currentDate,
            createdOn: currentDate,
            updatedOn: currentDate,
          },
          returnedFromService
        );

        service.create(new StudentRegister()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a StudentRegister', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateOfLesson: currentDate.format(DATE_TIME_FORMAT),
            pay: 1,
            attended: true,
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            updatedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfLesson: currentDate,
            createdOn: currentDate,
            updatedOn: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a StudentRegister', () => {
        const patchObject = Object.assign(
          {
            pay: 1,
            attended: true,
          },
          new StudentRegister()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateOfLesson: currentDate,
            createdOn: currentDate,
            updatedOn: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of StudentRegister', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateOfLesson: currentDate.format(DATE_TIME_FORMAT),
            pay: 1,
            attended: true,
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            updatedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfLesson: currentDate,
            createdOn: currentDate,
            updatedOn: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a StudentRegister', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStudentRegisterToCollectionIfMissing', () => {
        it('should add a StudentRegister to an empty array', () => {
          const studentRegister: IStudentRegister = { id: 123 };
          expectedResult = service.addStudentRegisterToCollectionIfMissing([], studentRegister);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentRegister);
        });

        it('should not add a StudentRegister to an array that contains it', () => {
          const studentRegister: IStudentRegister = { id: 123 };
          const studentRegisterCollection: IStudentRegister[] = [
            {
              ...studentRegister,
            },
            { id: 456 },
          ];
          expectedResult = service.addStudentRegisterToCollectionIfMissing(studentRegisterCollection, studentRegister);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a StudentRegister to an array that doesn't contain it", () => {
          const studentRegister: IStudentRegister = { id: 123 };
          const studentRegisterCollection: IStudentRegister[] = [{ id: 456 }];
          expectedResult = service.addStudentRegisterToCollectionIfMissing(studentRegisterCollection, studentRegister);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentRegister);
        });

        it('should add only unique StudentRegister to an array', () => {
          const studentRegisterArray: IStudentRegister[] = [{ id: 123 }, { id: 456 }, { id: 78074 }];
          const studentRegisterCollection: IStudentRegister[] = [{ id: 123 }];
          expectedResult = service.addStudentRegisterToCollectionIfMissing(studentRegisterCollection, ...studentRegisterArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const studentRegister: IStudentRegister = { id: 123 };
          const studentRegister2: IStudentRegister = { id: 456 };
          expectedResult = service.addStudentRegisterToCollectionIfMissing([], studentRegister, studentRegister2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentRegister);
          expect(expectedResult).toContain(studentRegister2);
        });

        it('should accept null and undefined values', () => {
          const studentRegister: IStudentRegister = { id: 123 };
          expectedResult = service.addStudentRegisterToCollectionIfMissing([], null, studentRegister, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentRegister);
        });

        it('should return initial array if no StudentRegister is added', () => {
          const studentRegisterCollection: IStudentRegister[] = [{ id: 123 }];
          expectedResult = service.addStudentRegisterToCollectionIfMissing(studentRegisterCollection, undefined, null);
          expect(expectedResult).toEqual(studentRegisterCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
