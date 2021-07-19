import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMenuItem, getMenuItemIdentifier } from '../menu-item.model';

export type EntityResponseType = HttpResponse<IMenuItem>;
export type EntityArrayResponseType = HttpResponse<IMenuItem[]>;

@Injectable({ providedIn: 'root' })
export class MenuItemService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/menu-items');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(menuItem: IMenuItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menuItem);
    return this.http
      .post<IMenuItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(menuItem: IMenuItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menuItem);
    return this.http
      .put<IMenuItem>(`${this.resourceUrl}/${getMenuItemIdentifier(menuItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(menuItem: IMenuItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menuItem);
    return this.http
      .patch<IMenuItem>(`${this.resourceUrl}/${getMenuItemIdentifier(menuItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMenuItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMenuItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMenuItemToCollectionIfMissing(menuItemCollection: IMenuItem[], ...menuItemsToCheck: (IMenuItem | null | undefined)[]): IMenuItem[] {
    const menuItems: IMenuItem[] = menuItemsToCheck.filter(isPresent);
    if (menuItems.length > 0) {
      const menuItemCollectionIdentifiers = menuItemCollection.map(menuItemItem => getMenuItemIdentifier(menuItemItem)!);
      const menuItemsToAdd = menuItems.filter(menuItemItem => {
        const menuItemIdentifier = getMenuItemIdentifier(menuItemItem);
        if (menuItemIdentifier == null || menuItemCollectionIdentifiers.includes(menuItemIdentifier)) {
          return false;
        }
        menuItemCollectionIdentifiers.push(menuItemIdentifier);
        return true;
      });
      return [...menuItemsToAdd, ...menuItemCollection];
    }
    return menuItemCollection;
  }

  protected convertDateFromClient(menuItem: IMenuItem): IMenuItem {
    return Object.assign({}, menuItem, {
      expirationDate: menuItem.expirationDate?.isValid() ? menuItem.expirationDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expirationDate = res.body.expirationDate ? dayjs(res.body.expirationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((menuItem: IMenuItem) => {
        menuItem.expirationDate = menuItem.expirationDate ? dayjs(menuItem.expirationDate) : undefined;
      });
    }
    return res;
  }
}
