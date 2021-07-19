import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFactor, getFactorIdentifier } from '../factor.model';

export type EntityResponseType = HttpResponse<IFactor>;
export type EntityArrayResponseType = HttpResponse<IFactor[]>;

@Injectable({ providedIn: 'root' })
export class FactorService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/factors');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(factor: IFactor): Observable<EntityResponseType> {
    return this.http.post<IFactor>(this.resourceUrl, factor, { observe: 'response' });
  }

  update(factor: IFactor): Observable<EntityResponseType> {
    return this.http.put<IFactor>(`${this.resourceUrl}/${getFactorIdentifier(factor) as number}`, factor, { observe: 'response' });
  }

  partialUpdate(factor: IFactor): Observable<EntityResponseType> {
    return this.http.patch<IFactor>(`${this.resourceUrl}/${getFactorIdentifier(factor) as number}`, factor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFactor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFactorToCollectionIfMissing(factorCollection: IFactor[], ...factorsToCheck: (IFactor | null | undefined)[]): IFactor[] {
    const factors: IFactor[] = factorsToCheck.filter(isPresent);
    if (factors.length > 0) {
      const factorCollectionIdentifiers = factorCollection.map(factorItem => getFactorIdentifier(factorItem)!);
      const factorsToAdd = factors.filter(factorItem => {
        const factorIdentifier = getFactorIdentifier(factorItem);
        if (factorIdentifier == null || factorCollectionIdentifiers.includes(factorIdentifier)) {
          return false;
        }
        factorCollectionIdentifiers.push(factorIdentifier);
        return true;
      });
      return [...factorsToAdd, ...factorCollection];
    }
    return factorCollection;
  }
}
