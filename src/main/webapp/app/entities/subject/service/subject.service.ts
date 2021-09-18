import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubject, getSubjectIdentifier } from '../subject.model';

export type EntityResponseType = HttpResponse<ISubject>;
export type EntityArrayResponseType = HttpResponse<ISubject[]>;

@Injectable({ providedIn: 'root' })
export class SubjectService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/subjects');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(subject: ISubject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subject);
    return this.http
      .post<ISubject>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(subject: ISubject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subject);
    return this.http
      .put<ISubject>(`${this.resourceUrl}/${getSubjectIdentifier(subject) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(subject: ISubject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subject);
    return this.http
      .patch<ISubject>(`${this.resourceUrl}/${getSubjectIdentifier(subject) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISubject>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISubject[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSubjectToCollectionIfMissing(subjectCollection: ISubject[], ...subjectsToCheck: (ISubject | null | undefined)[]): ISubject[] {
    const subjects: ISubject[] = subjectsToCheck.filter(isPresent);
    if (subjects.length > 0) {
      const subjectCollectionIdentifiers = subjectCollection.map(subjectItem => getSubjectIdentifier(subjectItem)!);
      const subjectsToAdd = subjects.filter(subjectItem => {
        const subjectIdentifier = getSubjectIdentifier(subjectItem);
        if (subjectIdentifier == null || subjectCollectionIdentifiers.includes(subjectIdentifier)) {
          return false;
        }
        subjectCollectionIdentifiers.push(subjectIdentifier);
        return true;
      });
      return [...subjectsToAdd, ...subjectCollection];
    }
    return subjectCollection;
  }

  protected convertDateFromClient(subject: ISubject): ISubject {
    return Object.assign({}, subject, {
      createdAt: subject.createdAt?.isValid() ? subject.createdAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((subject: ISubject) => {
        subject.createdAt = subject.createdAt ? dayjs(subject.createdAt) : undefined;
      });
    }
    return res;
  }
}
