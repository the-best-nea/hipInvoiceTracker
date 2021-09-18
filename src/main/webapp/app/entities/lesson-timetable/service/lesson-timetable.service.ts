import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILessonTimetable, getLessonTimetableIdentifier } from '../lesson-timetable.model';

export type EntityResponseType = HttpResponse<ILessonTimetable>;
export type EntityArrayResponseType = HttpResponse<ILessonTimetable[]>;

@Injectable({ providedIn: 'root' })
export class LessonTimetableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lesson-timetables');
  protected createInstanceResourceUrl = this.applicationConfigService.getEndpointFor('api/lesson-timetables/create-instance')

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lessonTimetable: ILessonTimetable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lessonTimetable);
    return this.http
      .post<ILessonTimetable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  createInstance(id: number): void {
       this.http.post(`${this.createInstanceResourceUrl}/${id}`, { observe: 'response' });
  }

  update(lessonTimetable: ILessonTimetable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lessonTimetable);
    return this.http
      .put<ILessonTimetable>(`${this.resourceUrl}/${getLessonTimetableIdentifier(lessonTimetable) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lessonTimetable: ILessonTimetable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lessonTimetable);
    return this.http
      .patch<ILessonTimetable>(`${this.resourceUrl}/${getLessonTimetableIdentifier(lessonTimetable) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILessonTimetable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILessonTimetable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }



  addLessonTimetableToCollectionIfMissing(
    lessonTimetableCollection: ILessonTimetable[],
    ...lessonTimetablesToCheck: (ILessonTimetable | null | undefined)[]
  ): ILessonTimetable[] {
    const lessonTimetables: ILessonTimetable[] = lessonTimetablesToCheck.filter(isPresent);
    if (lessonTimetables.length > 0) {
      const lessonTimetableCollectionIdentifiers = lessonTimetableCollection.map(
        lessonTimetableItem => getLessonTimetableIdentifier(lessonTimetableItem)!
      );
      const lessonTimetablesToAdd = lessonTimetables.filter(lessonTimetableItem => {
        const lessonTimetableIdentifier = getLessonTimetableIdentifier(lessonTimetableItem);
        if (lessonTimetableIdentifier == null || lessonTimetableCollectionIdentifiers.includes(lessonTimetableIdentifier)) {
          return false;
        }
        lessonTimetableCollectionIdentifiers.push(lessonTimetableIdentifier);
        return true;
      });
      return [...lessonTimetablesToAdd, ...lessonTimetableCollection];
    }
    return lessonTimetableCollection;
  }

  protected convertDateFromClient(lessonTimetable: ILessonTimetable): ILessonTimetable {
    return Object.assign({}, lessonTimetable, {
      startAt: lessonTimetable.startAt?.isValid() ? lessonTimetable.startAt.toJSON() : undefined,
      endAt: lessonTimetable.endAt?.isValid() ? lessonTimetable.endAt.toJSON() : undefined,
      createdAt: lessonTimetable.createdAt?.isValid() ? lessonTimetable.createdAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startAt = res.body.startAt ? dayjs(res.body.startAt) : undefined;
      res.body.endAt = res.body.endAt ? dayjs(res.body.endAt) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((lessonTimetable: ILessonTimetable) => {
        lessonTimetable.startAt = lessonTimetable.startAt ? dayjs(lessonTimetable.startAt) : undefined;
        lessonTimetable.endAt = lessonTimetable.endAt ? dayjs(lessonTimetable.endAt) : undefined;
        lessonTimetable.createdAt = lessonTimetable.createdAt ? dayjs(lessonTimetable.createdAt) : undefined;
      });
    }
    return res;
  }
}
