import { IFactorItem } from 'app/entities/factor-item/factor-item.model';
import { IParty } from 'app/entities/party/party.model';
import { IAddress } from 'app/entities/address/address.model';
import { FactorStatus } from 'app/entities/enumerations/factor-status.model';
import { FactorOrderWay } from 'app/entities/enumerations/factor-order-way.model';
import { FactorServing } from 'app/entities/enumerations/factor-serving.model';

export interface IFactor {
  id?: number;
  title?: string;
  factorCode?: string;
  lastStatus?: FactorStatus;
  orderWay?: FactorOrderWay;
  serving?: FactorServing;
  paymentStateClassId?: number;
  categoryClassId?: number | null;
  totalPrice?: number;
  discount?: number | null;
  tax?: number | null;
  netprice?: number;
  description?: string | null;
  factorItems?: IFactorItem[] | null;
  buyerParty?: IParty;
  sellerParty?: IParty;
  deliveryAddress?: IAddress | null;
}

export class Factor implements IFactor {
  constructor(
    public id?: number,
    public title?: string,
    public factorCode?: string,
    public lastStatus?: FactorStatus,
    public orderWay?: FactorOrderWay,
    public serving?: FactorServing,
    public paymentStateClassId?: number,
    public categoryClassId?: number | null,
    public totalPrice?: number,
    public discount?: number | null,
    public tax?: number | null,
    public netprice?: number,
    public description?: string | null,
    public factorItems?: IFactorItem[] | null,
    public buyerParty?: IParty,
    public sellerParty?: IParty,
    public deliveryAddress?: IAddress | null
  ) {}
}

export function getFactorIdentifier(factor: IFactor): number | undefined {
  return factor.id;
}
