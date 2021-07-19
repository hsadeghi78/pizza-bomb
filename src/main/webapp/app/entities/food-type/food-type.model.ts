import { IFood } from 'app/entities/food/food.model';
import { IParty } from 'app/entities/party/party.model';

export interface IFoodType {
  id?: number;
  title?: string;
  typeCode?: string;
  description?: string | null;
  foods?: IFood[] | null;
  party?: IParty;
}

export class FoodType implements IFoodType {
  constructor(
    public id?: number,
    public title?: string,
    public typeCode?: string,
    public description?: string | null,
    public foods?: IFood[] | null,
    public party?: IParty
  ) {}
}

export function getFoodTypeIdentifier(foodType: IFoodType): number | undefined {
  return foodType.id;
}
