import { Injectable, Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILessonTimetable } from '../lesson-timetable.model';
import { LessonTimetableService } from '../service/lesson-timetable.service';
import { LessonTimetableDeleteDialogComponent } from '../delete/lesson-timetable-delete-dialog.component';


@Component({
  selector: 'jhi-lesson-timetable',
  templateUrl: './lesson-timetable.component.html',
})
export class LessonTimetableComponent implements OnInit {
  lessonTimetables?: ILessonTimetable[];
  isLoading = false;

  constructor(protected lessonTimetableService: LessonTimetableService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lessonTimetableService.query().subscribe(
      (res: HttpResponse<ILessonTimetable[]>) => {
        this.isLoading = false;
        this.lessonTimetables = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILessonTimetable): number {
    return item.id!;
  }

  delete(lessonTimetable: ILessonTimetable): void {
    const modalRef = this.modalService.open(LessonTimetableDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lessonTimetable = lessonTimetable;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  createInstance(id: any): void {
    this.lessonTimetableService.createInstance(id);
  }

}
