import { IClassification } from 'app/entities/classification/classification.model';

export interface IClassType {
  id?: number;
  title?: string;
  typeCode?: number;
  description?: string | null;
  classifications?: IClassification[] | null;
}

export class ClassType implements IClassType {
  constructor(
    public id?: number,
    public title?: string,
    public typeCode?: number,
    public description?: string | null,
    public classifications?: IClassification[] | null
  ) {}
}

export function getClassTypeIdentifier(classType: IClassType): number | undefined {
  return classType.id;
}
