import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFactorStatusHistory, getFactorStatusHistoryIdentifier } from '../factor-status-history.model';

export type EntityResponseType = HttpResponse<IFactorStatusHistory>;
export type EntityArrayResponseType = HttpResponse<IFactorStatusHistory[]>;

@Injectable({ providedIn: 'root' })
export class FactorStatusHistoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/factor-status-histories');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(factorStatusHistory: IFactorStatusHistory): Observable<EntityResponseType> {
    return this.http.post<IFactorStatusHistory>(this.resourceUrl, factorStatusHistory, { observe: 'response' });
  }

  update(factorStatusHistory: IFactorStatusHistory): Observable<EntityResponseType> {
    return this.http.put<IFactorStatusHistory>(
      `${this.resourceUrl}/${getFactorStatusHistoryIdentifier(factorStatusHistory) as number}`,
      factorStatusHistory,
      { observe: 'response' }
    );
  }

  partialUpdate(factorStatusHistory: IFactorStatusHistory): Observable<EntityResponseType> {
    return this.http.patch<IFactorStatusHistory>(
      `${this.resourceUrl}/${getFactorStatusHistoryIdentifier(factorStatusHistory) as number}`,
      factorStatusHistory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFactorStatusHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactorStatusHistory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFactorStatusHistoryToCollectionIfMissing(
    factorStatusHistoryCollection: IFactorStatusHistory[],
    ...factorStatusHistoriesToCheck: (IFactorStatusHistory | null | undefined)[]
  ): IFactorStatusHistory[] {
    const factorStatusHistories: IFactorStatusHistory[] = factorStatusHistoriesToCheck.filter(isPresent);
    if (factorStatusHistories.length > 0) {
      const factorStatusHistoryCollectionIdentifiers = factorStatusHistoryCollection.map(
        factorStatusHistoryItem => getFactorStatusHistoryIdentifier(factorStatusHistoryItem)!
      );
      const factorStatusHistoriesToAdd = factorStatusHistories.filter(factorStatusHistoryItem => {
        const factorStatusHistoryIdentifier = getFactorStatusHistoryIdentifier(factorStatusHistoryItem);
        if (factorStatusHistoryIdentifier == null || factorStatusHistoryCollectionIdentifiers.includes(factorStatusHistoryIdentifier)) {
          return false;
        }
        factorStatusHistoryCollectionIdentifiers.push(factorStatusHistoryIdentifier);
        return true;
      });
      return [...factorStatusHistoriesToAdd, ...factorStatusHistoryCollection];
    }
    return factorStatusHistoryCollection;
  }
}
