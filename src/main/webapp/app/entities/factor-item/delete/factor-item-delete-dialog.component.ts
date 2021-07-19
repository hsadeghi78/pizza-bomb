import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFactorItem } from '../factor-item.model';
import { FactorItemService } from '../service/factor-item.service';

@Component({
  templateUrl: './factor-item-delete-dialog.component.html',
})
export class FactorItemDeleteDialogComponent {
  factorItem?: IFactorItem;

  constructor(protected factorItemService: FactorItemService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.factorItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
