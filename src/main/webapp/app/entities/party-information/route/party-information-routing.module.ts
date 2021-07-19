import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PartyInformationComponent } from '../list/party-information.component';
import { PartyInformationDetailComponent } from '../detail/party-information-detail.component';
import { PartyInformationUpdateComponent } from '../update/party-information-update.component';
import { PartyInformationRoutingResolveService } from './party-information-routing-resolve.service';

const partyInformationRoute: Routes = [
  {
    path: '',
    component: PartyInformationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PartyInformationDetailComponent,
    resolve: {
      partyInformation: PartyInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PartyInformationUpdateComponent,
    resolve: {
      partyInformation: PartyInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PartyInformationUpdateComponent,
    resolve: {
      partyInformation: PartyInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(partyInformationRoute)],
  exports: [RouterModule],
})
export class PartyInformationRoutingModule {}
