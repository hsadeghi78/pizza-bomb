import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFactorStatusHistory } from '../factor-status-history.model';
import { FactorStatusHistoryService } from '../service/factor-status-history.service';

@Component({
  templateUrl: './factor-status-history-delete-dialog.component.html',
})
export class FactorStatusHistoryDeleteDialogComponent {
  factorStatusHistory?: IFactorStatusHistory;

  constructor(protected factorStatusHistoryService: FactorStatusHistoryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.factorStatusHistoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
