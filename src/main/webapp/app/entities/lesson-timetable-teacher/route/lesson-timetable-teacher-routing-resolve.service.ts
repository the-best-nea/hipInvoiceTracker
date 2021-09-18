import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILessonTimetableTeacher, LessonTimetableTeacher } from '../lesson-timetable-teacher.model';
import { LessonTimetableTeacherService } from '../service/lesson-timetable-teacher.service';

@Injectable({ providedIn: 'root' })
export class LessonTimetableTeacherRoutingResolveService implements Resolve<ILessonTimetableTeacher> {
  constructor(protected service: LessonTimetableTeacherService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILessonTimetableTeacher> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lessonTimetableTeacher: HttpResponse<LessonTimetableTeacher>) => {
          if (lessonTimetableTeacher.body) {
            return of(lessonTimetableTeacher.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LessonTimetableTeacher());
  }
}
