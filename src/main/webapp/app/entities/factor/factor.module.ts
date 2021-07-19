import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FactorComponent } from './list/factor.component';
import { FactorDetailComponent } from './detail/factor-detail.component';
import { FactorUpdateComponent } from './update/factor-update.component';
import { FactorDeleteDialogComponent } from './delete/factor-delete-dialog.component';
import { FactorRoutingModule } from './route/factor-routing.module';

@NgModule({
  imports: [SharedModule, FactorRoutingModule],
  declarations: [FactorComponent, FactorDetailComponent, FactorUpdateComponent, FactorDeleteDialogComponent],
  entryComponents: [FactorDeleteDialogComponent],
})
export class FactorModule {}
