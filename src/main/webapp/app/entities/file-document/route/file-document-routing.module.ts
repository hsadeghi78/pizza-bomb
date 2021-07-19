import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FileDocumentComponent } from '../list/file-document.component';
import { FileDocumentDetailComponent } from '../detail/file-document-detail.component';
import { FileDocumentUpdateComponent } from '../update/file-document-update.component';
import { FileDocumentRoutingResolveService } from './file-document-routing-resolve.service';

const fileDocumentRoute: Routes = [
  {
    path: '',
    component: FileDocumentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileDocumentDetailComponent,
    resolve: {
      fileDocument: FileDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileDocumentUpdateComponent,
    resolve: {
      fileDocument: FileDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileDocumentUpdateComponent,
    resolve: {
      fileDocument: FileDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fileDocumentRoute)],
  exports: [RouterModule],
})
export class FileDocumentRoutingModule {}
