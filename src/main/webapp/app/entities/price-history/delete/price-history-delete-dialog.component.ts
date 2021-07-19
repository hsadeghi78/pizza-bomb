import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPriceHistory } from '../price-history.model';
import { PriceHistoryService } from '../service/price-history.service';

@Component({
  templateUrl: './price-history-delete-dialog.component.html',
})
export class PriceHistoryDeleteDialogComponent {
  priceHistory?: IPriceHistory;

  constructor(protected priceHistoryService: PriceHistoryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.priceHistoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
