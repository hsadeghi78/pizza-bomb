import { IParty } from 'app/entities/party/party.model';

export interface IFileDocument {
  id?: number;
  fileName?: string;
  fileContentContentType?: string | null;
  fileContent?: string | null;
  filePath?: string | null;
  description?: string;
  party?: IParty;
}

export class FileDocument implements IFileDocument {
  constructor(
    public id?: number,
    public fileName?: string,
    public fileContentContentType?: string | null,
    public fileContent?: string | null,
    public filePath?: string | null,
    public description?: string,
    public party?: IParty
  ) {}
}

export function getFileDocumentIdentifier(fileDocument: IFileDocument): number | undefined {
  return fileDocument.id;
}
