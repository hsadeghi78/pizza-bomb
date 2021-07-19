import { IParty } from 'app/entities/party/party.model';
import { ContactType } from 'app/entities/enumerations/contact-type.model';

export interface IContact {
  id?: number;
  title?: string;
  contactType?: ContactType;
  contactValue?: string;
  party?: IParty;
}

export class Contact implements IContact {
  constructor(
    public id?: number,
    public title?: string,
    public contactType?: ContactType,
    public contactValue?: string,
    public party?: IParty
  ) {}
}

export function getContactIdentifier(contact: IContact): number | undefined {
  return contact.id;
}
