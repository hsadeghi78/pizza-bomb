import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PriceHistoryComponent } from './list/price-history.component';
import { PriceHistoryDetailComponent } from './detail/price-history-detail.component';
import { PriceHistoryUpdateComponent } from './update/price-history-update.component';
import { PriceHistoryDeleteDialogComponent } from './delete/price-history-delete-dialog.component';
import { PriceHistoryRoutingModule } from './route/price-history-routing.module';

@NgModule({
  imports: [SharedModule, PriceHistoryRoutingModule],
  declarations: [PriceHistoryComponent, PriceHistoryDetailComponent, PriceHistoryUpdateComponent, PriceHistoryDeleteDialogComponent],
  entryComponents: [PriceHistoryDeleteDialogComponent],
})
export class PriceHistoryModule {}
