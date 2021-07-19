import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConsumeMaterialComponent } from '../list/consume-material.component';
import { ConsumeMaterialDetailComponent } from '../detail/consume-material-detail.component';
import { ConsumeMaterialUpdateComponent } from '../update/consume-material-update.component';
import { ConsumeMaterialRoutingResolveService } from './consume-material-routing-resolve.service';

const consumeMaterialRoute: Routes = [
  {
    path: '',
    component: ConsumeMaterialComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConsumeMaterialDetailComponent,
    resolve: {
      consumeMaterial: ConsumeMaterialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConsumeMaterialUpdateComponent,
    resolve: {
      consumeMaterial: ConsumeMaterialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConsumeMaterialUpdateComponent,
    resolve: {
      consumeMaterial: ConsumeMaterialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(consumeMaterialRoute)],
  exports: [RouterModule],
})
export class ConsumeMaterialRoutingModule {}
