import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FactorItemComponent } from '../list/factor-item.component';
import { FactorItemDetailComponent } from '../detail/factor-item-detail.component';
import { FactorItemUpdateComponent } from '../update/factor-item-update.component';
import { FactorItemRoutingResolveService } from './factor-item-routing-resolve.service';

const factorItemRoute: Routes = [
  {
    path: '',
    component: FactorItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FactorItemDetailComponent,
    resolve: {
      factorItem: FactorItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FactorItemUpdateComponent,
    resolve: {
      factorItem: FactorItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FactorItemUpdateComponent,
    resolve: {
      factorItem: FactorItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(factorItemRoute)],
  exports: [RouterModule],
})
export class FactorItemRoutingModule {}
