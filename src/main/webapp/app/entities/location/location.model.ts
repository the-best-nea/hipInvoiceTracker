import { ILessonTimetable } from 'app/entities/lesson-timetable/lesson-timetable.model';

export interface ILocation {
  id?: number;
  name?: string;
  address?: string;
  lessonTimetables?: ILessonTimetable[] | null;
}

export class Location implements ILocation {
  constructor(public id?: number, public name?: string, public address?: string, public lessonTimetables?: ILessonTimetable[] | null) {}
}

export function getLocationIdentifier(location: ILocation): number | undefined {
  return location.id;
}
