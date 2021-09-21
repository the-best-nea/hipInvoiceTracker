import { Injectable, Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILessonTimetable } from '../lesson-timetable.model';
import { ILessonInstance } from 'app/entities/lesson-instance/lesson-instance.model';
import { LessonTimetableService } from '../service/lesson-timetable.service';
import { LessonTimetableDeleteDialogComponent } from '../delete/lesson-timetable-delete-dialog.component';

import {Router} from '@angular/router';

@Component({
  selector: 'jhi-lesson-timetable',
  templateUrl: './lesson-timetable.component.html',
})
export class LessonTimetableComponent implements OnInit {
  lessonTimetables?: ILessonTimetable[];
  isLoading = false;

  filterId?: number;
  filterLessonName?: string;
  filterDayOfWeek?: string;
  filterActive?: string;
  filterLocation?: string;
  filterSubject?: string;

  filterQuery: any = {};

  constructor(protected lessonTimetableService: LessonTimetableService, protected modalService: NgbModal, private router: Router) {}

  loadAll(): void {
    this.isLoading = true;

    this.lessonTimetableService.query(this.filterQuery).subscribe(
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

  createInstance(lessonTimetable: ILessonTimetable): void {
    const createdInstance = this.lessonTimetableService.createInstance(lessonTimetable).subscribe(response => {
      const resString = JSON.stringify(response.body!.id);
      this.router.navigateByUrl('/lesson-instance/' + resString + '/register');
    } );
  }

  updateLessonTimetable(): void {
    this.filterQuery = {};
    if (this.filterId != null) {
      this.filterQuery['id.equals'] = this.filterId;
    }
    if (this.filterLessonName != null && this.filterLessonName !== "") {
      this.filterQuery['lessonName.equals'] = this.filterLessonName;
    }
    if (this.filterDayOfWeek != null && this.filterDayOfWeek !== "") {
      this.filterQuery['dayOfWeek.equals'] = this.filterDayOfWeek;
    }
    if (this.filterActive === "Active") {
      this.filterQuery['active.equals'] = true;
    }
    if (this.filterActive === "Inactive") {
      this.filterQuery['active.equals'] = false;
    }
    if (this.filterLocation != null && this.filterLocation !== "") {
      this.filterQuery['location.equals'] = this.filterLocation;
    }
    if (this.filterSubject != null && this.filterSubject !== "") {
      this.filterQuery['subject.equals'] = this.filterSubject;
    }


    this.loadAll();
  }

}
