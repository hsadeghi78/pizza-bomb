import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFoodType, FoodType } from '../food-type.model';
import { FoodTypeService } from '../service/food-type.service';

@Injectable({ providedIn: 'root' })
export class FoodTypeRoutingResolveService implements Resolve<IFoodType> {
  constructor(protected service: FoodTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFoodType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((foodType: HttpResponse<FoodType>) => {
          if (foodType.body) {
            return of(foodType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FoodType());
  }
}
