import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICriticism, getCriticismIdentifier } from '../criticism.model';

export type EntityResponseType = HttpResponse<ICriticism>;
export type EntityArrayResponseType = HttpResponse<ICriticism[]>;

@Injectable({ providedIn: 'root' })
export class CriticismService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/criticisms');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(criticism: ICriticism): Observable<EntityResponseType> {
    return this.http.post<ICriticism>(this.resourceUrl, criticism, { observe: 'response' });
  }

  update(criticism: ICriticism): Observable<EntityResponseType> {
    return this.http.put<ICriticism>(`${this.resourceUrl}/${getCriticismIdentifier(criticism) as number}`, criticism, {
      observe: 'response',
    });
  }

  partialUpdate(criticism: ICriticism): Observable<EntityResponseType> {
    return this.http.patch<ICriticism>(`${this.resourceUrl}/${getCriticismIdentifier(criticism) as number}`, criticism, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICriticism>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICriticism[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCriticismToCollectionIfMissing(
    criticismCollection: ICriticism[],
    ...criticismsToCheck: (ICriticism | null | undefined)[]
  ): ICriticism[] {
    const criticisms: ICriticism[] = criticismsToCheck.filter(isPresent);
    if (criticisms.length > 0) {
      const criticismCollectionIdentifiers = criticismCollection.map(criticismItem => getCriticismIdentifier(criticismItem)!);
      const criticismsToAdd = criticisms.filter(criticismItem => {
        const criticismIdentifier = getCriticismIdentifier(criticismItem);
        if (criticismIdentifier == null || criticismCollectionIdentifiers.includes(criticismIdentifier)) {
          return false;
        }
        criticismCollectionIdentifiers.push(criticismIdentifier);
        return true;
      });
      return [...criticismsToAdd, ...criticismCollection];
    }
    return criticismCollection;
  }
}
