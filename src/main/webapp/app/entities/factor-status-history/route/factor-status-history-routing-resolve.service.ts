import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactorStatusHistory, FactorStatusHistory } from '../factor-status-history.model';
import { FactorStatusHistoryService } from '../service/factor-status-history.service';

@Injectable({ providedIn: 'root' })
export class FactorStatusHistoryRoutingResolveService implements Resolve<IFactorStatusHistory> {
  constructor(protected service: FactorStatusHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFactorStatusHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((factorStatusHistory: HttpResponse<FactorStatusHistory>) => {
          if (factorStatusHistory.body) {
            return of(factorStatusHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FactorStatusHistory());
  }
}
