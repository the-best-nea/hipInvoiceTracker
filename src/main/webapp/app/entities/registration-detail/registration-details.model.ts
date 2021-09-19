export interface IRegistrationDetails {
  id?: number;
  firstName?: string;
  lastName?: string;
  attended?: boolean | null;
}

export class RegistrationDetails implements IRegistrationDetails {
  constructor(public id?: number, public firstName?: string, public lastName?: string, public attended?: boolean | null) {
    this.attended = this.attended ?? false;
  }
}

export function getRegistrationDetailIdentifier(registrationDetail: IRegistrationDetails): number | undefined {
  return registrationDetail.id;
}
