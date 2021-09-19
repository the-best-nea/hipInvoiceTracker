export interface IRegister {
  id?: number;
  studentId?: number;
  firstName?: string;
  lastName?: string;
  attended?: boolean | null;
}

export class Register implements IRegister {
  constructor(
    public id?: number,
    public studentId?: number,
    public firstName?: string,
    public lastName?: string,
    public attended?: boolean | null
  ) {
    this.attended = this.attended ?? false;
  }
}

export function getRegisterIdentifier(registrationDetail: IRegister): number | undefined {
  return registrationDetail.id;
}
