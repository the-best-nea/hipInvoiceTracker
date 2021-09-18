import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISubject, Subject } from '../subject.model';

import { SubjectService } from './subject.service';

describe('Service Tests', () => {
  describe('Subject Service', () => {
    let service: SubjectService;
    let httpMock: HttpTestingController;
    let elemDefault: ISubject;
    let expectedResult: ISubject | ISubject[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SubjectService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        subjectName: 'AAAAAAA',
        description: 'AAAAAAA',
        createdAt: currentDate,
        active: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Subject', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
          },
          returnedFromService
        );

        service.create(new Subject()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Subject', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            subjectName: 'BBBBBB',
            description: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            active: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Subject', () => {
        const patchObject = Object.assign(
          {
            subjectName: 'BBBBBB',
            description: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
          },
          new Subject()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdAt: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Subject', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            subjectName: 'BBBBBB',
            description: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            active: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Subject', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSubjectToCollectionIfMissing', () => {
        it('should add a Subject to an empty array', () => {
          const subject: ISubject = { id: 123 };
          expectedResult = service.addSubjectToCollectionIfMissing([], subject);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subject);
        });

        it('should not add a Subject to an array that contains it', () => {
          const subject: ISubject = { id: 123 };
          const subjectCollection: ISubject[] = [
            {
              ...subject,
            },
            { id: 456 },
          ];
          expectedResult = service.addSubjectToCollectionIfMissing(subjectCollection, subject);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Subject to an array that doesn't contain it", () => {
          const subject: ISubject = { id: 123 };
          const subjectCollection: ISubject[] = [{ id: 456 }];
          expectedResult = service.addSubjectToCollectionIfMissing(subjectCollection, subject);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subject);
        });

        it('should add only unique Subject to an array', () => {
          const subjectArray: ISubject[] = [{ id: 123 }, { id: 456 }, { id: 70733 }];
          const subjectCollection: ISubject[] = [{ id: 123 }];
          expectedResult = service.addSubjectToCollectionIfMissing(subjectCollection, ...subjectArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const subject: ISubject = { id: 123 };
          const subject2: ISubject = { id: 456 };
          expectedResult = service.addSubjectToCollectionIfMissing([], subject, subject2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subject);
          expect(expectedResult).toContain(subject2);
        });

        it('should accept null and undefined values', () => {
          const subject: ISubject = { id: 123 };
          expectedResult = service.addSubjectToCollectionIfMissing([], null, subject, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subject);
        });

        it('should return initial array if no Subject is added', () => {
          const subjectCollection: ISubject[] = [{ id: 123 }];
          expectedResult = service.addSubjectToCollectionIfMissing(subjectCollection, undefined, null);
          expect(expectedResult).toEqual(subjectCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
