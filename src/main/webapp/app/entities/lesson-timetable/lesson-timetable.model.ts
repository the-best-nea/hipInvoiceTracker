import * as dayjs from 'dayjs';
import { ILocation } from 'app/entities/location/location.model';
import { ISubject } from 'app/entities/subject/subject.model';
import { DayOfWeek } from 'app/entities/enumerations/day-of-week.model';

export interface ILessonTimetable {
  id?: number;
  lessonName?: string;
  startAt?: dayjs.Dayjs;
  endAt?: dayjs.Dayjs;
  dayOfWeek?: DayOfWeek;
  description?: string | null;
  createdAt?: dayjs.Dayjs;
  active?: boolean | null;
  registerTaken?: boolean | null;
  location?: ILocation | null;
  subject?: ISubject | null;
}

export class LessonTimetable implements ILessonTimetable {
  constructor(
    public id?: number,
    public lessonName?: string,
    public startAt?: dayjs.Dayjs,
    public endAt?: dayjs.Dayjs,
    public dayOfWeek?: DayOfWeek,
    public description?: string | null,
    public createdAt?: dayjs.Dayjs,
    public active?: boolean | null,
    public registerTaken?: boolean | null,
    public location?: ILocation | null,
    public subject?: ISubject | null
  ) {
    this.active = this.active ?? false;
    this.registerTaken = this.registerTaken ?? false;
  }
}

export function getLessonTimetableIdentifier(lessonTimetable: ILessonTimetable): number | undefined {
  return lessonTimetable.id;
}
