import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICriticism } from '../criticism.model';
import { CriticismService } from '../service/criticism.service';

@Component({
  templateUrl: './criticism-delete-dialog.component.html',
})
export class CriticismDeleteDialogComponent {
  criticism?: ICriticism;

  constructor(protected criticismService: CriticismService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.criticismService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
