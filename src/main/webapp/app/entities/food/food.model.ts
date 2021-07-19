import { IMenuItem } from 'app/entities/menu-item/menu-item.model';
import { IFactorItem } from 'app/entities/factor-item/factor-item.model';
import { IConsumeMaterial } from 'app/entities/consume-material/consume-material.model';
import { IParty } from 'app/entities/party/party.model';
import { IFoodType } from 'app/entities/food-type/food-type.model';

export interface IFood {
  id?: number;
  title?: string;
  foodCode?: string;
  sizeClassId?: number | null;
  photoContentType?: string | null;
  photo?: string | null;
  categoryClassId?: number | null;
  lastPrice?: number;
  description?: string | null;
  menuItems?: IMenuItem[] | null;
  factorItems?: IFactorItem[] | null;
  materials?: IConsumeMaterial[] | null;
  producerParty?: IParty;
  designerParty?: IParty | null;
  foodType?: IFoodType;
}

export class Food implements IFood {
  constructor(
    public id?: number,
    public title?: string,
    public foodCode?: string,
    public sizeClassId?: number | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public categoryClassId?: number | null,
    public lastPrice?: number,
    public description?: string | null,
    public menuItems?: IMenuItem[] | null,
    public factorItems?: IFactorItem[] | null,
    public materials?: IConsumeMaterial[] | null,
    public producerParty?: IParty,
    public designerParty?: IParty | null,
    public foodType?: IFoodType
  ) {}
}

export function getFoodIdentifier(food: IFood): number | undefined {
  return food.id;
}
