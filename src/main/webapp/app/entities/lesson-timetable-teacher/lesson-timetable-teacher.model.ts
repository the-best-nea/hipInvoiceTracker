import { ILessonTimetable } from 'app/entities/lesson-timetable/lesson-timetable.model';
import { IUser } from 'app/entities/user/user.model';

export interface ILessonTimetableTeacher {
  id?: number;
  pay?: number;
  lessonTimetable?: ILessonTimetable | null;
  internalUser?: IUser | null;
}

export class LessonTimetableTeacher implements ILessonTimetableTeacher {
  constructor(
    public id?: number,
    public pay?: number,
    public lessonTimetable?: ILessonTimetable | null,
    public internalUser?: IUser | null
  ) {}
}

export function getLessonTimetableTeacherIdentifier(lessonTimetableTeacher: ILessonTimetableTeacher): number | undefined {
  return lessonTimetableTeacher.id;
}
