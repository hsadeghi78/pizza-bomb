import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFoodType, getFoodTypeIdentifier } from '../food-type.model';

export type EntityResponseType = HttpResponse<IFoodType>;
export type EntityArrayResponseType = HttpResponse<IFoodType[]>;

@Injectable({ providedIn: 'root' })
export class FoodTypeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/food-types');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(foodType: IFoodType): Observable<EntityResponseType> {
    return this.http.post<IFoodType>(this.resourceUrl, foodType, { observe: 'response' });
  }

  update(foodType: IFoodType): Observable<EntityResponseType> {
    return this.http.put<IFoodType>(`${this.resourceUrl}/${getFoodTypeIdentifier(foodType) as number}`, foodType, { observe: 'response' });
  }

  partialUpdate(foodType: IFoodType): Observable<EntityResponseType> {
    return this.http.patch<IFoodType>(`${this.resourceUrl}/${getFoodTypeIdentifier(foodType) as number}`, foodType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFoodType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFoodType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFoodTypeToCollectionIfMissing(foodTypeCollection: IFoodType[], ...foodTypesToCheck: (IFoodType | null | undefined)[]): IFoodType[] {
    const foodTypes: IFoodType[] = foodTypesToCheck.filter(isPresent);
    if (foodTypes.length > 0) {
      const foodTypeCollectionIdentifiers = foodTypeCollection.map(foodTypeItem => getFoodTypeIdentifier(foodTypeItem)!);
      const foodTypesToAdd = foodTypes.filter(foodTypeItem => {
        const foodTypeIdentifier = getFoodTypeIdentifier(foodTypeItem);
        if (foodTypeIdentifier == null || foodTypeCollectionIdentifiers.includes(foodTypeIdentifier)) {
          return false;
        }
        foodTypeCollectionIdentifiers.push(foodTypeIdentifier);
        return true;
      });
      return [...foodTypesToAdd, ...foodTypeCollection];
    }
    return foodTypeCollection;
  }
}
