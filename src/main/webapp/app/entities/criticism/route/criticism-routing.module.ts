import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CriticismComponent } from '../list/criticism.component';
import { CriticismDetailComponent } from '../detail/criticism-detail.component';
import { CriticismUpdateComponent } from '../update/criticism-update.component';
import { CriticismRoutingResolveService } from './criticism-routing-resolve.service';

const criticismRoute: Routes = [
  {
    path: '',
    component: CriticismComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CriticismDetailComponent,
    resolve: {
      criticism: CriticismRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CriticismUpdateComponent,
    resolve: {
      criticism: CriticismRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CriticismUpdateComponent,
    resolve: {
      criticism: CriticismRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(criticismRoute)],
  exports: [RouterModule],
})
export class CriticismRoutingModule {}
