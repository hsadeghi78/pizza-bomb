import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsumeMaterial, getConsumeMaterialIdentifier } from '../consume-material.model';

export type EntityResponseType = HttpResponse<IConsumeMaterial>;
export type EntityArrayResponseType = HttpResponse<IConsumeMaterial[]>;

@Injectable({ providedIn: 'root' })
export class ConsumeMaterialService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/consume-materials');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(consumeMaterial: IConsumeMaterial): Observable<EntityResponseType> {
    return this.http.post<IConsumeMaterial>(this.resourceUrl, consumeMaterial, { observe: 'response' });
  }

  update(consumeMaterial: IConsumeMaterial): Observable<EntityResponseType> {
    return this.http.put<IConsumeMaterial>(
      `${this.resourceUrl}/${getConsumeMaterialIdentifier(consumeMaterial) as number}`,
      consumeMaterial,
      { observe: 'response' }
    );
  }

  partialUpdate(consumeMaterial: IConsumeMaterial): Observable<EntityResponseType> {
    return this.http.patch<IConsumeMaterial>(
      `${this.resourceUrl}/${getConsumeMaterialIdentifier(consumeMaterial) as number}`,
      consumeMaterial,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConsumeMaterial>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConsumeMaterial[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConsumeMaterialToCollectionIfMissing(
    consumeMaterialCollection: IConsumeMaterial[],
    ...consumeMaterialsToCheck: (IConsumeMaterial | null | undefined)[]
  ): IConsumeMaterial[] {
    const consumeMaterials: IConsumeMaterial[] = consumeMaterialsToCheck.filter(isPresent);
    if (consumeMaterials.length > 0) {
      const consumeMaterialCollectionIdentifiers = consumeMaterialCollection.map(
        consumeMaterialItem => getConsumeMaterialIdentifier(consumeMaterialItem)!
      );
      const consumeMaterialsToAdd = consumeMaterials.filter(consumeMaterialItem => {
        const consumeMaterialIdentifier = getConsumeMaterialIdentifier(consumeMaterialItem);
        if (consumeMaterialIdentifier == null || consumeMaterialCollectionIdentifiers.includes(consumeMaterialIdentifier)) {
          return false;
        }
        consumeMaterialCollectionIdentifiers.push(consumeMaterialIdentifier);
        return true;
      });
      return [...consumeMaterialsToAdd, ...consumeMaterialCollection];
    }
    return consumeMaterialCollection;
  }
}
