import * as dayjs from 'dayjs';
import { ICriticism } from 'app/entities/criticism/criticism.model';
import { IFileDocument } from 'app/entities/file-document/file-document.model';
import { IPartyInformation } from 'app/entities/party-information/party-information.model';
import { IComment } from 'app/entities/comment/comment.model';
import { IFoodType } from 'app/entities/food-type/food-type.model';
import { IContact } from 'app/entities/contact/contact.model';
import { IAddress } from 'app/entities/address/address.model';
import { IMenuItem } from 'app/entities/menu-item/menu-item.model';
import { IFood } from 'app/entities/food/food.model';
import { IFactor } from 'app/entities/factor/factor.model';
import { IPartner } from 'app/entities/partner/partner.model';
import { IPerson } from 'app/entities/person/person.model';

export interface IParty {
  id?: number;
  title?: string;
  photoContentType?: string | null;
  photo?: string | null;
  partyCode?: string;
  tradeTitle?: string;
  activationDate?: dayjs.Dayjs;
  expirationDate?: dayjs.Dayjs | null;
  activationStatus?: boolean;
  lat?: number;
  lon?: number;
  address?: string;
  postalCode?: string;
  mobile?: string;
  partyTypeClassId?: number;
  description?: string | null;
  criticisms?: ICriticism[] | null;
  files?: IFileDocument[] | null;
  moreInfos?: IPartyInformation[] | null;
  writedComments?: IComment[] | null;
  audienceComments?: IComment[] | null;
  foodTypes?: IFoodType[] | null;
  children?: IParty[] | null;
  contacts?: IContact[] | null;
  addresses?: IAddress[] | null;
  menuItems?: IMenuItem[] | null;
  produceFoods?: IFood[] | null;
  designedFoods?: IFood[] | null;
  buyerFactors?: IFactor[] | null;
  sellerFactors?: IFactor[] | null;
  parent?: IParty | null;
  partner?: IPartner | null;
  person?: IPerson | null;
}

export class Party implements IParty {
  constructor(
    public id?: number,
    public title?: string,
    public photoContentType?: string | null,
    public photo?: string | null,
    public partyCode?: string,
    public tradeTitle?: string,
    public activationDate?: dayjs.Dayjs,
    public expirationDate?: dayjs.Dayjs | null,
    public activationStatus?: boolean,
    public lat?: number,
    public lon?: number,
    public address?: string,
    public postalCode?: string,
    public mobile?: string,
    public partyTypeClassId?: number,
    public description?: string | null,
    public criticisms?: ICriticism[] | null,
    public files?: IFileDocument[] | null,
    public moreInfos?: IPartyInformation[] | null,
    public writedComments?: IComment[] | null,
    public audienceComments?: IComment[] | null,
    public foodTypes?: IFoodType[] | null,
    public children?: IParty[] | null,
    public contacts?: IContact[] | null,
    public addresses?: IAddress[] | null,
    public menuItems?: IMenuItem[] | null,
    public produceFoods?: IFood[] | null,
    public designedFoods?: IFood[] | null,
    public buyerFactors?: IFactor[] | null,
    public sellerFactors?: IFactor[] | null,
    public parent?: IParty | null,
    public partner?: IPartner | null,
    public person?: IPerson | null
  ) {
    this.activationStatus = this.activationStatus ?? false;
  }
}

export function getPartyIdentifier(party: IParty): number | undefined {
  return party.id;
}
