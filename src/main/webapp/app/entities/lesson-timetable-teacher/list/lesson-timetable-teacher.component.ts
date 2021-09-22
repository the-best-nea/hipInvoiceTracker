import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILessonTimetableTeacher } from '../lesson-timetable-teacher.model';
import { LessonTimetableTeacherService } from '../service/lesson-timetable-teacher.service';
import { LessonTimetableTeacherDeleteDialogComponent } from '../delete/lesson-timetable-teacher-delete-dialog.component';

@Component({
  selector: 'jhi-lesson-timetable-teacher',
  templateUrl: './lesson-timetable-teacher.component.html',
})
export class LessonTimetableTeacherComponent implements OnInit {
  lessonTimetableTeachers?: ILessonTimetableTeacher[];
  isLoading = false;

  constructor(protected lessonTimetableTeacherService: LessonTimetableTeacherService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lessonTimetableTeacherService.query().subscribe(
      (res: HttpResponse<ILessonTimetableTeacher[]>) => {
        this.isLoading = false;
        this.lessonTimetableTeachers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILessonTimetableTeacher): number {
    return item.id!;
  }

  delete(lessonTimetableTeacher: ILessonTimetableTeacher): void {
    const modalRef = this.modalService.open(LessonTimetableTeacherDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lessonTimetableTeacher = lessonTimetableTeacher;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

}
