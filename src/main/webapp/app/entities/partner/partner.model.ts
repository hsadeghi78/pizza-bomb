import * as dayjs from 'dayjs';
import { IParty } from 'app/entities/party/party.model';

export interface IPartner {
  id?: number;
  title?: string;
  partnerCode?: string;
  tradeTitle?: string;
  economicCode?: string | null;
  activityDate?: dayjs.Dayjs | null;
  parties?: IParty[] | null;
}

export class Partner implements IPartner {
  constructor(
    public id?: number,
    public title?: string,
    public partnerCode?: string,
    public tradeTitle?: string,
    public economicCode?: string | null,
    public activityDate?: dayjs.Dayjs | null,
    public parties?: IParty[] | null
  ) {}
}

export function getPartnerIdentifier(partner: IPartner): number | undefined {
  return partner.id;
}
