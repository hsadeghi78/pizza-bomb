import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassType, getClassTypeIdentifier } from '../class-type.model';

export type EntityResponseType = HttpResponse<IClassType>;
export type EntityArrayResponseType = HttpResponse<IClassType[]>;

@Injectable({ providedIn: 'root' })
export class ClassTypeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/class-types');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(classType: IClassType): Observable<EntityResponseType> {
    return this.http.post<IClassType>(this.resourceUrl, classType, { observe: 'response' });
  }

  update(classType: IClassType): Observable<EntityResponseType> {
    return this.http.put<IClassType>(`${this.resourceUrl}/${getClassTypeIdentifier(classType) as number}`, classType, {
      observe: 'response',
    });
  }

  partialUpdate(classType: IClassType): Observable<EntityResponseType> {
    return this.http.patch<IClassType>(`${this.resourceUrl}/${getClassTypeIdentifier(classType) as number}`, classType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassTypeToCollectionIfMissing(
    classTypeCollection: IClassType[],
    ...classTypesToCheck: (IClassType | null | undefined)[]
  ): IClassType[] {
    const classTypes: IClassType[] = classTypesToCheck.filter(isPresent);
    if (classTypes.length > 0) {
      const classTypeCollectionIdentifiers = classTypeCollection.map(classTypeItem => getClassTypeIdentifier(classTypeItem)!);
      const classTypesToAdd = classTypes.filter(classTypeItem => {
        const classTypeIdentifier = getClassTypeIdentifier(classTypeItem);
        if (classTypeIdentifier == null || classTypeCollectionIdentifiers.includes(classTypeIdentifier)) {
          return false;
        }
        classTypeCollectionIdentifiers.push(classTypeIdentifier);
        return true;
      });
      return [...classTypesToAdd, ...classTypeCollection];
    }
    return classTypeCollection;
  }
}
