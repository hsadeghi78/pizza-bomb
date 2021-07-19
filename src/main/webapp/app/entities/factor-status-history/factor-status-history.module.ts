import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FactorStatusHistoryComponent } from './list/factor-status-history.component';
import { FactorStatusHistoryDetailComponent } from './detail/factor-status-history-detail.component';
import { FactorStatusHistoryUpdateComponent } from './update/factor-status-history-update.component';
import { FactorStatusHistoryDeleteDialogComponent } from './delete/factor-status-history-delete-dialog.component';
import { FactorStatusHistoryRoutingModule } from './route/factor-status-history-routing.module';

@NgModule({
  imports: [SharedModule, FactorStatusHistoryRoutingModule],
  declarations: [
    FactorStatusHistoryComponent,
    FactorStatusHistoryDetailComponent,
    FactorStatusHistoryUpdateComponent,
    FactorStatusHistoryDeleteDialogComponent,
  ],
  entryComponents: [FactorStatusHistoryDeleteDialogComponent],
})
export class FactorStatusHistoryModule {}
