import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPartner, getPartnerIdentifier } from '../partner.model';

export type EntityResponseType = HttpResponse<IPartner>;
export type EntityArrayResponseType = HttpResponse<IPartner[]>;

@Injectable({ providedIn: 'root' })
export class PartnerService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/partners');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(partner: IPartner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(partner);
    return this.http
      .post<IPartner>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(partner: IPartner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(partner);
    return this.http
      .put<IPartner>(`${this.resourceUrl}/${getPartnerIdentifier(partner) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(partner: IPartner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(partner);
    return this.http
      .patch<IPartner>(`${this.resourceUrl}/${getPartnerIdentifier(partner) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPartner>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPartner[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPartnerToCollectionIfMissing(partnerCollection: IPartner[], ...partnersToCheck: (IPartner | null | undefined)[]): IPartner[] {
    const partners: IPartner[] = partnersToCheck.filter(isPresent);
    if (partners.length > 0) {
      const partnerCollectionIdentifiers = partnerCollection.map(partnerItem => getPartnerIdentifier(partnerItem)!);
      const partnersToAdd = partners.filter(partnerItem => {
        const partnerIdentifier = getPartnerIdentifier(partnerItem);
        if (partnerIdentifier == null || partnerCollectionIdentifiers.includes(partnerIdentifier)) {
          return false;
        }
        partnerCollectionIdentifiers.push(partnerIdentifier);
        return true;
      });
      return [...partnersToAdd, ...partnerCollection];
    }
    return partnerCollection;
  }

  protected convertDateFromClient(partner: IPartner): IPartner {
    return Object.assign({}, partner, {
      activityDate: partner.activityDate?.isValid() ? partner.activityDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.activityDate = res.body.activityDate ? dayjs(res.body.activityDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((partner: IPartner) => {
        partner.activityDate = partner.activityDate ? dayjs(partner.activityDate) : undefined;
      });
    }
    return res;
  }
}
