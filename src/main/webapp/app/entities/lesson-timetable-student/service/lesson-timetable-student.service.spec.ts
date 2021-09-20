import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILessonTimetableStudent, LessonTimetableStudent } from '../lesson-timetable-student.model';

import { LessonTimetableStudentService } from './lesson-timetable-student.service';

describe('Service Tests', () => {
  describe('LessonTimetableStudent Service', () => {
    let service: LessonTimetableStudentService;
    let httpMock: HttpTestingController;
    let elemDefault: ILessonTimetableStudent;
    let expectedResult: ILessonTimetableStudent | ILessonTimetableStudent[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LessonTimetableStudentService);
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

      it('should create a LessonTimetableStudent', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LessonTimetableStudent()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LessonTimetableStudent', () => {
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

      it('should partial update a LessonTimetableStudent', () => {
        const patchObject = Object.assign(
          {
            pay: 1,
          },
          new LessonTimetableStudent()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LessonTimetableStudent', () => {
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

      it('should delete a LessonTimetableStudent', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLessonTimetableStudentToCollectionIfMissing', () => {
        it('should add a LessonTimetableStudent to an empty array', () => {
          const lessonTimetableStudent: ILessonTimetableStudent = { id: 123 };
          expectedResult = service.addLessonTimetableStudentToCollectionIfMissing([], lessonTimetableStudent);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lessonTimetableStudent);
        });

        it('should not add a LessonTimetableStudent to an array that contains it', () => {
          const lessonTimetableStudent: ILessonTimetableStudent = { id: 123 };
          const lessonTimetableStudentCollection: ILessonTimetableStudent[] = [
            {
              ...lessonTimetableStudent,
            },
            { id: 456 },
          ];
          expectedResult = service.addLessonTimetableStudentToCollectionIfMissing(lessonTimetableStudentCollection, lessonTimetableStudent);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LessonTimetableStudent to an array that doesn't contain it", () => {
          const lessonTimetableStudent: ILessonTimetableStudent = { id: 123 };
          const lessonTimetableStudentCollection: ILessonTimetableStudent[] = [{ id: 456 }];
          expectedResult = service.addLessonTimetableStudentToCollectionIfMissing(lessonTimetableStudentCollection, lessonTimetableStudent);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lessonTimetableStudent);
        });

        it('should add only unique LessonTimetableStudent to an array', () => {
          const lessonTimetableStudentArray: ILessonTimetableStudent[] = [{ id: 123 }, { id: 456 }, { id: 69186 }];
          const lessonTimetableStudentCollection: ILessonTimetableStudent[] = [{ id: 123 }];
          expectedResult = service.addLessonTimetableStudentToCollectionIfMissing(
            lessonTimetableStudentCollection,
            ...lessonTimetableStudentArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lessonTimetableStudent: ILessonTimetableStudent = { id: 123 };
          const lessonTimetableStudent2: ILessonTimetableStudent = { id: 456 };
          expectedResult = service.addLessonTimetableStudentToCollectionIfMissing([], lessonTimetableStudent, lessonTimetableStudent2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lessonTimetableStudent);
          expect(expectedResult).toContain(lessonTimetableStudent2);
        });

        it('should accept null and undefined values', () => {
          const lessonTimetableStudent: ILessonTimetableStudent = { id: 123 };
          expectedResult = service.addLessonTimetableStudentToCollectionIfMissing([], null, lessonTimetableStudent, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lessonTimetableStudent);
        });

        it('should return initial array if no LessonTimetableStudent is added', () => {
          const lessonTimetableStudentCollection: ILessonTimetableStudent[] = [{ id: 123 }];
          expectedResult = service.addLessonTimetableStudentToCollectionIfMissing(lessonTimetableStudentCollection, undefined, null);
          expect(expectedResult).toEqual(lessonTimetableStudentCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
