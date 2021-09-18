import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILessonInstance } from '../lesson-instance.model';

@Component({
  selector: 'jhi-lesson-instance-detail',
  templateUrl: './lesson-instance-detail.component.html',
})
export class LessonInstanceDetailComponent implements OnInit {
  lessonInstance: ILessonInstance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lessonInstance }) => {
      this.lessonInstance = lessonInstance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
