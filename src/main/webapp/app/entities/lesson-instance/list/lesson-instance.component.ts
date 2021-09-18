import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILessonInstance } from '../lesson-instance.model';
import { LessonInstanceService } from '../service/lesson-instance.service';
import { LessonInstanceDeleteDialogComponent } from '../delete/lesson-instance-delete-dialog.component';

@Component({
  selector: 'jhi-lesson-instance',
  templateUrl: './lesson-instance.component.html',
})
export class LessonInstanceComponent implements OnInit {
  lessonInstances?: ILessonInstance[];
  isLoading = false;

  constructor(protected lessonInstanceService: LessonInstanceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lessonInstanceService.query().subscribe(
      (res: HttpResponse<ILessonInstance[]>) => {
        this.isLoading = false;
        this.lessonInstances = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILessonInstance): number {
    return item.id!;
  }

  delete(lessonInstance: ILessonInstance): void {
    const modalRef = this.modalService.open(LessonInstanceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lessonInstance = lessonInstance;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
