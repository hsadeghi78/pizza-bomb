import * as dayjs from 'dayjs';
import { IParty } from 'app/entities/party/party.model';

export interface IPerson {
  id?: number;
  fisrtName?: string;
  lastName?: string;
  birthDate?: dayjs.Dayjs | null;
  nationalCode?: string;
  parties?: IParty[] | null;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public fisrtName?: string,
    public lastName?: string,
    public birthDate?: dayjs.Dayjs | null,
    public nationalCode?: string,
    public parties?: IParty[] | null
  ) {}
}

export function getPersonIdentifier(person: IPerson): number | undefined {
  return person.id;
}
