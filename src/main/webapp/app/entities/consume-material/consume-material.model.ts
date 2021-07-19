import { IFood } from 'app/entities/food/food.model';

export interface IConsumeMaterial {
  id?: number;
  title?: string;
  type?: string | null;
  amount?: number;
  amountUnitClassId?: number;
  food?: IFood;
}

export class ConsumeMaterial implements IConsumeMaterial {
  constructor(
    public id?: number,
    public title?: string,
    public type?: string | null,
    public amount?: number,
    public amountUnitClassId?: number,
    public food?: IFood
  ) {}
}

export function getConsumeMaterialIdentifier(consumeMaterial: IConsumeMaterial): number | undefined {
  return consumeMaterial.id;
}
