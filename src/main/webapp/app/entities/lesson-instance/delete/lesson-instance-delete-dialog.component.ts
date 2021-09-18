import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILessonInstance } from '../lesson-instance.model';
import { LessonInstanceService } from '../service/lesson-instance.service';

@Component({
  templateUrl: './lesson-instance-delete-dialog.component.html',
})
export class LessonInstanceDeleteDialogComponent {
  lessonInstance?: ILessonInstance;

  constructor(protected lessonInstanceService: LessonInstanceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lessonInstanceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
