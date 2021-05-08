import * as dayjs from 'dayjs';
import { IBranch } from 'app/entities/branch/branch.model';
import { ICriticism } from 'app/entities/criticism/criticism.model';

export interface IParty {
  id?: number;
  title?: string;
  partyCode?: string;
  tradeTitle?: string;
  activationDate?: dayjs.Dayjs;
  expirationDate?: dayjs.Dayjs | null;
  activationStatus?: boolean;
  description?: string | null;
  branchs?: IBranch[] | null;
  criticisms?: ICriticism[] | null;
}

export class Party implements IParty {
  constructor(
    public id?: number,
    public title?: string,
    public partyCode?: string,
    public tradeTitle?: string,
    public activationDate?: dayjs.Dayjs,
    public expirationDate?: dayjs.Dayjs | null,
    public activationStatus?: boolean,
    public description?: string | null,
    public branchs?: IBranch[] | null,
    public criticisms?: ICriticism[] | null
  ) {
    this.activationStatus = this.activationStatus ?? false;
  }
}

export function getPartyIdentifier(party: IParty): number | undefined {
  return party.id;
}
