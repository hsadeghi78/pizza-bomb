import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FactorItemComponent } from './list/factor-item.component';
import { FactorItemDetailComponent } from './detail/factor-item-detail.component';
import { FactorItemUpdateComponent } from './update/factor-item-update.component';
import { FactorItemDeleteDialogComponent } from './delete/factor-item-delete-dialog.component';
import { FactorItemRoutingModule } from './route/factor-item-routing.module';

@NgModule({
  imports: [SharedModule, FactorItemRoutingModule],
  declarations: [FactorItemComponent, FactorItemDetailComponent, FactorItemUpdateComponent, FactorItemDeleteDialogComponent],
  entryComponents: [FactorItemDeleteDialogComponent],
})
export class FactorItemModule {}
