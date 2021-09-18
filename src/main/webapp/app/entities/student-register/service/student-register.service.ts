import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudentRegister, getStudentRegisterIdentifier } from '../student-register.model';

export type EntityResponseType = HttpResponse<IStudentRegister>;
export type EntityArrayResponseType = HttpResponse<IStudentRegister[]>;

@Injectable({ providedIn: 'root' })
export class StudentRegisterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/student-registers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(studentRegister: IStudentRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentRegister);
    return this.http
      .post<IStudentRegister>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(studentRegister: IStudentRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentRegister);
    return this.http
      .put<IStudentRegister>(`${this.resourceUrl}/${getStudentRegisterIdentifier(studentRegister) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(studentRegister: IStudentRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentRegister);
    return this.http
      .patch<IStudentRegister>(`${this.resourceUrl}/${getStudentRegisterIdentifier(studentRegister) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudentRegister>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudentRegister[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudentRegisterToCollectionIfMissing(
    studentRegisterCollection: IStudentRegister[],
    ...studentRegistersToCheck: (IStudentRegister | null | undefined)[]
  ): IStudentRegister[] {
    const studentRegisters: IStudentRegister[] = studentRegistersToCheck.filter(isPresent);
    if (studentRegisters.length > 0) {
      const studentRegisterCollectionIdentifiers = studentRegisterCollection.map(
        studentRegisterItem => getStudentRegisterIdentifier(studentRegisterItem)!
      );
      const studentRegistersToAdd = studentRegisters.filter(studentRegisterItem => {
        const studentRegisterIdentifier = getStudentRegisterIdentifier(studentRegisterItem);
        if (studentRegisterIdentifier == null || studentRegisterCollectionIdentifiers.includes(studentRegisterIdentifier)) {
          return false;
        }
        studentRegisterCollectionIdentifiers.push(studentRegisterIdentifier);
        return true;
      });
      return [...studentRegistersToAdd, ...studentRegisterCollection];
    }
    return studentRegisterCollection;
  }

  protected convertDateFromClient(studentRegister: IStudentRegister): IStudentRegister {
    return Object.assign({}, studentRegister, {
      dateOfLesson: studentRegister.dateOfLesson?.isValid() ? studentRegister.dateOfLesson.toJSON() : undefined,
      createdOn: studentRegister.createdOn?.isValid() ? studentRegister.createdOn.toJSON() : undefined,
      updatedOn: studentRegister.updatedOn?.isValid() ? studentRegister.updatedOn.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfLesson = res.body.dateOfLesson ? dayjs(res.body.dateOfLesson) : undefined;
      res.body.createdOn = res.body.createdOn ? dayjs(res.body.createdOn) : undefined;
      res.body.updatedOn = res.body.updatedOn ? dayjs(res.body.updatedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((studentRegister: IStudentRegister) => {
        studentRegister.dateOfLesson = studentRegister.dateOfLesson ? dayjs(studentRegister.dateOfLesson) : undefined;
        studentRegister.createdOn = studentRegister.createdOn ? dayjs(studentRegister.createdOn) : undefined;
        studentRegister.updatedOn = studentRegister.updatedOn ? dayjs(studentRegister.updatedOn) : undefined;
      });
    }
    return res;
  }
}
