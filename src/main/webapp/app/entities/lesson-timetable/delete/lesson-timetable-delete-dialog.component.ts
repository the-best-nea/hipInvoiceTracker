import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILessonTimetable } from '../lesson-timetable.model';
import { LessonTimetableService } from '../service/lesson-timetable.service';

@Component({
  templateUrl: './lesson-timetable-delete-dialog.component.html',
})
export class LessonTimetableDeleteDialogComponent {
  lessonTimetable?: ILessonTimetable;

  constructor(protected lessonTimetableService: LessonTimetableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lessonTimetableService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
