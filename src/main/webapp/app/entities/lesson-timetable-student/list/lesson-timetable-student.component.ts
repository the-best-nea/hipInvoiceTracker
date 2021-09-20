import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILessonTimetableStudent } from '../lesson-timetable-student.model';
import { LessonTimetableStudentService } from '../service/lesson-timetable-student.service';
import { LessonTimetableStudentDeleteDialogComponent } from '../delete/lesson-timetable-student-delete-dialog.component';

@Component({
  selector: 'jhi-lesson-timetable-student',
  templateUrl: './lesson-timetable-student.component.html',
})
export class LessonTimetableStudentComponent implements OnInit {
  lessonTimetableStudents?: ILessonTimetableStudent[];
  isLoading = false;

  constructor(protected lessonTimetableStudentService: LessonTimetableStudentService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lessonTimetableStudentService.query().subscribe(
      (res: HttpResponse<ILessonTimetableStudent[]>) => {
        this.isLoading = false;
        this.lessonTimetableStudents = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILessonTimetableStudent): number {
    return item.id!;
  }

  delete(lessonTimetableStudent: ILessonTimetableStudent): void {
    const modalRef = this.modalService.open(LessonTimetableStudentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lessonTimetableStudent = lessonTimetableStudent;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
