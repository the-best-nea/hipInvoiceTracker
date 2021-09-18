import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILessonTimetable } from '../lesson-timetable.model';

@Component({
  selector: 'jhi-lesson-timetable-detail',
  templateUrl: './lesson-timetable-detail.component.html',
})
export class LessonTimetableDetailComponent implements OnInit {
  lessonTimetable: ILessonTimetable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lessonTimetable }) => {
      this.lessonTimetable = lessonTimetable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
