import * as dayjs from 'dayjs';
import { IStudentRegister } from 'app/entities/student-register/student-register.model';
import { IUser } from 'app/entities/user/user.model';
import { IStudent } from 'app/entities/student/student.model';
import { ISubject } from 'app/entities/subject/subject.model';
import { DayOfWeek } from 'app/entities/enumerations/day-of-week.model';

export interface ILessonInstance {
  id?: number;
  lessonName?: string;
  startAt?: dayjs.Dayjs;
  endAt?: dayjs.Dayjs;
  dayOfWeek?: DayOfWeek;
  description?: string | null;
  cretedOn?: dayjs.Dayjs;
  studentRegisters?: IStudentRegister[] | null;
  internalUser?: IUser | null;
  students?: IStudent[] | null;
  subject?: ISubject | null;
}

export class LessonInstance implements ILessonInstance {
  constructor(
    public id?: number,
    public lessonName?: string,
    public startAt?: dayjs.Dayjs,
    public endAt?: dayjs.Dayjs,
    public dayOfWeek?: DayOfWeek,
    public description?: string | null,
    public cretedOn?: dayjs.Dayjs,
    public studentRegisters?: IStudentRegister[] | null,
    public internalUser?: IUser | null,
    public students?: IStudent[] | null,
    public subject?: ISubject | null
  ) {}
}

export function getLessonInstanceIdentifier(lessonInstance: ILessonInstance): number | undefined {
  return lessonInstance.id;
}
