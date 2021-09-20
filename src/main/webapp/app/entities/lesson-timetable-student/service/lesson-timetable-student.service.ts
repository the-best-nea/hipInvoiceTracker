import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILessonTimetableStudent, getLessonTimetableStudentIdentifier } from '../lesson-timetable-student.model';

export type EntityResponseType = HttpResponse<ILessonTimetableStudent>;
export type EntityArrayResponseType = HttpResponse<ILessonTimetableStudent[]>;

@Injectable({ providedIn: 'root' })
export class LessonTimetableStudentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lesson-timetable-students');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lessonTimetableStudent: ILessonTimetableStudent): Observable<EntityResponseType> {
    return this.http.post<ILessonTimetableStudent>(this.resourceUrl, lessonTimetableStudent, { observe: 'response' });
  }

  update(lessonTimetableStudent: ILessonTimetableStudent): Observable<EntityResponseType> {
    return this.http.put<ILessonTimetableStudent>(
      `${this.resourceUrl}/${getLessonTimetableStudentIdentifier(lessonTimetableStudent) as number}`,
      lessonTimetableStudent,
      { observe: 'response' }
    );
  }

  partialUpdate(lessonTimetableStudent: ILessonTimetableStudent): Observable<EntityResponseType> {
    return this.http.patch<ILessonTimetableStudent>(
      `${this.resourceUrl}/${getLessonTimetableStudentIdentifier(lessonTimetableStudent) as number}`,
      lessonTimetableStudent,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILessonTimetableStudent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILessonTimetableStudent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLessonTimetableStudentToCollectionIfMissing(
    lessonTimetableStudentCollection: ILessonTimetableStudent[],
    ...lessonTimetableStudentsToCheck: (ILessonTimetableStudent | null | undefined)[]
  ): ILessonTimetableStudent[] {
    const lessonTimetableStudents: ILessonTimetableStudent[] = lessonTimetableStudentsToCheck.filter(isPresent);
    if (lessonTimetableStudents.length > 0) {
      const lessonTimetableStudentCollectionIdentifiers = lessonTimetableStudentCollection.map(
        lessonTimetableStudentItem => getLessonTimetableStudentIdentifier(lessonTimetableStudentItem)!
      );
      const lessonTimetableStudentsToAdd = lessonTimetableStudents.filter(lessonTimetableStudentItem => {
        const lessonTimetableStudentIdentifier = getLessonTimetableStudentIdentifier(lessonTimetableStudentItem);
        if (
          lessonTimetableStudentIdentifier == null ||
          lessonTimetableStudentCollectionIdentifiers.includes(lessonTimetableStudentIdentifier)
        ) {
          return false;
        }
        lessonTimetableStudentCollectionIdentifiers.push(lessonTimetableStudentIdentifier);
        return true;
      });
      return [...lessonTimetableStudentsToAdd, ...lessonTimetableStudentCollection];
    }
    return lessonTimetableStudentCollection;
  }
}
