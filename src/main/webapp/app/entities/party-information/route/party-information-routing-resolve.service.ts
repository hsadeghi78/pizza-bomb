import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPartyInformation, PartyInformation } from '../party-information.model';
import { PartyInformationService } from '../service/party-information.service';

@Injectable({ providedIn: 'root' })
export class PartyInformationRoutingResolveService implements Resolve<IPartyInformation> {
  constructor(protected service: PartyInformationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPartyInformation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((partyInformation: HttpResponse<PartyInformation>) => {
          if (partyInformation.body) {
            return of(partyInformation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PartyInformation());
  }
}
