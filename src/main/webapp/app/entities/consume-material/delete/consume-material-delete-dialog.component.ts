import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConsumeMaterial } from '../consume-material.model';
import { ConsumeMaterialService } from '../service/consume-material.service';

@Component({
  templateUrl: './consume-material-delete-dialog.component.html',
})
export class ConsumeMaterialDeleteDialogComponent {
  consumeMaterial?: IConsumeMaterial;

  constructor(protected consumeMaterialService: ConsumeMaterialService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.consumeMaterialService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
