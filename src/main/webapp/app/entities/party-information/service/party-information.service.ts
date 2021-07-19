import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPartyInformation, getPartyInformationIdentifier } from '../party-information.model';

export type EntityResponseType = HttpResponse<IPartyInformation>;
export type EntityArrayResponseType = HttpResponse<IPartyInformation[]>;

@Injectable({ providedIn: 'root' })
export class PartyInformationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/party-informations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(partyInformation: IPartyInformation): Observable<EntityResponseType> {
    return this.http.post<IPartyInformation>(this.resourceUrl, partyInformation, { observe: 'response' });
  }

  update(partyInformation: IPartyInformation): Observable<EntityResponseType> {
    return this.http.put<IPartyInformation>(
      `${this.resourceUrl}/${getPartyInformationIdentifier(partyInformation) as number}`,
      partyInformation,
      { observe: 'response' }
    );
  }

  partialUpdate(partyInformation: IPartyInformation): Observable<EntityResponseType> {
    return this.http.patch<IPartyInformation>(
      `${this.resourceUrl}/${getPartyInformationIdentifier(partyInformation) as number}`,
      partyInformation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPartyInformation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPartyInformation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPartyInformationToCollectionIfMissing(
    partyInformationCollection: IPartyInformation[],
    ...partyInformationsToCheck: (IPartyInformation | null | undefined)[]
  ): IPartyInformation[] {
    const partyInformations: IPartyInformation[] = partyInformationsToCheck.filter(isPresent);
    if (partyInformations.length > 0) {
      const partyInformationCollectionIdentifiers = partyInformationCollection.map(
        partyInformationItem => getPartyInformationIdentifier(partyInformationItem)!
      );
      const partyInformationsToAdd = partyInformations.filter(partyInformationItem => {
        const partyInformationIdentifier = getPartyInformationIdentifier(partyInformationItem);
        if (partyInformationIdentifier == null || partyInformationCollectionIdentifiers.includes(partyInformationIdentifier)) {
          return false;
        }
        partyInformationCollectionIdentifiers.push(partyInformationIdentifier);
        return true;
      });
      return [...partyInformationsToAdd, ...partyInformationCollection];
    }
    return partyInformationCollection;
  }
}
