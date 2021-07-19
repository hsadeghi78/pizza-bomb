import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassTypeComponent } from '../list/class-type.component';
import { ClassTypeDetailComponent } from '../detail/class-type-detail.component';
import { ClassTypeUpdateComponent } from '../update/class-type-update.component';
import { ClassTypeRoutingResolveService } from './class-type-routing-resolve.service';

const classTypeRoute: Routes = [
  {
    path: '',
    component: ClassTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassTypeDetailComponent,
    resolve: {
      classType: ClassTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassTypeUpdateComponent,
    resolve: {
      classType: ClassTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassTypeUpdateComponent,
    resolve: {
      classType: ClassTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classTypeRoute)],
  exports: [RouterModule],
})
export class ClassTypeRoutingModule {}
