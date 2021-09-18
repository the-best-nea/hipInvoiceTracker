import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentRegister, StudentRegister } from '../student-register.model';
import { StudentRegisterService } from '../service/student-register.service';

@Injectable({ providedIn: 'root' })
export class StudentRegisterRoutingResolveService implements Resolve<IStudentRegister> {
  constructor(protected service: StudentRegisterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentRegister> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((studentRegister: HttpResponse<StudentRegister>) => {
          if (studentRegister.body) {
            return of(studentRegister.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentRegister());
  }
}
