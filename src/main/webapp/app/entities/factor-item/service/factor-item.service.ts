import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFactorItem, getFactorItemIdentifier } from '../factor-item.model';

export type EntityResponseType = HttpResponse<IFactorItem>;
export type EntityArrayResponseType = HttpResponse<IFactorItem[]>;

@Injectable({ providedIn: 'root' })
export class FactorItemService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/factor-items');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(factorItem: IFactorItem): Observable<EntityResponseType> {
    return this.http.post<IFactorItem>(this.resourceUrl, factorItem, { observe: 'response' });
  }

  update(factorItem: IFactorItem): Observable<EntityResponseType> {
    return this.http.put<IFactorItem>(`${this.resourceUrl}/${getFactorItemIdentifier(factorItem) as number}`, factorItem, {
      observe: 'response',
    });
  }

  partialUpdate(factorItem: IFactorItem): Observable<EntityResponseType> {
    return this.http.patch<IFactorItem>(`${this.resourceUrl}/${getFactorItemIdentifier(factorItem) as number}`, factorItem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFactorItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactorItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFactorItemToCollectionIfMissing(
    factorItemCollection: IFactorItem[],
    ...factorItemsToCheck: (IFactorItem | null | undefined)[]
  ): IFactorItem[] {
    const factorItems: IFactorItem[] = factorItemsToCheck.filter(isPresent);
    if (factorItems.length > 0) {
      const factorItemCollectionIdentifiers = factorItemCollection.map(factorItemItem => getFactorItemIdentifier(factorItemItem)!);
      const factorItemsToAdd = factorItems.filter(factorItemItem => {
        const factorItemIdentifier = getFactorItemIdentifier(factorItemItem);
        if (factorItemIdentifier == null || factorItemCollectionIdentifiers.includes(factorItemIdentifier)) {
          return false;
        }
        factorItemCollectionIdentifiers.push(factorItemIdentifier);
        return true;
      });
      return [...factorItemsToAdd, ...factorItemCollection];
    }
    return factorItemCollection;
  }
}
