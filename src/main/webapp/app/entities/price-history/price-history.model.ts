export interface IPriceHistory {
  id?: number;
  foodId?: number | null;
  materialId?: number | null;
  price?: number;
}

export class PriceHistory implements IPriceHistory {
  constructor(public id?: number, public foodId?: number | null, public materialId?: number | null, public price?: number) {}
}

export function getPriceHistoryIdentifier(priceHistory: IPriceHistory): number | undefined {
  return priceHistory.id;
}
