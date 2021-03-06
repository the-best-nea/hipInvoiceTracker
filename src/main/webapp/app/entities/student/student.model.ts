import * as dayjs from 'dayjs';
import { IStudentRegister } from 'app/entities/student-register/student-register.model';
import { ILessonInstance } from 'app/entities/lesson-instance/lesson-instance.model';

export interface IStudent {
  id?: number;
  firstName?: string;
  lastName?: string;
  yearGroup?: number;
  email?: string;
  phoneNumber?: string | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  active?: boolean | null;
  createdAt?: dayjs.Dayjs;
  balance?: number | null;
  studentRegisters?: IStudentRegister[] | null;
  lessonInstances?: ILessonInstance[] | null;
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public yearGroup?: number,
    public email?: string,
    public phoneNumber?: string | null,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs | null,
    public active?: boolean | null,
    public createdAt?: dayjs.Dayjs,
    public balance?: number | null,
    public studentRegisters?: IStudentRegister[] | null,
    public lessonInstances?: ILessonInstance[] | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getStudentIdentifier(student: IStudent): number | undefined {
  return student.id;
}
