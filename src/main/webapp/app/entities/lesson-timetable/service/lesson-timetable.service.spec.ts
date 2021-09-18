import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { DayOfWeek } from 'app/entities/enumerations/day-of-week.model';
import { ILessonTimetable, LessonTimetable } from '../lesson-timetable.model';

import { LessonTimetableService } from './lesson-timetable.service';

describe('Service Tests', () => {
  describe('LessonTimetable Service', () => {
    let service: LessonTimetableService;
    let httpMock: HttpTestingController;
    let elemDefault: ILessonTimetable;
    let expectedResult: ILessonTimetable | ILessonTimetable[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LessonTimetableService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        lessonName: 'AAAAAAA',
        startAt: currentDate,
        endAt: currentDate,
        dayOfWeek: DayOfWeek.MONDAY,
        description: 'AAAAAAA',
        createdAt: currentDate,
        active: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startAt: currentDate.format(DATE_TIME_FORMAT),
            endAt: currentDate.format(DATE_TIME_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LessonTimetable', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startAt: currentDate.format(DATE_TIME_FORMAT),
            endAt: currentDate.format(DATE_TIME_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startAt: currentDate,
            endAt: currentDate,
            createdAt: currentDate,
          },
          returnedFromService
        );

        service.create(new LessonTimetable()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LessonTimetable', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            lessonName: 'BBBBBB',
            startAt: currentDate.format(DATE_TIME_FORMAT),
            endAt: currentDate.format(DATE_TIME_FORMAT),
            dayOfWeek: 'BBBBBB',
            description: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            active: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startAt: currentDate,
            endAt: currentDate,
            createdAt: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LessonTimetable', () => {
        const patchObject = Object.assign(
          {
            lessonName: 'BBBBBB',
            startAt: currentDate.format(DATE_TIME_FORMAT),
            active: true,
          },
          new LessonTimetable()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startAt: currentDate,
            endAt: currentDate,
            createdAt: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LessonTimetable', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            lessonName: 'BBBBBB',
            startAt: currentDate.format(DATE_TIME_FORMAT),
            endAt: currentDate.format(DATE_TIME_FORMAT),
            dayOfWeek: 'BBBBBB',
            description: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            active: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startAt: currentDate,
            endAt: currentDate,
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

      it('should delete a LessonTimetable', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLessonTimetableToCollectionIfMissing', () => {
        it('should add a LessonTimetable to an empty array', () => {
          const lessonTimetable: ILessonTimetable = { id: 123 };
          expectedResult = service.addLessonTimetableToCollectionIfMissing([], lessonTimetable);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lessonTimetable);
        });

        it('should not add a LessonTimetable to an array that contains it', () => {
          const lessonTimetable: ILessonTimetable = { id: 123 };
          const lessonTimetableCollection: ILessonTimetable[] = [
            {
              ...lessonTimetable,
            },
            { id: 456 },
          ];
          expectedResult = service.addLessonTimetableToCollectionIfMissing(lessonTimetableCollection, lessonTimetable);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LessonTimetable to an array that doesn't contain it", () => {
          const lessonTimetable: ILessonTimetable = { id: 123 };
          const lessonTimetableCollection: ILessonTimetable[] = [{ id: 456 }];
          expectedResult = service.addLessonTimetableToCollectionIfMissing(lessonTimetableCollection, lessonTimetable);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lessonTimetable);
        });

        it('should add only unique LessonTimetable to an array', () => {
          const lessonTimetableArray: ILessonTimetable[] = [{ id: 123 }, { id: 456 }, { id: 41224 }];
          const lessonTimetableCollection: ILessonTimetable[] = [{ id: 123 }];
          expectedResult = service.addLessonTimetableToCollectionIfMissing(lessonTimetableCollection, ...lessonTimetableArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lessonTimetable: ILessonTimetable = { id: 123 };
          const lessonTimetable2: ILessonTimetable = { id: 456 };
          expectedResult = service.addLessonTimetableToCollectionIfMissing([], lessonTimetable, lessonTimetable2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lessonTimetable);
          expect(expectedResult).toContain(lessonTimetable2);
        });

        it('should accept null and undefined values', () => {
          const lessonTimetable: ILessonTimetable = { id: 123 };
          expectedResult = service.addLessonTimetableToCollectionIfMissing([], null, lessonTimetable, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lessonTimetable);
        });

        it('should return initial array if no LessonTimetable is added', () => {
          const lessonTimetableCollection: ILessonTimetable[] = [{ id: 123 }];
          expectedResult = service.addLessonTimetableToCollectionIfMissing(lessonTimetableCollection, undefined, null);
          expect(expectedResult).toEqual(lessonTimetableCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
