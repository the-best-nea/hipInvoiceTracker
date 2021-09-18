import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILessonInstance, LessonInstance } from '../lesson-instance.model';
import { LessonInstanceService } from '../service/lesson-instance.service';

@Injectable({ providedIn: 'root' })
export class LessonInstanceRoutingResolveService implements Resolve<ILessonInstance> {
  constructor(protected service: LessonInstanceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILessonInstance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lessonInstance: HttpResponse<LessonInstance>) => {
          if (lessonInstance.body) {
            return of(lessonInstance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LessonInstance());
  }
}
