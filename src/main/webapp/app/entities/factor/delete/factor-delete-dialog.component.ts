import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFactor } from '../factor.model';
import { FactorService } from '../service/factor.service';

@Component({
  templateUrl: './factor-delete-dialog.component.html',
})
export class FactorDeleteDialogComponent {
  factor?: IFactor;

  constructor(protected factorService: FactorService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.factorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
