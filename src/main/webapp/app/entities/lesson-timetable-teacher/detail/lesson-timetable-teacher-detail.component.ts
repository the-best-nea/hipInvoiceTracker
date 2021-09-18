import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILessonTimetableTeacher } from '../lesson-timetable-teacher.model';

@Component({
  selector: 'jhi-lesson-timetable-teacher-detail',
  templateUrl: './lesson-timetable-teacher-detail.component.html',
})
export class LessonTimetableTeacherDetailComponent implements OnInit {
  lessonTimetableTeacher: ILessonTimetableTeacher | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lessonTimetableTeacher }) => {
      this.lessonTimetableTeacher = lessonTimetableTeacher;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
