import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConsumeMaterial, ConsumeMaterial } from '../consume-material.model';
import { ConsumeMaterialService } from '../service/consume-material.service';

@Injectable({ providedIn: 'root' })
export class ConsumeMaterialRoutingResolveService implements Resolve<IConsumeMaterial> {
  constructor(protected service: ConsumeMaterialService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConsumeMaterial> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((consumeMaterial: HttpResponse<ConsumeMaterial>) => {
          if (consumeMaterial.body) {
            return of(consumeMaterial.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ConsumeMaterial());
  }
}
