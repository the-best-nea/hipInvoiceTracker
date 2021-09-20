import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILessonTimetableStudent } from '../lesson-timetable-student.model';

@Component({
  selector: 'jhi-lesson-timetable-student-detail',
  templateUrl: './lesson-timetable-student-detail.component.html',
})
export class LessonTimetableStudentDetailComponent implements OnInit {
  lessonTimetableStudent: ILessonTimetableStudent | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lessonTimetableStudent }) => {
      this.lessonTimetableStudent = lessonTimetableStudent;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
