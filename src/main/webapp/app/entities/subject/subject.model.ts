import * as dayjs from 'dayjs';
import { ILessonTimetable } from 'app/entities/lesson-timetable/lesson-timetable.model';
import { ILessonInstance } from 'app/entities/lesson-instance/lesson-instance.model';

export interface ISubject {
  id?: number;
  subjectName?: string;
  description?: string | null;
  createdAt?: dayjs.Dayjs;
  active?: boolean | null;
  lessonTimetables?: ILessonTimetable[] | null;
  lessonInstances?: ILessonInstance[] | null;
}

export class Subject implements ISubject {
  constructor(
    public id?: number,
    public subjectName?: string,
    public description?: string | null,
    public createdAt?: dayjs.Dayjs,
    public active?: boolean | null,
    public lessonTimetables?: ILessonTimetable[] | null,
    public lessonInstances?: ILessonInstance[] | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getSubjectIdentifier(subject: ISubject): number | undefined {
  return subject.id;
}
