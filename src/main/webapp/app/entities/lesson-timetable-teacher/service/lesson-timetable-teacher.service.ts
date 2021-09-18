import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILessonTimetableTeacher, getLessonTimetableTeacherIdentifier } from '../lesson-timetable-teacher.model';

export type EntityResponseType = HttpResponse<ILessonTimetableTeacher>;
export type EntityArrayResponseType = HttpResponse<ILessonTimetableTeacher[]>;

@Injectable({ providedIn: 'root' })
export class LessonTimetableTeacherService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lesson-timetable-teachers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lessonTimetableTeacher: ILessonTimetableTeacher): Observable<EntityResponseType> {
    return this.http.post<ILessonTimetableTeacher>(this.resourceUrl, lessonTimetableTeacher, { observe: 'response' });
  }

  update(lessonTimetableTeacher: ILessonTimetableTeacher): Observable<EntityResponseType> {
    return this.http.put<ILessonTimetableTeacher>(
      `${this.resourceUrl}/${getLessonTimetableTeacherIdentifier(lessonTimetableTeacher) as number}`,
      lessonTimetableTeacher,
      { observe: 'response' }
    );
  }

  partialUpdate(lessonTimetableTeacher: ILessonTimetableTeacher): Observable<EntityResponseType> {
    return this.http.patch<ILessonTimetableTeacher>(
      `${this.resourceUrl}/${getLessonTimetableTeacherIdentifier(lessonTimetableTeacher) as number}`,
      lessonTimetableTeacher,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILessonTimetableTeacher>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILessonTimetableTeacher[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLessonTimetableTeacherToCollectionIfMissing(
    lessonTimetableTeacherCollection: ILessonTimetableTeacher[],
    ...lessonTimetableTeachersToCheck: (ILessonTimetableTeacher | null | undefined)[]
  ): ILessonTimetableTeacher[] {
    const lessonTimetableTeachers: ILessonTimetableTeacher[] = lessonTimetableTeachersToCheck.filter(isPresent);
    if (lessonTimetableTeachers.length > 0) {
      const lessonTimetableTeacherCollectionIdentifiers = lessonTimetableTeacherCollection.map(
        lessonTimetableTeacherItem => getLessonTimetableTeacherIdentifier(lessonTimetableTeacherItem)!
      );
      const lessonTimetableTeachersToAdd = lessonTimetableTeachers.filter(lessonTimetableTeacherItem => {
        const lessonTimetableTeacherIdentifier = getLessonTimetableTeacherIdentifier(lessonTimetableTeacherItem);
        if (
          lessonTimetableTeacherIdentifier == null ||
          lessonTimetableTeacherCollectionIdentifiers.includes(lessonTimetableTeacherIdentifier)
        ) {
          return false;
        }
        lessonTimetableTeacherCollectionIdentifiers.push(lessonTimetableTeacherIdentifier);
        return true;
      });
      return [...lessonTimetableTeachersToAdd, ...lessonTimetableTeacherCollection];
    }
    return lessonTimetableTeacherCollection;
  }
}
