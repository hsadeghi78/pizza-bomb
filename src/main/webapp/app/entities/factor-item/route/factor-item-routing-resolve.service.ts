import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactorItem, FactorItem } from '../factor-item.model';
import { FactorItemService } from '../service/factor-item.service';

@Injectable({ providedIn: 'root' })
export class FactorItemRoutingResolveService implements Resolve<IFactorItem> {
  constructor(protected service: FactorItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFactorItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((factorItem: HttpResponse<FactorItem>) => {
          if (factorItem.body) {
            return of(factorItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FactorItem());
  }
}
