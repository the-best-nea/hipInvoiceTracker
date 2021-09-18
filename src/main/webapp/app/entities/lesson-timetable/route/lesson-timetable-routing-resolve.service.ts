import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILessonTimetable, LessonTimetable } from '../lesson-timetable.model';
import { LessonTimetableService } from '../service/lesson-timetable.service';

@Injectable({ providedIn: 'root' })
export class LessonTimetableRoutingResolveService implements Resolve<ILessonTimetable> {
  constructor(protected service: LessonTimetableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILessonTimetable> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lessonTimetable: HttpResponse<LessonTimetable>) => {
          if (lessonTimetable.body) {
            return of(lessonTimetable.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LessonTimetable());
  }
}
