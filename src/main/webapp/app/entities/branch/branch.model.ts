import * as dayjs from 'dayjs';
import { IParty } from 'app/entities/party/party.model';

export interface IBranch {
  id?: number;
  title?: string;
  branchCode?: string;
  tradeTitle?: string;
  activationDate?: dayjs.Dayjs;
  expirationDate?: dayjs.Dayjs | null;
  activationStatus?: boolean;
  lat?: number;
  address?: string;
  postalCode?: string;
  description?: string | null;
  party?: IParty;
}

export class Branch implements IBranch {
  constructor(
    public id?: number,
    public title?: string,
    public branchCode?: string,
    public tradeTitle?: string,
    public activationDate?: dayjs.Dayjs,
    public expirationDate?: dayjs.Dayjs | null,
    public activationStatus?: boolean,
    public lat?: number,
    public address?: string,
    public postalCode?: string,
    public description?: string | null,
    public party?: IParty
  ) {
    this.activationStatus = this.activationStatus ?? false;
  }
}

export function getBranchIdentifier(branch: IBranch): number | undefined {
  return branch.id;
}
