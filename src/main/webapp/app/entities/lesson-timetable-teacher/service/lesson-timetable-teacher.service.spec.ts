import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILessonTimetableTeacher, LessonTimetableTeacher } from '../lesson-timetable-teacher.model';

import { LessonTimetableTeacherService } from './lesson-timetable-teacher.service';

describe('Service Tests', () => {
  describe('LessonTimetableTeacher Service', () => {
    let service: LessonTimetableTeacherService;
    let httpMock: HttpTestingController;
    let elemDefault: ILessonTimetableTeacher;
    let expectedResult: ILessonTimetableTeacher | ILessonTimetableTeacher[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LessonTimetableTeacherService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        pay: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LessonTimetableTeacher', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LessonTimetableTeacher()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LessonTimetableTeacher', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pay: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LessonTimetableTeacher', () => {
        const patchObject = Object.assign({}, new LessonTimetableTeacher());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LessonTimetableTeacher', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pay: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LessonTimetableTeacher', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLessonTimetableTeacherToCollectionIfMissing', () => {
        it('should add a LessonTimetableTeacher to an empty array', () => {
          const lessonTimetableTeacher: ILessonTimetableTeacher = { id: 123 };
          expectedResult = service.addLessonTimetableTeacherToCollectionIfMissing([], lessonTimetableTeacher);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lessonTimetableTeacher);
        });

        it('should not add a LessonTimetableTeacher to an array that contains it', () => {
          const lessonTimetableTeacher: ILessonTimetableTeacher = { id: 123 };
          const lessonTimetableTeacherCollection: ILessonTimetableTeacher[] = [
            {
              ...lessonTimetableTeacher,
            },
            { id: 456 },
          ];
          expectedResult = service.addLessonTimetableTeacherToCollectionIfMissing(lessonTimetableTeacherCollection, lessonTimetableTeacher);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LessonTimetableTeacher to an array that doesn't contain it", () => {
          const lessonTimetableTeacher: ILessonTimetableTeacher = { id: 123 };
          const lessonTimetableTeacherCollection: ILessonTimetableTeacher[] = [{ id: 456 }];
          expectedResult = service.addLessonTimetableTeacherToCollectionIfMissing(lessonTimetableTeacherCollection, lessonTimetableTeacher);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lessonTimetableTeacher);
        });

        it('should add only unique LessonTimetableTeacher to an array', () => {
          const lessonTimetableTeacherArray: ILessonTimetableTeacher[] = [{ id: 123 }, { id: 456 }, { id: 53787 }];
          const lessonTimetableTeacherCollection: ILessonTimetableTeacher[] = [{ id: 123 }];
          expectedResult = service.addLessonTimetableTeacherToCollectionIfMissing(
            lessonTimetableTeacherCollection,
            ...lessonTimetableTeacherArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lessonTimetableTeacher: ILessonTimetableTeacher = { id: 123 };
          const lessonTimetableTeacher2: ILessonTimetableTeacher = { id: 456 };
          expectedResult = service.addLessonTimetableTeacherToCollectionIfMissing([], lessonTimetableTeacher, lessonTimetableTeacher2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lessonTimetableTeacher);
          expect(expectedResult).toContain(lessonTimetableTeacher2);
        });

        it('should accept null and undefined values', () => {
          const lessonTimetableTeacher: ILessonTimetableTeacher = { id: 123 };
          expectedResult = service.addLessonTimetableTeacherToCollectionIfMissing([], null, lessonTimetableTeacher, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lessonTimetableTeacher);
        });

        it('should return initial array if no LessonTimetableTeacher is added', () => {
          const lessonTimetableTeacherCollection: ILessonTimetableTeacher[] = [{ id: 123 }];
          expectedResult = service.addLessonTimetableTeacherToCollectionIfMissing(lessonTimetableTeacherCollection, undefined, null);
          expect(expectedResult).toEqual(lessonTimetableTeacherCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
