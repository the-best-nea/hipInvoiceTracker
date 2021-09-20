import * as dayjs from 'dayjs';
import { IStudent } from 'app/entities/student/student.model';
import { ILessonInstance } from 'app/entities/lesson-instance/lesson-instance.model';

export interface IStudentRegister {
  id?: number;
  dateOfLesson?: dayjs.Dayjs;
  pay?: number | null;
  attended?: boolean | null;
  createdOn?: dayjs.Dayjs;
  updatedOn?: dayjs.Dayjs;
  student?: IStudent | null;
  lessonTimetable?: ILessonInstance | null;
}

export class StudentRegister implements IStudentRegister {
  constructor(
    public id?: number,
    public dateOfLesson?: dayjs.Dayjs,
    public pay?: number | null,
    public attended?: boolean | null,
    public createdOn?: dayjs.Dayjs,
    public updatedOn?: dayjs.Dayjs,
    public student?: IStudent | null,
    public lessonTimetable?: ILessonInstance | null
  ) {
    this.attended = this.attended ?? false;
  }
}

export function getStudentRegisterIdentifier(studentRegister: IStudentRegister): number | undefined {
  return studentRegister.id;
}
