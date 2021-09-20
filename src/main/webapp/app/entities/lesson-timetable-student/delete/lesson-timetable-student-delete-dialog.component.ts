import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILessonTimetableStudent } from '../lesson-timetable-student.model';
import { LessonTimetableStudentService } from '../service/lesson-timetable-student.service';

@Component({
  templateUrl: './lesson-timetable-student-delete-dialog.component.html',
})
export class LessonTimetableStudentDeleteDialogComponent {
  lessonTimetableStudent?: ILessonTimetableStudent;

  constructor(protected lessonTimetableStudentService: LessonTimetableStudentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lessonTimetableStudentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
