import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FactorStatusHistoryComponent } from '../list/factor-status-history.component';
import { FactorStatusHistoryDetailComponent } from '../detail/factor-status-history-detail.component';
import { FactorStatusHistoryUpdateComponent } from '../update/factor-status-history-update.component';
import { FactorStatusHistoryRoutingResolveService } from './factor-status-history-routing-resolve.service';

const factorStatusHistoryRoute: Routes = [
  {
    path: '',
    component: FactorStatusHistoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FactorStatusHistoryDetailComponent,
    resolve: {
      factorStatusHistory: FactorStatusHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FactorStatusHistoryUpdateComponent,
    resolve: {
      factorStatusHistory: FactorStatusHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FactorStatusHistoryUpdateComponent,
    resolve: {
      factorStatusHistory: FactorStatusHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(factorStatusHistoryRoute)],
  exports: [RouterModule],
})
export class FactorStatusHistoryRoutingModule {}
