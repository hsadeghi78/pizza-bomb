import { IClassType } from 'app/entities/class-type/class-type.model';

export interface IClassification {
  id?: number;
  title?: string;
  classCode?: number;
  description?: string | null;
  classType?: IClassType;
}

export class Classification implements IClassification {
  constructor(
    public id?: number,
    public title?: string,
    public classCode?: number,
    public description?: string | null,
    public classType?: IClassType
  ) {}
}

export function getClassificationIdentifier(classification: IClassification): number | undefined {
  return classification.id;
}
