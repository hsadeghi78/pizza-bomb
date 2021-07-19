import { IFood } from 'app/entities/food/food.model';
import { IFactor } from 'app/entities/factor/factor.model';

export interface IFactorItem {
  id?: number;
  rowNum?: number;
  title?: string;
  count?: number;
  discount?: number | null;
  tax?: number | null;
  description?: string | null;
  food?: IFood;
  factor?: IFactor;
}

export class FactorItem implements IFactorItem {
  constructor(
    public id?: number,
    public rowNum?: number,
    public title?: string,
    public count?: number,
    public discount?: number | null,
    public tax?: number | null,
    public description?: string | null,
    public food?: IFood,
    public factor?: IFactor
  ) {}
}

export function getFactorItemIdentifier(factorItem: IFactorItem): number | undefined {
  return factorItem.id;
}
