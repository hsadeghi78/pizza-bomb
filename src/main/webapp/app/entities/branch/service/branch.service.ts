import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBranch, getBranchIdentifier } from '../branch.model';

export type EntityResponseType = HttpResponse<IBranch>;
export type EntityArrayResponseType = HttpResponse<IBranch[]>;

@Injectable({ providedIn: 'root' })
export class BranchService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/branches');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(branch: IBranch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(branch);
    return this.http
      .post<IBranch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(branch: IBranch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(branch);
    return this.http
      .put<IBranch>(`${this.resourceUrl}/${getBranchIdentifier(branch) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(branch: IBranch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(branch);
    return this.http
      .patch<IBranch>(`${this.resourceUrl}/${getBranchIdentifier(branch) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBranch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBranch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBranchToCollectionIfMissing(branchCollection: IBranch[], ...branchesToCheck: (IBranch | null | undefined)[]): IBranch[] {
    const branches: IBranch[] = branchesToCheck.filter(isPresent);
    if (branches.length > 0) {
      const branchCollectionIdentifiers = branchCollection.map(branchItem => getBranchIdentifier(branchItem)!);
      const branchesToAdd = branches.filter(branchItem => {
        const branchIdentifier = getBranchIdentifier(branchItem);
        if (branchIdentifier == null || branchCollectionIdentifiers.includes(branchIdentifier)) {
          return false;
        }
        branchCollectionIdentifiers.push(branchIdentifier);
        return true;
      });
      return [...branchesToAdd, ...branchCollection];
    }
    return branchCollection;
  }

  protected convertDateFromClient(branch: IBranch): IBranch {
    return Object.assign({}, branch, {
      activationDate: branch.activationDate?.isValid() ? branch.activationDate.format(DATE_FORMAT) : undefined,
      expirationDate: branch.expirationDate?.isValid() ? branch.expirationDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.activationDate = res.body.activationDate ? dayjs(res.body.activationDate) : undefined;
      res.body.expirationDate = res.body.expirationDate ? dayjs(res.body.expirationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((branch: IBranch) => {
        branch.activationDate = branch.activationDate ? dayjs(branch.activationDate) : undefined;
        branch.expirationDate = branch.expirationDate ? dayjs(branch.expirationDate) : undefined;
      });
    }
    return res;
  }
}
