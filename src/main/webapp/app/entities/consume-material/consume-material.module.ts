import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ConsumeMaterialComponent } from './list/consume-material.component';
import { ConsumeMaterialDetailComponent } from './detail/consume-material-detail.component';
import { ConsumeMaterialUpdateComponent } from './update/consume-material-update.component';
import { ConsumeMaterialDeleteDialogComponent } from './delete/consume-material-delete-dialog.component';
import { ConsumeMaterialRoutingModule } from './route/consume-material-routing.module';

@NgModule({
  imports: [SharedModule, ConsumeMaterialRoutingModule],
  declarations: [
    ConsumeMaterialComponent,
    ConsumeMaterialDetailComponent,
    ConsumeMaterialUpdateComponent,
    ConsumeMaterialDeleteDialogComponent,
  ],
  entryComponents: [ConsumeMaterialDeleteDialogComponent],
})
export class ConsumeMaterialModule {}
