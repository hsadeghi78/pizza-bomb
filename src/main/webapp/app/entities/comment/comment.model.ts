import { IParty } from 'app/entities/party/party.model';

export interface IComment {
  id?: number;
  rating?: number | null;
  description?: string | null;
  children?: IComment[] | null;
  writerParty?: IParty;
  audienceParty?: IParty;
  parent?: IComment | null;
}

export class Comment implements IComment {
  constructor(
    public id?: number,
    public rating?: number | null,
    public description?: string | null,
    public children?: IComment[] | null,
    public writerParty?: IParty,
    public audienceParty?: IParty,
    public parent?: IComment | null
  ) {}
}

export function getCommentIdentifier(comment: IComment): number | undefined {
  return comment.id;
}
