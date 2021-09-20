import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILessonTimetableStudent, LessonTimetableStudent } from '../lesson-timetable-student.model';
import { LessonTimetableStudentService } from '../service/lesson-timetable-student.service';

@Injectable({ providedIn: 'root' })
export class LessonTimetableStudentRoutingResolveService implements Resolve<ILessonTimetableStudent> {
  constructor(protected service: LessonTimetableStudentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILessonTimetableStudent> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lessonTimetableStudent: HttpResponse<LessonTimetableStudent>) => {
          if (lessonTimetableStudent.body) {
            return of(lessonTimetableStudent.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LessonTimetableStudent());
  }
}
