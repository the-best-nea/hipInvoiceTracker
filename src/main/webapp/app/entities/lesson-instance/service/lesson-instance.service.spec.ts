import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { DayOfWeek } from 'app/entities/enumerations/day-of-week.model';
import { ILessonInstance, LessonInstance } from '../lesson-instance.model';

import { LessonInstanceService } from './lesson-instance.service';

describe('Service Tests', () => {
  describe('LessonInstance Service', () => {
    let service: LessonInstanceService;
    let httpMock: HttpTestingController;
    let elemDefault: ILessonInstance;
    let expectedResult: ILessonInstance | ILessonInstance[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LessonInstanceService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        lessonName: 'AAAAAAA',
        startAt: currentDate,
        endAt: currentDate,
        dayOfWeek: DayOfWeek.MONDAY,
        description: 'AAAAAAA',
        cretedOn: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startAt: currentDate.format(DATE_TIME_FORMAT),
            endAt: currentDate.format(DATE_TIME_FORMAT),
            cretedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LessonInstance', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startAt: currentDate.format(DATE_TIME_FORMAT),
            endAt: currentDate.format(DATE_TIME_FORMAT),
            cretedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startAt: currentDate,
            endAt: currentDate,
            cretedOn: currentDate,
          },
          returnedFromService
        );

        service.create(new LessonInstance()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LessonInstance', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            lessonName: 'BBBBBB',
            startAt: currentDate.format(DATE_TIME_FORMAT),
            endAt: currentDate.format(DATE_TIME_FORMAT),
            dayOfWeek: 'BBBBBB',
            description: 'BBBBBB',
            cretedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startAt: currentDate,
            endAt: currentDate,
            cretedOn: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LessonInstance', () => {
        const patchObject = Object.assign(
          {
            lessonName: 'BBBBBB',
            dayOfWeek: 'BBBBBB',
            description: 'BBBBBB',
            cretedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          new LessonInstance()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startAt: currentDate,
            endAt: currentDate,
            cretedOn: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LessonInstance', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            lessonName: 'BBBBBB',
            startAt: currentDate.format(DATE_TIME_FORMAT),
            endAt: currentDate.format(DATE_TIME_FORMAT),
            dayOfWeek: 'BBBBBB',
            description: 'BBBBBB',
            cretedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startAt: currentDate,
            endAt: currentDate,
            cretedOn: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LessonInstance', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLessonInstanceToCollectionIfMissing', () => {
        it('should add a LessonInstance to an empty array', () => {
          const lessonInstance: ILessonInstance = { id: 123 };
          expectedResult = service.addLessonInstanceToCollectionIfMissing([], lessonInstance);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lessonInstance);
        });

        it('should not add a LessonInstance to an array that contains it', () => {
          const lessonInstance: ILessonInstance = { id: 123 };
          const lessonInstanceCollection: ILessonInstance[] = [
            {
              ...lessonInstance,
            },
            { id: 456 },
          ];
          expectedResult = service.addLessonInstanceToCollectionIfMissing(lessonInstanceCollection, lessonInstance);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LessonInstance to an array that doesn't contain it", () => {
          const lessonInstance: ILessonInstance = { id: 123 };
          const lessonInstanceCollection: ILessonInstance[] = [{ id: 456 }];
          expectedResult = service.addLessonInstanceToCollectionIfMissing(lessonInstanceCollection, lessonInstance);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lessonInstance);
        });

        it('should add only unique LessonInstance to an array', () => {
          const lessonInstanceArray: ILessonInstance[] = [{ id: 123 }, { id: 456 }, { id: 54132 }];
          const lessonInstanceCollection: ILessonInstance[] = [{ id: 123 }];
          expectedResult = service.addLessonInstanceToCollectionIfMissing(lessonInstanceCollection, ...lessonInstanceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lessonInstance: ILessonInstance = { id: 123 };
          const lessonInstance2: ILessonInstance = { id: 456 };
          expectedResult = service.addLessonInstanceToCollectionIfMissing([], lessonInstance, lessonInstance2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lessonInstance);
          expect(expectedResult).toContain(lessonInstance2);
        });

        it('should accept null and undefined values', () => {
          const lessonInstance: ILessonInstance = { id: 123 };
          expectedResult = service.addLessonInstanceToCollectionIfMissing([], null, lessonInstance, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lessonInstance);
        });

        it('should return initial array if no LessonInstance is added', () => {
          const lessonInstanceCollection: ILessonInstance[] = [{ id: 123 }];
          expectedResult = service.addLessonInstanceToCollectionIfMissing(lessonInstanceCollection, undefined, null);
          expect(expectedResult).toEqual(lessonInstanceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
