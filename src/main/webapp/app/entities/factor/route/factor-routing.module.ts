import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FactorComponent } from '../list/factor.component';
import { FactorDetailComponent } from '../detail/factor-detail.component';
import { FactorUpdateComponent } from '../update/factor-update.component';
import { FactorRoutingResolveService } from './factor-routing-resolve.service';

const factorRoute: Routes = [
  {
    path: '',
    component: FactorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FactorDetailComponent,
    resolve: {
      factor: FactorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FactorUpdateComponent,
    resolve: {
      factor: FactorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FactorUpdateComponent,
    resolve: {
      factor: FactorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(factorRoute)],
  exports: [RouterModule],
})
export class FactorRoutingModule {}
