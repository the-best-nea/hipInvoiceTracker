import { IStudent } from 'app/entities/student/student.model';
import { ILessonTimetable } from 'app/entities/lesson-timetable/lesson-timetable.model';

export interface ILessonTimetableStudent {
  id?: number;
  pay?: number | null;
  student?: IStudent | null;
  lessonTimetable?: ILessonTimetable | null;
}

export class LessonTimetableStudent implements ILessonTimetableStudent {
  constructor(
    public id?: number,
    public pay?: number | null,
    public student?: IStudent | null,
    public lessonTimetable?: ILessonTimetable | null
  ) {}
}

export function getLessonTimetableStudentIdentifier(lessonTimetableStudent: ILessonTimetableStudent): number | undefined {
  return lessonTimetableStudent.id;
}
