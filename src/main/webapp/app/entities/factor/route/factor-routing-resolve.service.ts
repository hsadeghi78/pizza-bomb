import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactor, Factor } from '../factor.model';
import { FactorService } from '../service/factor.service';

@Injectable({ providedIn: 'root' })
export class FactorRoutingResolveService implements Resolve<IFactor> {
  constructor(protected service: FactorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFactor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((factor: HttpResponse<Factor>) => {
          if (factor.body) {
            return of(factor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Factor());
  }
}
