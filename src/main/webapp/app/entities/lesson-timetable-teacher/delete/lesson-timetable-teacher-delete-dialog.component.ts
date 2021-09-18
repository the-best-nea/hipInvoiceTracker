import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILessonTimetableTeacher } from '../lesson-timetable-teacher.model';
import { LessonTimetableTeacherService } from '../service/lesson-timetable-teacher.service';

@Component({
  templateUrl: './lesson-timetable-teacher-delete-dialog.component.html',
})
export class LessonTimetableTeacherDeleteDialogComponent {
  lessonTimetableTeacher?: ILessonTimetableTeacher;

  constructor(protected lessonTimetableTeacherService: LessonTimetableTeacherService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lessonTimetableTeacherService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
