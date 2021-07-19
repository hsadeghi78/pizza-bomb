import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPriceHistory, getPriceHistoryIdentifier } from '../price-history.model';

export type EntityResponseType = HttpResponse<IPriceHistory>;
export type EntityArrayResponseType = HttpResponse<IPriceHistory[]>;

@Injectable({ providedIn: 'root' })
export class PriceHistoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/price-histories');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(priceHistory: IPriceHistory): Observable<EntityResponseType> {
    return this.http.post<IPriceHistory>(this.resourceUrl, priceHistory, { observe: 'response' });
  }

  update(priceHistory: IPriceHistory): Observable<EntityResponseType> {
    return this.http.put<IPriceHistory>(`${this.resourceUrl}/${getPriceHistoryIdentifier(priceHistory) as number}`, priceHistory, {
      observe: 'response',
    });
  }

  partialUpdate(priceHistory: IPriceHistory): Observable<EntityResponseType> {
    return this.http.patch<IPriceHistory>(`${this.resourceUrl}/${getPriceHistoryIdentifier(priceHistory) as number}`, priceHistory, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPriceHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPriceHistory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPriceHistoryToCollectionIfMissing(
    priceHistoryCollection: IPriceHistory[],
    ...priceHistoriesToCheck: (IPriceHistory | null | undefined)[]
  ): IPriceHistory[] {
    const priceHistories: IPriceHistory[] = priceHistoriesToCheck.filter(isPresent);
    if (priceHistories.length > 0) {
      const priceHistoryCollectionIdentifiers = priceHistoryCollection.map(
        priceHistoryItem => getPriceHistoryIdentifier(priceHistoryItem)!
      );
      const priceHistoriesToAdd = priceHistories.filter(priceHistoryItem => {
        const priceHistoryIdentifier = getPriceHistoryIdentifier(priceHistoryItem);
        if (priceHistoryIdentifier == null || priceHistoryCollectionIdentifiers.includes(priceHistoryIdentifier)) {
          return false;
        }
        priceHistoryCollectionIdentifiers.push(priceHistoryIdentifier);
        return true;
      });
      return [...priceHistoriesToAdd, ...priceHistoryCollection];
    }
    return priceHistoryCollection;
  }
}
