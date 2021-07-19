import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassType, ClassType } from '../class-type.model';
import { ClassTypeService } from '../service/class-type.service';

@Injectable({ providedIn: 'root' })
export class ClassTypeRoutingResolveService implements Resolve<IClassType> {
  constructor(protected service: ClassTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((classType: HttpResponse<ClassType>) => {
          if (classType.body) {
            return of(classType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassType());
  }
}
