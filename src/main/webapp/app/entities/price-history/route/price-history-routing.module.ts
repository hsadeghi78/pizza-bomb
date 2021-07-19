import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PriceHistoryComponent } from '../list/price-history.component';
import { PriceHistoryDetailComponent } from '../detail/price-history-detail.component';
import { PriceHistoryUpdateComponent } from '../update/price-history-update.component';
import { PriceHistoryRoutingResolveService } from './price-history-routing-resolve.service';

const priceHistoryRoute: Routes = [
  {
    path: '',
    component: PriceHistoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PriceHistoryDetailComponent,
    resolve: {
      priceHistory: PriceHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PriceHistoryUpdateComponent,
    resolve: {
      priceHistory: PriceHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PriceHistoryUpdateComponent,
    resolve: {
      priceHistory: PriceHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(priceHistoryRoute)],
  exports: [RouterModule],
})
export class PriceHistoryRoutingModule {}
