import * as dayjs from 'dayjs';
import { IParty } from 'app/entities/party/party.model';
import { IFood } from 'app/entities/food/food.model';

export interface IMenuItem {
  id?: number;
  title?: string;
  expirationDate?: dayjs.Dayjs;
  description?: string | null;
  party?: IParty;
  food?: IFood;
}

export class MenuItem implements IMenuItem {
  constructor(
    public id?: number,
    public title?: string,
    public expirationDate?: dayjs.Dayjs,
    public description?: string | null,
    public party?: IParty,
    public food?: IFood
  ) {}
}

export function getMenuItemIdentifier(menuItem: IMenuItem): number | undefined {
  return menuItem.id;
}
