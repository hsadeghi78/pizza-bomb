import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FoodTypeComponent } from '../list/food-type.component';
import { FoodTypeDetailComponent } from '../detail/food-type-detail.component';
import { FoodTypeUpdateComponent } from '../update/food-type-update.component';
import { FoodTypeRoutingResolveService } from './food-type-routing-resolve.service';

const foodTypeRoute: Routes = [
  {
    path: '',
    component: FoodTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FoodTypeDetailComponent,
    resolve: {
      foodType: FoodTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FoodTypeUpdateComponent,
    resolve: {
      foodType: FoodTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FoodTypeUpdateComponent,
    resolve: {
      foodType: FoodTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(foodTypeRoute)],
  exports: [RouterModule],
})
export class FoodTypeRoutingModule {}
