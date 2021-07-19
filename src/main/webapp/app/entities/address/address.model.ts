import { IFactor } from 'app/entities/factor/factor.model';
import { IParty } from 'app/entities/party/party.model';

export interface IAddress {
  id?: number;
  title?: string;
  lat?: number;
  lon?: number;
  street1?: string | null;
  street2?: string | null;
  address?: string;
  postalCode?: string;
  factors?: IFactor[] | null;
  party?: IParty;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public title?: string,
    public lat?: number,
    public lon?: number,
    public street1?: string | null,
    public street2?: string | null,
    public address?: string,
    public postalCode?: string,
    public factors?: IFactor[] | null,
    public party?: IParty
  ) {}
}

export function getAddressIdentifier(address: IAddress): number | undefined {
  return address.id;
}
