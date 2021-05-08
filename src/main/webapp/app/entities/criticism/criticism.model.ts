import { IParty } from 'app/entities/party/party.model';

export interface ICriticism {
  id?: number;
  fullName?: string;
  email?: string | null;
  contactNumber?: string | null;
  description?: string;
  party?: IParty | null;
}

export class Criticism implements ICriticism {
  constructor(
    public id?: number,
    public fullName?: string,
    public email?: string | null,
    public contactNumber?: string | null,
    public description?: string,
    public party?: IParty | null
  ) {}
}

export function getCriticismIdentifier(criticism: ICriticism): number | undefined {
  return criticism.id;
}
