import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFileDocument, getFileDocumentIdentifier } from '../file-document.model';

export type EntityResponseType = HttpResponse<IFileDocument>;
export type EntityArrayResponseType = HttpResponse<IFileDocument[]>;

@Injectable({ providedIn: 'root' })
export class FileDocumentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/file-documents');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(fileDocument: IFileDocument): Observable<EntityResponseType> {
    return this.http.post<IFileDocument>(this.resourceUrl, fileDocument, { observe: 'response' });
  }

  update(fileDocument: IFileDocument): Observable<EntityResponseType> {
    return this.http.put<IFileDocument>(`${this.resourceUrl}/${getFileDocumentIdentifier(fileDocument) as number}`, fileDocument, {
      observe: 'response',
    });
  }

  partialUpdate(fileDocument: IFileDocument): Observable<EntityResponseType> {
    return this.http.patch<IFileDocument>(`${this.resourceUrl}/${getFileDocumentIdentifier(fileDocument) as number}`, fileDocument, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFileDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFileDocument[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFileDocumentToCollectionIfMissing(
    fileDocumentCollection: IFileDocument[],
    ...fileDocumentsToCheck: (IFileDocument | null | undefined)[]
  ): IFileDocument[] {
    const fileDocuments: IFileDocument[] = fileDocumentsToCheck.filter(isPresent);
    if (fileDocuments.length > 0) {
      const fileDocumentCollectionIdentifiers = fileDocumentCollection.map(
        fileDocumentItem => getFileDocumentIdentifier(fileDocumentItem)!
      );
      const fileDocumentsToAdd = fileDocuments.filter(fileDocumentItem => {
        const fileDocumentIdentifier = getFileDocumentIdentifier(fileDocumentItem);
        if (fileDocumentIdentifier == null || fileDocumentCollectionIdentifiers.includes(fileDocumentIdentifier)) {
          return false;
        }
        fileDocumentCollectionIdentifiers.push(fileDocumentIdentifier);
        return true;
      });
      return [...fileDocumentsToAdd, ...fileDocumentCollection];
    }
    return fileDocumentCollection;
  }
}
