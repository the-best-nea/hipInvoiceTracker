import * as dayjs from 'dayjs';
import { ISubject } from 'app/entities/subject/subject.model';
import { DaysOfWeek } from 'app/entities/enumerations/days-of-week.model';

export interface ILesson {
  id?: number;
  lessonName?: string;
  startAt?: dayjs.Dayjs;
  endAt?: dayjs.Dayjs;
  dayOfWeek?: DaysOfWeek;
  desciption?: string | null;
  createdAt?: dayjs.Dayjs;
  active?: boolean | null;
  subject?: ISubject | null;
}

export class Lesson implements ILesson {
  constructor(
    public id?: number,
    public lessonName?: string,
    public startAt?: dayjs.Dayjs,
    public endAt?: dayjs.Dayjs,
    public dayOfWeek?: DaysOfWeek,
    public desciption?: string | null,
    public createdAt?: dayjs.Dayjs,
    public active?: boolean | null,
    public subject?: ISubject | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getLessonIdentifier(lesson: ILesson): number | undefined {
  return lesson.id;
}
