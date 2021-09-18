import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILessonInstance, getLessonInstanceIdentifier } from '../lesson-instance.model';

export type EntityResponseType = HttpResponse<ILessonInstance>;
export type EntityArrayResponseType = HttpResponse<ILessonInstance[]>;

@Injectable({ providedIn: 'root' })
export class LessonInstanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lesson-instances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lessonInstance: ILessonInstance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lessonInstance);
    return this.http
      .post<ILessonInstance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lessonInstance: ILessonInstance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lessonInstance);
    return this.http
      .put<ILessonInstance>(`${this.resourceUrl}/${getLessonInstanceIdentifier(lessonInstance) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lessonInstance: ILessonInstance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lessonInstance);
    return this.http
      .patch<ILessonInstance>(`${this.resourceUrl}/${getLessonInstanceIdentifier(lessonInstance) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILessonInstance>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILessonInstance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLessonInstanceToCollectionIfMissing(
    lessonInstanceCollection: ILessonInstance[],
    ...lessonInstancesToCheck: (ILessonInstance | null | undefined)[]
  ): ILessonInstance[] {
    const lessonInstances: ILessonInstance[] = lessonInstancesToCheck.filter(isPresent);
    if (lessonInstances.length > 0) {
      const lessonInstanceCollectionIdentifiers = lessonInstanceCollection.map(
        lessonInstanceItem => getLessonInstanceIdentifier(lessonInstanceItem)!
      );
      const lessonInstancesToAdd = lessonInstances.filter(lessonInstanceItem => {
        const lessonInstanceIdentifier = getLessonInstanceIdentifier(lessonInstanceItem);
        if (lessonInstanceIdentifier == null || lessonInstanceCollectionIdentifiers.includes(lessonInstanceIdentifier)) {
          return false;
        }
        lessonInstanceCollectionIdentifiers.push(lessonInstanceIdentifier);
        return true;
      });
      return [...lessonInstancesToAdd, ...lessonInstanceCollection];
    }
    return lessonInstanceCollection;
  }

  protected convertDateFromClient(lessonInstance: ILessonInstance): ILessonInstance {
    return Object.assign({}, lessonInstance, {
      startAt: lessonInstance.startAt?.isValid() ? lessonInstance.startAt.toJSON() : undefined,
      endAt: lessonInstance.endAt?.isValid() ? lessonInstance.endAt.toJSON() : undefined,
      cretedOn: lessonInstance.cretedOn?.isValid() ? lessonInstance.cretedOn.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startAt = res.body.startAt ? dayjs(res.body.startAt) : undefined;
      res.body.endAt = res.body.endAt ? dayjs(res.body.endAt) : undefined;
      res.body.cretedOn = res.body.cretedOn ? dayjs(res.body.cretedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((lessonInstance: ILessonInstance) => {
        lessonInstance.startAt = lessonInstance.startAt ? dayjs(lessonInstance.startAt) : undefined;
        lessonInstance.endAt = lessonInstance.endAt ? dayjs(lessonInstance.endAt) : undefined;
        lessonInstance.cretedOn = lessonInstance.cretedOn ? dayjs(lessonInstance.cretedOn) : undefined;
      });
    }
    return res;
  }
}
