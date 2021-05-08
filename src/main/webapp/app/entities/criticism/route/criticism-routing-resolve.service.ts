import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICriticism, Criticism } from '../criticism.model';
import { CriticismService } from '../service/criticism.service';

@Injectable({ providedIn: 'root' })
export class CriticismRoutingResolveService implements Resolve<ICriticism> {
  constructor(protected service: CriticismService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICriticism> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((criticism: HttpResponse<Criticism>) => {
          if (criticism.body) {
            return of(criticism.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Criticism());
  }
}
