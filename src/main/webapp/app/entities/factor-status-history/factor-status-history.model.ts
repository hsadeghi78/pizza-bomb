import { FactorStatus } from 'app/entities/enumerations/factor-status.model';

export interface IFactorStatusHistory {
  id?: number;
  factorId?: number;
  status?: FactorStatus;
}

export class FactorStatusHistory implements IFactorStatusHistory {
  constructor(public id?: number, public factorId?: number, public status?: FactorStatus) {}
}

export function getFactorStatusHistoryIdentifier(factorStatusHistory: IFactorStatusHistory): number | undefined {
  return factorStatusHistory.id;
}
